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
    private ArrayList<Transaction> transactionArrayList;
    private ArrayList<PaymentAccount> paymentAccountArrayList;
    private ArrayList<Causes> causesArrayList;
    private ArrayList<Frequency> frequencyArrayList;

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
                            } else
                                EventBus.getDefault().post("GetSchedule False@#@" + response.getString("Message"));


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
                                if (response.getJSONObject("data").has("Entities")) {
                                    JSONArray jsonArray = response.getJSONObject("data").getJSONArray("Entities");
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
                            } else
                                EventBus.getDefault().post("GetCharity False@#@" + response.getString("Message"));

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
            getRecurringList(activity, pageNumber);
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

                                            recurring.setDonationName(jsonArray.getJSONObject(i).getString("DonationName"));
                                            recurring.setDonorID(jsonArray.getJSONObject(i).getString("DonorID"));
                                            recurring.setDonationAmount(jsonArray.getJSONObject(i).getString("DonationAmount"));
                                            recurring.setAccountType(jsonArray.getJSONObject(i).getString("AccountType"));
                                            recurring.setPaymentFrom(jsonArray.getJSONObject(i).getString("PaymentFrom"));
                                            recurring.setServiceName(jsonArray.getJSONObject(i).getString("ServiceName"));
                                            recurring.setEntityName(jsonArray.getJSONObject(i).getString("EntityName"));
                                            recurring.setId(jsonArray.getJSONObject(i).getString("Id"));
                                            recurring.setDataBaseAction(jsonArray.getJSONObject(i).getString("DataBaseAction"));
                                            recurring.setNextScheduleRunDate(jsonArray.getJSONObject(i).getString("NextScheduleRunDate"));
                                            recurring.setAccountType(jsonArray.getJSONObject(i).getString("AccountType"));
                                            recurring.setPaymentFrom(jsonArray.getJSONObject(i).getString("PaymentFrom"));
                                            recurring.setCardType(jsonArray.getJSONObject(i).getString("CardType"));
                                            recurring.setScheduleName(jsonArray.getJSONObject(i).getString("ScheduleName"));

                                            recurringArrayList.add(recurring);
                                        }
                                    EventBus.getDefault().post("Recurring True");
                                }
                            } else
                                EventBus.getDefault().post("Recurring False@#@" + response.getString("Message"));

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

    public void setArrayLists() {
        transactionArrayList = null;
        recurringArrayList = null;
    }
    public void setScheduleArrayLists() {
        scheduleArrayList = null;
    }

    public ArrayList<Transaction> getTransactions(Activity activity, boolean shouldRefresh, int pageNumber) {
        if (shouldRefresh)
            getTransactionList(activity, pageNumber);
        return transactionArrayList;
    }

    private void getTransactionList(Activity activity, int pageNumber) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("OrganizationKey", ServiceApi.ORGANISATION_KEY);
            jsonObject.put("PageSize", ServiceApi.PAGE_SIZE);
            jsonObject.put("PageNumber", pageNumber);
            jsonObject.put("Action", "gettransactions");
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
                                    transactionArrayList = new ArrayList<>();
                                    if (count > 0)
                                        for (int i = 0; i < count; i++) {
                                            Transaction transaction = new Transaction();
                                            transaction.setId(jsonArray.getJSONObject(i).getString("Id"));
                                            transaction.setDataBaseAction(jsonArray.getJSONObject(i).getString("DataBaseAction"));
                                            transaction.setRowNum(jsonArray.getJSONObject(i).getString("RowNum"));
                                            transaction.setPaymentFrom(jsonArray.getJSONObject(i).getString("PaymentFrom"));
                                            transaction.setDeviceName(jsonArray.getJSONObject(i).getString("DeviceName"));

                                            transaction.setStatus(jsonArray.getJSONObject(i).getString("TranStatus"));

                                            transaction.setInvoiceNo(jsonArray.getJSONObject(i).getString("InvoiceNo"));
                                            transaction.setTranDate(jsonArray.getJSONObject(i).getString("TranDate"));
                                            transaction.setDonationName(jsonArray.getJSONObject(i).getString("DonationName"));
                                            transaction.setTranAmount(jsonArray.getJSONObject(i).getString("TranAmount"));
                                            transaction.setPaymentType(jsonArray.getJSONObject(i).getString("PaymentType"));
                                            transaction.setServiceName(jsonArray.getJSONObject(i).getString("ServiceName"));
                                            transaction.setEntityName(jsonArray.getJSONObject(i).getString("EntityName"));

                                            transaction.setCardTypeCode(jsonArray.getJSONObject(i).getString("CardTypeCode"));
                                            transaction.setNameOnCard(jsonArray.getJSONObject(i).getString("NameOnCard"));

                                            transactionArrayList.add(transaction);
                                        }
                                    EventBus.getDefault().post("Transactions True");
                                }
                            } else
                                EventBus.getDefault().post("Transactions False@#@" + response.getString("Message"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                            EventBus.getDefault().post("Transactions False");
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                PPLog.e("Error Response : ", "Error: " + error.getMessage());
                EventBus.getDefault().post("Transactions False");
            }
        });
        RequestQueue requestQueue = Utils.getVolleyRequestQueue(activity);
        requestQueue.add(jsonObjReq);
    }

    public void deleteRecurring(Activity activity, JSONObject jsonObject) {
        PPLog.e("json data : ", jsonObject.toString());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, ServiceApi.DELETE_RECURRING_SCHEDULE, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "onSuccess  --> " + response.toString());

                        try {

                            boolean state = response.getBoolean("Status");
                            if (state) {
                                if (response.has("data")) {
                                    JSONObject jsonObject1 = new JSONObject(response.getString("data"));
                                    EventBus.getDefault().post("DeleteRecurring True@#@" + jsonObject1.getString("Msg"));
                                }
                            } else
                                EventBus.getDefault().post("DeleteRecurring False");

                        } catch (JSONException e) {
                            e.printStackTrace();
                            EventBus.getDefault().post("DeleteRecurring False");
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                PPLog.e("Error Response : ", "Error: " + error.getMessage());
                EventBus.getDefault().post("DeleteRecurring False");
            }
        });
        RequestQueue requestQueue = Utils.getVolleyRequestQueue(activity);
        requestQueue.add(jsonObjReq);
    }

    public void addRecurring(Activity activity, JSONObject jsonObject) {
        PPLog.e("json data : ", jsonObject.toString());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, ServiceApi.SAVE_DONATION_SCHEDULE, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "onSuccess  --> " + response.toString());

                        try {

                            boolean state = response.getBoolean("Status");
                            if (state) {
                                if (response.has("data")) {
                                    JSONObject jsonObject1 = new JSONObject(response.getString("data"));
                                    EventBus.getDefault().post("AddRecurring True@#@" + jsonObject1.getString("Msg"));
                                }
                            } else
                                EventBus.getDefault().post("AddRecurring False@#@" + response.getString("Message"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                            EventBus.getDefault().post("AddRecurring False");
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                PPLog.e("Error Response : ", "Error: " + error.getMessage());
                EventBus.getDefault().post("AddRecurring False");
            }
        });
        RequestQueue requestQueue = Utils.getVolleyRequestQueue(activity);
        requestQueue.add(jsonObjReq);
    }


    public ArrayList<PaymentAccount> getPaymentAccounts(Activity activity, boolean shouldRefresh, int pageNumber) {
        if (shouldRefresh)
            getAccountList(activity, pageNumber);
        return paymentAccountArrayList;
    }

    private void getAccountList(Activity activity, int pageNumber) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("OrganizationKey", ServiceApi.ORGANISATION_KEY);
            jsonObject.put("PageSize", ServiceApi.PAGE_SIZE);
            jsonObject.put("PageNumber", pageNumber);
            jsonObject.put("Action", "GetDonorPaymentAccounts");
            jsonObject.put("Token", Preferences.readString(activity, Preferences.AUTH_TOKEN, ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        PPLog.e("json data : ", jsonObject.toString());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, ServiceApi.GET_DONOR_PAYMENT_ACCOUNTS, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "onSuccess  --> " + response.toString());

                        try {

                            boolean state = response.getBoolean("Status");
                            if (state) {
                                if (response.has("data") && !response.isNull("data")) {
                                    JSONArray jsonArray = response.getJSONArray("data");
                                    int count = jsonArray.length();
                                    paymentAccountArrayList = new ArrayList<>();
                                    if (count > 0)
                                        for (int i = 0; i < count; i++) {
                                            PaymentAccount paymentAccount = new PaymentAccount();

                                            paymentAccount.setAccountId(jsonArray.getJSONObject(i).getString("AccountId"));
                                            paymentAccount.setAccountType(jsonArray.getJSONObject(i).getString("AccountType"));
                                            paymentAccount.setCardType(jsonArray.getJSONObject(i).getString("CardType"));
                                            paymentAccount.setAccountNumber(jsonArray.getJSONObject(i).getString("AccountNumber"));

                                            paymentAccountArrayList.add(paymentAccount);
                                        }
                                    EventBus.getDefault().post("PaymentAccount True");
                                }
                            } else
                                EventBus.getDefault().post("PaymentAccount False@#@" + response.getString("Message"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                            EventBus.getDefault().post("PaymentAccount False");
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                PPLog.e("Error Response : ", "Error: " + error.getMessage());
                EventBus.getDefault().post("PaymentAccount False");
            }
        });
        RequestQueue requestQueue = Utils.getVolleyRequestQueue(activity);
        requestQueue.add(jsonObjReq);
    }

    public ArrayList<Causes> getCauses(Activity activity, boolean shouldRefresh, int pageNumber) {
        if (shouldRefresh)
            getCausesList(activity, pageNumber);
        return causesArrayList;
    }

    private void getCausesList(Activity activity, int pageNumber) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("OrganizationKey", ServiceApi.ORGANISATION_KEY);
            jsonObject.put("PageSize", ServiceApi.PAGE_SIZE);
            jsonObject.put("PageNumber", pageNumber);
            jsonObject.put("Action", "GetDonationCauseList");
            jsonObject.put("Token", Preferences.readString(activity, Preferences.AUTH_TOKEN, ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        PPLog.e("json data : ", jsonObject.toString());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, ServiceApi.GET_DONATION_CAUSE_LIST, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "onSuccess  --> " + response.toString());

                        try {

                            boolean state = response.getBoolean("Status");
                            if (state) {
                                if (response.has("data") && !response.isNull("data")) {
                                    JSONArray jsonArray = response.getJSONArray("data");
                                    int count = jsonArray.length();
                                    causesArrayList = new ArrayList<>();
                                    if (count > 0)
                                        for (int i = 0; i < count; i++) {
                                            Causes causes = new Causes();

                                            causes.setDonationID(jsonArray.getJSONObject(i).getString("DonationID"));
                                            causes.setDonationName(jsonArray.getJSONObject(i).getString("DonationName"));

                                            causesArrayList.add(causes);
                                        }
                                    EventBus.getDefault().post("Causes True");
                                }
                            } else
                                EventBus.getDefault().post("Causes False@#@" + response.getString("Message"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                            EventBus.getDefault().post("Causes False");
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                PPLog.e("Error Response : ", "Error: " + error.getMessage());
                EventBus.getDefault().post("Causes False");
            }
        });
        RequestQueue requestQueue = Utils.getVolleyRequestQueue(activity);
        requestQueue.add(jsonObjReq);
    }

    public ArrayList<Frequency> getFrequency(Activity activity, boolean shouldRefresh, int pageNumber) {
        if (shouldRefresh)
            getFrequencyList(activity, pageNumber);
        return frequencyArrayList;
    }

    private void getFrequencyList(Activity activity, int pageNumber) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("OrganizationKey", ServiceApi.ORGANISATION_KEY);
            jsonObject.put("PageSize", ServiceApi.PAGE_SIZE);
            jsonObject.put("PageNumber", pageNumber);
            jsonObject.put("Action", "GetDonationFrequency");
            jsonObject.put("Token", Preferences.readString(activity, Preferences.AUTH_TOKEN, ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        PPLog.e("json data : ", jsonObject.toString());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, ServiceApi.GET_DONATION_FREQUENCY, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "onSuccess  --> " + response.toString());

                        try {

                            boolean state = response.getBoolean("Status");
                            if (state) {
                                if (response.has("data") && !response.isNull("data")) {
                                    JSONArray jsonArray = response.getJSONArray("data");
                                    int count = jsonArray.length();
                                    frequencyArrayList = new ArrayList<>();
                                    if (count > 0)
                                        for (int i = 0; i < count; i++) {
                                            Frequency frequency = new Frequency();

                                            frequency.setDonationScheduleId(jsonArray.getJSONObject(i).getString("DonationScheduleId"));
                                            frequency.setDonationScheduleName(jsonArray.getJSONObject(i).getString("DonationScheduleName"));

                                            frequencyArrayList.add(frequency);
                                        }
                                    EventBus.getDefault().post("Frequency True");
                                }
                            } else
                                EventBus.getDefault().post("Frequency False@#@" + response.getString("Message"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                            EventBus.getDefault().post("Frequency False");
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                PPLog.e("Error Response : ", "Error: " + error.getMessage());
                EventBus.getDefault().post("Frequency False");
            }
        });
        RequestQueue requestQueue = Utils.getVolleyRequestQueue(activity);
        requestQueue.add(jsonObjReq);
    }
}
