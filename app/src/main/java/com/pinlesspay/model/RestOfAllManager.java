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
 * Created by arun.sharma on 8/8/2017.
 */

public class RestOfAllManager {

    private String TAG = ScheduleManager.class.getSimpleName();
    private ArrayList<DonorDevice> donorDeviceArrayList;

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
                        Log.e(TAG, "onSuccess  --> " + response.toString());

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

                                            donorDevice.setDeviceName(jsonArray.getJSONObject(i).getString("DeviceName"));
                                            donorDevice.setDeviceType(jsonArray.getJSONObject(i).getString("DeviceType"));
                                            donorDevice.setDonorId(jsonArray.getJSONObject(i).getString("DonorId"));
                                            donorDevice.setDeviceIdentifier(jsonArray.getJSONObject(i).getString("DeviceIdentifier"));

                                            donorDeviceArrayList.add(donorDevice);
                                        }
                                    EventBus.getDefault().post("GetDevices True");
                                }
                            }

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
}
