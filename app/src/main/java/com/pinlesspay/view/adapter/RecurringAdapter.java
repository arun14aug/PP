package com.pinlesspay.view.adapter;

/*
 * Created by arun.sharma on 29/07/15.
 */

import android.app.Activity;
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
    private Activity activity;


    public RecurringAdapter(Activity context, ArrayList<Recurring> data) {
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
        holder.txt_recurring_time.setText(recurring.getScheduleName());
        holder.txt_next_date.setText(recurring.getNextScheduleRunDate());
        holder.txt_recurring_heading.setText(recurring.getDonationName());
        holder.txt_amount.setText(activity.getString(R.string.dollar) + recurring.getDonationAmount());

        if (recurring.getAccountType().equalsIgnoreCase("Card")) {
            if (recurring.getCardType().equalsIgnoreCase("MasterCard"))
                holder.icon_account.setImageResource(R.drawable.mastercard_round);
            else if (recurring.getCardType().equalsIgnoreCase("Amex"))
                holder.icon_account.setImageResource(R.drawable.american_round);
            else if (recurring.getCardType().equalsIgnoreCase("Discover"))
                holder.icon_account.setImageResource(R.drawable.discover_round);
            else if (recurring.getCardType().equalsIgnoreCase("Visa"))
                holder.icon_account.setImageResource(R.drawable.visa_round);
            else if (recurring.getCardType().equalsIgnoreCase("DInnersClub"))
                holder.icon_account.setImageResource(R.drawable.visa_round);
            else if (recurring.getCardType().equalsIgnoreCase("JCB"))
                holder.icon_account.setImageResource(R.drawable.visa_round);
            else if (recurring.getCardType().equalsIgnoreCase("DINERS"))
                holder.icon_account.setImageResource(R.drawable.visa_round);
        } else if (recurring.getAccountType().equalsIgnoreCase("Bank"))
            holder.icon_account.setImageResource(R.drawable.bank_round);
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

            icon_account = (ImageView) itemView.findViewById(R.id.icon_account);
        }
    }
}
