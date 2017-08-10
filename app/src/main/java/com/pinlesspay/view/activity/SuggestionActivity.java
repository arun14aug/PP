package com.pinlesspay.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.pinlesspay.R;
import com.pinlesspay.customUi.MyEditText;
import com.pinlesspay.model.ModelManager;
import com.pinlesspay.utility.PPLog;
import com.pinlesspay.utility.Preferences;
import com.pinlesspay.utility.ServiceApi;
import com.pinlesspay.utility.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

/*
 * Created by arun.sharma on 8/8/2017.
 */

public class SuggestionActivity extends Activity implements View.OnClickListener {

    private String TAG = SuggestionActivity.this.getClass().getName();
    private Activity activity;
    private MyEditText et_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);
        activity = SuggestionActivity.this;

        et_message = (MyEditText) findViewById(R.id.et_message);
        ImageView img_back = (ImageView) findViewById(R.id.img_back);
        ImageView img_send = (ImageView) findViewById(R.id.img_send);

        img_back.setOnClickListener(this);
        img_send.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.img_send:
                if (et_message.getText().toString().trim().length() == 0) {
                    et_message.requestFocus();
                    Utils.showMessage(activity, getString(R.string.please_enter_card_name));
                } else {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("OrganizationKey", ServiceApi.ORGANISATION_KEY);
                        jsonObject.put("Token", Preferences.readString(activity, Preferences.AUTH_TOKEN, ""));
                        jsonObject.put("Action", "PostTicketReply");
                        JSONObject jsonObject1 = new JSONObject();
                        jsonObject1.put("Comment", et_message.getText().toString().trim());
                        jsonObject.put("data", jsonObject1.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Utils.showLoading(activity);
                    ModelManager.getInstance().getRestOfAllManager().sendSuggestion(activity, jsonObject);
                }
                break;
        }
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
        if (message.contains("SendSuggestion True")) {
            Utils.dismissLoading();
            if (message.contains("@#@")) {
                String[] m = message.split("@#@");
                Utils.showMessage(activity, m[1]);
            }
            finish();
            PPLog.e(TAG, "SendSuggestion True");
        } else if (message.contains("SendSuggestion False")) {
            // showMatchHistoryList();
            if (message.contains("@#@")) {
                String[] m = message.split("@#@");
                Utils.showMessage(activity, m[1]);
            } else
                Utils.showMessage(activity, getString(R.string.error_message));
            PPLog.e(TAG, "SendSuggestion False");
            Utils.dismissLoading();
        }

    }

}
