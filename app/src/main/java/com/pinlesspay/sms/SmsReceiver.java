package com.pinlesspay.sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.pinlesspay.view.activity.VerifyActivity;

/*
 * Created by arun.sharma on 19-04-2016.
 */
public class SmsReceiver extends BroadcastReceiver {

    private static SmsListener mListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (Object aPdusObj : pdusObj) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();
                    try {
                        if (senderNum.equals("TA-DOCOMO")) {
                            VerifyActivity Sms = new VerifyActivity();
                            Sms.recivedSms(message );
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}
