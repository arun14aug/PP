package com.pinlesspay.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.pinlesspay.R;

/*
 * Created by arun.sharma on 8/1/2017.
 */

public class TellFriendActivity extends Activity implements View.OnClickListener {

    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tell_a_friend);

        activity = TellFriendActivity.this;

        LinearLayout layout_facebook = (LinearLayout) findViewById(R.id.layout_facebook);
        LinearLayout layout_twitter = (LinearLayout) findViewById(R.id.layout_twitter);
        LinearLayout layout_email = (LinearLayout) findViewById(R.id.layout_email);
        LinearLayout layout_sms = (LinearLayout) findViewById(R.id.layout_sms);

        ImageView img_back = (ImageView) findViewById(R.id.img_back);

        img_back.setOnClickListener(this);
        layout_sms.setOnClickListener(this);
        layout_email.setOnClickListener(this);
        layout_twitter.setOnClickListener(this);
        layout_facebook.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.layout_facebook:
                break;
            case R.id.layout_twitter:
                break;
            case R.id.layout_email:
                break;
            case R.id.layout_sms:
                break;
        }
    }
}
