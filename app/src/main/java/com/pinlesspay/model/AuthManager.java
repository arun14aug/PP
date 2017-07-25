package delusion.achievers.pinlesspay.model;

import android.app.Activity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import de.greenrobot.event.EventBus;
import delusion.achievers.pinlesspay.utility.PPLog;
import delusion.achievers.pinlesspay.utility.Preferences;
import delusion.achievers.pinlesspay.utility.ServiceApi;
import delusion.achievers.pinlesspay.utility.Utils;

/*
 * Created by HP on 22-07-2017.
 */
public class AuthManager {

    private String TAG = AuthManager.class.getSimpleName();
    private String deviceToken;
    private String userToken;
    public void logIn(final Activity activity, JSONObject post_data) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, ServiceApi.LOGIN, post_data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        PPLog.e("Success Response : ", "Response: " + response.toString());

                        try {
                            boolean state = false;
                            if (response.has("success"))
                                state = response.getBoolean("success");

                            if (state) {
                                Preferences.writeBoolean(activity, Preferences.LOGIN, true);
                                Preferences.writeString(activity, Preferences.USER_ID, response.getString("user_id"));
                                Preferences.writeString(activity, Preferences.USER_TOKEN, response.getString("token"));
//                                ModelManager.getInstance().getAuthManager().setUserToken(response.getString("token"));
                                EventBus.getDefault().postSticky("Login True");
                            } else if (response.has("token")) {
                                Preferences.writeBoolean(activity, Preferences.LOGIN, true);
                                if (response.has("user_id"))
                                    Preferences.writeString(activity, Preferences.USER_ID, response.getString("user_id"));
                                Preferences.writeString(activity, Preferences.USER_TOKEN, response.getString("token"));
//                                ModelManager.getInstance().getAuthManager().setUserToken(response.getString("token"));
                                EventBus.getDefault().postSticky("Login True");
                            } else
                                EventBus.getDefault().postSticky("Login False@#@" + response.getString("msg"));

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

    public void registerUser(final Activity activity, JSONObject jsonObject) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, ServiceApi.REGISTER, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        PPLog.e("Success Response : ", "Response: " + response.toString());

                        try {
                            boolean state = response.getBoolean("success");
                            if (state) {
                                Preferences.writeBoolean(activity, Preferences.REGISTRATION, true);

                                EventBus.getDefault().postSticky("Register True");
                            } else {
                                EventBus.getDefault().postSticky("Register False@#@" + response.getString("msg"));
                            }
                        } catch (JSONException e) {
                            EventBus.getDefault().postSticky("Register False");
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                PPLog.e("Error Response : ", "Error: " + error.getMessage());
                EventBus.getDefault().postSticky("Register False");
            }
        });
        RequestQueue requestQueue = Utils.getVolleyRequestQueue(activity);
        requestQueue.add(jsonObjReq);
    }

    public void verifyUser(final Activity activity, JSONObject jsonObject) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, ServiceApi.VERIFY_USER, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        PPLog.e("Success Response : ", "Response: " + response.toString());

                        try {
                            boolean state = response.getBoolean("success");
                            if (state) {
                                Preferences.writeBoolean(activity, Preferences.LOGIN, true);
                                Preferences.writeBoolean(activity, Preferences.REGISTRATION, false);

                                EventBus.getDefault().postSticky("Verify True");
                            } else {
                                EventBus.getDefault().postSticky("Verify False@#@" + response.getString("msg"));
                            }
                        } catch (JSONException e) {
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


}
