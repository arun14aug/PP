package com.pinlesspay.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.pinlesspay.R;
import com.pinlesspay.customUi.MyButton;
import com.pinlesspay.customUi.MyTextView;
import com.pinlesspay.model.Charity;
import com.pinlesspay.model.ModelManager;
import com.pinlesspay.utility.CustomVolleyRequestQueue;
import com.pinlesspay.utility.PPLog;
import com.pinlesspay.utility.Preferences;
import com.pinlesspay.utility.ServiceApi;
import com.pinlesspay.utility.Utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/*
 * Created by arun.sharma on 7/25/2017.
 */

public class DonationFragment extends Fragment {

    private Activity activity;
    private String TAG = DonationFragment.this.getClass().getName();
    private ArrayList<Charity> charityArrayList;
    private MyTextView txt_description;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        activity = super.getActivity();
        Intent intent = new Intent("Header");
        intent.putExtra("message", activity.getString(R.string.title_donation));

        LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
        View rootView = inflater.inflate(R.layout.fragment_donation, container, false);

        txt_description = (MyTextView) rootView.findViewById(R.id.txt_description);
        NetworkImageView imageView = (NetworkImageView) rootView.findViewById(R.id.img_logo);

        if (!Utils.isEmptyString(ServiceApi.DONATION_LOGO_URL)) {
            String url = ServiceApi.DONATION_LOGO_URL;
            Log.e(" URL : ", "" + url);
            ImageLoader mImageLoader;
            mImageLoader = new CustomVolleyRequestQueue(activity)
                    .getImageLoader();
////        if (!imageUrl.equalsIgnoreCase("null"))
            mImageLoader.get(url, ImageLoader.getImageListener(imageView,
                /*R.drawable.logo*/ 0, /*R.drawable.logo*/ 0));
            imageView.setImageUrl(url, mImageLoader);
            imageView.setTag(url);

        } else
            imageView.setImageResource(R.drawable.avatar);

        MyButton btn_make_donation = (MyButton) rootView.findViewById(R.id.btn_make_payment);
        btn_make_donation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = ServiceApi.MAKE_DONATION + Preferences.readString(activity, Preferences.AUTH_TOKEN, "");
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                activity.startActivity(intent);
                // setting array lists to null
                ModelManager.getInstance().getScheduleManager().setArrayLists();
            }
        });


        charityArrayList = ModelManager.getInstance().getScheduleManager().getCharity(activity, false, 1);
        if (charityArrayList == null) {
            Utils.showLoading(activity);
            ModelManager.getInstance().getScheduleManager().getCharity(activity, true, 1);
        } else if (charityArrayList.size() > 0)
            setData();

        // Inflate the layout for this fragment
        return rootView;
    }

    private void setData() {
        byte[] bytes; // Charset to encode into
        String s2 = "";
        try {
            bytes = charityArrayList.get(0).getDescription().getBytes("UTF-8");
            s2 = new String(bytes, "UTF-8"); // Charset with which bytes were encoded
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        try {
//            s2 = URLDecoder.decode(charityArrayList.get(0).getDescription(), "ISO-8859-1");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        txt_description.setText(s2);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);

    }

    public void onEventMainThread(String message) {
        if (message.equalsIgnoreCase("GetCharity True")) {
            Utils.dismissLoading();
            PPLog.e(TAG, "GetCharity True");
            charityArrayList = ModelManager.getInstance().getScheduleManager().getCharity(activity, false, 1);
            if (charityArrayList != null)
                if (charityArrayList.size() > 0)
                    setData();
        } else if (message.contains("GetCharity False")) {
            // showMatchHistoryList();
            if (message.contains("@#@")) {
                String[] m = message.split("@#@");
                Utils.showMessage(activity, m[1]);
            } else
                Utils.showMessage(activity, getString(R.string.error_message));
            PPLog.e(TAG, "GetCharity False");
            Utils.dismissLoading();
        }

    }
}
