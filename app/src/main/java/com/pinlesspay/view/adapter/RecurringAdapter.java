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
import com.pinlesspay.model.Recurring;

import java.util.ArrayList;


public class RecurringAdapter extends RecyclerView.Adapter<RecurringAdapter.MyViewHolder> {
    private ArrayList<Recurring> data;
    private LayoutInflater inflater;


    public RecurringAdapter(Context context, ArrayList<Recurring> data) {
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
        View view = inflater.inflate(R.layout.row_recurring_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Recurring recurring = data.get(position);

//        DateFormat format = new SimpleDateFormat("mm-dd-yyyy", Locale.ENGLISH);
//        Date date = null;
//        try {
//            date = format.parse(current.getTaskDate());
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        System.out.println(date); // Sat Jan 02 00:00:00 GMT 2010
//
        holder.txt_recurring_time.setText(recurring.getScheduleStartDate());
        holder.txt_next_date.setText(recurring.getNextScheduleRunDate());
        holder.txt_recurring_heading.setText(recurring.getDonationName());
        holder.txt_amount.setText(recurring.getPaymentFrom());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_recurring_time, txt_next_date, txt_recurring_heading, txt_amount;
        ImageView icon_account;

        MyViewHolder(View itemView) {
            super(itemView);
            txt_recurring_time = (TextView) itemView.findViewById(R.id.txt_recurring_time);
            txt_next_date = (TextView) itemView.findViewById(R.id.txt_next_date);
            txt_recurring_heading = (TextView) itemView.findViewById(R.id.txt_recurring_heading);
            txt_amount = (TextView) itemView.findViewById(R.id.txt_amount);
        }
    }
}