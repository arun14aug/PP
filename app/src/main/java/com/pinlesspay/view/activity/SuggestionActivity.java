package com.pinlesspay.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.pinlesspay.R;

/*
 * Created by arun.sharma on 8/8/2017.
 */

public class SuggestionActivity extends Activity implements View.OnClickListener {

    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);
        activity = SuggestionActivity.this;

        ImageView img_back = (ImageView) findViewById(R.id.img_back);

        img_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
        }
    }
}
