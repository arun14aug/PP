package com.pinlesspay.model;

import android.app.Activity;
import android.util.Log;

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
 * Created by HP on 22-07-2017.
 */
public class ScheduleManager {

    private String TAG = ScheduleManager.class.getSimpleName();
    private ArrayList<Schedule> scheduleArrayList;
    private ArrayList<Charity> charityArrayList;
    private ArrayList<Recurring> recurringArrayList;

    public ArrayList<Schedule> getSchedules(Activity activity, boolean shouldRefresh, int pageNumber) {
        if (shouldRefresh)
            getScheduleList(activity, pageNumber);
        return scheduleArrayList;
    }

    private void getScheduleList(Activity activity, int pageNumber) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("OrganizationKey", ServiceApi.ORGANISATION_KEY);
            jsonObject.put("PageSize", ServiceApi.PAGE_SIZE);
            jsonObject.put("PageNumber", pageNumber);
            jsonObject.put("Action", "getschedules");
            jsonObject.put("Token", Preferences.readString(activity, Preferences.AUTH_TOKEN, ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        PPLog.e("json data : ", jsonObject.toString());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, ServiceApi.SCHEDULES, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "onSuccess  --> " + response.toString());

                        try {

                            boolean state = response.getBoolean("Status");
                            if (state) {
                                if (response.getJSONObject("data").has("Entities")) {
                                    JSONArray jsonArray = response.getJSONObject("data").getJSONArray("Entities");
                                    if (jsonArray == null)
                                        jsonArray = new JSONArray();
                                    int count = jsonArray.length();
                                    scheduleArrayList = new ArrayList<>();
                                    if (count > 0)
                                        for (int i = 0; i < count; i++) {
                                            Schedule schedule = new Schedule();
//                                            "ServiceName": "TaskService",
//                                                    "EntityName": "OrganizationSchedule",
//                                                    "Id": 4,
//                                                    "DataBaseAction": 2,
//                                                    "RowNum": 1,
//                                                    "TaskId": 4,
//                                                    "TaskTitle": "Don't miss your last chance to grab the Best Deals!",
//                                                    "TaskDescription": "Don't miss your last chance to grab the Best Deals!\nAs a customer of YES BANK, you are requested to register your GST Registration Number with YES BANK. Updation of correct GST Registration Number with the Bank is important to ensure compliance of GST Input Tax Credit (ITC). In case, GST Registration number is not updated with the Bank, GST deducted will not be eligible for Input Tax Credit (ITC).\nPlease note:\n\n    In case of multiple GST, please ensure to update the \"Default\" GST number.\n    Status of updation (success/failure) will be informed by the Bank with an email to your registered Email ID.",
//                                                    "TaskDate": "07-13-2017"
                                            schedule.setId(jsonArray.getJSONObject(i).getString("ServiceName"));
                                            schedule.setEntityName(jsonArray.getJSONObject(i).getString("EntityName"));
                                            schedule.setId(jsonArray.getJSONObject(i).getString("Id"));
                                            schedule.setDataBaseAction(jsonArray.getJSONObject(i).getString("DataBaseAction"));
                                            schedule.setRowNum(jsonArray.getJSONObject(i).getString("RowNum"));
                                            schedule.setTaskId(jsonArray.getJSONObject(i).getString("TaskId"));
                                            schedule.setTaskTitle(jsonArray.getJSONObject(i).getString("TaskTitle"));
                                            schedule.setTaskDescription(jsonArray.getJSONObject(i).getString("TaskDescription"));
                                            schedule.setTaskDate(jsonArray.getJSONObject(i).getString("TaskDate"));
//                                            schedule.setCreatedOn(jsonArray.getJSONObject(i).getString("CreatedOn"));
//                                            schedule.setCreatedBy(jsonArray.getJSONObject(i).getString("CreatedBy"));
//                                            schedule.setOrganizationID(jsonArray.getJSONObject(i).getString("OrganizationID"));
//                                            schedule.setIsActive(jsonArray.getJSONObject(i).getString("IsActive"));

                                            scheduleArrayList.add(schedule);
                                        }
                                    EventBus.getDefault().post("GetSchedule True");
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            EventBus.getDefault().post("GetSchedule False");
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                PPLog.e("Error Response : ", "Error: " + error.getMessage());
                EventBus.getDefault().post("GetSchedule False");
            }
        });
        RequestQueue requestQueue = Utils.getVolleyRequestQueue(activity);
        requestQueue.add(jsonObjReq);
    }

    public ArrayList<Charity> getCharity(Activity activity, boolean shouldRefresh, int pageNumber) {
        if (shouldRefresh)
            getCharityData(activity, pageNumber);
        return charityArrayList;
    }

