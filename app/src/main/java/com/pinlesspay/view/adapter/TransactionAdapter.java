package com.pinlesspay.view.adapter;

/*
 * Created by Ravi on 29/07/15.
 */

import android.content.Context;
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


    public TransactionAdapter(Context context, ArrayList<Transaction> data) {
//        this.context = context;
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

        holder.txt_transaction_time.setText(transaction.getProcessorTransID());
        holder.txt_next_date.setText(transaction.getTranDate());
        holder.txt_transaction_heading.setText(transaction.getDonationName());
        holder.txt_amount.setText(transaction.getTranAmount());

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
