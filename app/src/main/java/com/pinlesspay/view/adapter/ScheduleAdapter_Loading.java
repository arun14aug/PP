package com.pinlesspay.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pinlesspay.R;
import com.pinlesspay.customUi.MyTextView;
import com.pinlesspay.model.Schedule;

import java.util.ArrayList;


public class ScheduleAdapter_Loading extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private ArrayList<Schedule> scheduleList;
    private Context context;

    private boolean isLoadingAdded = false;

    public ScheduleAdapter_Loading(Context context, ArrayList<Schedule> scheduleList) {
        this.context = context;
        this.scheduleList = scheduleList;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.row_schedule_list, parent, false);
        viewHolder = new HolderClass(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        Schedule schedule = scheduleList.get(position);

        switch (getItemViewType(position)) {
            case ITEM:
                HolderClass scheduleVH = (HolderClass) holder;

                scheduleVH.txt_day.setText(schedule.getServiceName());
                break;
            case LOADING:
//                Do nothing
                break;
        }

    }

    @Override
    public int getItemCount() {
        return scheduleList == null ? 0 : scheduleList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == scheduleList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(Schedule schedule) {
        scheduleList.add(schedule);
        notifyItemInserted(scheduleList.size() - 1);
    }

    public void addAll(ArrayList<Schedule> scList) {
        for (Schedule schedule : scList) {
            add(schedule);
        }
    }

    public void remove(Schedule schedule) {
        int position = scheduleList.indexOf(schedule);
        if (position > -1) {
            scheduleList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Schedule());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = scheduleList.size() - 1;
        Schedule item = getItem(position);

        if (item != null) {
            scheduleList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public Schedule getItem(int position) {
        return scheduleList.get(position);
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    /**
     * Main list's content ViewHolder
     */
    protected class HolderClass extends RecyclerView.ViewHolder {
        private MyTextView txt_day, txt_month, txt_schedule, txt_description;

        public HolderClass(View itemView) {
            super(itemView);

            txt_day = (MyTextView) itemView.findViewById(R.id.txt_day);
        }
    }


    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }


}
