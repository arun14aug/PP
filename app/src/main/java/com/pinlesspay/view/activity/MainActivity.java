package com.pinlesspay.view.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.pinlesspay.R;
import com.pinlesspay.customUi.MyTextView;
import com.pinlesspay.model.AuthManager;
import com.pinlesspay.model.ModelManager;
import com.pinlesspay.utility.NotificationUtils;
import com.pinlesspay.utility.PPLog;
import com.pinlesspay.utility.Preferences;
import com.pinlesspay.utility.ServiceApi;
import com.pinlesspay.utility.Utils;
import com.pinlesspay.view.fragment.DonationFragment;
import com.pinlesspay.view.fragment.RecurringFragment;
import com.pinlesspay.view.fragment.ScheduleFragment;
import com.pinlesspay.view.fragment.TransactionsFragment;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener,
        View.OnClickListener {

    private String TAG = MainActivity.class.getSimpleName();
    private Activity activity;
    private FragmentManager fragmentManager;
    private boolean backer = false;
    private MyTextView tvTitle;
    private ImageView img_donation, img_schedule, img_recurring, img_transactions;
    private FragmentDrawer drawerFragment;
    private Toolbar mToolbar;
    private String check = "";
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    @SuppressLint("StaticFieldLeak")
    public static MainActivity instance;


    public static MainActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = MainActivity.this;
        instance = MainActivity.this;

        LocalBroadcastManager.getInstance(activity).registerReceiver(
                mHeaderReceiver, new IntentFilter("Header"));

        /*      GCM Token getting and saving        */
        AuthManager authManager = ModelManager.getInstance().getAuthManager();
        String deviceId = Preferences.readString(getApplicationContext(), Preferences.GCM_TOKEN, "");
        if (Utils.isEmptyString(deviceId)) {
            mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    // checking for type intent filter
                    if (intent.getAction().equals(ServiceApi.REGISTRATION_COMPLETE)) {
                        // gcm successfully registered
                        // now subscribe to `global` topic to receive app wide notifications
                        FirebaseMessaging.getInstance().subscribeToTopic(ServiceApi.TOPIC_GLOBAL);

                    } else if (intent.getAction().equals(ServiceApi.PUSH_NOTIFICATION)) {
                        // new push notification is received

                        String message = intent.getStringExtra("message");

                        Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
                    }
                }
            };
            authManager.setDeviceToken(Preferences.readString(getApplicationContext(), Preferences.GCM_TOKEN, ""));
        } else {
            authManager.setDeviceToken(deviceId);
        }
        sendRegistrationToServer();


        img_donation = (ImageView) findViewById(R.id.img_donation);
        img_schedule = (ImageView) findViewById(R.id.img_schedule);
        img_recurring = (ImageView) findViewById(R.id.img_recurring);
        img_transactions = (ImageView) findViewById(R.id.img_transactions);

        img_donation.setOnClickListener(this);
        img_schedule.setOnClickListener(this);
        img_recurring.setOnClickListener(this);
        img_transactions.setOnClickListener(this);

        fragmentManager = getSupportFragmentManager();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitle = (MyTextView) findViewById(R.id.header_text);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        // display the first navigation drawer view on app launch
