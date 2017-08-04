package com.pinlesspay.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.hbb20.CountryCodePicker;
import com.pinlesspay.R;
import com.pinlesspay.customUi.MyButton;
import com.pinlesspay.customUi.MyEditText;
import com.pinlesspay.model.ModelManager;
import com.pinlesspay.model.User;
import com.pinlesspay.utility.PPLog;
import com.pinlesspay.utility.Preferences;
import com.pinlesspay.utility.ServiceApi;
import com.pinlesspay.utility.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/*
 * Created by arun.sharma on 8/1/2017.
 */

public class ProfileActivity extends Activity {

    private String TAG = ProfileActivity.this.getClass().getName();
    private Activity activity;
    private MyEditText et_first_name, et_last_name, et_email, et_mobile,
            et_address_1, et_address_2, et_city, et_state, et_zip;
    //    private View vw_first_name, vw_last_name, vw_email, vw_mobile,
//            vw_address_1, vw_address_2, vw_city, vw_state, vw_zip;
    private CountryCodePicker namePicker;
    private String countryCode = "";
    private ArrayList<User> userArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        activity = ProfileActivity.this;

        et_first_name = (MyEditText) findViewById(R.id.et_first_name);
        et_last_name = (MyEditText) findViewById(R.id.et_last_name);
        et_email = (MyEditText) findViewById(R.id.et_email);
        et_mobile = (MyEditText) findViewById(R.id.et_mobile);
        et_address_1 = (MyEditText) findViewById(R.id.et_address_1);
        et_address_2 = (MyEditText) findViewById(R.id.et_address_2);
        et_city = (MyEditText) findViewById(R.id.et_city);
        et_state = (MyEditText) findViewById(R.id.et_state);
        et_zip = (MyEditText) findViewById(R.id.et_zip);

        namePicker = (CountryCodePicker) findViewById(R.id.txt_country_name);


        namePicker.setOnCountryChangeListener(countryChangeListener);

//        vw_first_name = findViewById(R.id.vw_first_name);
//        vw_last_name = findViewById(R.id.vw_last_name);
//        vw_email = findViewById(R.id.vw_email);
//        vw_mobile = findViewById(R.id.vw_mobile);
//        vw_address_1 = findViewById(R.id.vw_address_1);
//        vw_address_2 = findViewById(R.id.vw_address_2);
//        vw_city = findViewById(R.id.vw_city);
//        vw_state = findViewById(R.id.vw_state);
//        vw_zip = findViewById(R.id.vw_zip);

