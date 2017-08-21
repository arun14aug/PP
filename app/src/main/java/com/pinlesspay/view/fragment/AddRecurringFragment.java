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
import com.pinlesspay.model.Causes;
import com.pinlesspay.model.Frequency;
import com.pinlesspay.model.ModelManager;
import com.pinlesspay.model.PaymentAccount;
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
    private Spinner spinner_cause_type, spinner_payment_type, spinner_next_date;
    private MyEditText et_start_date, et_amount;
    //    private View vw_amount, vw_next_date;
    private String cause = "", payment = "", frequency = "", DonationID = "", AccountId = "",
            DonationScheduleId = "";
    private ArrayList<String> causeType = new ArrayList<>();
    private ArrayList<String> paymentType = new ArrayList<>();
    private ArrayList<String> frequencyType = new ArrayList<>();
    private ArrayList<Causes> causesArrayList;
    private ArrayList<PaymentAccount> paymentAccountArrayList;
    private ArrayList<Frequency> frequencyArrayList;
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
        spinner_next_date = (Spinner) rootView.findViewById(R.id.spinner_next_date);

        et_amount = (MyEditText) rootView.findViewById(R.id.et_amount);
        et_start_date = (MyEditText) rootView.findViewById(R.id.et_start_date);

        MyButton btn_save = (MyButton) rootView.findViewById(R.id.btn_save);
        btn_save.setOnClickListener(this);
        img_back.setOnClickListener(this);
        et_start_date.setOnClickListener(this);

        // set spinner adapter
        causeType.add(activity.getString(R.string.category));
        paymentType.add(activity.getString(R.string.payment_method));
        frequencyType.add(activity.getString(R.string.schedule));
        setData();

        spinner_cause_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                cause = causeType.get(position);
                if (position > 0)
                    DonationID = causesArrayList.get(position - 1).getDonationID();
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
                if (position > 0)
                    AccountId = paymentAccountArrayList.get(position - 1).getAccountId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });
        spinner_next_date.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                frequency = frequencyType.get(position);
                if (position > 0) {
                    DonationScheduleId = frequencyArrayList.get(position - 1).getDonationScheduleId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        Utils.showLoading(activity);
        ModelManager.getInstance().getScheduleManager().getCauses(activity, true, 1);
        return rootView;
    }

    private void setData() {
        SpinnerListAdapter adapter = new SpinnerListAdapter(activity, causeType);
        spinner_cause_type.setAdapter(adapter);
        SpinnerListAdapter adapter2 = new SpinnerListAdapter(activity, paymentType);
        spinner_payment_type.setAdapter(adapter2);
        SpinnerListAdapter adapter3 = new SpinnerListAdapter(activity, frequencyType);
        spinner_next_date.setAdapter(adapter3);
    }

    private void addList() {
        causesArrayList = ModelManager.getInstance().getScheduleManager().getCauses(activity, false, 1);
        frequencyArrayList = ModelManager.getInstance().getScheduleManager().getFrequency(activity, false, 1);
        paymentAccountArrayList = ModelManager.getInstance().getScheduleManager().getPaymentAccounts(activity, false, 1);
        if (causesArrayList == null)
            causesArrayList = new ArrayList<>();
        if (frequencyArrayList == null)
            frequencyArrayList = new ArrayList<>();
        if (paymentAccountArrayList == null)
            paymentAccountArrayList = new ArrayList<>();

        for (int i = 0; i < causesArrayList.size(); i++) {
            causeType.add(causesArrayList.get(i).getDonationName());
        }

        for (int i = 0; i < frequencyArrayList.size(); i++) {
            frequencyType.add(frequencyArrayList.get(i).getDonationScheduleName());
        }

        for (int i = 0; i < paymentAccountArrayList.size(); i++) {
            paymentType.add(paymentAccountArrayList.get(i).getAccountNumber());
        }

        setData();
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
                } else if (frequency.equalsIgnoreCase(getString(R.string.schedule))) {
                    Utils.showMessage(activity, getString(R.string.please_select_date));
                } else if (et_start_date.getText().toString().trim().length() == 0) {
                    et_start_date.requestFocus();
                    Utils.showMessage(activity, getString(R.string.please_select_date));
                } else if (payment.equalsIgnoreCase(getString(R.string.payment_method))) {
                    Utils.showMessage(activity, getString(R.string.please_select_payment_method));
                } else {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("OrganizationKey", ServiceApi.ORGANISATION_KEY);
                        jsonObject.put("Action", "SaveDonationSchedule");
                        jsonObject.put("Token", Preferences.readString(activity, Preferences.AUTH_TOKEN, ""));
                        JSONObject jsonObject1 = new JSONObject();

                        jsonObject1.put("TranAmount", et_amount.getText().toString().trim());
                        jsonObject1.put("DonationID", DonationID);
                        jsonObject1.put("AccountId", AccountId);
                        jsonObject1.put("ScheduleStartDate", et_start_date.getText().toString().trim());
                        jsonObject1.put("DonationScheduleId", DonationScheduleId);

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

            case R.id.et_start_date:
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

            et_start_date.setText(sdf.format(myCalendar.getTime()));
        }

    };


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
            ModelManager.getInstance().getScheduleManager().setArrayLists();
            ModelManager.getInstance().getScheduleManager().setScheduleArrayLists();
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
        } else if (message.contains("Causes True")) {
            Utils.dismissLoading();
            if (message.contains("@#@")) {
                String[] m = message.split("@#@");
                Utils.showMessage(activity, m[1]);
            }
            Utils.showLoading(activity);
            ModelManager.getInstance().getScheduleManager().getFrequency(activity, true, 1);
            PPLog.e(TAG, "Causes True");
        } else if (message.contains("Causes False")) {
            // showMatchHistoryList();
            if (message.contains("@#@")) {
                String[] m = message.split("@#@");
                Utils.showMessage(activity, m[1]);
            } else
                Utils.showMessage(activity, getString(R.string.error_message));
            Utils.showLoading(activity);
            ModelManager.getInstance().getScheduleManager().getFrequency(activity, true, 1);
            PPLog.e(TAG, "Causes False");
            Utils.dismissLoading();
        } else if (message.contains("PaymentAccount True")) {
            Utils.dismissLoading();
            if (message.contains("@#@")) {
                String[] m = message.split("@#@");
                Utils.showMessage(activity, m[1]);
            }
            addList();
            PPLog.e(TAG, "PaymentAccount True");
        } else if (message.contains("PaymentAccount False")) {
            // showMatchHistoryList();
            if (message.contains("@#@")) {
                String[] m = message.split("@#@");
                Utils.showMessage(activity, m[1]);
            } else
                Utils.showMessage(activity, getString(R.string.error_message));
            addList();
            PPLog.e(TAG, "PaymentAccount False");
            Utils.dismissLoading();
        } else if (message.contains("Frequency True")) {
            Utils.dismissLoading();
            if (message.contains("@#@")) {
                String[] m = message.split("@#@");
                Utils.showMessage(activity, m[1]);
            }
            Utils.showLoading(activity);
            ModelManager.getInstance().getScheduleManager().getPaymentAccounts(activity, true, 1);
            PPLog.e(TAG, "Frequency True");
        } else if (message.contains("Frequency False")) {
            // showMatchHistoryList();
            if (message.contains("@#@")) {
                String[] m = message.split("@#@");
                Utils.showMessage(activity, m[1]);
            } else
                Utils.showMessage(activity, getString(R.string.error_message));
            Utils.showLoading(activity);
            ModelManager.getInstance().getScheduleManager().getPaymentAccounts(activity, true, 1);
            PPLog.e(TAG, "Frequency False");
            Utils.dismissLoading();
        }

    }
}

