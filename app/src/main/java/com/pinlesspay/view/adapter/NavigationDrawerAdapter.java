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
import com.pinlesspay.model.NavDrawerItem;

import java.util.Collections;
import java.util.List;


public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder> {
    private List<NavDrawerItem> data = Collections.emptyList();
    private LayoutInflater inflater;
    //    private Context context;
    private Integer[] icons = {R.drawable.icon_payment,
            R.drawable.icon_security, R.drawable.icon_share,
            R.drawable.icon_support, R.drawable.icon_suggestion,
            R.drawable.icon_logout};


    public NavigationDrawerAdapter(Context context, List<NavDrawerItem> data) {
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
        View view = inflater.inflate(R.layout.nav_drawer_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NavDrawerItem current = data.get(position);
        holder.title.setText(current.getTitle());
        holder.ic_option.setImageResource(icons[position]);
        if (position == 1 && position == 4)
            holder.line_view.setVisibility(View.VISIBLE);
        else
            holder.line_view.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView ic_option;
        View line_view;

        MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            ic_option = (ImageView) itemView.findViewById(R.id.icon_menu_option);
            line_view = itemView.findViewById(R.id.line_view);
        }
    }
}