        ImageView img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        MyButton btn_save = (MyButton) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidate()) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("OrganizationKey", ServiceApi.ORGANISATION_KEY);
                        jsonObject.put("Action", "Profile");
                        jsonObject.put("Token", Preferences.readString(activity, Preferences.AUTH_TOKEN, ""));
                        JSONObject jsonObject1 = new JSONObject();
                        jsonObject1.put("FirstName", et_first_name.getText().toString().trim());
                        jsonObject1.put("LastName", et_last_name.getText().toString().trim());
                        jsonObject1.put("Email", et_email.getText().toString().trim());
                        jsonObject1.put("MobileNo", et_mobile.getText().toString().trim());
                        jsonObject1.put("City", et_city.getText().toString().trim());
                        jsonObject1.put("State", et_state.getText().toString().trim());
                        jsonObject1.put("Zip", et_zip.getText().toString().trim());
                        jsonObject1.put("CountryISO3", countryCode);
                        jsonObject1.put("Address1", et_address_1.getText().toString().trim());
                        jsonObject1.put("Address2", et_address_2.getText().toString().trim());
                        jsonObject.put("data", jsonObject1.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    PPLog.e("JSON DATA : ", jsonObject.toString());
                    Utils.showLoading(activity);
                    ModelManager.getInstance().getAuthManager().userProfile(activity, jsonObject);
                }
            }
        });

        Utils.showLoading(activity);
        ModelManager.getInstance().getAuthManager().getProfile(activity, true, 1);
    }

    CountryCodePicker.OnCountryChangeListener countryChangeListener = new CountryCodePicker.OnCountryChangeListener() {
        @Override
        public void onCountrySelected() {
            countryCode = namePicker.getSelectedCountryNameCode();
        }
    };

    private void setData() {
        if (!Utils.isEmptyString(userArrayList.get(0).getFirstName()))
            et_first_name.setText(userArrayList.get(0).getFirstName());
        if (!Utils.isEmptyString(userArrayList.get(0).getLastName()))
            et_last_name.setText(userArrayList.get(0).getLastName());
        if (!Utils.isEmptyString(userArrayList.get(0).getEmail()))
            et_email.setText(userArrayList.get(0).getEmail());
        if (!Utils.isEmptyString(userArrayList.get(0).getMobileNo()))
            et_mobile.setText(userArrayList.get(0).getMobileNo());
        if (!Utils.isEmptyString(userArrayList.get(0).getAddress1()))
            et_address_1.setText(userArrayList.get(0).getAddress1());
        if (!Utils.isEmptyString(userArrayList.get(0).getAddress2()))
            et_address_2.setText(userArrayList.get(0).getAddress2());
        if (!Utils.isEmptyString(userArrayList.get(0).getCity()))
            et_city.setText(userArrayList.get(0).getCity());
        if (!Utils.isEmptyString(userArrayList.get(0).getState()))
            et_state.setText(userArrayList.get(0).getState());
        if (!Utils.isEmptyString(userArrayList.get(0).getZip()))
            et_zip.setText(userArrayList.get(0).getZip());

        if (!Utils.isEmptyString(userArrayList.get(0).getCountry())) {
            PPLog.e("Country Code : ", userArrayList.get(0).getCountry());
            namePicker.setCountryForNameCode(userArrayList.get(0).getCountry());
            namePicker.setDefaultCountryUsingNameCode(userArrayList.get(0).getCountry());
        }
    }

    private boolean isValidate() {
        if (et_first_name.getText().toString().trim().length() == 0) {
            requestFocus(et_first_name);
            Utils.showMessage(activity, getString(R.string.please_enter_first_name));
            return false;
        } else if (et_last_name.getText().toString().trim().length() == 0) {
            requestFocus(et_last_name);
            Utils.showMessage(activity, getString(R.string.please_enter_last_name));
            return false;
        } else if (!validateEmail()) {
//            et_email.requestFocus();
            Utils.showMessage(activity, getString(R.string.please_enter_email));
            return false;
        } else if (et_mobile.getText().toString().trim().length() == 0) {
            requestFocus(et_mobile);
            Utils.showMessage(activity, getString(R.string.please_enter_mobile));
            return false;
//        } else if (et_address_1.getText().toString().trim().length() == 0
//                && et_address_2.getText().toString().trim().length() == 0) {
//            requestFocus(et_address_1);
//            Utils.showMessage(activity, getString(R.string.please_enter_address));
//            return false;
//        } else if (et_city.getText().toString().trim().length() == 0) {
//            requestFocus(et_city);
//            Utils.showMessage(activity, getString(R.string.please_enter_city));
//            return false;
//        } else if (et_state.getText().toString().trim().length() == 0) {
//            requestFocus(et_state);
//            Utils.showMessage(activity, getString(R.string.please_enter_state));
//            return false;
//        } else if (et_zip.getText().toString().trim().length() == 0) {
//            requestFocus(et_zip);
//            Utils.showMessage(activity, getString(R.string.please_enter_zip));
//            return false;
        }
        return true;
    }

    private boolean validateEmail() {
        String email = et_email.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            requestFocus(et_email);
            return false;
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
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
        if (message.contains("Profile True")) {
            Utils.dismissLoading();
            PPLog.e(TAG, "Profile True");
            String[] m = message.split("@#@");
            Utils.showMessage(activity, m[1]);
            finish();
        } else if (message.contains("Profile False")) {
            // showMatchHistoryList();
            if (message.contains("@#@")) {
                String[] m = message.split("@#@");
                Utils.showMessage(activity, m[1]);
            } else
                Utils.showMessage(activity, getString(R.string.error_message));
            PPLog.e(TAG, "Profile False");
            Utils.dismissLoading();
        } else if (message.contains("ProfileInfo True")) {
            Utils.dismissLoading();
            PPLog.e(TAG, "ProfileInfo True");
            userArrayList = ModelManager.getInstance().getAuthManager().getProfile(activity, false, 1);
            if (userArrayList != null)
                if (userArrayList.size() > 0)
                    setData();
        } else if (message.contains("ProfileInfo False")) {
            // showMatchHistoryList();
            if (message.contains("@#@")) {
                String[] m = message.split("@#@");
                Utils.showMessage(activity, m[1]);
            } else
                Utils.showMessage(activity, getString(R.string.error_message));
            PPLog.e(TAG, "Profile False");
            Utils.dismissLoading();
        }

    }
}
