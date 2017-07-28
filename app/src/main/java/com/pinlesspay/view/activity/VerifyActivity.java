package com.pinlesspay.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.pinlesspay.R;
import com.pinlesspay.customUi.MyEditText;
import com.pinlesspay.customUi.MyTextView;
import com.pinlesspay.model.ModelManager;
import com.pinlesspay.utility.PPLog;
import com.pinlesspay.utility.Preferences;
import com.pinlesspay.utility.ServiceApi;
import com.pinlesspay.utility.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import de.greenrobot.event.EventBus;

/*
 * Created by arun.sharma on 7/17/2017.
 */

public class VerifyActivity extends Activity implements View.OnFocusChangeListener {

    private String TAG = VerifyActivity.this.getClass().getName();
    private MyEditText et_code_1, et_code_2, et_code_3, et_code_4;
    private View vw_code_1, vw_code_2, vw_code_3, vw_code_4;
    private MyTextView txt_phone_number, txt_resend;
    private boolean isBtnEnable = false;
    private Activity activity;
    private String mobile = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        activity = VerifyActivity.this;

        et_code_1 = (MyEditText) findViewById(R.id.edt_code_1);
        et_code_2 = (MyEditText) findViewById(R.id.edt_code_2);
        et_code_3 = (MyEditText) findViewById(R.id.edt_code_3);
        et_code_4 = (MyEditText) findViewById(R.id.edt_code_4);

        vw_code_1 = findViewById(R.id.vw_code_1);
        vw_code_2 = findViewById(R.id.vw_code_2);
        vw_code_3 = findViewById(R.id.vw_code_3);
        vw_code_4 = findViewById(R.id.vw_code_4);

        txt_resend = (MyTextView) findViewById(R.id.txt_resend_code);
        txt_phone_number = (MyTextView) findViewById(R.id.txt_phone_number);

        et_code_1.setOnFocusChangeListener(this);
        et_code_2.setOnFocusChangeListener(this);
        et_code_3.setOnFocusChangeListener(this);
        et_code_4.setOnFocusChangeListener(this);

        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                String time = String.format("%d:%d",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))
                );
                txt_resend.setText(getString(R.string.resend_code_within) + " " + time);
            }

            public void onFinish() {
                txt_resend.setText(getString(R.string.resend_code));
            }
        }.start();


        et_code_1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0)
                    et_code_2.requestFocus();
                else
                    et_code_1.requestFocus();
            }
        });
        et_code_2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0)
                    et_code_3.requestFocus();
                else
                    et_code_1.requestFocus();
            }
        });
        et_code_3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0)
                    et_code_4.requestFocus();
                else
                    et_code_2.requestFocus();
            }
        });
        et_code_4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    if (validate()) {
                        String code = et_code_1.getText().toString() +
                                et_code_2.getText().toString() +
                                et_code_3.getText().toString() +
                                et_code_4.getText().toString();
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("OrganizationKey", ServiceApi.ORGANISATION_KEY);
                            jsonObject.put("RegisterMobile", Preferences.readString(activity, Preferences.MOBILE_NUMBER, ""));
                            jsonObject.put("DeviceIdentifier", Preferences.readString(activity, Preferences.MAC_ADDRESS, ""));
                            jsonObject.put("DeviceName", Preferences.readString(activity, Preferences.DEVICE_NAME, ""));
                            jsonObject.put("DeviceType", "Mobile");
                            jsonObject.put("Appkey", Preferences.readString(activity, Preferences.UUID, ""));
                            jsonObject.put("OTPCode", code);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Utils.showLoading(activity);
                        ModelManager.getInstance().getAuthManager().verifyUser(activity, jsonObject);
                    }
                } else
                    et_code_3.requestFocus();
            }
        });
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        switch (v.getId()) {
            case R.id.edt_code_1:
                if (hasFocus) {
                    vw_code_1.setBackgroundColor(Utils.setColor(activity, R.color.blue));
                    vw_code_2.setBackgroundColor(Utils.setColor(activity, R.color.login_line_color));
                    vw_code_3.setBackgroundColor(Utils.setColor(activity, R.color.login_line_color));
                    vw_code_4.setBackgroundColor(Utils.setColor(activity, R.color.login_line_color));
                }
                break;
            case R.id.edt_code_2:
                if (hasFocus) {
                    vw_code_2.setBackgroundColor(Utils.setColor(activity, R.color.blue));
                    vw_code_1.setBackgroundColor(Utils.setColor(activity, R.color.login_line_color));
                    vw_code_3.setBackgroundColor(Utils.setColor(activity, R.color.login_line_color));
                    vw_code_4.setBackgroundColor(Utils.setColor(activity, R.color.login_line_color));
                }
                break;
            case R.id.edt_code_3:
                if (hasFocus) {
                    vw_code_3.setBackgroundColor(Utils.setColor(activity, R.color.blue));
                    vw_code_2.setBackgroundColor(Utils.setColor(activity, R.color.login_line_color));
                    vw_code_1.setBackgroundColor(Utils.setColor(activity, R.color.login_line_color));
                    vw_code_4.setBackgroundColor(Utils.setColor(activity, R.color.login_line_color));
                }
                break;
            case R.id.edt_code_4:
                if (hasFocus) {
                    vw_code_4.setBackgroundColor(Utils.setColor(activity, R.color.blue));
                    vw_code_2.setBackgroundColor(Utils.setColor(activity, R.color.login_line_color));
                    vw_code_3.setBackgroundColor(Utils.setColor(activity, R.color.login_line_color));
                    vw_code_1.setBackgroundColor(Utils.setColor(activity, R.color.login_line_color));
                }
                break;
        }
    }


    private boolean validate() {
        if (et_code_1.getText().toString().trim().length() == 0) {
            Utils.showMessage(activity, getString(R.string.please_enter_code));
            et_code_1.requestFocus();
            return false;
        } else if (et_code_2.getText().toString().trim().length() == 0) {
            Utils.showMessage(activity, getString(R.string.please_enter_code));
            et_code_2.requestFocus();
            return false;
        } else if (et_code_3.getText().toString().trim().length() == 0) {
            Utils.showMessage(activity, getString(R.string.please_enter_code));
            et_code_3.requestFocus();
            return false;
        } else if (et_code_4.getText().toString().trim().length() == 0) {
            Utils.showMessage(activity, getString(R.string.please_enter_code));
            et_code_4.requestFocus();
            return false;
        }
        return true;
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
        if (message.equalsIgnoreCase("Verify True")) {
            Utils.dismissLoading();
            PPLog.e(TAG, "Verify True");
            Preferences.writeString(activity, Preferences.OTP_SENT, "");
            Preferences.writeString(activity, Preferences.LOGIN, "true");
            startActivity(new Intent(activity, MainActivity.class));
            finish();
        } else if (message.contains("Verify False")) {
            // showMatchHistoryList();
            Utils.showMessage(activity, "Please check your credentials!");
            PPLog.e(TAG, "Verify False");
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("Verify Network Error")) {
            Utils.showMessage(activity, "Network Error! Please try again");
            PPLog.e(TAG, "Logout Network Error");
            Utils.dismissLoading();
        }

    }
}
