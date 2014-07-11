package com.netlynxtech.noiselynx.classes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.netlynxtech.noiselynx.AlertsActivity;
import com.netlynxtech.noiselynx.Consts;
import com.netlynxtech.noiselynx.R;
import com.securepreferences.SecurePreferences;

public class Utils {
	Context context;

	public Utils(Context context) {
		this.context = context;
	}

	public boolean checkIfLoggedIn() {
		SecurePreferences sp = new SecurePreferences(context);
		if (!sp.getString(Consts.PREFERENCES_PHONE_NO, "0").equals("0") && !sp.getString(Consts.PREFERENCES_GCMID, "0").equals("0") && !sp.getString(Consts.PREFERENCES_UDID, "0").equals("0")) {
			return true;
		}
		return false;
	}

	public String getPassword() {
		SecurePreferences sp = new SecurePreferences(context);
		return sp.getString(Consts.PREFERENCES_PASSWORD, "0");
	}

	public void setPassword(String pw) {
		SecurePreferences sp = new SecurePreferences(context);
		sp.edit().putString(Consts.PREFERENCES_PASSWORD, pw).commit();
	}
	
	public void setGCMID(String id) {
		SecurePreferences sp = new SecurePreferences(context);
		sp.edit().putString(Consts.PREFERENCES_GCMID, id).commit();
	}

	public String getDeviceId() {
		SecurePreferences sp = new SecurePreferences(context);
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		final String deviceId = tm.getDeviceId();
		if (deviceId != null) {
			sp.edit().putString(Consts.PREFERENCES_UDID, deviceId).commit();
			return deviceId;
		} else {
			sp.edit().putString(Consts.PREFERENCES_UDID, android.os.Build.SERIAL).commit();
			return android.os.Build.SERIAL;
		}
	}

	public String getUDID() {
		SecurePreferences sp = new SecurePreferences(context);
		Log.e("DEVICE ID", sp.getString(Consts.PREFERENCES_UDID, "0"));
		return sp.getString(Consts.PREFERENCES_UDID, "0");
	}

	public String getHousekeep() {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		return sp.getString("pref_housekeep", "5");
	}

	private MediaPlayer mMediaPlayer;

