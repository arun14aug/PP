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
import com.pinlesspay.view.activity.NewTicketActivity;

import java.util.ArrayList;


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


        holder.txt_date.setText(current.getDateCreated());
        holder.txt_description.setText(current.getTicketShortDesc());
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
