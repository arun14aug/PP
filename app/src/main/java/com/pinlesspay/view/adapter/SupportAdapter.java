package com.pinlesspay.view.adapter;

/*
 * Created by arun.sharma on 29/07/15.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.pinlesspay.R;
import com.pinlesspay.customUi.MyTextView;
import com.pinlesspay.model.Ticket;
import com.pinlesspay.utility.Utils;
import com.pinlesspay.view.activity.NewTicketActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class SupportAdapter extends RecyclerView.Adapter<SupportAdapter.MyViewHolder> {
    private ArrayList<Ticket> data;
    private LayoutInflater inflater;
    private Activity activity;


    public SupportAdapter(Activity context, ArrayList<Ticket> data) {
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
        View view = inflater.inflate(R.layout.row_support_list, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final Ticket current = data.get(position);

        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        final SimpleDateFormat sd = new SimpleDateFormat("MMM dd, yyyy HH:mm a");
//        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat format2 = new SimpleDateFormat("HH:mm a");
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
        Date dt = new Date();
        Date currentDate = null;
        Date date = null;
        Date dts = null;
        try {
            currentDate = date_format.parse(date_format.format(dt));
            date = sdf.parse(current.getDateCreated());
            dts = date_format.parse(date_format.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String dateText = "";
        if (currentDate.compareTo(dts) > 0)
            dateText = sd.format(date);
        else if (currentDate.compareTo(dts) == 0)
            dateText = format2.format(date);
        System.out.println(dateText);
        holder.txt_date.setText(dateText);

//        holder.txt_date.setText(current.getDateCreated());
        holder.txt_description.setText(current.getTicketShortDesc());
        if (current.getTicketStatus().equalsIgnoreCase("N")) {
            holder.txt_date.setTextColor(Utils.setColor(activity, R.color.support_new_text));
            holder.txt_description.setTextColor(Utils.setColor(activity, R.color.schedule_heading_color));
        } else {
            holder.txt_date.setTextColor(Utils.setColor(activity, R.color.schedule_subheading_color));
            holder.txt_description.setTextColor(Utils.setColor(activity, R.color.schedule_subheading_color));
        }
        holder.row_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, NewTicketActivity.class);
                intent.putExtra("id", current.getTicketID());
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        MyTextView txt_date, txt_description;
        LinearLayout row_layout;

        MyViewHolder(View itemView) {
            super(itemView);
            txt_date = (MyTextView) itemView.findViewById(R.id.txt_date);
            txt_description = (MyTextView) itemView.findViewById(R.id.txt_description);
            row_layout = (LinearLayout) itemView.findViewById(R.id.row_layout);
        }
    }
}