	public void playNotificationSound() {
		SharedPreferences getAlarms = PreferenceManager.getDefaultSharedPreferences(context);
		String alarms = getAlarms.getString("pref_notification", "default ringtone");
		Uri uri = Uri.parse(alarms);
		playSound(context, uri);
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				mMediaPlayer.stop();
			}
		}, 5000);
		//
	}

	private void playSound(Context context, Uri alert) {
		mMediaPlayer = new MediaPlayer();
		try {
			mMediaPlayer.setDataSource(context, alert);
			final AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
			if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
				mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
				mMediaPlayer.prepare();
				mMediaPlayer.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getRingtoneName(String path) {
		Uri uri = Uri.parse(path);
		Ringtone ringtone = RingtoneManager.getRingtone(context, uri);
		return ringtone.getTitle(context);
	}

	public String parseDatetime(String datetime) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		// Log.e("DATE DATE", sp.getString("pref_timing", "1"));
		final String pattern = "yyyy-MM-dd'T'HH:mm:ss";
		final SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
		try {
			SimpleDateFormat outFormatter;
			if (sp.getString("pref_timing", "1").equals("1")) {
				outFormatter = new SimpleDateFormat("d MMMM yyyy HH:mm", Locale.getDefault());
			} else {
				outFormatter = new SimpleDateFormat("d MMMM yyyy KK:mma", Locale.getDefault());
			}
			Date d = sdf.parse(datetime);
			return outFormatter.format(d).toString();
		} catch (Exception e) {
			return datetime;
		}
	}

	public String parseTimeOnly(String time) {
		Pattern mpattern = Pattern.compile("^(.*?):.*? - (.*?):.*?$", Pattern.CASE_INSENSITIVE);
		Matcher matcher = mpattern.matcher(time);
		// using Matcher find(), group(), start() and end() methods
		if (matcher.find()) {
			Log.e("FIRST NUMBER", matcher.group(1));
			Log.e("SECOND NUMBER", matcher.group(2));
		}
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
		// Log.e("DATE DATE", sp.getString("pref_timing", "1"));
		final String pattern = "HH:mm";
		final SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.getDefault());
		try {
			SimpleDateFormat outFormatter;
			if (sp.getString("pref_timing", "1").equals("1")) {
				outFormatter = new SimpleDateFormat("HH:mm - HH:mm", Locale.getDefault());
			} else {
				outFormatter = new SimpleDateFormat("KK:mma - KK:mma", Locale.getDefault());
			}
			Date d = sdf.parse(time);
			return outFormatter.format(d).toString();
		} catch (Exception e) {
			return time;
		}
	}

	public String convertTimetoUnix(String datetime) {
		// Log.e("TIME", datetime);
		final String pattern = "yyyy-MM-dd'T'HH:mm:ss";
		final SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.ENGLISH);
		try {
			sdf.setTimeZone(TimeZone.getTimeZone("GMT")); // unble to get GMT + 8
			Date d = sdf.parse(datetime);
			long unix = d.getTime() / 1000;
			Log.e("TIME TO UNIX", String.valueOf(unix));
			return String.valueOf(unix);
		} catch (Exception e) {
			e.printStackTrace();
			return datetime;
		}

	}

	public void showNotifications(String shortTitle, String title, String message) {
		SecurePreferences sp = new SecurePreferences(context);
		long[] vibration;
		if (sp.getBoolean("pref_vibration", false)) {
			vibration = new long[] { 100, 250, 100, 500 };
		} else {
			vibration = new long[] { 0 };
		}
		NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Intent myIntent = new Intent(context, AlertsActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, myIntent, Intent.FLAG_ACTIVITY_NEW_TASK);

		Notification myNotification = new NotificationCompat.Builder(context).setContentTitle(title).setContentText(message).setTicker(shortTitle).setWhen(System.currentTimeMillis())
				.setContentIntent(pendingIntent).setDefaults(Notification.DEFAULT_SOUND).setAutoCancel(true).setSmallIcon(R.drawable.ic_launcher).setVibrate(vibration).build();

		notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.notify(999, myNotification);
	}

	private static final int SECOND_MILLIS = 1000;
	private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
	private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
	private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

	public static String getTimeAgo(long time, Context ctx) {
		if (time < 1000000000000L) {
			// if timestamp given in seconds, convert to millis
			time *= 1000;
		}

		long now = System.currentTimeMillis();
		if (time > now || time <= 0) {
			return null;
		}

		// TODO: localize
		final long diff = now - time;
		if (diff < MINUTE_MILLIS) {
			return "just now";
		} else if (diff < 2 * MINUTE_MILLIS) {
			return "a minute ago";
		} else if (diff < 50 * MINUTE_MILLIS) {
			return diff / MINUTE_MILLIS + " minutes ago";
		} else if (diff < 90 * MINUTE_MILLIS) {
			return "an hour ago";
		} else if (diff < 24 * HOUR_MILLIS) {
			return diff / HOUR_MILLIS + " hours ago";
		} else if (diff < 48 * HOUR_MILLIS) {
			return "yesterday";
		} else {
			return diff / DAY_MILLIS + " days ago";
		}
	}

	public String getDaysAgo(String timee) {
		long time = Long.parseLong(timee);
		if (time < 1000000000000L) {
			// if timestamp given in seconds, convert to millis
			time *= 1000;
		}

		long now = System.currentTimeMillis();
		if (time > now || time <= 0) {
			return null;
		}

		// TODO: localize
		final long diff = now - time;
		if (diff < MINUTE_MILLIS) {
			return null;
		} else if (diff < 2 * MINUTE_MILLIS) {
			return null;
		} else if (diff < 50 * MINUTE_MILLIS) {
			return null;
		} else if (diff < 90 * MINUTE_MILLIS) {
			return null;
		} else if (diff < 24 * HOUR_MILLIS) {
			return null;
		} else if (diff < 48 * HOUR_MILLIS) {
			return null;
		} else {
			return String.valueOf(diff / DAY_MILLIS);
		}
	}
}
