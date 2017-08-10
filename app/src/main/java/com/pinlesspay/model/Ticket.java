package com.pinlesspay.model;

/*
 * Created by arun.sharma on 8/10/2017.
 */

import android.app.Activity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.pinlesspay.utility.PPLog;
import com.pinlesspay.utility.ServiceApi;
import com.pinlesspay.utility.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

public class Ticket {

    private String TAG = Ticket.class.getSimpleName();
    private String ServiceName, EntityName, Id, DataBaseAction, TicketID, DateCreated, TicketShortDesc;
    private ArrayList<TicketDetail> ticketArrayList;

    public String getServiceName() {
        return ServiceName;
    }

    public void setServiceName(String serviceName) {
        ServiceName = serviceName;
    }

    public String getEntityName() {
        return EntityName;
    }

    public void setEntityName(String entityName) {
        EntityName = entityName;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getDataBaseAction() {
        return DataBaseAction;
    }

    public void setDataBaseAction(String dataBaseAction) {
        DataBaseAction = dataBaseAction;
    }

    public String getTicketID() {
        return TicketID;
    }

    public void setTicketID(String ticketID) {
        TicketID = ticketID;
    }

    public String getDateCreated() {
        return DateCreated;
    }

    public void setDateCreated(String dateCreated) {
        DateCreated = dateCreated;
    }

    public String getTicketShortDesc() {
        return TicketShortDesc;
    }

    public void setTicketShortDesc(String ticketShortDesc) {
        TicketShortDesc = ticketShortDesc;
    }

    public ArrayList<TicketDetail> getTicketDetail(Activity activity, boolean shouldRefresh, JSONObject jsonObject) {
        if (shouldRefresh)
            getList(activity, jsonObject);
        return ticketArrayList;
    }

    private void getList(Activity activity, JSONObject jsonObject) {

        PPLog.e("json data : ", jsonObject.toString());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, ServiceApi.GET_TICKET_DETAIL, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        PPLog.e(TAG, "onSuccess  --> " + response.toString());

                        try {

                            boolean state = response.getBoolean("Status");
                            if (state) {
                                if (response.has("data") && !response.isNull("data")) {
                                    JSONArray jsonArray = response.getJSONArray("data");
                                    int count = jsonArray.length();
                                    ticketArrayList = new ArrayList<>();
                                    if (count > 0)
                                        for (int i = 0; i < count; i++) {
                                            TicketDetail ticket = new TicketDetail();

                                            ticket.setTicketDesc(jsonArray.getJSONObject(i).getString("TicketDesc"));
                                            ticket.setDateCreated(jsonArray.getJSONObject(i).getString("DateCreated"));
                                            ticket.setIsUserComment(jsonArray.getJSONObject(i).getString("IsUserComment"));
                                            ticket.setCommentBy(jsonArray.getJSONObject(i).getString("CommentBy"));

                                            ticketArrayList.add(ticket);
                                        }
                                    EventBus.getDefault().post("GetTicketDetail True");
                                }
                            } else
                                EventBus.getDefault().post("GetTicketDetail False@#@" + response.getString("Message"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                            EventBus.getDefault().post("GetTicketDetail False");
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                PPLog.e("Error Response : ", "Error: " + error.getMessage());
                EventBus.getDefault().post("GetTicketDetail False");
            }
        });
        RequestQueue requestQueue = Utils.getVolleyRequestQueue(activity);
        requestQueue.add(jsonObjReq);
    }
}
