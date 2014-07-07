package dev.blacksheep.netlynx;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.dd.processbutton.iml.ActionProcessButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import dev.blacksheep.netlynx.classes.ProgressGenerator;
import dev.blacksheep.netlynx.classes.ProgressGenerator.OnCompleteListener;

public class SiteLocationActivity extends SherlockFragmentActivity {
	ActionProcessButton bUpdateLocation;
	private SupportMapFragment mapFragment;
	private GoogleMap googleMap;
	double latitude, longitude;
	String locationName = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		setContentView(R.layout.site_location_activity);
		Intent i = getIntent();
		locationName = i.getStringExtra(Consts.MONITORING_LOCATION);
		((TextView) findViewById(R.id.tvTime)).setText(i.getStringExtra(Consts.MONITORING_DATE_TIME));
		((TextView) findViewById(R.id.tvDevice)).setText(i.getStringExtra(Consts.MONITORING_LOCATION));
		((TextView) findViewById(R.id.tvCurrentDBA)).setText(i.getStringExtra(Consts.MONITORING_LEQ_FIVE_MINUTES));
		((TextView) findViewById(R.id.tvLEQ1hour)).setText(i.getStringExtra(Consts.MONITORING_LEQ_ONE_HOUR));
		((TextView) findViewById(R.id.tvLEQ12hour)).setText(i.getStringExtra(Consts.MONITORING_LEQ_TWELVE_HOUR));
		latitude = Double.parseDouble(i.getStringExtra(Consts.MONITORING_LOCATION_LAT));
		longitude = Double.parseDouble(i.getStringExtra(Consts.MONITORING_LOCATION_LONG));
		bUpdateLocation = (ActionProcessButton) findViewById(R.id.bUpdateLocation);
		bUpdateLocation.setMode(ActionProcessButton.Mode.ENDLESS);
		final ProgressGenerator progressGenerator = new ProgressGenerator(new OnCompleteListener() {

			@Override
			public void onComplete() {
				if (!bUpdateLocation.getText().toString().equals("Success")) {
					bUpdateLocation.setEnabled(true);
					bUpdateLocation.setProgress(0);
				} else {
					Toast.makeText(SiteLocationActivity.this, "Successfully updated", Toast.LENGTH_SHORT).show();
				}
			}
		});

		bUpdateLocation.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				bUpdateLocation.setEnabled(false);
				progressGenerator.start(bUpdateLocation);
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

}
