package com.pinlesspay.view.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pinlesspay.R;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/*
 * Created by arun.sharma on 8/10/2017.
 */

public class SpinnerAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private Activity activity;
    private String[] title;


    public SpinnerAdapter(Activity context, String[] data) {
        this.activity = context;
        mInflater = LayoutInflater.from(context);
        this.title = data;
    }

    @Override
    public int getCount() {
        return title.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ListContent holder;
        View v = convertView;
        if (v == null) {
            mInflater = (LayoutInflater) activity.getSystemService(LAYOUT_INFLATER_SERVICE);
            v = mInflater.inflate(R.layout.row_spinner_account_type, null);
            holder = new ListContent();
            holder.text = (TextView) v.findViewById(R.id.textView1);

            v.setTag(holder);
        } else {
            holder = (ListContent) v.getTag();
        }

        holder.text.setText(title[position]);

        return v;
    }

    private class ListContent {
        TextView text;
    }
}
