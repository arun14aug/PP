package com.pinlesspay.view.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.pinlesspay.R;
import com.pinlesspay.customUi.MyEditText;
import com.pinlesspay.model.ModelManager;
import com.pinlesspay.model.Ticket;
import com.pinlesspay.model.TicketDetail;
import com.pinlesspay.utility.PPLog;
import com.pinlesspay.utility.Preferences;
import com.pinlesspay.utility.ServiceApi;
import com.pinlesspay.utility.Utils;
import com.pinlesspay.view.adapter.TicketDetailAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.greenrobot.event.EventBus;

/*
 * Created by arun.sharma on 8/2/2017.
 */

public class NewTicketActivity extends Activity {

    private String TAG = NewTicketActivity.this.getClass().getName();
    private Activity activity;
    private LinearLayout waterfall_layout;
    private RecyclerView ticket_chat_list;
    private String id = "";
    private ArrayList<TicketDetail> ticketDetailArrayList;
    private MyEditText et_message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ticket);

        activity = NewTicketActivity.this;

        try {
            id = getIntent().getExtras().getString("id");
        } catch (Exception e) {
            e.printStackTrace();
            id = "";
        }

        et_message = (MyEditText) findViewById(R.id.et_message);

        waterfall_layout = (LinearLayout) findViewById(R.id.waterfall_layout);
        ticket_chat_list = (RecyclerView) findViewById(R.id.ticket_chat_list);

        ImageView img_send = (ImageView) findViewById(R.id.img_send);
        img_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_message.getText().toString().trim().length() == 0) {
                    et_message.requestFocus();
                    Utils.showMessage(activity, getString(R.string.please_enter_card_name));
                } else {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("OrganizationKey", ServiceApi.ORGANISATION_KEY);
                        jsonObject.put("Token", Preferences.readString(activity, Preferences.AUTH_TOKEN, ""));
                        JSONObject jsonObject1 = new JSONObject();
                        if (id.length() > 0) {
                            jsonObject.put("Action", "PostTicketReply");
                            jsonObject1.put("TicketID", id);
                            jsonObject1.put("TicketDesc", et_message.getText().toString().trim());
                        } else {
                            jsonObject.put("Action", "PostTicket");
                            jsonObject1.put("TicketShortDesc", et_message.getText().toString().trim());
                        }
                        jsonObject.put("data", jsonObject1.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Utils.showLoading(activity);
                    if (id.length() > 0)
                        ModelManager.getInstance().getRestOfAllManager().ticketReply(activity, jsonObject);
                    else
                        ModelManager.getInstance().getRestOfAllManager().addTicket(activity, jsonObject);
                }
            }
        });

        ImageView img_back = (ImageView) findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

