package com.pinlesspay.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pinlesspay.R;
import com.pinlesspay.customUi.MyTextView;
import com.pinlesspay.model.ModelManager;
import com.pinlesspay.model.Recurring;

import java.util.ArrayList;

/*
 * Created by arun.sharma on 7/25/2017.
 */

public class RecurringDetailFragment extends Fragment implements View.OnClickListener {

    private Activity activity;
    //    private ImageView img_back, img_menu, img_transaction_status;
    private MyTextView txt_amount, txt_recurring_time, txt_date, txt_category, txt_card_name, txt_card_number;
    private Toolbar mToolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.activity = super.getActivity();
        Intent intent = new Intent("Header");
        intent.putExtra("message", activity.getString(R.string.title_recurring));

        LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
        View rootView = inflater.inflate(R.layout.fragment_recurring_details, container, false);

        String id = "";
        try {
            if (getArguments() != null) {
                Bundle bundle = getArguments();
                id = bundle.getString("id");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            id = "";
        }

        mToolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        mToolbar.setVisibility(View.GONE);

        txt_amount = (MyTextView) rootView.findViewById(R.id.txt_amount);
        txt_recurring_time = (MyTextView) rootView.findViewById(R.id.txt_recurring_time);
        txt_date = (MyTextView) rootView.findViewById(R.id.txt_date);
        txt_category = (MyTextView) rootView.findViewById(R.id.txt_category);
        txt_card_name = (MyTextView) rootView.findViewById(R.id.txt_card_name);
        txt_card_number = (MyTextView) rootView.findViewById(R.id.txt_card_number);

        ImageView img_back = (ImageView) rootView.findViewById(R.id.img_back);
        ImageView img_menu = (ImageView) rootView.findViewById(R.id.img_menu);
        ImageView img_transaction_status = (ImageView) rootView.findViewById(R.id.img_transaction_status);

        img_back.setOnClickListener(this);
        img_menu.setOnClickListener(this);

        setData(id);

        // Inflate the layout for this fragment
        return rootView;
    }

    private void setData(String id) {
        ArrayList<Recurring> recurringArrayList = ModelManager.getInstance().getScheduleManager().getRecurring(activity, false, 1);
        for (int i = 0; i < recurringArrayList.size(); i++) {
            Recurring recurring = recurringArrayList.get(i);
            if (recurring.getDonationScheduleId().equalsIgnoreCase(id)) {
                txt_amount.setText(recurring.getPaymentFrom());
                txt_recurring_time.setText(recurring.getNextScheduleRunDate());
                txt_date.setText(recurring.getScheduleStartDate());
                txt_category.setText(recurring.getDonationName());
                txt_card_name.setText(recurring.getCardType());
                txt_card_number.setText(recurring.getMaskCardNumber());
                break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                ((FragmentActivity) activity).getSupportFragmentManager()
                        .popBackStack();
                break;
            case R.id.img_menu:
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mToolbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mToolbar.setVisibility(View.VISIBLE);
    }
}
