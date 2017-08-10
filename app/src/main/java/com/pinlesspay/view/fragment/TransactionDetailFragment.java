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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/*
 * Created by arun.sharma on 7/25/2017.
 */

public class TransactionDetailFragment extends Fragment implements View.OnClickListener {

    private Activity activity;
    private ImageView icon_account;
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
        icon_account = (ImageView) rootView.findViewById(R.id.icon_account);

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
            if (transaction.getInvoiceNo().equalsIgnoreCase(id)) {
                txt_amount.setText(activity.getString(R.string.dollar) + transaction.getTranAmount());
                txt_status.setText(transaction.getStatus());

//                String format = transaction.getTranDate().replace("T", " ");
                final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
//                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
                SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy HH:mm a");
                Date date = null;
                try {
                    date = sdf.parse(transaction.getTranDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                System.out.println(format2.format(date));

                txt_date.setText(format2.format(date));
                txt_transaction_heading.setText(transaction.getDonationName());
                txt_id.setText(transaction.getInvoiceNo());
                txt_card_name.setText(transaction.getPaymentType());
                txt_card_number.setText(transaction.getPaymentFrom());

                if (transaction.getPaymentType().equalsIgnoreCase("Card")) {
                    if (transaction.getCardTypeCode().equalsIgnoreCase("MasterCard"))
                        icon_account.setImageResource(R.drawable.mastercard_round);
                    else if (transaction.getCardTypeCode().equalsIgnoreCase("Amex"))
                        icon_account.setImageResource(R.drawable.american_round);
                    else if (transaction.getCardTypeCode().equalsIgnoreCase("Discover"))
                        icon_account.setImageResource(R.drawable.discover_round);
                    else if (transaction.getCardTypeCode().equalsIgnoreCase("Visa"))
                        icon_account.setImageResource(R.drawable.visa_round);
                    else if (transaction.getCardTypeCode().equalsIgnoreCase("DinnersClub"))
                        icon_account.setImageResource(R.drawable.visa_round);
                    else if (transaction.getCardTypeCode().equalsIgnoreCase("JCB"))
                        icon_account.setImageResource(R.drawable.visa_round);
                    else if (transaction.getCardTypeCode().equalsIgnoreCase("DINERS"))
                        icon_account.setImageResource(R.drawable.visa_round);
                } else if (transaction.getPaymentType().equalsIgnoreCase("Bank"))
                    icon_account.setImageResource(R.drawable.bank_round);

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
    public void onStart() {
        super.onStart();
        mToolbar.setVisibility(View.GONE);
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
