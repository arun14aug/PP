package com.pinlesspay.view.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;

import com.pinlesspay.R;
import com.pinlesspay.customUi.MyButton;
import com.pinlesspay.customUi.MyEditText;
import com.pinlesspay.model.ModelManager;
import com.pinlesspay.utility.PPLog;
import com.pinlesspay.utility.Preferences;
import com.pinlesspay.utility.ServiceApi;
import com.pinlesspay.utility.Utils;
import com.pinlesspay.view.adapter.SpinnerListAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.greenrobot.event.EventBus;

/*
 * Created by HP on 09-08-2017.
 */

public class AddRecurringFragment extends Fragment implements View.OnClickListener {

    private String TAG = AddRecurringFragment.this.getClass().getName();
    private Activity activity;
    private Toolbar mToolbar;
    private Spinner spinner_cause_type, spinner_payment_type;
    private MyEditText et_next_date, et_amount;
    //    private View vw_amount, vw_next_date;
    private String cause = "", payment = "";
    private ArrayList<String> causeType = new ArrayList<>();
    private ArrayList<String> paymentType = new ArrayList<>();
    private Calendar myCalendar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.activity = super.getActivity();
        Intent intent = new Intent("Header");
        intent.putExtra("message", activity.getString(R.string.title_recurring));

        LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
        View rootView = inflater.inflate(R.layout.fragment_add_recurring, container, false);

        myCalendar = Calendar.getInstance();

        mToolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        mToolbar.setVisibility(View.GONE);
        ImageView img_back = (ImageView) rootView.findViewById(R.id.img_back);

        spinner_cause_type = (Spinner) rootView.findViewById(R.id.spinner_cause_type);
        spinner_payment_type = (Spinner) rootView.findViewById(R.id.spinner_payment_type);

        et_amount = (MyEditText) rootView.findViewById(R.id.et_amount);
        et_next_date = (MyEditText) rootView.findViewById(R.id.et_next_date);

        MyButton btn_save = (MyButton) rootView.findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);
        img_back.setOnClickListener(this);
        et_next_date.setOnClickListener(this);

//        vw_amount = rootView.findViewById(R.id.vw_amount);
//        vw_next_date = rootView.findViewById(R.id.vw_next_date);

//        et_amount.setOnFocusChangeListener(this);
//        et_next_date.setOnFocusChangeListener(this);

        causeType.add(activity.getString(R.string.category));
        paymentType.add(activity.getString(R.string.payment_method));
        SpinnerListAdapter adapter = new SpinnerListAdapter(activity, causeType);
        spinner_cause_type.setAdapter(adapter);
        SpinnerListAdapter adapter2 = new SpinnerListAdapter(activity, paymentType);
        spinner_payment_type.setAdapter(adapter2);
        spinner_cause_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                cause = causeType.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        spinner_payment_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                payment = paymentType.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
        return rootView;
    }

    private void setData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                if (et_amount.getText().toString().trim().length() == 0) {
                    et_amount.requestFocus();
                    Utils.showMessage(activity, getString(R.string.please_enter_amount));
                } else if (cause.equalsIgnoreCase(getString(R.string.category))) {
                    Utils.showMessage(activity, getString(R.string.please_select_category));
                } else if (et_next_date.getText().toString().trim().length() == 0) {
                    Utils.showMessage(activity, getString(R.string.please_select_date));
                } else if (payment.equalsIgnoreCase(getString(R.string.payment_method))) {
                    Utils.showMessage(activity, getString(R.string.please_select_payment_method));
                } else {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("OrganizationKey", ServiceApi.ORGANISATION_KEY);
                        jsonObject.put("Action", "AddRecuSchedule");
                        jsonObject.put("Token", Preferences.readString(activity, Preferences.AUTH_TOKEN, ""));
                        JSONObject jsonObject1 = new JSONObject();
                        jsonObject1.put("Amount", et_amount.getText().toString().trim());
                        jsonObject1.put("NextDate", et_next_date.getText().toString().trim());
                        jsonObject1.put("Category", cause);
                        jsonObject1.put("PaymentMethod", payment);
                        jsonObject.put("data", jsonObject1.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    PPLog.e("JSON DATA : ", jsonObject.toString());
                    Utils.showLoading(activity);
                    ModelManager.getInstance().getScheduleManager().addRecurring(activity, jsonObject);
                }
                break;
            case R.id.img_back:
                ((FragmentActivity) activity).getSupportFragmentManager()
                        .popBackStack();
                break;

            case R.id.et_next_date:
                myCalendar.add(Calendar.DATE, 0);
                Date newDate = myCalendar.getTime();
                DatePickerDialog datePickerDialog = new DatePickerDialog(activity, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(newDate.getTime());
                datePickerDialog.show();
                break;
        }
    }

    // date picker diaSPLog for date Text
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.US);

            et_next_date.setText(sdf.format(myCalendar.getTime()));
        }

    };

    //    @Override
//    public void onFocusChange(View v, boolean hasFocus) {
//        switch (v.getId()) {
//            case R.id.et_amount:
//                if (hasFocus) {
//                    et_amount.requestFocus();
//                    vw_amount.setBackgroundColor(Utils.setColor(activity, R.color.light_blue));
//                } else
//                    vw_amount.setBackgroundColor(Utils.setColor(activity, R.color.login_line_color));
//                break;
//            case R.id.et_next_date:
//                if (hasFocus) {
//                    et_next_date.requestFocus();
//                    vw_next_date.setBackgroundColor(Utils.setColor(activity, R.color.light_blue));
//                } else
//                    vw_next_date.setBackgroundColor(Utils.setColor(activity, R.color.login_line_color));
//                break;
//        }
//    }
    @Override
    public void onStop() {
        super.onStop();
        mToolbar.setVisibility(View.VISIBLE);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mToolbar.setVisibility(View.VISIBLE);
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mToolbar.setVisibility(View.GONE);
        EventBus.getDefault().register(this);
    }


    public void onEventMainThread(String message) {
        if (message.contains("AddRecurring True")) {
            Utils.dismissLoading();
            if (message.contains("@#@")) {
                String[] m = message.split("@#@");
                Utils.showMessage(activity, m[1]);
            }
            ((FragmentActivity) activity).getSupportFragmentManager()
                    .popBackStack();
            PPLog.e(TAG, "AddRecurring True");
        } else if (message.contains("AddRecurring False")) {
            // showMatchHistoryList();
            if (message.contains("@#@")) {
                String[] m = message.split("@#@");
                Utils.showMessage(activity, m[1]);
            } else
                Utils.showMessage(activity, getString(R.string.error_message));
            PPLog.e(TAG, "AddRecurring False");
            Utils.dismissLoading();
        }

    }
}

