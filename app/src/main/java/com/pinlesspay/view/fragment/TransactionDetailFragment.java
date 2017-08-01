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
import com.pinlesspay.model.Transaction;

import java.util.ArrayList;

/*
 * Created by arun.sharma on 7/25/2017.
 */

public class TransactionDetailFragment extends Fragment implements View.OnClickListener {

    private Activity activity;
    //    private ImageView img_back, img_menu, img_transaction_status;
    private MyTextView txt_amount, txt_status, txt_date, txt_transaction_heading, txt_id, txt_card_name, txt_card_number;
    private Toolbar mToolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.activity = super.getActivity();
        Intent intent = new Intent("Header");
        intent.putExtra("message", activity.getString(R.string.title_transactions));

        LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
        View rootView = inflater.inflate(R.layout.fragment_transaction_details, container, false);

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
        txt_status = (MyTextView) rootView.findViewById(R.id.txt_status);
        txt_date = (MyTextView) rootView.findViewById(R.id.txt_date);
        txt_transaction_heading = (MyTextView) rootView.findViewById(R.id.txt_transaction_heading);
        txt_id = (MyTextView) rootView.findViewById(R.id.txt_id);
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
        ArrayList<Transaction> transactions = ModelManager.getInstance().getScheduleManager().getTransactions(activity, false, 1);
        for (int i = 0; i < transactions.size(); i++) {
            Transaction transaction = transactions.get(i);
            if (transaction.getDonationID().equalsIgnoreCase(id)) {
                txt_amount.setText(transaction.getTranAmount());
                txt_status.setText(transaction.getProcessorAuthMsg());
                txt_date.setText(transaction.getTranDate());
                txt_transaction_heading.setText(transaction.getTranSource());
                txt_id.setText(transaction.getDonationID());
                txt_card_name.setText(transaction.getNameOnCard());
                txt_card_number.setText(transaction.getCardNumber());
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
