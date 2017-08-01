package com.pinlesspay.view.activity;

import android.app.Activity;
import android.os.Bundle;

import com.pinlesspay.R;

/*
 * Created by arun.sharma on 8/1/2017.
 */

public class ProfileActivity extends Activity {
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        activity = ProfileActivity.this;
    }
}
