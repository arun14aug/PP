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
                                    transactionArrayList = new ArrayList<>();
                                    if (count > 0)
                                        for (int i = 0; i < count; i++) {
                                            Transaction transaction = new Transaction();
//                                            InvoiceNo: ""
//                                                    ,TranDate: ""
//                                                    ,DonorID: ""
//                                                    ,RegisterMobile: ""
//                                                    ,OrganizationID: ""
//                                                    ,DonationID: ""
//                                                    ,DonationName: ""
//                                                    ,TranSource: ""
//                                                    ,SourceIP: ""
//                                                    ,TranAmount: ""
//                                                    ,TransactionType: ""
//                                                    ,PaymentType: ""
//                                                    ,AgentID: ""
//                                                    ,CommissionPercent: ""
//                                                    ,Fees: ""
//                                                    ,CardTypeCode: ""
//                                                    ,CardNumber: ""
//                                                    ,CardShort: ""
//                                                    ,CCardExpMM: ""
//                                                    ,CCardExpYYYY: ""
//                                                    ,CCProcessorID: ""
//                                                    ,CCTranApproved: ""
//                                                    ,NameOnCard: ""
//                                                    ,Address1: ""
//                                                    ,Address2: ""
//                                                    ,City: ""
//                                                    ,State: ""
//                                                    ,Zip: ""
//                                                    ,CountryISO3: ""
//                                                    ,EmailAddress: ""
//                                                    ,PhoneNumber: ""
//                                                    ,ProcessorTransID: ""
//                                                    ,ProcessorAuthCode: ""
//                                                    ,ProcessorAuthMsg: ""
//                                                    ,ProcessorAVSCode: ""
//                                                    ,ProcessorCVVCode: ""
//                                                    ,ProcessorResponseCode: ""
//                                                    ,ProcessorErrorCode: ""
//                                                    ,RequestID: ""
//                                                    ,RequestTokenID: ""
//                                                    ,IsVoided: ""
//                                                    ,IsFraud: ""
//                                                    ,BankRoutingNum: ""
//                                                    ,BankAccountNum: ""
//                                                    ,BankAccountLastNum: ""
//                                                    ,BankAccountType


                                            transaction.setInvoiceNo(jsonArray.getJSONObject(i).getString("InvoiceNo"));
                                            transaction.setTranDate(jsonArray.getJSONObject(i).getString("TranDate"));
                                            transaction.setDonorID(jsonArray.getJSONObject(i).getString("DonorID"));
                                            transaction.setRegisterMobile(jsonArray.getJSONObject(i).getString("RegisterMobile"));
                                            transaction.setOrganizationID(jsonArray.getJSONObject(i).getString("OrganizationID"));
                                            transaction.setDonationID(jsonArray.getJSONObject(i).getString("DonationID"));
                                            transaction.setDonationName(jsonArray.getJSONObject(i).getString("DonationName"));
                                            transaction.setTranSource(jsonArray.getJSONObject(i).getString("TranSource"));
                                            transaction.setSourceIP(jsonArray.getJSONObject(i).getString("SourceIP"));
                                            transaction.setTranAmount(jsonArray.getJSONObject(i).getString("TranAmount"));
                                            transaction.setTransactionType(jsonArray.getJSONObject(i).getString("TransactionType"));
                                            transaction.setPaymentType(jsonArray.getJSONObject(i).getString("PaymentType"));

                                            transaction.setAgentID(jsonArray.getJSONObject(i).getString("AgentID"));
                                            transaction.setCommissionPercent(jsonArray.getJSONObject(i).getString("CommissionPercent"));
                                            transaction.setFees(jsonArray.getJSONObject(i).getString("Fees"));
                                            transaction.setCardTypeCode(jsonArray.getJSONObject(i).getString("CardTypeCode"));
                                            transaction.setCardNumber(jsonArray.getJSONObject(i).getString("CardNumber"));
                                            transaction.setCardShort(jsonArray.getJSONObject(i).getString("CardShort"));
                                            transaction.setCCardExpMM(jsonArray.getJSONObject(i).getString("CCardExpMM"));
                                            transaction.setCCardExpYYYY(jsonArray.getJSONObject(i).getString("CCardExpYYYY"));
                                            transaction.setCCProcessorID(jsonArray.getJSONObject(i).getString("CCProcessorID"));
                                            transaction.setCCTranApproved(jsonArray.getJSONObject(i).getString("CCTranApproved"));
                                            transaction.setNameOnCard(jsonArray.getJSONObject(i).getString("NameOnCard"));
                                            transaction.setAddress1(jsonArray.getJSONObject(i).getString("Address1"));
                                            transaction.setAddress2(jsonArray.getJSONObject(i).getString("Address2"));
                                            transaction.setCity(jsonArray.getJSONObject(i).getString("City"));
                                            transaction.setState(jsonArray.getJSONObject(i).getString("State"));
                                            transaction.setZip(jsonArray.getJSONObject(i).getString("Zip"));
                                            transaction.setCountryISO3(jsonArray.getJSONObject(i).getString("CountryISO3"));
                                            transaction.setEmailAddress(jsonArray.getJSONObject(i).getString("EmailAddress"));
                                            transaction.setPhoneNumber(jsonArray.getJSONObject(i).getString("PhoneNumber"));
                                            transaction.setProcessorTransID(jsonArray.getJSONObject(i).getString("ProcessorTransID"));
                                            transaction.setProcessorAuthCode(jsonArray.getJSONObject(i).getString("ProcessorAuthCode"));
                                            transaction.setProcessorAuthMsg(jsonArray.getJSONObject(i).getString("ProcessorAuthMsg"));
                                            transaction.setProcessorAVSCode(jsonArray.getJSONObject(i).getString("ProcessorAVSCode"));
                                            transaction.setProcessorCVVCode(jsonArray.getJSONObject(i).getString("ProcessorCVVCode"));
                                            transaction.setProcessorResponseCode(jsonArray.getJSONObject(i).getString("ProcessorResponseCode"));
                                            transaction.setProcessorErrorCode(jsonArray.getJSONObject(i).getString("ProcessorErrorCode"));
                                            transaction.setRequestID(jsonArray.getJSONObject(i).getString("RequestID"));
                                            transaction.setRequestTokenID(jsonArray.getJSONObject(i).getString("RequestTokenID"));
                                            transaction.setIsVoided(jsonArray.getJSONObject(i).getString("IsVoided"));
                                            transaction.setIsFraud(jsonArray.getJSONObject(i).getString("IsFraud"));
                                            transaction.setBankRoutingNum(jsonArray.getJSONObject(i).getString("BankRoutingNum"));
                                            transaction.setBankAccountNum(jsonArray.getJSONObject(i).getString("BankAccountNum"));
                                            transaction.setBankAccountLastNum(jsonArray.getJSONObject(i).getString("BankAccountLastNum"));
                                            transaction.setBankAccountType(jsonArray.getJSONObject(i).getString("BankAccountType"));

                                            transactionArrayList.add(transaction);
                                        }
                                    EventBus.getDefault().post("Transactions True");
                                }
                            }

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
}
