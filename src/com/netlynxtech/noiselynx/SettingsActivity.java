package com.netlynxtech.noiselynx;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

		Preference pref_about = (Preference) findPreference("pref_about");
		pref_about.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			@Override
			public boolean onPreferenceClick(Preference preference) {
				// Toast.makeText(SettingsActivity.this, "LOL", Toast.LENGTH_SHORT).show();
				LayoutInflater inflater = LayoutInflater.from(SettingsActivity.this);
				View layout = inflater.inflate(R.layout.settings_about, null);
				TextView tvAbout = (TextView) layout.findViewById(R.id.tvAbout);
				tvAbout.setText(Html.fromHtml(SettingsActivity.this.getResources().getString(R.string.pref_about_dialog_text)));
				AlertDialog.Builder ad = new AlertDialog.Builder(SettingsActivity.this);
				ad.setIcon(R.drawable.ic_launcher);
				ad.setTitle("About Us");
				ad.setView(layout);

				ad.setPositiveButton("OK", new OnClickListener() {
					public void onClick(DialogInterface dialog, int arg1) {

					}
				});
				ad.show();
				return true;
			}
		});

		final ListPreference pref_housekeep = (ListPreference) findPreference("pref_housekeep");
		pref_housekeep.setSummary(SettingsActivity.this.getResources().getString(R.string.pref_housekeep_summary).toString()
				.replace(" X ", " " + new Utils(SettingsActivity.this).getHousekeep() + " "));

		pref_housekeep.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				pref_housekeep.setSummary(SettingsActivity.this.getResources().getString(R.string.pref_housekeep_summary).toString().replace(" X ", " " + newValue + " "));
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
