package com.pinlesspay.view.adapter;

/*
 * Created by Ravi on 29/07/15.
 */

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pinlesspay.R;
import com.pinlesspay.model.Transaction;

import java.util.ArrayList;


public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.MyViewHolder> {
    private ArrayList<Transaction> data;
    private LayoutInflater inflater;
    private Activity activity;


    public TransactionAdapter(Activity context, ArrayList<Transaction> data) {
        this.activity = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.row_transaction_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Transaction transaction = data.get(position);

        holder.txt_transaction_time.setText(activity.getString(R.string.hash_tag) + transaction.getInvoiceNo());
        holder.txt_next_date.setText(transaction.getTranDate());
        holder.txt_transaction_heading.setText(transaction.getDonationName());
        holder.txt_amount.setText(activity.getString(R.string.dollar) + transaction.getTranAmount());

        if (transaction.getPaymentType().equalsIgnoreCase("Card")) {
            if (transaction.getCardTypeCode().equalsIgnoreCase("MasterCard"))
                holder.icon_account.setImageResource(R.drawable.mastercard_round);
            else if (transaction.getCardTypeCode().equalsIgnoreCase("Amex"))
                holder.icon_account.setImageResource(R.drawable.american_round);
            else if (transaction.getCardTypeCode().equalsIgnoreCase("Discover"))
                holder.icon_account.setImageResource(R.drawable.discover_round);
            else if (transaction.getCardTypeCode().equalsIgnoreCase("Visa"))
                holder.icon_account.setImageResource(R.drawable.visa_round);
            else if (transaction.getCardTypeCode().equalsIgnoreCase("DInnersClub"))
                holder.icon_account.setImageResource(R.drawable.visa_round);
            else if (transaction.getCardTypeCode().equalsIgnoreCase("JCB"))
                holder.icon_account.setImageResource(R.drawable.visa_round);
            else if (transaction.getCardTypeCode().equalsIgnoreCase("DINERS"))
                holder.icon_account.setImageResource(R.drawable.visa_round);
        } else if (transaction.getPaymentType().equalsIgnoreCase("Bank"))
            holder.icon_account.setImageResource(R.drawable.bank_round);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_transaction_time, txt_next_date, txt_transaction_heading, txt_amount;
        ImageView icon_account;

        MyViewHolder(View itemView) {
            super(itemView);
            txt_transaction_time = (TextView) itemView.findViewById(R.id.txt_transaction_time);
            txt_next_date = (TextView) itemView.findViewById(R.id.txt_next_date);
            txt_transaction_heading = (TextView) itemView.findViewById(R.id.txt_transaction_heading);
            txt_amount = (TextView) itemView.findViewById(R.id.txt_amount);

            icon_account = (ImageView) itemView.findViewById(R.id.icon_account);
        }
    }
}
