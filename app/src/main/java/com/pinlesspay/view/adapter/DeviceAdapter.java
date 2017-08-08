package com.pinlesspay.view.adapter;

/*
 * Created by Ravi on 29/07/15.
 */

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pinlesspay.R;
import com.pinlesspay.customUi.MyTextView;
import com.pinlesspay.model.DonorDevice;
import com.pinlesspay.utility.Preferences;

import java.util.ArrayList;


public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.MyViewHolder> {
    private ArrayList<DonorDevice> data;
    private LayoutInflater inflater;
    private Activity activity;


    public DeviceAdapter(Activity context, ArrayList<DonorDevice> data) {
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
        View view = inflater.inflate(R.layout.row_device_list, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SimpleDateFormat")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        DonorDevice current = data.get(position);


        holder.txt_device_name.setText(current.getDeviceName());
        holder.txt_type.setText(current.getDeviceType());
        if (Preferences.readString(activity, Preferences.MAC_ADDRESS, "").equalsIgnoreCase(current.getDeviceIdentifier()))
            holder.txt_current_device.setVisibility(View.VISIBLE);
        else
            holder.txt_current_device.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        MyTextView txt_device_name, txt_type, txt_current_device;

        MyViewHolder(View itemView) {
            super(itemView);
            txt_device_name = (MyTextView) itemView.findViewById(R.id.txt_device_name);
            txt_type = (MyTextView) itemView.findViewById(R.id.txt_type);
            txt_current_device = (MyTextView) itemView.findViewById(R.id.txt_current_device);
        }
    }
}
