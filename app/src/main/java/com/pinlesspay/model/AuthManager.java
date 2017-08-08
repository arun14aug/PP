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
public class AuthManager {

    private String TAG = AuthManager.class.getSimpleName();
    private String deviceToken;
    private String userToken;
    private ArrayList<User> userArrayList;

    public void logIn(final Activity activity, JSONObject post_data) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, ServiceApi.LOGIN, post_data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        PPLog.e("Success Response : ", "Response: " + response.toString());

                        try {
                            boolean state = false;
                            if (response.has("Status"))
                                state = response.getBoolean("Status");
                            if (state) {
                                Preferences.writeString(activity, Preferences.OTP_SENT, "true");
                                EventBus.getDefault().postSticky("Login True");
                            } else {
                                EventBus.getDefault().postSticky("Login False@#@" + response.getString("Message"));
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            EventBus.getDefault().postSticky("Login False");
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                PPLog.e("Error Response : ", "Error: " + error.getMessage());
                EventBus.getDefault().postSticky("Login False");
            }
        });
        RequestQueue requestQueue = Utils.getVolleyRequestQueue(activity);
        requestQueue.add(jsonObjReq);
    }

    public void resendOTP(final Activity activity) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("OrganizationKey", ServiceApi.ORGANISATION_KEY);
            jsonObject.put("Action", "resenddonorotp");
            jsonObject.put("Token", Preferences.readString(activity, Preferences.AUTH_TOKEN, ""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, ServiceApi.RESEND_OTP, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        PPLog.e("Success Response : ", "Response: " + response.toString());

                        try {
                            boolean state = response.getBoolean("Status");
                            if (state) {
                                String data = response.getString("data");
                                JSONObject jsonObject1 = new JSONObject(data);

                                Preferences.writeString(activity, Preferences.OTP_SENT, "true");

                                EventBus.getDefault().postSticky("ResendOTP True@#@" + jsonObject1.getString("Msg"));
                            } else {
                                EventBus.getDefault().postSticky("ResendOTP False@#@" + response.getString("Message"));
                            }
                        } catch (JSONException e) {
                            EventBus.getDefault().postSticky("ResendOTP False");
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                PPLog.e("Error Response : ", "Error: " + error.getMessage());
                EventBus.getDefault().postSticky("ResendOTP False");
            }
        });
        RequestQueue requestQueue = Utils.getVolleyRequestQueue(activity);
        requestQueue.add(jsonObjReq);
    }

    public void verifyUser(final Activity activity, final JSONObject jsonObject) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, ServiceApi.VERIFY_USER, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        PPLog.e("Success Response : ", "Response: " + response.toString());

                        try {
                            boolean state = response.getBoolean("Status");
                            if (state) {
                                Preferences.writeString(activity, Preferences.LOGIN, "true");
//                                if (response.has("data"))
//                                    if (response.getJSONObject("data").has("AuthenticationToken"))
//                                        Preferences.writeString(activity, Preferences.AUTH_TOKEN, response.getJSONObject("data").getString("AuthenticationToken"));
                                if (response.has("data")) {
                                    String data = response.getString("data");
                                    JSONObject jsonObject1 = new JSONObject(data);

//                                    String[] auth = data.split("\"AuthenticationToken\": \"");
//                                    String auth_token = auth[1].substring(0, auth[1].length() - 4);
                                    String auth_token = jsonObject1.getString("AuthenticationToken");
                                    Preferences.writeString(activity, Preferences.AUTH_TOKEN, auth_token);
                                    Preferences.writeString(activity, Preferences.BANKING_ENABLED, jsonObject1.getString("EnableBanking"));
                                }
                                EventBus.getDefault().postSticky("Verify True");
                            } else
                                EventBus.getDefault().postSticky("Verify False@#@" + response.getString("Message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            EventBus.getDefault().postSticky("Verify False");
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                PPLog.e("Error Response : ", "Error: " + error.getMessage());
                EventBus.getDefault().postSticky("Verify False");
            }
        });
        RequestQueue requestQueue = Utils.getVolleyRequestQueue(activity);
        requestQueue.add(jsonObjReq);
    }

    public void userProfile(final Activity activity, JSONObject jsonObject) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, ServiceApi.PROFILE, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        PPLog.e("Success Response : ", "Response: " + response.toString());

                        try {
                            boolean state = response.getBoolean("Status");
                            if (state) {
//                                if (response.has("data"))
//                                    if (response.getJSONObject("data").has("AuthenticationToken"))
//                                        Preferences.writeString(activity, Preferences.AUTH_TOKEN, response.getJSONObject("data").getString("AuthenticationToken"));
                                if (response.has("data")) {
                                    String data = response.getString("data");
                                    String[] auth = data.split("\"Msg\": \"");
                                    String auth_token = auth[1].substring(0, auth[1].length() - 4);
                                    EventBus.getDefault().postSticky("Profile True@#@" + auth_token);
                                }
                            } else
                                EventBus.getDefault().postSticky("Profile False@#@" + response.getString("Message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                            EventBus.getDefault().postSticky("Profile False");
                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                PPLog.e("Error Response : ", "Error: " + error.getMessage());
                EventBus.getDefault().postSticky("Profile False");
            }
        });
        RequestQueue requestQueue = Utils.getVolleyRequestQueue(activity);
        requestQueue.add(jsonObjReq);
    }

    public ArrayList<User> getProfile(Activity activity, boolean shouldRefresh, int pageNumber) {
        if (shouldRefresh)
            getProfileInfo(activity, pageNumber);
        return userArrayList;
    }

    private void getProfileInfo(Activity activity, int pageNumber) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("OrganizationKey", ServiceApi.ORGANISATION_KEY);
            jsonObject.put("PageSize", ServiceApi.PAGE_SIZE);
            jsonObject.put("PageNumber", pageNumber);
            jsonObject.put("Action", "GetDonorProfile");
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
                                if (response.has("data")) {
                                    JSONArray jsonArray = response.getJSONArray("data");
                                    if (jsonArray == null)
                                        jsonArray = new JSONArray();
                                    int count = jsonArray.length();
                                    userArrayList = new ArrayList<>();
                                    if (count > 0)
                                        for (int i = 0; i < count; i++) {
                                            User user = new User();

//                                            "ServiceName": null,
//                                                    "EntityName": null,
//                                                    "Id": null,
//                                                    "DataBaseAction": 2,
//                                                    "DonorId": 39,
//                                                    "RegisterMobile": "918699155321",
//                                                    "FirstName": null,
//                                                    "LastName": null,
//                                                    "Email": null,
//                                                    "MobileNo": null,
//                                                    "Address1": null,
//                                                    "Address2": null,
//                                                    "City": null,
//                                                    "State": null,
//                                                    "Zip": null,
//                                                    "CountryISO3": null

                                            user.setServiceName(jsonArray.getJSONObject(i).getString("ServiceName"));
                                            user.setEntityName(jsonArray.getJSONObject(i).getString("EntityName"));
                                            user.setDataBaseAction(jsonArray.getJSONObject(i).getString("DataBaseAction"));
                                            user.setDonorId(jsonArray.getJSONObject(i).getString("DonorId"));
                                            user.setRegisterMobile(jsonArray.getJSONObject(i).getString("RegisterMobile"));
                                            user.setFirstName(jsonArray.getJSONObject(i).getString("FirstName"));
                                            user.setLastName(jsonArray.getJSONObject(i).getString("LastName"));
                                            user.setId(jsonArray.getJSONObject(i).getString("Id"));
                                            user.setEmail(jsonArray.getJSONObject(i).getString("Email"));
                                            user.setMobileNo(jsonArray.getJSONObject(i).getString("MobileNo"));
                                            user.setAddress1(jsonArray.getJSONObject(i).getString("Address1"));
                                            user.setAddress2(jsonArray.getJSONObject(i).getString("Address2"));
                                            user.setCity(jsonArray.getJSONObject(i).getString("City"));
                                            user.setState(jsonArray.getJSONObject(i).getString("State"));
                                            user.setZip(jsonArray.getJSONObject(i).getString("Zip"));
                                            user.setCountry(jsonArray.getJSONObject(i).getString("CountryISO3"));

                                            userArrayList.add(user);
                                        }
                                    EventBus.getDefault().post("ProfileInfo True");
                                }
                            } else
                                EventBus.getDefault().postSticky("ProfileInfo False@#@" + response.getString("Message"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                            EventBus.getDefault().post("ProfileInfo False");
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                PPLog.e("Error Response : ", "Error: " + error.getMessage());
                EventBus.getDefault().post("ProfileInfo False");
            }
        });
        RequestQueue requestQueue = Utils.getVolleyRequestQueue(activity);
        requestQueue.add(jsonObjReq);
    }

}
