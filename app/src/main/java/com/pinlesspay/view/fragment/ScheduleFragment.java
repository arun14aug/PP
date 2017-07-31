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
import com.pinlesspay.model.Schedule;
import com.pinlesspay.utility.PPLog;
import com.pinlesspay.utility.Utils;
import com.pinlesspay.view.adapter.SchedulesAdapter;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/*
 * Created by arun.sharma on 7/25/2017.
 */

public class ScheduleFragment extends Fragment {

    private String TAG = ScheduleFragment.this.getClass().getName();
    private Activity activity;
    private RecyclerView recyclerView;
    private ArrayList<Schedule> scheduleArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.activity = super.getActivity();
        Intent intent = new Intent("Header");
        intent.putExtra("message", activity.getString(R.string.title_schedules));

        LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
        View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.schedule_list);

        Utils.showLoading(activity);
        ModelManager.getInstance().getScheduleManager().getSchedules(activity, true, 1);

        // Inflate the layout for this fragment
        return rootView;
    }



    private void setData() {
        SchedulesAdapter adapter = new SchedulesAdapter(activity, scheduleArrayList);
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
        if (message.equalsIgnoreCase("GetSchedule True")) {
            Utils.dismissLoading();
            PPLog.e(TAG, "GetSchedule True");
            scheduleArrayList = ModelManager.getInstance().getScheduleManager().getSchedules(activity, false, 1);
            if (scheduleArrayList != null)
                if (scheduleArrayList.size() > 0)
                    setData();
        } else if (message.contains("GetSchedule False")) {
            // showMatchHistoryList();
            Utils.showMessage(activity, activity.getString(R.string.please_wait));
            PPLog.e(TAG, "GetSchedule False");
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("GetSchedule Network Error")) {
            Utils.showMessage(activity, "Network Error! Please try again");
            PPLog.e(TAG, "GetSchedule Network Error");
            Utils.dismissLoading();
        }

    }

}
