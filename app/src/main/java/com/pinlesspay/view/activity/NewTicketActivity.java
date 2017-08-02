package com.pinlesspay.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.pinlesspay.R;

/*
 * Created by arun.sharma on 8/2/2017.
 */

public class NewTicketActivity extends Activity {

    private Activity activity;
    private LinearLayout waterfall_layout;
    private RecyclerView ticket_chat_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ticket);

        activity = NewTicketActivity.this;

        waterfall_layout = (LinearLayout) findViewById(R.id.waterfall_layout);
        ticket_chat_list = (RecyclerView) findViewById(R.id.ticket_chat_list);

        ImageView img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ticket_chat_list.setVisibility(View.GONE);
        waterfall_layout.setVisibility(View.VISIBLE);

    }
}