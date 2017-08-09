package com.pinlesspay.view.activity;

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

import com.pinlesspay.R;
import com.pinlesspay.customUi.MyTextView;
import com.pinlesspay.utility.PPLog;
import com.pinlesspay.utility.Preferences;
import com.pinlesspay.view.fragment.DonationFragment;
import com.pinlesspay.view.fragment.RecurringFragment;
import com.pinlesspay.view.fragment.ScheduleFragment;
import com.pinlesspay.view.fragment.TransactionsFragment;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener,
        View.OnClickListener {

    private String TAG = MainActivity.class.getSimpleName();
    private Activity activity;
    private FragmentManager fragmentManager;
    private boolean backer = false;
    private MyTextView tvTitle;
    private ImageView img_donation, img_schedule, img_recurring, img_transactions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = MainActivity.this;

        LocalBroadcastManager.getInstance(activity).registerReceiver(
                mHeaderReceiver, new IntentFilter("Header"));

        img_donation = (ImageView) findViewById(R.id.img_donation);
        img_schedule = (ImageView) findViewById(R.id.img_schedule);
        img_recurring = (ImageView) findViewById(R.id.img_recurring);
        img_transactions = (ImageView) findViewById(R.id.img_transactions);

        img_donation.setOnClickListener(this);
        img_schedule.setOnClickListener(this);
        img_recurring.setOnClickListener(this);
        img_transactions.setOnClickListener(this);

        fragmentManager = getSupportFragmentManager();
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitle = (MyTextView) findViewById(R.id.header_text);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FragmentDrawer drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        // display the first navigation drawer view on app launch
//        displayView(0);
        bottomBarFragments(0);
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
//                fragment = new PaymentMethodsFragment();
//                title = getString(R.string.title_payment_method);
                startActivity(new Intent(activity, PaymentMethodsActivity.class));
                break;
            case 1:
//                fragment = new SecurityFragment();
//                title = getString(R.string.title_security);
                startActivity(new Intent(activity, SecurityActivity.class));
                break;
            case 2:
//                fragment = new TellYourFriendsFragment();
//                title = getString(R.string.title_tell_your);
                startActivity(new Intent(activity, TellFriendActivity.class));
                break;
            case 3:
//                fragment = new SupportFragment();
//                title = getString(R.string.title_support);
                startActivity(new Intent(activity, SupportActivity.class));
                break;
            case 4:
//                fragment = new SuggestionsFragment();
//                title = getString(R.string.title_suggestions);
                startActivity(new Intent(activity, SuggestionActivity.class));
                break;
            case 5:
                if (Preferences.readString(activity, Preferences.PASSCODE_VALUE, "").length() > 1) {
                    Preferences.writeString(activity, Preferences.LOGOUT, "true");
                    Intent intent = new Intent(activity, LockActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else
                    showAlert(activity, getString(R.string.lock_alert));
                break;
            default:
                break;
        }

    }

    public void showAlert(final Activity activity, String msg) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        startActivity(new Intent(activity, SecurityActivity.class));
                        finish();

                        dialog.cancel();
                    }
                })
                .setNegativeButton(getString(R.string.later), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Preferences.writeString(activity, Preferences.LOGOUT, "true");
                        Intent intent = new Intent(activity, LockActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
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
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}