package com.pinlesspay.view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.pinlesspay.R;
import com.pinlesspay.customUi.MyTextView;
import com.pinlesspay.model.DonorDevice;
import com.pinlesspay.model.ModelManager;
import com.pinlesspay.utility.PPLog;
import com.pinlesspay.utility.Preferences;
import com.pinlesspay.utility.Utils;
import com.pinlesspay.view.adapter.DeviceAdapter;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/*
 * Created by arun.sharma on 8/2/2017.
 */

public class SecurityActivity extends Activity {

    private String TAG = SecurityActivity.this.getClass().getName();
    private Activity activity;
    private RecyclerView device_list;
    private ArrayList<DonorDevice> donorDeviceArrayList;
    private MyTextView txt_turn_passcode_on;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);

        activity = SecurityActivity.this;

        device_list = (RecyclerView) findViewById(R.id.device_list);

        txt_turn_passcode_on = (MyTextView) findViewById(R.id.txt_turn_passcode_on);

        ImageView img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Utils.showLoading(activity);
        ModelManager.getInstance().getRestOfAllManager().getDevices(activity, true);

        device_list.addOnItemTouchListener(new RecyclerTouchListener(activity, device_list, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                showAlert(donorDeviceArrayList.get(position).getDeviceName(), 0);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        txt_turn_passcode_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Preferences.readBoolean(activity, Preferences.PASSCODE_TURN_ON, false))
                    if (Preferences.readString(activity, Preferences.PASSCODE_VALUE, "").length() > 0)
                        showAlert("", 1);
                    else
                        startActivity(new Intent(activity, PassCodeConfirmationActivity.class));
                else
                    startActivity(new Intent(activity, PassCodeConfirmationActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Preferences.readBoolean(activity, Preferences.PASSCODE_TURN_ON, false))
            if (Preferences.readString(activity, Preferences.PASSCODE_VALUE, "").length() > 0)
                txt_turn_passcode_on.setText(getString(R.string.turn_passcode_off));
            else
                txt_turn_passcode_on.setText(getString(R.string.turn_passcode_on));
        else
            txt_turn_passcode_on.setText(getString(R.string.turn_passcode_on));
    }

    private void showAlert(String msg, final int type) {
        String message;
        if (type == 0)
            message = getString(R.string.delete) + " " + msg + "?";
        else
            message = getString(R.string.passcode_delete_alert);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .setPositiveButton(getString(R.string.caps_delete), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (type == 0)
                            Utils.showMessage(activity, "Delete operation will be performed");
                        else {
                            Utils.showMessage(activity, "Passcode Turned Off");
                            Preferences.writeString(activity, Preferences.LOGOUT, "false");
                            Preferences.writeString(activity, Preferences.PASSCODE_VALUE, "");
                            Preferences.writeBoolean(activity, Preferences.PASSCODE_TURN_ON, false);
                            txt_turn_passcode_on.setText(getString(R.string.turn_passcode_on));
                        }
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // show it
        alertDialog.show();
    }

    private void setData() {
        DeviceAdapter adapter = new DeviceAdapter(activity, donorDeviceArrayList);
        device_list.setAdapter(adapter);
        device_list.setLayoutManager(new LinearLayoutManager(activity));
    }

    interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    private class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
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
        if (message.equalsIgnoreCase("GetDevices True")) {
            Utils.dismissLoading();
            PPLog.e(TAG, "GetDevices True");
            donorDeviceArrayList = ModelManager.getInstance().getRestOfAllManager().getDevices(activity, false);
            if (donorDeviceArrayList != null)
                if (donorDeviceArrayList.size() > 0)
                    setData();

        } else if (message.contains("GetDevices False")) {
            // showMatchHistoryList();
            if (message.contains("@#@")) {
                String[] m = message.split("@#@");
                Utils.showMessage(activity, m[1]);
            } else
                Utils.showMessage(activity, getString(R.string.error_message));
            PPLog.e(TAG, "GetDevices False");
            Utils.dismissLoading();
        }

    }

}
