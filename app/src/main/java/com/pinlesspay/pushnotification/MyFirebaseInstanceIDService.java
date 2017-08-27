package com.pinlesspay.pushnotification;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.pinlesspay.R;
import com.pinlesspay.model.ModelManager;
import com.pinlesspay.utility.Preferences;
import com.pinlesspay.utility.ServiceApi;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Ravi Tamada on 08/08/16.
 * www.androidhive.info
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);

        // sending reg id to your server
        sendRegistrationToServer(refreshedToken);

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(ServiceApi.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationToServer(final String token) {
        // sending gcm token to server
        Log.e(TAG, "sendRegistrationToServer: " + token);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("OrganizationKey", ServiceApi.ORGANISATION_KEY);
            jsonObject.put("Action", "PostPushNtfy");
            jsonObject.put("Token", Preferences.readString(this, Preferences.AUTH_TOKEN, ""));
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("ApplicationName", getString(R.string.app_name));
            jsonObject1.put("DeviceToken", Preferences.readString(this, Preferences.GCM_TOKEN, ""));
            jsonObject1.put("Provider", "Android");
            jsonObject.put("data", jsonObject1.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ModelManager.getInstance().getAuthManager().savePushToken(this, jsonObject);
    }

    private void storeRegIdInPref(String token) {

        Preferences.writeString(getApplicationContext(), Preferences.GCM_TOKEN, token);
    }
}

