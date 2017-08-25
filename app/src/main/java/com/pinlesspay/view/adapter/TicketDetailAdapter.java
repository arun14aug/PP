package com.pinlesspay.view.adapter;

/*
 * Created by arun.sharma on 29/07/15.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.pinlesspay.R;
import com.pinlesspay.customUi.MyTextView;
import com.pinlesspay.model.TicketDetail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;


public class TicketDetailAdapter extends RecyclerView.Adapter<TicketDetailAdapter.MyViewHolder> {
    private ArrayList<TicketDetail> data;
    private LayoutInflater inflater;
    private Activity activity;


    public TicketDetailAdapter(Activity context, ArrayList<TicketDetail> data) {
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
        View view = inflater.inflate(R.layout.row_chat_bubbles, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final TicketDetail current = data.get(position);
        final Calendar c = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        c.set(1, 0, 1, 0, 0, 0);
        c.set(Calendar.MILLISECOND, 0);

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
        if (current.getIsUserComment().equalsIgnoreCase("Y")) {
            holder.user_layout.setVisibility(View.VISIBLE);
            holder.reply_layout.setVisibility(View.GONE);
            holder.txt_user_message.setText(current.getTicketDesc());
            holder.txt_user_message_time.setText(dateText);
        } else {
            holder.user_layout.setVisibility(View.GONE);
            holder.reply_layout.setVisibility(View.VISIBLE);
            holder.txt_response.setText(current.getTicketDesc());
            holder.txt_response_time.setText(dateText);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        MyTextView txt_user_message, txt_response, txt_response_time, txt_user_message_time;
        LinearLayout user_layout, reply_layout;

        MyViewHolder(View itemView) {
            super(itemView);
            txt_user_message = (MyTextView) itemView.findViewById(R.id.txt_user_message);
            txt_response = (MyTextView) itemView.findViewById(R.id.txt_response);
            txt_user_message_time = (MyTextView) itemView.findViewById(R.id.txt_user_message_time);
            txt_response_time = (MyTextView) itemView.findViewById(R.id.txt_response_time);
            user_layout = (LinearLayout) itemView.findViewById(R.id.user_layout);
            reply_layout = (LinearLayout) itemView.findViewById(R.id.reply_layout);
        }
    }
}
