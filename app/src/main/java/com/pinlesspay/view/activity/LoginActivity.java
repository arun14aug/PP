package com.pinlesspay.view.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;

import com.hbb20.CountryCodePicker;
import com.pinlesspay.R;
import com.pinlesspay.customUi.MyButton;
import com.pinlesspay.customUi.MyEditText;
import com.pinlesspay.customUi.MyTextView;
import com.pinlesspay.model.ModelManager;
import com.pinlesspay.utility.PPLog;
import com.pinlesspay.utility.Preferences;
import com.pinlesspay.utility.ServiceApi;
import com.pinlesspay.utility.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.UUID;

import de.greenrobot.event.EventBus;

/*
 * Created by arun.sharma on 7/17/2017.
 */

public class LoginActivity extends Activity {

    private String TAG = LoginActivity.this.getClass().getName();
    private MyEditText edt_phone_number;
    private MyButton btn_next;
    private boolean isBtnEnable = false;
    private Activity activity;
    private String mobile = "", countryCode = "";
    private CountryCodePicker namePicker;
    private MyTextView codePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        activity = LoginActivity.this;

        edt_phone_number = (MyEditText) findViewById(R.id.et_phone_number);
        btn_next = (MyButton) findViewById(R.id.btn_next);

        codePicker = (MyTextView) findViewById(R.id.txt_country_code);
        namePicker = (CountryCodePicker) findViewById(R.id.txt_country_name);


        namePicker.setOnCountryChangeListener(countryChangeListener);
        codePicker.setText(namePicker.getSelectedCountryCodeWithPlus());

        if (!Utils.isEmptyString(Preferences.readString(activity, Preferences.UUID, ""))) {
            Preferences.writeString(activity, Preferences.UUID, UUID.randomUUID().toString());
        }

        // get mac address
        if (!Utils.isEmptyString(Preferences.readString(activity, Preferences.MAC_ADDRESS, ""))) {
            Preferences.writeString(activity, Preferences.MAC_ADDRESS, getMacAddress());
        }

        // get device name
        if (!Utils.isEmptyString(Preferences.readString(activity, Preferences.DEVICE_NAME, ""))) {
            Preferences.writeString(activity, Preferences.DEVICE_NAME, getDeviceName());
        }

        edt_phone_number.addTextChangedListener(new PhoneNumberFormattingTextWatcher() {
            //we need to know if the user is erasing or inputing some new character
            private boolean backspacingFlag = false;
            //we need to block the :afterTextChanges method to be called again after we just replaced the EditText text
            private boolean editedFlag = false;
            //we need to mark the cursor position and restore it after the edition
            private int cursorComplement;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //we store the cursor local relative to the end of the string in the EditText before the edition
                cursorComplement = s.length() - edt_phone_number.getSelectionStart();
                //we check if the user ir inputing or erasing a character
                backspacingFlag = count > after;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // nothing to do here =D
            }

            @Override
            public void afterTextChanged(Editable s) {
                String string = s.toString();
                //what matters are the phone digits beneath the mask, so we always work with a raw string with only digits
                String phone = string.replaceAll("[^\\d]", "");

                if (phone.length() >= 10) {
                    isBtnEnable = true;
                    btn_next.setBackgroundResource(R.drawable.button_blue_bg);
                    btn_next.setTextColor(Utils.setColor(activity, R.color.white));
                } else {
                    isBtnEnable = false;
                    btn_next.setBackgroundResource(R.drawable.button_disable_bg);
                    btn_next.setTextColor(Utils.setColor(activity, R.color.dark_grey));
                }
                //if the text was just edited, :afterTextChanged is called another time... so we need to verify the flag of edition
                //if the flag is false, this is a original user-typed entry. so we go on and do some magic
                if (!editedFlag) {

                    //we start verifying the worst case, many characters mask need to be added
                    //example: 999999999 <- 6+ digits already typed
                    // masked: (999) 999-999
                    if (phone.length() >= 6 && !backspacingFlag) {
                        //we will edit. next call on this textWatcher will be ignored
                        editedFlag = true;
                        //here is the core. we substring the raw digits and add the mask as convenient
                        String ans = "(" + phone.substring(0, 3) + ") " + phone.substring(3, 6) + "-" + phone.substring(6);
                        edt_phone_number.setText(ans);
                        //we deliver the cursor to its original position relative to the end of the string
                        edt_phone_number.setSelection(edt_phone_number.getText().length() - cursorComplement);

                        //we end at the most simple case, when just one character mask is needed
                        //example: 99999 <- 3+ digits already typed
                        // masked: (999) 99
                    } else if (phone.length() >= 3 && !backspacingFlag) {
                        editedFlag = true;
                        String ans = "(" + phone.substring(0, 3) + ") " + phone.substring(3);
                        edt_phone_number.setText(ans);
                        edt_phone_number.setSelection(edt_phone_number.getText().length() - cursorComplement);
                    }
                    // We just edited the field, ignoring this cicle of the watcher and getting ready for the next
                } else {
                    editedFlag = false;
                }

                mobile = phone;
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PPLog.e("Selected Country code : ", codePicker.getText().toString() + mobile);
                if (isBtnEnable) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("OrganizationKey", ServiceApi.ORGANISATION_KEY);
                        jsonObject.put("RegisterMobile", countryCode + mobile);
                        jsonObject.put("DeviceIdentifier", Preferences.readString(activity, Preferences.MAC_ADDRESS, ""));
                        jsonObject.put("DeviceName", Preferences.readString(activity, Preferences.DEVICE_NAME, ""));
                        jsonObject.put("DeviceType", "Mobile");
                        jsonObject.put("Appkey", Preferences.readString(activity, Preferences.UUID, ""));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    PPLog.e("JSON Data : ", jsonObject.toString());
                    Utils.showLoading(activity);
                    ModelManager.getInstance().getAuthManager().logIn(activity, jsonObject);
                }
            }
        });
    }

    @SuppressLint("WifiManagerLeak")
    private String getMacAddress() {
        WifiManager manager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        @SuppressLint("HardwareIds") String address = info.getMacAddress();
//        Preferences.writeString(activity, Preferences.MAC_ADDRESS, address);
        return address;
    }

    /**
     * Returns the consumer friendly device name
     */
    private String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;
        String phrase = "";
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase += Character.toUpperCase(c);
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase += c;
        }
        return phrase;
    }


    CountryCodePicker.OnCountryChangeListener countryChangeListener = new CountryCodePicker.OnCountryChangeListener() {
        @Override
        public void onCountrySelected() {
            codePicker.setText(namePicker.getSelectedCountryCodeWithPlus());
            countryCode = namePicker.getSelectedCountryCode();
        }
    };

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
        if (message.equalsIgnoreCase("Login True")) {
            Utils.dismissLoading();
            PPLog.e(TAG, "Login True");
            startActivity(new Intent(activity, VerifyActivity.class));
            finish();
        } else if (message.contains("Login False")) {
            // showMatchHistoryList();
            Utils.showMessage(activity, "Please check your credentials!");
            PPLog.e(TAG, "Login False");
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("Login Network Error")) {
            Utils.showMessage(activity, "Network Error! Please try again");
            PPLog.e(TAG, "Login Network Error");
            Utils.dismissLoading();
        }

    }
}
