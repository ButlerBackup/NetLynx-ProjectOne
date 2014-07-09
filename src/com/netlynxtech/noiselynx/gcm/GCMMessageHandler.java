package com.netlynxtech.noiselynx.gcm;

import java.io.ByteArrayInputStream;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.xmlpull.v1.XmlPullParser;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.netlynxtech.noiselynx.classes.Utils;

public class GCMMessageHandler extends IntentService {

	String TAG = "GCMMessageHandler";

	public GCMMessageHandler() {
		super("GcmMessageHandler");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		Bundle extras = intent.getExtras();

		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
		// The getMessageType() intent parameter must be the intent you received
		// in your BroadcastReceiver.
		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) { // has effect of unparcelling Bundle
			/*
			 * Filter messages based on message type. Since it is likely that GCM will be extended in the future with new message types, just ignore any message types you're not interested in, or that
			 * you don't recognize.
			 */
			if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
				sendNotification("Send error: " + extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
				sendNotification("Deleted messages on server: " + extras.toString());
				// If it's a regular GCM message, do some work.
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
				// sendNotification("Received: " + extras.toString());
				// sendNotification(extras.getString("test"));
				parseData(extras.getString("test"));
			}
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GCMBroadcastReceiver.completeWakefulIntent(intent);
	}

	private void parseData(String data) {
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		SoapObject result = null;
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(data.getBytes("UTF-8"));
			XmlPullParser p = Xml.newPullParser();
			p.setInput(inputStream, "UTF-8");
			// p.setInput(inputStream, UTF8_Encoding);
			envelope.parse(p);
			result = (SoapObject) envelope.getResponse();
			Log.e("TEST", result.toString());
			if (result.getProperty(0).toString().equals("1")) {
				new Utils(GCMMessageHandler.this).showNotifications("", "", "");
			} else {
				new Utils(GCMMessageHandler.this).showNotifications("", "", "");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void sendNotification(String msg) {
		new Utils(this).showNotifications("New Message", "Title", msg);
	}
}