    private void getCharityData(Activity activity, int pageNumber) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("OrganizationKey", ServiceApi.ORGANISATION_KEY);
            jsonObject.put("PageSize", ServiceApi.PAGE_SIZE);
            jsonObject.put("PageNumber", pageNumber);
            jsonObject.put("Action", "Aboutus");
            jsonObject.put("Token", Preferences.readString(activity, Preferences.AUTH_TOKEN, ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, ServiceApi.SCHEDULES, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "onSuccess  --> " + response.toString());

                        try {

                            boolean state = response.getBoolean("Status");
                            if (state) {
                                JSONArray jsonArray = response.getJSONArray("data");
                                if (jsonArray == null)
                                    jsonArray = new JSONArray();
                                int count = jsonArray.length();
                                charityArrayList = new ArrayList<>();
                                if (count > 0)
                                    for (int i = 0; i < count; i++) {
                                        Charity charity = new Charity();
                                        charity.setServiceName(jsonArray.getJSONObject(i).getString("ServiceName"));
                                        charity.setEntityName(jsonArray.getJSONObject(i).getString("EntityName"));
                                        charity.setId(jsonArray.getJSONObject(i).getString("Id"));
                                        charity.setDataBaseAction(jsonArray.getJSONObject(i).getString("DataBaseAction"));
                                        charity.setAboutUsId(jsonArray.getJSONObject(i).getString("AboutUsId"));
                                        charity.setDescription(jsonArray.getJSONObject(i).getString("Description"));
                                        charity.setCreatedOn(jsonArray.getJSONObject(i).getString("CreatedOn"));
                                        charity.setCreatedBy(jsonArray.getJSONObject(i).getString("CreatedBy"));
                                        charity.setIsActive(jsonArray.getJSONObject(i).getString("IsActive"));

                                        charityArrayList.add(charity);
                                    }
                                EventBus.getDefault().post("GetCharity True");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            EventBus.getDefault().post("GetCharity False");
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                PPLog.e("Error Response : ", "Error: " + error.getMessage());
                EventBus.getDefault().post("GetCharity False");
            }
        });
        RequestQueue requestQueue = Utils.getVolleyRequestQueue(activity);
        requestQueue.add(jsonObjReq);
    }

    public ArrayList<Recurring> getRecurring(Activity activity, boolean shouldRefresh, int pageNumber) {
        if (shouldRefresh)
            getScheduleList(activity, pageNumber);
        return recurringArrayList;
    }

    private void getRecurringList(Activity activity, int pageNumber) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("OrganizationKey", ServiceApi.ORGANISATION_KEY);
            jsonObject.put("PageSize", ServiceApi.PAGE_SIZE);
            jsonObject.put("PageNumber", pageNumber);
            jsonObject.put("Action", "getrecurring");
            jsonObject.put("Token", Preferences.readString(activity, Preferences.AUTH_TOKEN, ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        PPLog.e("json data : ", jsonObject.toString());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, ServiceApi.SCHEDULES, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "onSuccess  --> " + response.toString());

                        try {

                            boolean state = response.getBoolean("Status");
                            if (state) {
                                if (response.getJSONObject("data").has("Entities")) {
                                    JSONArray jsonArray = response.getJSONObject("data").getJSONArray("Entities");
                                    if (jsonArray == null)
                                        jsonArray = new JSONArray();
                                    int count = jsonArray.length();
                                    recurringArrayList = new ArrayList<>();
                                    if (count > 0)
                                        for (int i = 0; i < count; i++) {
                                            Recurring recurring = new Recurring();

                                            recurring.setDonorScheduleId(jsonArray.getJSONObject(i).getString("DonorScheduleId"));
                                            recurring.setDonationName(jsonArray.getJSONObject(i).getString("DonationName"));
                                            recurring.setAccountType(jsonArray.getJSONObject(i).getString("AccountType"));
                                            recurring.setPaymentFrom(jsonArray.getJSONObject(i).getString("PaymentFrom"));
                                            recurring.setCardType(jsonArray.getJSONObject(i).getString("CardType"));
                                            recurring.setMaskCardNumber(jsonArray.getJSONObject(i).getString("MaskCardNumber"));
                                            recurring.setScheduleStartDate(jsonArray.getJSONObject(i).getString("ScheduleStartDate"));
                                            recurring.setDonationScheduleId(jsonArray.getJSONObject(i).getString("DonationScheduleId"));
                                            recurring.setNextScheduleRunDate(jsonArray.getJSONObject(i).getString("NextScheduleRunDate"));
                                            recurring.setLastScheduleRunDate(jsonArray.getJSONObject(i).getString("LastScheduleRunDate"));
                                            recurring.setLastRunStatus(jsonArray.getJSONObject(i).getString("LastRunStatus"));
                                            recurring.setIsActive(jsonArray.getJSONObject(i).getString("IsActive"));

                                            recurringArrayList.add(recurring);
                                        }
                                    EventBus.getDefault().post("Recurring True");
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            EventBus.getDefault().post("Recurring False");
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                PPLog.e("Error Response : ", "Error: " + error.getMessage());
                EventBus.getDefault().post("Recurring False");
            }
        });
        RequestQueue requestQueue = Utils.getVolleyRequestQueue(activity);
        requestQueue.add(jsonObjReq);
    }
}
