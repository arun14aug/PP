package com.pinlesspay.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pinlesspay.R;
import com.pinlesspay.customUi.MyTextView;
import com.pinlesspay.model.Charity;
import com.pinlesspay.model.ModelManager;
import com.pinlesspay.utility.PPLog;
import com.pinlesspay.utility.Utils;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/*
 * Created by arun.sharma on 7/25/2017.
 */

public class DonationFragment extends Fragment {

    private Activity activity;
    private String TAG = DonationFragment.this.getClass().getName();
    private ArrayList<Charity> charityArrayList;
    private MyTextView txt_description;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = super.getActivity();
        Intent intent = new Intent("Header");
        intent.putExtra("message", activity.getString(R.string.title_donation));

        LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
        View rootView = inflater.inflate(R.layout.fragment_donation, container, false);

        txt_description = (MyTextView) activity.findViewById(R.id.txt_description);


        charityArrayList = ModelManager.getInstance().getScheduleManager().getCharity(activity, false, 1);
        if (charityArrayList == null) {
            Utils.showLoading(activity);
            ModelManager.getInstance().getScheduleManager().getCharity(activity, true, 1);
        } else
            setData();

        // Inflate the layout for this fragment
        return rootView;
    }

    private void setData() {
        txt_description.setText(Html.fromHtml(charityArrayList.get(0).getDescription()));
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
        if (message.equalsIgnoreCase("Charity True")) {
            Utils.dismissLoading();
            PPLog.e(TAG, "Charity True");
            charityArrayList = ModelManager.getInstance().getScheduleManager().getCharity(activity, false, 1);
            if (charityArrayList != null)
                if (charityArrayList.size() > 0)
                    setData();
        } else if (message.contains("Charity False")) {
            // showMatchHistoryList();
            Utils.showMessage(activity, activity.getString(R.string.please_wait));
            PPLog.e(TAG, "Charity False");
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("Charity Network Error")) {
            Utils.showMessage(activity, "Network Error! Please try again");
            PPLog.e(TAG, "Charity Network Error");
            Utils.dismissLoading();
        }

    }
}
