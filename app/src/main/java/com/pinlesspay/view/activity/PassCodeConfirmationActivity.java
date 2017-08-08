package com.pinlesspay.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;

import com.pinlesspay.R;
import com.pinlesspay.customUi.MyEditText;
import com.pinlesspay.customUi.MyTextView;
import com.pinlesspay.utility.Preferences;
import com.pinlesspay.utility.Utils;

/*
 * Created by arun.sharma on 8/8/2017.
 */

public class PassCodeConfirmationActivity extends Activity implements View.OnFocusChangeListener {

    private String TAG = PassCodeConfirmationActivity.this.getClass().getName();
    private Activity activity;
    private MyTextView txt_heading;
    private MyEditText et_code_1, et_code_2, et_code_3, et_code_4;
    private View vw_code_1, vw_code_2, vw_code_3, vw_code_4;
    private String Previous_code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_passcode);

        activity = PassCodeConfirmationActivity.this;

        txt_heading = (MyTextView) findViewById(R.id.txt_heading);

        if (Preferences.readBoolean(activity, Preferences.PASSCODE_TURN_ON, false))
            txt_heading.setText(getString(R.string.confirm_passcode));
        else
            txt_heading.setText(getString(R.string.enter_new_passcode));

        et_code_1 = (MyEditText) findViewById(R.id.edt_code_1);
        et_code_2 = (MyEditText) findViewById(R.id.edt_code_2);
        et_code_3 = (MyEditText) findViewById(R.id.edt_code_3);
        et_code_4 = (MyEditText) findViewById(R.id.edt_code_4);

        vw_code_1 = findViewById(R.id.vw_code_1);
        vw_code_2 = findViewById(R.id.vw_code_2);
        vw_code_3 = findViewById(R.id.vw_code_3);
        vw_code_4 = findViewById(R.id.vw_code_4);

        ImageView img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity, LoginActivity.class));
                finish();
            }
        });

        et_code_1.setOnFocusChangeListener(this);
        et_code_2.setOnFocusChangeListener(this);
        et_code_3.setOnFocusChangeListener(this);
        et_code_4.setOnFocusChangeListener(this);

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
//                if (s.length() > 0) {
//                    if (Preferences.readBoolean(activity, Preferences.PASSCODE_TURN_ON, false)) {
//                        if (validate()) {
//                            String code = et_code_1.getText().toString() +
//                                    et_code_2.getText().toString() +
//                                    et_code_3.getText().toString() +
//                                    et_code_4.getText().toString();
//                            if (Previous_code.equalsIgnoreCase(code)) {
//                                Preferences.writeString(activity, Preferences.PASSCODE_VALUE, code);
//                                finish();
//                            } else {
//                                Utils.showMessage(activity, getString(R.string.passcode_does_not_match));
//                                clearFields();
//                            }
//                        }
//                    } else {
//                        if (validate()) {
//                            Previous_code = et_code_1.getText().toString() +
//                                    et_code_2.getText().toString() +
//                                    et_code_3.getText().toString() +
//                                    et_code_4.getText().toString();
//                            txt_heading.setText(getString(R.string.confirm_passcode));
//                            Preferences.writeBoolean(activity, Preferences.PASSCODE_TURN_ON, true);
//                            clearFields();
//                        }
//                    }
//                } else
//                    et_code_3.requestFocus();


                if (s.length() > 0) {
                    String code = "";
                    if (validate()) {
                        code = et_code_1.getText().toString() +
                                et_code_2.getText().toString() +
                                et_code_3.getText().toString() +
                                et_code_4.getText().toString();
                    }
                    if (Preferences.readBoolean(activity, Preferences.PASSCODE_TURN_ON, false)) {
                        if (Previous_code.equalsIgnoreCase(code)) {
                            Preferences.writeString(activity, Preferences.PASSCODE_VALUE, code);
                            finish();
                        } else {
                            Utils.showMessage(activity, getString(R.string.passcode_does_not_match));
                            clearFields();
                        }
                    } else {
                        Previous_code = code;
                        txt_heading.setText(getString(R.string.confirm_passcode));
                        Preferences.writeBoolean(activity, Preferences.PASSCODE_TURN_ON, true);
                        clearFields();
                    }
                } else
                    et_code_3.requestFocus();
            }
        });
    }

    private void clearFields() {
        et_code_4.setText("");
        et_code_3.setText("");
        et_code_2.setText("");
        et_code_1.setText("");
        et_code_1.requestFocus();
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
}