//        ticket_chat_list.setVisibility(View.GONE);
//        waterfall_layout.setVisibility(View.VISIBLE);

        if (!Utils.isEmptyString(id)) {
            callDetail();
        }
    }

    private void callDetail() {
        ArrayList<Ticket> tickets = ModelManager.getInstance().getRestOfAllManager().getTickets(activity, false);
        for (int i = 0; i < tickets.size(); i++)
            if (tickets.get(i).getTicketID().equalsIgnoreCase(id)) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("OrganizationKey", ServiceApi.ORGANISATION_KEY);
                    jsonObject.put("Action", "GetTicketDetail");
                    jsonObject.put("Token", Preferences.readString(activity, Preferences.AUTH_TOKEN, ""));
                    JSONObject jsonObject1 = new JSONObject();
                    jsonObject1.put("TicketID", id);
                    jsonObject.put("data", jsonObject1.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Utils.showLoading(activity);
                tickets.get(i).getTicketDetail(activity, true, jsonObject);
                break;
            }
    }

    private void setData() {
        ticket_chat_list.setVisibility(View.VISIBLE);
        waterfall_layout.setVisibility(View.GONE);
        TicketDetailAdapter adapter = new TicketDetailAdapter(activity, ticketDetailArrayList);
        ticket_chat_list.setAdapter(adapter);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        ticket_chat_list.setLayoutManager(mLinearLayoutManager);
        mLinearLayoutManager.scrollToPosition(ticketDetailArrayList.size() - 1);
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
        if (message.equalsIgnoreCase("GetTickets True")) {
            Utils.dismissLoading();
            PPLog.e(TAG, "GetTickets True");
            callDetail();
        } else if (message.contains("GetTickets False")) {
            // showMatchHistoryList();
            if (message.contains("@#@")) {
                String[] m = message.split("@#@");
                Utils.showMessage(activity, m[1]);
            } else
                Utils.showMessage(activity, getString(R.string.error_message));

            PPLog.e(TAG, "GetTickets False");
            Utils.dismissLoading();
        } else if (message.equalsIgnoreCase("GetTicketDetail True")) {
            Utils.dismissLoading();
            PPLog.e(TAG, "GetTicketDetail True");
            ArrayList<Ticket> tickets = ModelManager.getInstance().getRestOfAllManager().getTickets(activity, false);
            for (int i = 0; i < tickets.size(); i++)
                if (tickets.get(i).getTicketID().equalsIgnoreCase(id)) {
                    ticketDetailArrayList = tickets.get(i).getTicketDetail(activity, false, new JSONObject());
                    break;
                }
            if (ticketDetailArrayList != null)
                if (ticketDetailArrayList.size() > 0)
                    setData();
                else {
                    ticket_chat_list.setVisibility(View.GONE);
                    waterfall_layout.setVisibility(View.VISIBLE);
                }
            else {
                ticket_chat_list.setVisibility(View.GONE);
                waterfall_layout.setVisibility(View.VISIBLE);
            }
        } else if (message.contains("GetTicketDetail False")) {
            // showMatchHistoryList();
            if (message.contains("@#@")) {
                String[] m = message.split("@#@");
                Utils.showMessage(activity, m[1]);
            } else
                Utils.showMessage(activity, getString(R.string.error_message));
            ticket_chat_list.setVisibility(View.GONE);
            waterfall_layout.setVisibility(View.VISIBLE);
            PPLog.e(TAG, "GetTicketDetail False");
            Utils.dismissLoading();
        } else if (message.contains("AddTicket True")) {
            Utils.dismissLoading();
            PPLog.e(TAG, "AddTicket True");
            if (message.contains("@#@")) {
                String[] m = message.split("@#@");
                id = m[1];
            }
            et_message.setText("");
            Utils.showLoading(activity);
            ModelManager.getInstance().getRestOfAllManager().getTickets(activity, true);
        } else if (message.contains("AddTicket False")) {
            // showMatchHistoryList();
            if (message.contains("@#@")) {
                String[] m = message.split("@#@");
                Utils.showMessage(activity, m[1]);
            } else
                Utils.showMessage(activity, getString(R.string.error_message));
            PPLog.e(TAG, "AddTicket False");
            Utils.dismissLoading();
        } else if (message.contains("TicketReply True")) {
            Utils.dismissLoading();
            PPLog.e(TAG, "TicketReply True");
            TicketDetail ticket = new TicketDetail();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date = new Date();
            ticket.setDateCreated(sdf.format(date));
            ticket.setTicketDesc(et_message.getText().toString().trim());
            ticket.setIsUserComment("Y");
            ticketDetailArrayList.add(ticket);
            et_message.setText("");
//            callDetail();
            setData();
        } else if (message.contains("TicketReply False")) {
            // showMatchHistoryList();
            if (message.contains("@#@")) {
                String[] m = message.split("@#@");
                Utils.showMessage(activity, m[1]);
            } else
                Utils.showMessage(activity, getString(R.string.error_message));
            PPLog.e(TAG, "TicketReply False");
            Utils.dismissLoading();
        }

    }
}
