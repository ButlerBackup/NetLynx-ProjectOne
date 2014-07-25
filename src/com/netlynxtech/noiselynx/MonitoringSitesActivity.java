package com.netlynxtech.noiselynx;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.manuelpeinado.refreshactionitem.ProgressIndicatorType;
import com.manuelpeinado.refreshactionitem.RefreshActionItem;
import com.manuelpeinado.refreshactionitem.RefreshActionItem.RefreshActionListener;
import com.netlynxtech.noiselynx.adapter.MonitoringSitesAdapter;
import com.netlynxtech.noiselynx.classes.Utils;
import com.netlynxtech.noiselynx.classes.WebRequestAPI;

public class MonitoringSitesActivity extends SherlockActivity {
	private RefreshActionItem mRefreshActionItem;
	ListView lvMonitoringSite;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.monitoring_sites_activity);
		if (!new Utils(MonitoringSitesActivity.this).checkIfLoggedIn()) {
			finish();
		}
		lvMonitoringSite = (ListView) findViewById(R.id.lvMonitoringSite);
		lvMonitoringSite.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				TextView tvID = (TextView) view.findViewById(R.id.tvID);
				TextView tvLocation = (TextView) view.findViewById(R.id.tvDevice);
				TextView tvTime = (TextView) view.findViewById(R.id.tvTime);
				TextView tvCurrent = (TextView) view.findViewById(R.id.tvCurrentDBA);
				TextView tvOneHour = (TextView) view.findViewById(R.id.tvLEQ1hour);
				TextView tvTwelveHour = (TextView) view.findViewById(R.id.tvLEQ12hour);
				TextView tvLocationLat = (TextView) view.findViewById(R.id.tvLocationLat);
				TextView tvLocationLong = (TextView) view.findViewById(R.id.tvLocationLong);
				TextView tvAlert = (TextView) view.findViewById(R.id.tvAlert);
				Intent i = new Intent(MonitoringSitesActivity.this, HistoryActivity.class);
				i.putExtra(Consts.MONITORING_DEVICE_ID, tvID.getText().toString());
				i.putExtra(Consts.MONITORING_DATE_TIME, tvTime.getText().toString());
				i.putExtra(Consts.MONITORING_LOCATION, tvLocation.getText().toString());
				i.putExtra(Consts.MONITORING_LEQ_FIVE_MINUTES, tvCurrent.getText().toString());
				i.putExtra(Consts.MONITORING_LEQ_ONE_HOUR, tvOneHour.getText().toString());
				i.putExtra(Consts.MONITORING_LEQ_TWELVE_HOUR, tvTwelveHour.getText().toString());
				i.putExtra(Consts.MONITORING_LOCATION_LAT, tvLocationLat.getText().toString());
				i.putExtra(Consts.MONITORING_LOCATION_LONG, tvLocationLong.getText().toString());
				i.putExtra(Consts.MONITORING_ALERT, tvAlert.getText().toString());
				startActivity(i);
			}
		});
		// setContentView(R.layout.progress_loading);
		// ViewGroupUtils.replaceView(findViewById(R.layout.monitoring_sites_activity), findViewById(R.layout.progress_loading));
		getDevices();
	}

	@Override
	protected void onResume() {
		super.onResume();
		getDevices();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			startActivity(new Intent(MonitoringSitesActivity.this, SettingsActivity.class));
			break;
		case R.id.menu_monitoring_alert:
			startActivity(new Intent(MonitoringSitesActivity.this, AlertsActivity.class));
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.monitoring_sites_menu, menu);
		MenuItem item = menu.findItem(R.id.menu_refresh);
		mRefreshActionItem = (RefreshActionItem) item.getActionView();
		mRefreshActionItem.setMenuItem(item);
		mRefreshActionItem.setProgressIndicatorType(ProgressIndicatorType.INDETERMINATE);
		mRefreshActionItem.showProgress(true);
		mRefreshActionItem.setRefreshActionListener(new RefreshActionListener() {

			@Override
			public void onRefreshButtonClick(RefreshActionItem sender) {
				mRefreshActionItem.showProgress(true);
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
						}
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								getDevices();
							}
						});
					}

				}).start();

			}
		});
		return true;
	}

	private void getDevices() {
		new AsyncTask<Void, Void, Void>() {
			MonitoringSitesAdapter adapter;
			ArrayList<HashMap<String, String>> data;
			WebRequestAPI api = new WebRequestAPI(MonitoringSitesActivity.this);

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				MonitoringSitesActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if (data != null) {
							if (data.size() > 0) {
								lvMonitoringSite.setAdapter(adapter);
								/*
								 * LayoutInflater inflator = (LayoutInflater) MonitoringSitesActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE); View v =
								 * inflator.inflate(R.layout.monitoring_sites_activity_actionbar, null); getSupportActionBar().setDisplayShowCustomEnabled(true);
								 * getSupportActionBar().setCustomView(v); ArrayAdapter<String> adapter = new ArrayAdapter<String>(MonitoringSitesActivity.this,
								 * android.R.layout.simple_dropdown_item_1line, api.getLocationList()); AutoCompleteTextView textView = (AutoCompleteTextView) v.findViewById(R.id.etPlace);
								 * textView.setAdapter(adapter);
								 */
							} else {
								Toast.makeText(MonitoringSitesActivity.this, "Unable to retreive data", Toast.LENGTH_SHORT).show();
							}
							try {
								mRefreshActionItem.showProgress(false);
							} catch (Exception e) {
							}
						}
					}
				});
			}

			@Override
			protected Void doInBackground(Void... params) {
				try {
					data = api.getDevices(new Utils(MonitoringSitesActivity.this).getUDID());
					adapter = new MonitoringSitesAdapter(MonitoringSitesActivity.this, data);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		}.execute(null, null, null);
	}
}
