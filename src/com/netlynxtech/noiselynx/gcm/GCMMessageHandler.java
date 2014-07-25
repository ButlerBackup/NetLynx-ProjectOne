package com.netlynxtech.noiselynx.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

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
				//sendNotification("Send error: " + extras.toString());
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
				//sendNotification("Deleted messages on server: " + extras.toString());
				// If it's a regular GCM message, do some work.
			} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
				// sendNotification("Received: " + extras.toString());
				// sendNotification(extras.getString("test"));
				//parseData(extras.getString("test"));
				//Log.e("GCM", extras.toString());
				//longtitle
				//shorttitle
				//body
				sendNotification(extras);
			}
		}
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GCMBroadcastReceiver.completeWakefulIntent(intent);
	}

	private void sendNotification(Bundle msg) {
		new Utils(this).showNotifications(msg.getString("shorttitle"), msg.getString("longtitle"), msg.getString("body"));
	}
}
