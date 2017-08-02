package com.pinlesspay.view.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.TelephonyManager;
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

import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.RECEIVE_SMS;

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
    private final int ASK_MULTIPLE_PERMISSION_REQUEST_CODE = 0x01;

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

        if (!checkPermission())
            requestPermission();
        else {
            if (Utils.isEmptyString(Preferences.readString(activity, Preferences.UUID, ""))) {
                Preferences.writeString(activity, Preferences.UUID, UUID.randomUUID().toString());
            }

            // get mac address
            if (Utils.isEmptyString(Preferences.readString(activity, Preferences.MAC_ADDRESS, ""))) {
                Preferences.writeString(activity, Preferences.MAC_ADDRESS, getMacAddress());
            }

            // get device name
            if (Utils.isEmptyString(Preferences.readString(activity, Preferences.DEVICE_NAME, ""))) {
                Preferences.writeString(activity, Preferences.DEVICE_NAME, getDeviceName());
            }
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

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_STATE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_SMS);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), RECEIVE_SMS);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
                && result2 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        String[] permissions = new String[]{READ_PHONE_STATE, READ_SMS, RECEIVE_SMS};
        ActivityCompat.requestPermissions(this, permissions, ASK_MULTIPLE_PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull final String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case ASK_MULTIPLE_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    final String[] perm = new String[]{READ_PHONE_STATE, READ_SMS, RECEIVE_SMS};
                    boolean phoneState = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean readSMS = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean receiveSMS = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    if (!(phoneState && readSMS && receiveSMS)) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(READ_PHONE_STATE)) {
                                showMessageOKCancel(getString(R.string.permission_alert),
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(perm,
                                                            ASK_MULTIPLE_PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            } else {
                                if (Utils.isEmptyString(Preferences.readString(activity, Preferences.UUID, ""))) {
                                    Preferences.writeString(activity, Preferences.UUID, UUID.randomUUID().toString());
                                }

                                // get mac address
                                if (Utils.isEmptyString(Preferences.readString(activity, Preferences.MAC_ADDRESS, ""))) {
                                    Preferences.writeString(activity, Preferences.MAC_ADDRESS, getMacAddress());
                                }

                                // get device name
                                if (Utils.isEmptyString(Preferences.readString(activity, Preferences.DEVICE_NAME, ""))) {
                                    Preferences.writeString(activity, Preferences.DEVICE_NAME, getDeviceName());
                                }
                            }
                        }
                    } else {
                        if (Utils.isEmptyString(Preferences.readString(activity, Preferences.UUID, ""))) {
                            Preferences.writeString(activity, Preferences.UUID, UUID.randomUUID().toString());
                        }

                        // get mac address
                        if (Utils.isEmptyString(Preferences.readString(activity, Preferences.MAC_ADDRESS, ""))) {
                            Preferences.writeString(activity, Preferences.MAC_ADDRESS, getMacAddress());
                        }

                        // get device name
                        if (Utils.isEmptyString(Preferences.readString(activity, Preferences.DEVICE_NAME, ""))) {
                            Preferences.writeString(activity, Preferences.DEVICE_NAME, getDeviceName());
                        }
                    }
                }
                break;
        }
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(activity)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @SuppressLint("WifiManagerLeak")
    private String getMacAddress() {
        TelephonyManager telephonyManager;

        telephonyManager = (TelephonyManager) getSystemService(Context.
                TELEPHONY_SERVICE);

/*
* getDeviceId() returns the unique device ID.
* For example,the IMEI for GSM and the MEID or ESN for CDMA phones.
*/
        @SuppressLint("HardwareIds") String deviceId = telephonyManager.getDeviceId();


//        String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
//
//        PPLog.e("androidId ID : ", androidId);
        PPLog.e("Device ID : ", deviceId);

        return deviceId;
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
            Preferences.writeString(activity, Preferences.MOBILE_NUMBER, countryCode + mobile);
            Preferences.writeString(activity, Preferences.FORMATTED_MOBILE_NUMBER, edt_phone_number.getText().toString());
            startActivity(new Intent(activity, VerifyActivity.class));
            finish();
        } else if (message.contains("Login False")) {
            // showMatchHistoryList();
            if (message.contains("@#@")) {
                String[] m = message.split("@#@");
                Utils.showMessage(activity, m[1]);
            } else
                Utils.showMessage(activity, getString(R.string.error_message));
            PPLog.e(TAG, "Login False");
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("Login Network Error")) {
            Utils.showMessage(activity, getString(R.string.network_error));
            PPLog.e(TAG, "Login Network Error");
            Utils.dismissLoading();
        }

    }
}
