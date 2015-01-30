package com.netlynxtech.noiselynx;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.netlynxtech.noiselynx.classes.WebRequestAPI;

public class SiteLocationActivity extends SherlockFragmentActivity {
	Button bUpdateLocation;
	private SupportMapFragment mapFragment;
	private GoogleMap googleMap;
	double latitude, longitude;
	String locationName = "", deviceID = "";
	AdView adView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		setContentView(R.layout.site_location_activity);
		adView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
		Intent i = getIntent();
		locationName = i.getStringExtra(Consts.MONITORING_LOCATION);
		deviceID = i.getStringExtra(Consts.MONITORING_DEVICE_ID);
		((TextView) findViewById(R.id.tvTime)).setText(i.getStringExtra(Consts.MONITORING_DATE_TIME));
		((TextView) findViewById(R.id.tvDevice)).setText(i.getStringExtra(Consts.MONITORING_LOCATION));
		getSupportActionBar().setTitle(i.getStringExtra(Consts.MONITORING_LOCATION));
		((TextView) findViewById(R.id.tvCurrentDBA)).setText(i.getStringExtra(Consts.MONITORING_LEQ_FIVE_MINUTES));
		((TextView) findViewById(R.id.tvLEQ1hour)).setText(i.getStringExtra(Consts.MONITORING_LEQ_ONE_HOUR));
		((TextView) findViewById(R.id.tvLEQ12hour)).setText(i.getStringExtra(Consts.MONITORING_LEQ_TWELVE_HOUR));
		latitude = Double.parseDouble(i.getStringExtra(Consts.MONITORING_LOCATION_LAT));
		longitude = Double.parseDouble(i.getStringExtra(Consts.MONITORING_LOCATION_LONG));
		bUpdateLocation = (Button) findViewById(R.id.bUpdateLocation);
		if (i.getStringExtra(Consts.MONITORING_ALERT).equals(Consts.MONITORING_ALERT_YES)) {
			((View) findViewById(R.id.view1)).setBackgroundColor(Color.parseColor("#FF0000"));
			((TextView) findViewById(R.id.tvCurrentDBA)).setTextColor(Color.parseColor("#FF0000"));
		}

		bUpdateLocation.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String buttonText = bUpdateLocation.getText().toString();
				if (buttonText.equals(SiteLocationActivity.this.getResources().getString(R.string.site_location_get_location))) { // get current location
					bUpdateLocation.setEnabled(false);
					bUpdateLocation.setText(SiteLocationActivity.this.getResources().getString(R.string.site_location_loading));
					Location myLocation = googleMap.getMyLocation();
					if (myLocation != null) {
						LatLng sydney = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
						googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 18));
						googleMap.addMarker(new MarkerOptions().title(locationName).position(sydney));
						bUpdateLocation.setText(SiteLocationActivity.this.getResources().getString(R.string.site_location_show_text));
					} else {
						Toast.makeText(SiteLocationActivity.this, "Unable to get your current location. Do make sure your GPS is turned on and/or click the compass on the top right of the maps",
								Toast.LENGTH_SHORT).show();
						bUpdateLocation.setEnabled(true);
						bUpdateLocation.setText(SiteLocationActivity.this.getResources().getString(R.string.site_location_get_location));
					}
					bUpdateLocation.setEnabled(true);
				} else if (buttonText.equals(SiteLocationActivity.this.getResources().getString(R.string.site_location_show_text))) {
					AlertDialog.Builder builder = new AlertDialog.Builder(SiteLocationActivity.this);
					builder.setMessage("Are you sure you're on the right location? You can do so by turning on your GPS and clicking the compass icon on the top right corner of the map.")
							.setPositiveButton("Yes", new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									try {
										Location loc = googleMap.getMyLocation();
										if (loc != null) {
											Log.e("Location", loc.getLatitude() + "|" + loc.getLongitude());
											// progressGenerator.updateLocation(bUpdateLocation, loc, deviceID, SiteLocationActivity.this);
											new updateLocation().execute(String.valueOf(loc.getLatitude()), String.valueOf(loc.getLongitude()), deviceID);
										}
									} catch (Exception e) {
										e.printStackTrace();
										Toast.makeText(SiteLocationActivity.this, "Unable to get your location. Make sure Google Map above is loaded and your network is stable.", Toast.LENGTH_LONG)
												.show();
									}
								}
							}).setNegativeButton("No", new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									bUpdateLocation.setEnabled(true);
								}
							}).show();
				}
			}
		});
		try {
			initilizeMap();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initilizeMap() {
		if (googleMap == null) {
			mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
			googleMap = mapFragment.getMap();
			googleMap.setMyLocationEnabled(true);
			LatLng sydney = new LatLng(latitude, longitude);
			googleMap.setMyLocationEnabled(true);
			googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 18));
			googleMap.addMarker(new MarkerOptions().title(locationName).position(sydney));
			if (googleMap == null) {
				Toast.makeText(getApplicationContext(), "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
			}
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (adView != null) {
			adView.resume();
		}
		initilizeMap();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// getSupportMenuInflater().inflate(R.menu.alerts_menu, menu);
		return true;
	}

	class updateLocation extends AsyncTask<String, Void, Void> {
		boolean error = false;
		String data = "";

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			SiteLocationActivity.this.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					if (data.contains("success|")) {
						bUpdateLocation.setText("Success!");
						bUpdateLocation.setBackgroundColor(Color.parseColor("#009C12"));
						bUpdateLocation.setTextColor(Color.parseColor("#FFFFFF"));
					} else {
						bUpdateLocation.setEnabled(true);
						Toast.makeText(SiteLocationActivity.this, "Unable to update location. Please contact admin", Toast.LENGTH_SHORT).show();
					}
				}
			});
		}

		@Override
		protected Void doInBackground(String... params) {
			WebRequestAPI w = new WebRequestAPI(SiteLocationActivity.this);
			data = w.updateLocation(params[2], params[0], params[1]);
			return null;
		}

	}

	@Override
	public void onPause() {
		if (adView != null) {
			adView.pause();
		}
		super.onPause();
	}

	/** Called before the activity is destroyed */
	@Override
	public void onDestroy() {
		if (adView != null) {
			adView.destroy();
		}
		super.onDestroy();
	}
}
