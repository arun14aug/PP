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
public class PaymentManager {

    private String TAG = PaymentManager.class.getSimpleName();
    private ArrayList<CreditCard> creditCardArrayList;
    private ArrayList<Bank> bankArrayList;

    public ArrayList<CreditCard> getCreditCard(Activity activity, boolean shouldRefresh, int pageNumber) {
        if (shouldRefresh)
            getCardList(activity, pageNumber);
        return creditCardArrayList;
    }

    private void getCardList(Activity activity, int pageNumber) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("OrganizationKey", ServiceApi.ORGANISATION_KEY);
            jsonObject.put("PageSize", ServiceApi.PAGE_SIZE);
            jsonObject.put("PageNumber", pageNumber);
            jsonObject.put("Action", "DonorCreditCards");
            jsonObject.put("Token", Preferences.readString(activity, Preferences.AUTH_TOKEN, ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        PPLog.e("json data : ", jsonObject.toString());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, ServiceApi.GET_ALL_DATA, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "onSuccess  --> " + response.toString());

                        try {

                            boolean state = response.getBoolean("Status");
                            if (state) {
                                if (response.has("data") && !response.isNull("data")) {
                                    JSONArray jsonArray = response.getJSONArray("data");
                                    if (jsonArray == null)
                                        jsonArray = new JSONArray();
                                    int count = jsonArray.length();
                                    creditCardArrayList = new ArrayList<>();
                                    if (count > 0)
                                        for (int i = 0; i < count; i++) {
                                            CreditCard creditCard = new CreditCard();

                                            creditCard.setServiceName(jsonArray.getJSONObject(i).getString("ServiceName"));
                                            creditCard.setEntityName(jsonArray.getJSONObject(i).getString("EntityName"));
                                            creditCard.setId(jsonArray.getJSONObject(i).getString("Id"));
                                            creditCard.setDataBaseAction(jsonArray.getJSONObject(i).getString("DataBaseAction"));
                                            creditCard.setAccountId(jsonArray.getJSONObject(i).getString("AccountId"));
                                            creditCard.setCardTypeCode(jsonArray.getJSONObject(i).getString("CardTypeCode"));
                                            creditCard.setMaskCardNumber(jsonArray.getJSONObject(i).getString("MaskCardNumber"));
                                            creditCard.setCCardExpMM(jsonArray.getJSONObject(i).getString("CCardExpMM"));
                                            creditCard.setCCardExpYYYY(jsonArray.getJSONObject(i).getString("CCardExpYYYY"));
                                            creditCard.setNickName(jsonArray.getJSONObject(i).getString("NickName"));
                                            creditCard.setIsDefault(jsonArray.getJSONObject(i).getString("IsDefault"));
//                                            creditCard.setOrganizationID(jsonArray.getJSONObject(i).getString("OrganizationID"));
//                                            creditCard.setIsActive(jsonArray.getJSONObject(i).getString("IsActive"));

                                            creditCardArrayList.add(creditCard);
                                        }
                                }
                                EventBus.getDefault().post("CreditCard True");
                            } else
                                EventBus.getDefault().post("CreditCard False@#@" + response.getString("Message"));


                        } catch (JSONException e) {
                            e.printStackTrace();
                            EventBus.getDefault().post("CreditCard False");
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                PPLog.e("Error Response : ", "Error: " + error.getMessage());
                EventBus.getDefault().post("CreditCard False");
            }
        });
        RequestQueue requestQueue = Utils.getVolleyRequestQueue(activity);
        requestQueue.add(jsonObjReq);
    }

    public ArrayList<Bank> getBank(Activity activity, boolean shouldRefresh, int pageNumber) {
        if (shouldRefresh)
            getBankList(activity, pageNumber);
        return bankArrayList;
    }

    private void getBankList(Activity activity, int pageNumber) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("OrganizationKey", ServiceApi.ORGANISATION_KEY);
            jsonObject.put("PageSize", ServiceApi.PAGE_SIZE);
            jsonObject.put("PageNumber", pageNumber);
            jsonObject.put("Action", "DonorBankAccounts");
            jsonObject.put("Token", Preferences.readString(activity, Preferences.AUTH_TOKEN, ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, ServiceApi.GET_ALL_DATA, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "onSuccess  --> " + response.toString());

                        try {

                            boolean state = response.getBoolean("Status");
                            if (state) {
                                if (response.has("data") && !response.isNull("data")) {
                                    JSONArray jsonArray = response.getJSONArray("data");
//                                }
//
//                                if (response.getJSONObject("data").has("Entities")) {
//                                    JSONArray jsonArray = response.getJSONObject("data").getJSONArray("Entities");
//                                    if (jsonArray == null)
//                                        jsonArray = new JSONArray();
                                    int count = jsonArray.length();
                                    bankArrayList = new ArrayList<>();
                                    if (count > 0)
                                        for (int i = 0; i < count; i++) {
                                            Bank bank = new Bank();

                                            bank.setServiceName(jsonArray.getJSONObject(i).getString("ServiceName"));
                                            bank.setEntityName(jsonArray.getJSONObject(i).getString("EntityName"));
                                            bank.setId(jsonArray.getJSONObject(i).getString("Id"));
                                            bank.setDataBaseAction(jsonArray.getJSONObject(i).getString("DataBaseAction"));
                                            bank.setAccountId(jsonArray.getJSONObject(i).getString("AccountId"));
                                            bank.setBankAccountType(jsonArray.getJSONObject(i).getString("BankAccountType"));
                                            bank.setMaskBankRoutingNum(jsonArray.getJSONObject(i).getString("MaskBankRoutingNum"));
                                            bank.setMaskBankAccountNum(jsonArray.getJSONObject(i).getString("MaskBankAccountNum"));
                                            bank.setNickName(jsonArray.getJSONObject(i).getString("NickName"));
                                            bank.setIsDefault(jsonArray.getJSONObject(i).getString("IsDefault"));
                                            bank.setFirstName(jsonArray.getJSONObject(i).getString("FirstName"));
                                            bank.setLastName(jsonArray.getJSONObject(i).getString("LastName"));
                                            bank.setRepeatBankAccountNum(jsonArray.getJSONObject(i).getString("RepeatBankAccountNum"));

                                            bankArrayList.add(bank);
                                        }
                                }
                                EventBus.getDefault().post("BankList True");
                            } else
                                EventBus.getDefault().post("BankList False@#@" + response.getString("Message"));


                        } catch (JSONException e) {
                            e.printStackTrace();
                            EventBus.getDefault().post("BankList False");
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                PPLog.e("Error Response : ", "Error: " + error.getMessage());
                EventBus.getDefault().post("BankList False");
            }
        });
        RequestQueue requestQueue = Utils.getVolleyRequestQueue(activity);
        requestQueue.add(jsonObjReq);
    }

    public void addBankAccount(final Activity activity, JSONObject jsonObject) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, ServiceApi.PROCESS_BANK_ACCOUNT, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        PPLog.e("Success Response : ", "Response: " + response.toString());

                        try {
                            boolean state = response.getBoolean("Status");
                            if (state) {
                                EventBus.getDefault().postSticky("AddBankAccount True");
                            } else
                                EventBus.getDefault().postSticky("AddBankAccount False@#@" + response.getString("Message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            EventBus.getDefault().postSticky("AddBankAccount False");
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                PPLog.e("Error Response : ", "Error: " + error.getMessage());
                EventBus.getDefault().postSticky("AddBankAccount False");
            }
        });
        RequestQueue requestQueue = Utils.getVolleyRequestQueue(activity);
        requestQueue.add(jsonObjReq);
    }
    public void updateBankAccount(final Activity activity, JSONObject jsonObject) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, ServiceApi.UPDATE_BANK_ACCOUNT, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        PPLog.e("Success Response : ", "Response: " + response.toString());

                        try {
                            boolean state = response.getBoolean("Status");
                            if (state) {
                                EventBus.getDefault().postSticky("UpdateBankAccount True");
                            } else
                                EventBus.getDefault().postSticky("UpdateBankAccount False@#@" + response.getString("Message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            EventBus.getDefault().postSticky("UpdateBankAccount False");
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                PPLog.e("Error Response : ", "Error: " + error.getMessage());
                EventBus.getDefault().postSticky("UpdateBankAccount False");
            }
        });
        RequestQueue requestQueue = Utils.getVolleyRequestQueue(activity);
        requestQueue.add(jsonObjReq);
    }

    public void addCreditCard(final Activity activity, JSONObject jsonObject) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, ServiceApi.PROCESS_CREDIT_CARD, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        PPLog.e("Success Response : ", "Response: " + response.toString());

                        try {
                            boolean state = response.getBoolean("Status");
                            if (state) {
                                EventBus.getDefault().postSticky("AddCC True");
                            } else
                                EventBus.getDefault().postSticky("AddCC False@#@" + response.getString("Message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            EventBus.getDefault().postSticky("AddCC False");
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                PPLog.e("Error Response : ", "Error: " + error.getMessage());
                EventBus.getDefault().postSticky("AddCC False");
            }
        });
        RequestQueue requestQueue = Utils.getVolleyRequestQueue(activity);
        requestQueue.add(jsonObjReq);
    }
    public void updateCreditCard(final Activity activity, JSONObject jsonObject) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, ServiceApi.UPDATE_CREDIT_CARD, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        PPLog.e("Success Response : ", "Response: " + response.toString());

                        try {
                            boolean state = response.getBoolean("Status");
                            if (state) {
                                EventBus.getDefault().postSticky("UpdateCC True");
                            } else
                                EventBus.getDefault().postSticky("UpdateCC False@#@" + response.getString("Message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            EventBus.getDefault().postSticky("UpdateCC False");
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                PPLog.e("Error Response : ", "Error: " + error.getMessage());
                EventBus.getDefault().postSticky("UpdateCC False");
            }
        });
        RequestQueue requestQueue = Utils.getVolleyRequestQueue(activity);
        requestQueue.add(jsonObjReq);
    }
    public void deleteDonorAccount(final Activity activity, JSONObject jsonObject) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, ServiceApi.DELETE_DONOR_ACOUNT, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        PPLog.e("Success Response : ", "Response: " + response.toString());

                        try {
                            boolean state = response.getBoolean("Status");
                            if (state) {
                                EventBus.getDefault().postSticky("DeleteDonorAccount True");
                            } else
                                EventBus.getDefault().postSticky("DeleteDonorAccount False@#@" + response.getString("Message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            EventBus.getDefault().postSticky("DeleteDonorAccount False");
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                PPLog.e("Error Response : ", "Error: " + error.getMessage());
                EventBus.getDefault().postSticky("DeleteDonorAccount False");
            }
        });
        RequestQueue requestQueue = Utils.getVolleyRequestQueue(activity);
        requestQueue.add(jsonObjReq);
    }
}
