package com.netlynxtech.noiselynx;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceManager;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;
import com.netlynxtech.noiselynx.classes.Utils;

public class SettingsActivity extends SherlockPreferenceActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		addPreferencesFromResource(R.xml.settings_activity);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
		final Preference pref_notification = (Preference) findPreference("pref_notification");
		pref_notification.setSummary(new Utils(SettingsActivity.this).getRingtoneName(prefs.getString("pref_notification", "")));
		pref_notification.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				pref_notification.setSummary(new Utils(SettingsActivity.this).getRingtoneName(newValue.toString()));
				return true;
			}
		});

		Preference pref_test = (Preference) findPreference("pref_test");
		pref_test.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				new Utils(SettingsActivity.this).playNotificationSound();
				new Utils(SettingsActivity.this).showNotifications("Noise level above threshold!", "Place2 : 80 dBA", "some other message");
				return true;
			}
		});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
