package com.pinlesspay.model;

import android.app.Activity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.pinlesspay.utility.PPLog;
import com.pinlesspay.utility.Preferences;
import com.pinlesspay.utility.ServiceApi;
import com.pinlesspay.utility.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/*
 * Created by arun.sharma on 8/8/2017.
 */

public class RestOfAllManager {

    private String TAG = RestOfAllManager.class.getSimpleName();
    private ArrayList<DonorDevice> donorDeviceArrayList;
    private ArrayList<Ticket> ticketArrayList;

    public ArrayList<DonorDevice> getDevices(Activity activity, boolean shouldRefresh) {
        if (shouldRefresh)
            getDeviceList(activity);
        return donorDeviceArrayList;
    }

    private void getDeviceList(Activity activity) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("OrganizationKey", ServiceApi.ORGANISATION_KEY);
            jsonObject.put("Action", "GetDevices");
            jsonObject.put("Token", Preferences.readString(activity, Preferences.AUTH_TOKEN, ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        PPLog.e("json data : ", jsonObject.toString());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, ServiceApi.GET_DONOR_DEVICES, jsonObject,
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
                                    donorDeviceArrayList = new ArrayList<>();
                                    if (count > 0)
                                        for (int i = 0; i < count; i++) {
                                            DonorDevice donorDevice = new DonorDevice();

                                            donorDevice.setDonorDeviceID(jsonArray.getJSONObject(i).getString("DonorDeviceID"));
                                            donorDevice.setDeviceName(jsonArray.getJSONObject(i).getString("DeviceName"));
                                            donorDevice.setDeviceType(jsonArray.getJSONObject(i).getString("DeviceType"));
                                            donorDevice.setDonorId(jsonArray.getJSONObject(i).getString("DonorId"));
                                            donorDevice.setDeviceIdentifier(jsonArray.getJSONObject(i).getString("DeviceIdentifier"));

                                            donorDeviceArrayList.add(donorDevice);
                                        }
                                    EventBus.getDefault().post("GetDevices True");
                                }
                            } else
                                EventBus.getDefault().post("GetDevices False@#@" + response.getString("Message"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                            EventBus.getDefault().post("GetDevices False");
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                PPLog.e("Error Response : ", "Error: " + error.getMessage());
                EventBus.getDefault().post("GetDevices False");
            }
        });
        RequestQueue requestQueue = Utils.getVolleyRequestQueue(activity);
        requestQueue.add(jsonObjReq);
    }

    public ArrayList<Ticket> getTickets(Activity activity, boolean shouldRefresh) {
        if (shouldRefresh)
            getTicketList(activity);
        return ticketArrayList;
    }

    private void getTicketList(Activity activity) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("OrganizationKey", ServiceApi.ORGANISATION_KEY);
            jsonObject.put("Action", "GetAllTickets");
            jsonObject.put("Token", Preferences.readString(activity, Preferences.AUTH_TOKEN, ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        PPLog.e("json data : ", jsonObject.toString());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, ServiceApi.GET_ALL_TICKETS, jsonObject,
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
                                            Ticket ticket = new Ticket();

                                            ticket.setServiceName(jsonArray.getJSONObject(i).getString("ServiceName"));
                                            ticket.setEntityName(jsonArray.getJSONObject(i).getString("EntityName"));
                                            ticket.setId(jsonArray.getJSONObject(i).getString("Id"));
                                            ticket.setDataBaseAction(jsonArray.getJSONObject(i).getString("DataBaseAction"));
                                            ticket.setTicketID(jsonArray.getJSONObject(i).getString("TicketID"));
                                            ticket.setDateCreated(jsonArray.getJSONObject(i).getString("DateCreated"));
                                            ticket.setTicketShortDesc(jsonArray.getJSONObject(i).getString("TicketShortDesc"));
                                            ticket.setTicketStatus(jsonArray.getJSONObject(i).getString("TicketStatus"));

                                            ticketArrayList.add(ticket);
                                        }
                                    EventBus.getDefault().post("GetTickets True");
                                }
                            } else
                                EventBus.getDefault().post("GetTickets False@#@" + response.getString("Message"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                            EventBus.getDefault().post("GetTickets False");
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                PPLog.e("Error Response : ", "Error: " + error.getMessage());
                EventBus.getDefault().post("GetTickets False");
            }
        });
        RequestQueue requestQueue = Utils.getVolleyRequestQueue(activity);
        requestQueue.add(jsonObjReq);
    }

    public void addTicket(final Activity activity, JSONObject jsonObject) {
        PPLog.e("json data : ", jsonObject.toString());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, ServiceApi.POST_TICKET, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        PPLog.e("Success Response : ", "Response: " + response.toString());

                        try {
                            boolean state = response.getBoolean("Status");
                            if (state) {
//                                "data":{"TicketID":15,"Msg":"","isSuccess":true}
                                EventBus.getDefault().postSticky("AddTicket True@#@" + response.getJSONObject("data").getString("TicketID"));
                            } else
                                EventBus.getDefault().postSticky("AddTicket False@#@" + response.getString("Message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            EventBus.getDefault().postSticky("AddTicket False");
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                PPLog.e("Error Response : ", "Error: " + error.getMessage());
                EventBus.getDefault().postSticky("AddTicket False");
            }
        });
        RequestQueue requestQueue = Utils.getVolleyRequestQueue(activity);
        requestQueue.add(jsonObjReq);
    }

    public void ticketReply(final Activity activity, JSONObject jsonObject) {
        PPLog.e("json data : ", jsonObject.toString());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, ServiceApi.POST_TICKET_REPLY, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        PPLog.e("Success Response : ", "Response: " + response.toString());

                        try {
                            boolean state = response.getBoolean("Status");
                            if (state) {
                                EventBus.getDefault().postSticky("TicketReply True");
                            } else
                                EventBus.getDefault().postSticky("TicketReply False@#@" + response.getString("Message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            EventBus.getDefault().postSticky("TicketReply False");
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                PPLog.e("Error Response : ", "Error: " + error.getMessage());
                EventBus.getDefault().postSticky("TicketReply False");
            }
        });
        RequestQueue requestQueue = Utils.getVolleyRequestQueue(activity);
        requestQueue.add(jsonObjReq);
    }

    public void sendSuggestion(final Activity activity, JSONObject jsonObject) {
        PPLog.e("json data : ", jsonObject.toString());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, ServiceApi.POST_SUGGESTIONS, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        PPLog.e("Success Response : ", "Response: " + response.toString());

                        try {
                            boolean state = response.getBoolean("Status");
                            if (state) {
                                EventBus.getDefault().postSticky("SendSuggestion True@#@" + response.getString("Message"));
                            } else
                                EventBus.getDefault().postSticky("SendSuggestion False@#@" + response.getString("Message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            EventBus.getDefault().postSticky("SendSuggestion False");
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                PPLog.e("Error Response : ", "Error: " + error.getMessage());
                EventBus.getDefault().postSticky("SendSuggestion False");
            }
        });
        RequestQueue requestQueue = Utils.getVolleyRequestQueue(activity);
        requestQueue.add(jsonObjReq);
    }

    public void deleteDevice(final Activity activity, JSONObject jsonObject) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, ServiceApi.DELETE_DONOR_DEVICE, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        PPLog.e("Success Response : ", "Response: " + response.toString());

                        try {
                            boolean state = response.getBoolean("Status");
                            if (state) {
                                EventBus.getDefault().postSticky("DeleteDonorDevice True");
                            } else
                                EventBus.getDefault().postSticky("DeleteDonorDevice False@#@" + response.getString("Message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            EventBus.getDefault().postSticky("DeleteDonorDevice False");
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                PPLog.e("Error Response : ", "Error: " + error.getMessage());
                EventBus.getDefault().postSticky("DeleteDonorDevice False");
            }
        });
        RequestQueue requestQueue = Utils.getVolleyRequestQueue(activity);
        requestQueue.add(jsonObjReq);
    }
}
