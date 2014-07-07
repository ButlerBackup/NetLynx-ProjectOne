package dev.blacksheep.netlynx.classes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.securepreferences.SecurePreferences;

import dev.blacksheep.netlynx.Consts;
import dev.blacksheep.netlynx.R;

public class Utils {
	Context context;

	public Utils(Context context) {
		this.context = context;
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
		return sp.getString(Consts.PREFERENCES_UDID, "0");
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
		Log.e("DATE DATE", sp.getString("pref_timing", "1"));
		final String pattern = "yyyy-MM-dd'T'hh:mm:ss";
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
		Log.e("DATE DATE", sp.getString("pref_timing", "1"));
		final String pattern = "hh:mm";
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

	public void showNotifications(String shortTitle, String title, String message) {
		NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notif = new Notification(R.drawable.ic_launcher, shortTitle, System.currentTimeMillis());
		notif.setLatestEventInfo(context, title, message, null);

		// ---100ms delay, vibrate for 250ms, pause for 100 ms and then vibrate for 500ms---
		notif.vibrate = new long[] { 100, 250, 100, 500 };
		nm.notify(111, notif);
	}
}
