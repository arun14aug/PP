package com.pinlesspay.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pinlesspay.R;
import com.pinlesspay.model.ModelManager;
import com.pinlesspay.model.Recurring;
import com.pinlesspay.utility.PPLog;
import com.pinlesspay.utility.Utils;
import com.pinlesspay.view.adapter.RecurringAdapter;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/*
 * Created by arun.sharma on 7/25/2017.
 */

public class RecurringFragment extends Fragment {

    private String TAG = RecurringFragment.this.getClass().getName();
    private Activity activity;
    private RecyclerView recyclerView;
    private ArrayList<Recurring> recurringArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.activity = super.getActivity();
        Intent intent = new Intent("Header");
        intent.putExtra("message", activity.getString(R.string.title_recurring));

        LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
        View rootView = inflater.inflate(R.layout.fragment_recurring, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recurring_list);

        Utils.showLoading(activity);
        ModelManager.getInstance().getScheduleManager().getRecurring(activity, true, 1);

        // Inflate the layout for this fragment
        return rootView;
    }


    private void setData() {
        RecurringAdapter adapter = new RecurringAdapter(activity, recurringArrayList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);

    }

    public void onEventMainThread(String message) {
        if (message.equalsIgnoreCase("Recurring True")) {
            Utils.dismissLoading();
            PPLog.e(TAG, "Recurring True");
            recurringArrayList = ModelManager.getInstance().getScheduleManager().getRecurring(activity, false, 1);
            if (recurringArrayList != null)
                if (recurringArrayList.size() > 0)
                    setData();
        } else if (message.contains("Recurring False")) {
            // showMatchHistoryList();
            Utils.showMessage(activity, activity.getString(R.string.please_wait));
            PPLog.e(TAG, "Recurring False");
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("Recurring Network Error")) {
            Utils.showMessage(activity, "Network Error! Please try again");
            PPLog.e(TAG, "Recurring Network Error");
            Utils.dismissLoading();
        }

    }

}
