package com.pinlesspay.view.adapter;

/*
 * Created by arun.sharma on 29/07/15.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pinlesspay.R;
import com.pinlesspay.customUi.MyTextView;
import com.pinlesspay.model.Schedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


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

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Schedule current = data.get(position);


        SimpleDateFormat format1 = new SimpleDateFormat("MM-dd-yyyy");
        SimpleDateFormat format2 = new SimpleDateFormat("dd");
        SimpleDateFormat format3 = new SimpleDateFormat("MMM");
        Date date = null;
        try {
            date = format1.parse(current.getTaskDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(format2.format(date));


        holder.txt_day.setText(format2.format(date));
        holder.txt_description.setText(current.getTaskDescription());
        holder.txt_month.setText(format3.format(date));
        holder.txt_schedule.setText(current.getTaskTitle());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        MyTextView txt_day, txt_month, txt_description, txt_schedule;

        MyViewHolder(View itemView) {
            super(itemView);
            txt_day = (MyTextView) itemView.findViewById(R.id.txt_day);
            txt_month = (MyTextView) itemView.findViewById(R.id.txt_month);
            txt_description = (MyTextView) itemView.findViewById(R.id.txt_description);
            txt_schedule = (MyTextView) itemView.findViewById(R.id.txt_schedule);
        }
    }
}
