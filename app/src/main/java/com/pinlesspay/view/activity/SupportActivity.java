package com.pinlesspay.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.pinlesspay.R;

/*
 * Created by arun.sharma on 8/2/2017.
 */

public class SupportActivity extends Activity {

    private Activity activity;
    private LinearLayout waterfall_layout;
    private RecyclerView ticket_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        activity = SupportActivity.this;

        waterfall_layout = (LinearLayout) findViewById(R.id.waterfall_layout);
        ticket_list = (RecyclerView) findViewById(R.id.ticket_list);

        ImageView img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ticket_list.setVisibility(View.GONE);
        waterfall_layout.setVisibility(View.VISIBLE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, NewTicketActivity.class);
                startActivity(intent);
            }
        });
    }
}
