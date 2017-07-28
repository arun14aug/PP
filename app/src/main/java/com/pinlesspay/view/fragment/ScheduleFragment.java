package com.pinlesspay.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.pinlesspay.R;
import com.pinlesspay.model.ModelManager;
import com.pinlesspay.model.Schedule;
import com.pinlesspay.utility.PPLog;
import com.pinlesspay.utility.PaginationScrollListener;
import com.pinlesspay.utility.Utils;
import com.pinlesspay.view.adapter.ScheduleAdapter;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/*
 * Created by arun.sharma on 7/25/2017.
 */

public class ScheduleFragment extends Fragment {

    private String TAG = ScheduleFragment.this.getClass().getName();
    private Activity activity;
    private LinearLayoutManager linearLayoutManager;
    private LinearLayout waterfall_layout;
    private FrameLayout list_layout;
    private RecyclerView rv;
    private ProgressBar progressBar;
    private final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 3;
    private int currentPage = PAGE_START;
    private ScheduleAdapter adapter;
    private ArrayList<Schedule> scheduleArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.activity = super.getActivity();
        Intent intent = new Intent("Header");
        intent.putExtra("message", activity.getString(R.string.title_schedules));

        LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
        View rootView = inflater.inflate(R.layout.fragment_schedule, container, false);

        rv = (RecyclerView) activity.findViewById(R.id.schedule_list);
        progressBar = (ProgressBar) activity.findViewById(R.id.main_progress);
        list_layout = (FrameLayout) activity.findViewById(R.id.list_layout);
        waterfall_layout = (LinearLayout) activity.findViewById(R.id.waterfall_layout);

        linearLayoutManager = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);

        rv.setItemAnimator(new DefaultItemAnimator());

        rv.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;

                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPage();
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });


        // Inflate the layout for this fragment
        return rootView;
    }

    private void loadFirstPage() {

        Utils.showLoading(activity);
        ModelManager.getInstance().getScheduleManager().getSchedules(activity, true, 1);

    }

    private void setData() {
        adapter = new ScheduleAdapter(activity, scheduleArrayList);
        rv.setAdapter(adapter);

        if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
        else isLastPage = true;
    }

    private void loadNextPage() {
//        Log.d(TAG, "loadNextPage: " + currentPage);
//        List<Movie> movies = Movie.createMovies(adapter.getItemCount());
//
//        adapter.removeLoadingFooter();
//        isLoading = false;
//
//        adapter.addAll(movies);

        if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
        else isLastPage = true;
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
            scheduleArrayList = ModelManager.getInstance().getScheduleManager().getSchedules(activity, false, 1);
            if (scheduleArrayList != null)
                if (scheduleArrayList.size() > 0)
                    loadFirstPage();
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
