package com.pinlesspay.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pinlesspay.R;
import com.pinlesspay.customUi.MyTextView;
import com.pinlesspay.model.ModelManager;
import com.pinlesspay.model.Schedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/*
 * Created by arun.sharma on 7/25/2017.
 */

public class ScheduleDetailFragment extends Fragment {

    private Toolbar mToolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Activity activity = super.getActivity();
        Intent intent = new Intent("Header");
        intent.putExtra("message", activity.getString(R.string.title_schedules));

        LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
        View rootView = inflater.inflate(R.layout.fragment_schedule_detail, container, false);

        String id = "";
        try {
            if (getArguments() != null) {
                Bundle bundle = getArguments();
                id = bundle.getString("id");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            id = "";
        }


        MyTextView txt_heading = (MyTextView) rootView.findViewById(R.id.txt_heading);
        MyTextView txt_description = (MyTextView) rootView.findViewById(R.id.txt_description);
        mToolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        mToolbar.setVisibility(View.GONE);

        ImageView imageView = (ImageView) rootView.findViewById(R.id.img_back);
        MyTextView txt_schedule_header = (MyTextView) rootView.findViewById(R.id.txt_schedule_header);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((FragmentActivity) activity).getSupportFragmentManager()
                        .popBackStack();
            }
        });

        ArrayList<Schedule> schedules = ModelManager.getInstance().getScheduleManager().getSchedules(activity, false, 1);
        for (int i = 0; i < schedules.size(); i++) {
            if (schedules.get(i).getId().equalsIgnoreCase(id)) {
                SimpleDateFormat format1 = new SimpleDateFormat("MM-dd-yyyy");
                SimpleDateFormat format2 = new SimpleDateFormat("dd MMM yyyy");
                Date date = null;
                try {
                    date = format1.parse(schedules.get(i).getTaskDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                txt_schedule_header.setText(format2.format(date));
                txt_heading.setText(schedules.get(i).getTaskTitle());
                txt_description.setText(schedules.get(i).getTaskDescription());
                break;
            }
        }

        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        mToolbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mToolbar.setVisibility(View.VISIBLE);
    }
}
