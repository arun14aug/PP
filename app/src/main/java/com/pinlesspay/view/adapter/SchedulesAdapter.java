package com.pinlesspay.view.adapter;

/*
 * Created by Ravi on 29/07/15.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pinlesspay.R;
import com.pinlesspay.model.Schedule;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class SchedulesAdapter extends RecyclerView.Adapter<SchedulesAdapter.MyViewHolder> {
    private ArrayList<Schedule> data;
    private LayoutInflater inflater;


    public SchedulesAdapter(Context context, ArrayList<Schedule> data) {
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
        View view = inflater.inflate(R.layout.row_schedule_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Schedule current = data.get(position);

        DateFormat format = new SimpleDateFormat("mm-dd-yyyy", Locale.ENGLISH);
        Date date = null;
        try {
            date = format.parse(current.getTaskDate());

        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(date); // Sat Jan 02 00:00:00 GMT 2010

        holder.txt_day.setText(current.getTaskDate());
        holder.txt_description.setText(current.getTaskDescription());
        holder.txt_month.setText(current.getTaskDescription());
        holder.txt_schedule.setText(current.getTaskTitle());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txt_day, txt_month, txt_description, txt_schedule;

        MyViewHolder(View itemView) {
            super(itemView);
            txt_day = (TextView) itemView.findViewById(R.id.txt_day);
            txt_month = (TextView) itemView.findViewById(R.id.txt_month);
            txt_description = (TextView) itemView.findViewById(R.id.txt_description);
            txt_schedule = (TextView) itemView.findViewById(R.id.txt_schedule);
        }
    }
}