//        displayView(0);
        bottomBarFragments(0);
    }

    private void sendRegistrationToServer() {
        // sending gcm token to server
        PPLog.e(TAG, "sendRegistrationToServer: " + Preferences.readString(this, Preferences.GCM_TOKEN, ""));
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("OrganizationKey", ServiceApi.ORGANISATION_KEY);
            jsonObject.put("Action", "PostPushNtfy");
            jsonObject.put("Token", Preferences.readString(this, Preferences.AUTH_TOKEN, ""));
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("ApplicationName", getString(R.string.app_name));
            jsonObject1.put("DeviceToken", Preferences.readString(this, Preferences.GCM_TOKEN, ""));
            jsonObject1.put("Provider", "Android");
            jsonObject.put("data", jsonObject1.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ModelManager.getInstance().getAuthManager().savePushToken(this, jsonObject);
    }

    @Override
    protected void onResume() {
        super.onResume();
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(ServiceApi.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(ServiceApi.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_donation:
                img_donation.setImageResource(R.drawable.bottom_icon_donate_active);
                img_schedule.setImageResource(R.drawable.bottom_icon_schedule_normal);
                img_recurring.setImageResource(R.drawable.bottom_icon_recurring_normal);
                img_transactions.setImageResource(R.drawable.bottom_icon_transaction_normal);
                bottomBarFragments(0);
                break;
            case R.id.img_schedule:
                img_schedule.setImageResource(R.drawable.bottom_icon_schedule_active);
                img_donation.setImageResource(R.drawable.bottom_icon_donate_normal);
                img_recurring.setImageResource(R.drawable.bottom_icon_recurring_normal);
                img_transactions.setImageResource(R.drawable.bottom_icon_transaction_normal);
                bottomBarFragments(1);
                break;
            case R.id.img_recurring:
                img_recurring.setImageResource(R.drawable.bottom_icon_recurring_active);
                img_schedule.setImageResource(R.drawable.bottom_icon_schedule_normal);
                img_donation.setImageResource(R.drawable.bottom_icon_donate_normal);
                img_transactions.setImageResource(R.drawable.bottom_icon_transaction_normal);
                bottomBarFragments(2);
                break;
            case R.id.img_transactions:
                img_transactions.setImageResource(R.drawable.bottom_icon_transactions_active);
                img_schedule.setImageResource(R.drawable.bottom_icon_schedule_normal);
                img_recurring.setImageResource(R.drawable.bottom_icon_recurring_normal);
                img_donation.setImageResource(R.drawable.bottom_icon_donate_normal);
                bottomBarFragments(3);
                break;
        }
    }

    private void bottomBarFragments(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new DonationFragment();
                title = getString(R.string.title_donation);
                break;
            case 1:
                fragment = new ScheduleFragment();
                title = getString(R.string.title_schedules);
                break;
            case 2:
                fragment = new RecurringFragment();
                title = getString(R.string.title_recurring);
                break;
            case 3:
                fragment = new TransactionsFragment();
                title = getString(R.string.title_transactions);
                break;
            default:
                break;
        }

        backer = false;
        if (fragment != null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment, title);
            fragmentTransaction.addToBackStack(title);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }

    /**
     * Header heading update method
     **/
    private final BroadcastReceiver mHeaderReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            tvTitle.setText(message);
            PPLog.d("receiver", "Got message: " + message);
        }
    };

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        switch (position) {
            case 0:
                startActivity(new Intent(activity, PaymentMethodsActivity.class));
                break;
            case 1:
                startActivity(new Intent(activity, SecurityActivity.class));
                break;
//            case 2:
//                startActivity(new Intent(activity, TellFriendActivity.class));
//                break;
            case 2:
                startActivity(new Intent(activity, SupportActivity.class));
                break;
            case 3:
                startActivity(new Intent(activity, SuggestionActivity.class));
                break;
            case 4:
                if (Preferences.readString(activity, Preferences.PASSCODE_VALUE, "").length() > 1) {
                    Preferences.writeString(activity, Preferences.LOGOUT, "true");

                    check = "Lock";
                    deletePushCall();
                } else
                    showAlert(activity, getString(R.string.lock_alert));
                break;
            default:
                break;
        }

    }

    private void deletePushCall() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("OrganizationKey", ServiceApi.ORGANISATION_KEY);
            jsonObject.put("Action", "DeletePushNtfy");
            jsonObject.put("Token", Preferences.readString(activity, Preferences.AUTH_TOKEN, ""));
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("ApplicationName", getString(R.string.app_name));
            jsonObject1.put("DeviceToken", Preferences.readString(this, Preferences.GCM_TOKEN, ""));
            jsonObject1.put("Provider", "Android");
            jsonObject.put("data", jsonObject1.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Utils.showLoading(activity);
        ModelManager.getInstance().getAuthManager().deletePushToken(activity, jsonObject);
    }

    public void showAlert(final Activity activity, String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        check = "Security";
                        deletePushCall();
                        dialog.cancel();
                    }
                })
                .setNegativeButton(getString(R.string.later), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                        Preferences.writeString(activity, Preferences.LATER_CASE, "true");
//                        check = "Lock";
//                        deletePushCall();
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // show it
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        Fragment f = fragmentManager.findFragmentById(R.id.container_body);
        try {
            if (f instanceof DonationFragment || f instanceof ScheduleFragment
                    || f instanceof RecurringFragment || f instanceof TransactionsFragment) {
                if (backer)
                    finish();
                else {
                    backer = true;
                    Toast.makeText(activity, "Press again to exit the app.", Toast.LENGTH_SHORT).show();
                }
            } else {
                super.onBackPressed();
                backer = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        setTitle();
    }

    private void setTitle() {
        String title = "";
        Fragment f = fragmentManager.findFragmentById(R.id.container_body);
        try {
            if (f instanceof ScheduleFragment) {
                title = getString(R.string.title_schedules);
            } else if (f instanceof RecurringFragment) {
                title = getString(R.string.title_recurring);
            } else if (f instanceof TransactionsFragment) {
                title = getString(R.string.title_transactions);
            } else if (f instanceof DonationFragment) {
                title = getString(R.string.title_donation);
            }
        } catch (Exception e) {
            e.printStackTrace();
            title = getString(R.string.title_donation);
        }
        // set the toolbar title
        getSupportActionBar().setTitle(title);
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
        if (message.equalsIgnoreCase("DeletePushNotification True")) {
            Utils.dismissLoading();
            PPLog.e(TAG, "DeletePushNotification True");

            if (check.equalsIgnoreCase("Lock")) {
                ModelManager.getInstance().getScheduleManager().setArrayLists();
                ModelManager.getInstance().getScheduleManager().setScheduleArrayLists();
                Preferences.writeString(activity, Preferences.USER_NAME, "");
                Preferences.writeString(activity, Preferences.LOGIN, "false");

                Intent intent = new Intent(activity, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            } else if (check.equalsIgnoreCase("Security"))
                startActivity(new Intent(activity, SecurityActivity.class));

        } else if (message.contains("DeletePushNotification False")) {
            // showMatchHistoryList();
            if (message.contains("@#@")) {
                String[] m = message.split("@#@");
                Utils.showMessage(activity, m[1]);
            } else
                Utils.showMessage(activity, getString(R.string.error_message));

            PPLog.e(TAG, "DeletePushNotification False");
            Utils.dismissLoading();
        }

    }
}
