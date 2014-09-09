package com.netlynxtech.noiselynx;

import com.securepreferences.SecurePreferences;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsMessage;
import android.util.Log;

public class SMSReceiver extends BroadcastReceiver {
	private static final String TAG = "aeGis";

	private static final String ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
	private static final String EXTRA_SMS_PDUS = "pdus";
	protected static String address;

	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(ACTION_SMS_RECEIVED)) {
			Bundle extras = intent.getExtras();
			if (extras != null) {
				SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

				Log.i(TAG, "Received SMS");

				SmsMessage[] messages = getMessagesFromIntent(intent);
				for (SmsMessage sms : messages) {
					String body = sms.getMessageBody();
					address = sms.getOriginatingAddress();
					if (preferences.getString("pref_remote_reset", "").length() > 0) {
						if (preferences.getString("pref_remote_reset", "").equals(body)) {
							abortBroadcast();
							SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
							sp.edit().clear().commit();
							SecurePreferences scp = new SecurePreferences(context);
							scp.edit().clear().commit();
							android.os.Process.killProcess(android.os.Process.myPid());
						}
					}
				}
			}
		}
	}

	private SmsMessage[] getMessagesFromIntent(Intent intent) {
		Object[] messages = (Object[]) intent.getSerializableExtra(EXTRA_SMS_PDUS);
		byte[][] pduObjs = new byte[messages.length][];

		for (int i = 0; i < messages.length; i++) {
			pduObjs[i] = (byte[]) messages[i];
		}
		byte[][] pdus = new byte[pduObjs.length][];
		int pduCount = pdus.length;
		SmsMessage[] msgs = new SmsMessage[pduCount];
		for (int i = 0; i < pduCount; i++) {
			pdus[i] = pduObjs[i];
			msgs[i] = SmsMessage.createFromPdu(pdus[i]);
		}
		return msgs;
	}
}