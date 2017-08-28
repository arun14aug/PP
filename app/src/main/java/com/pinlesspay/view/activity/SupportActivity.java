package com.pinlesspay.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.pinlesspay.R;
import com.pinlesspay.model.ModelManager;
import com.pinlesspay.model.Ticket;
import com.pinlesspay.utility.PPLog;
import com.pinlesspay.utility.Utils;
import com.pinlesspay.view.adapter.SupportAdapter;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/*
 * Created by arun.sharma on 8/2/2017.
 */

public class SupportActivity extends Activity {

    private String TAG = SupportActivity.this.getClass().getName();
    private ArrayList<Ticket> ticketArrayList;
    private Activity activity;
    private LinearLayout waterfall_layout;
    private RecyclerView ticket_list;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        activity = SupportActivity.this;

        waterfall_layout = (LinearLayout) findViewById(R.id.waterfall_layout);
        ticket_list = (RecyclerView) findViewById(R.id.ticket_list);

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        ImageView img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        swipeContainer.setVisibility(View.GONE);
        waterfall_layout.setVisibility(View.VISIBLE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, NewTicketActivity.class);
                startActivity(intent);
            }
        });

        ticketArrayList = ModelManager.getInstance().getRestOfAllManager().getTickets(activity, false);
        if (ticketArrayList == null) {
            Utils.showLoading(activity);
            ModelManager.getInstance().getRestOfAllManager().getTickets(activity, true);
        } else
            setData();
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                Utils.showLoading(activity);
                ModelManager.getInstance().getRestOfAllManager().getTickets(activity, true);
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    private void setData() {
        if (ticketArrayList.size() > 0) {
            swipeContainer.setVisibility(View.VISIBLE);
            waterfall_layout.setVisibility(View.GONE);
            SupportAdapter adapter = new SupportAdapter(activity, ticketArrayList);
            ticket_list.setAdapter(adapter);
            ticket_list.setLayoutManager(new LinearLayoutManager(this));
            swipeContainer.setRefreshing(false);
        } else {
            swipeContainer.setVisibility(View.GONE);
            waterfall_layout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        ticketArrayList = ModelManager.getInstance().getRestOfAllManager().getTickets(activity, false);
        if (ticketArrayList != null)
            if (ticketArrayList.size() > 0)
                setData();
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
        if (message.equalsIgnoreCase("GetTickets True")) {
            Utils.dismissLoading();
            PPLog.e(TAG, "GetTickets True");
            ticketArrayList = ModelManager.getInstance().getRestOfAllManager().getTickets(activity, false);
            if (ticketArrayList != null)
                if (ticketArrayList.size() > 0)
                    setData();
                else {
                    swipeContainer.setVisibility(View.GONE);
                    waterfall_layout.setVisibility(View.VISIBLE);
                }
            else {
                swipeContainer.setVisibility(View.GONE);
                waterfall_layout.setVisibility(View.VISIBLE);
            }
        } else if (message.contains("GetTickets False")) {
            // showMatchHistoryList();
            if (message.contains("@#@")) {
                String[] m = message.split("@#@");
                Utils.showMessage(activity, m[1]);
            } else
                Utils.showMessage(activity, getString(R.string.error_message));
            swipeContainer.setVisibility(View.GONE);
            waterfall_layout.setVisibility(View.VISIBLE);
            PPLog.e(TAG, "GetTickets False");
            Utils.dismissLoading();
        }

    }
}
