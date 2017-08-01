package com.pinlesspay.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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
    private LinearLayout waterfall_layout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.activity = super.getActivity();
        Intent intent = new Intent("Header");
        intent.putExtra("message", activity.getString(R.string.title_recurring));

        LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
        View rootView = inflater.inflate(R.layout.fragment_recurring, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recurring_list);
        waterfall_layout = (LinearLayout) rootView.findViewById(R.id.waterfall_layout);

        Utils.showLoading(activity);
        ModelManager.getInstance().getScheduleManager().getRecurring(activity, true, 1);

        recyclerView.addOnItemTouchListener(new RecurringFragment.RecyclerTouchListener(getActivity(), recyclerView, new RecurringFragment.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Fragment fragment = new RecurringDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putString("id", recurringArrayList.get(position).getDonationScheduleId());
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = ((FragmentActivity) activity)
                        .getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment, "RecurringDetailFragment");
                fragmentTransaction.addToBackStack("RecurringDetailFragment");
                fragmentTransaction.commit();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        // Inflate the layout for this fragment
        return rootView;
    }


    interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    private class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }


    }

    private void setData() {
        recyclerView.setVisibility(View.VISIBLE);
        waterfall_layout.setVisibility(View.GONE);
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
                if (recurringArrayList.size() > 0) {
                    setData();
                } else {
                    recyclerView.setVisibility(View.GONE);
                    waterfall_layout.setVisibility(View.VISIBLE);
                }
            else {
                recyclerView.setVisibility(View.GONE);
                waterfall_layout.setVisibility(View.VISIBLE);
            }
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
