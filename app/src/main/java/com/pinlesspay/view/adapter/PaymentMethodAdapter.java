package com.pinlesspay.view.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pinlesspay.R;
import com.pinlesspay.customUi.MyTextView;

import java.util.ArrayList;

public class PaymentMethodAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_ITEM = 2;

    private ArrayList<String> stringArrayList;
    private Activity activity;

    public PaymentMethodAdapter(Activity activity, ArrayList<String> strings) {
        this.activity = activity;
        this.stringArrayList = strings;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            //Inflating recycle view item layout
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_payment_list, parent, false);
            return new ItemViewHolder(itemView);
        } else if (viewType == TYPE_HEADER) {
            //Inflating header view
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_payment_header, parent, false);
            return new HeaderViewHolder(itemView);
        } else if (viewType == TYPE_FOOTER) {
            //Inflating footer view
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_payment_footer, parent, false);
            return new FooterViewHolder(itemView);
        } else return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            headerHolder.headerTitle.setText("Header View");
            headerHolder.headerTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(activity, "You clicked at Header View!", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (holder instanceof FooterViewHolder) {
            FooterViewHolder footerHolder = (FooterViewHolder) holder;
            footerHolder.footerText.setText("Footer View");
            footerHolder.footerText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(activity, "You clicked at Footer View", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.txt_card_name.setText("Recycler Item " + position);
            itemViewHolder.txt_card_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(activity, "You clicked at item " + position, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else if (position == stringArrayList.size() + 1) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return stringArrayList.size() + 2;
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView headerTitle;

        HeaderViewHolder(View view) {
            super(view);
            headerTitle = (TextView) view.findViewById(R.id.header_text);
        }
    }

    private class FooterViewHolder extends RecyclerView.ViewHolder {
        TextView footerText;

        FooterViewHolder(View view) {
            super(view);
            footerText = (TextView) view.findViewById(R.id.footer_text);
        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        MyTextView txt_card_name, txt_card_number;
        ImageView icon_account;

        ItemViewHolder(View itemView) {
            super(itemView);
            txt_card_name = (MyTextView) itemView.findViewById(R.id.txt_card_name);
            txt_card_number = (MyTextView) itemView.findViewById(R.id.txt_card_number);
        }
    }
}
