package com.pinlesspay.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pinlesspay.R;
import com.pinlesspay.customUi.MyTextView;
import com.pinlesspay.model.Charity;
import com.pinlesspay.model.ModelManager;
import com.pinlesspay.utility.PPLog;
import com.pinlesspay.utility.Utils;

import java.io.UnsupportedEncodingException;
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

        txt_description = (MyTextView) rootView.findViewById(R.id.txt_description);


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
        byte[] bytes = new byte[0]; // Charset to encode into
        String s2 = "";
        try {
            bytes = charityArrayList.get(0).getDescription().getBytes("UTF-8");
            s2 = new String(bytes, "UTF-8"); // Charset with which bytes were encoded
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        try {
//            s2 = URLDecoder.decode(charityArrayList.get(0).getDescription(), "ISO-8859-1");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        txt_description.setText(s2);
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
        if (message.equalsIgnoreCase("GetCharity True")) {
            Utils.dismissLoading();
            PPLog.e(TAG, "GetCharity True");
            charityArrayList = ModelManager.getInstance().getScheduleManager().getCharity(activity, false, 1);
            if (charityArrayList != null)
                if (charityArrayList.size() > 0)
                    setData();
        } else if (message.contains("GetCharity False")) {
            // showMatchHistoryList();
            if (message.contains("@#@")) {
                String[] m = message.split("@#@");
                Utils.showMessage(activity, m[1]);
            } else
                Utils.showMessage(activity, getString(R.string.error_message));
            PPLog.e(TAG, "GetCharity False");
            Utils.dismissLoading();
        }

    }
}
