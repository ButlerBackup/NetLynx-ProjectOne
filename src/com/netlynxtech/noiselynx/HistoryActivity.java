package com.netlynxtech.noiselynx;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.manuelpeinado.refreshactionitem.ProgressIndicatorType;
import com.manuelpeinado.refreshactionitem.RefreshActionItem;
import com.manuelpeinado.refreshactionitem.RefreshActionItem.RefreshActionListener;
import com.netlynxtech.noiselynx.adapter.HistoryAdapter;
import com.netlynxtech.noiselynx.classes.DataWrapper;
import com.netlynxtech.noiselynx.classes.WebRequestAPI;

public class HistoryActivity extends SherlockActivity {
	private RefreshActionItem mRefreshActionItem;
	ListView lvHistory;
	String deviceID, latitude, longitude;
	Intent i;
	ArrayList<HashMap<String, String>> historyData;
	boolean canShowGraphMenu = false;
	MenuItem itemGraph;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setTitle(HistoryActivity.this.getResources().getString(R.string.history_name));
		setContentView(R.layout.history_activity_layout);
		i = getIntent();
		deviceID = i.getStringExtra(Consts.MONITORING_DEVICE_ID);
		((TextView) findViewById(R.id.tvTime)).setText(i.getStringExtra(Consts.MONITORING_DATE_TIME));
		((TextView) findViewById(R.id.tvDevice)).setText(i.getStringExtra(Consts.MONITORING_LOCATION));
		((TextView) findViewById(R.id.tvCurrentDBA)).setText(i.getStringExtra(Consts.MONITORING_LEQ_FIVE_MINUTES));
		((TextView) findViewById(R.id.tvLEQ1hour)).setText(i.getStringExtra(Consts.MONITORING_LEQ_ONE_HOUR));
		((TextView) findViewById(R.id.tvLEQ12hour)).setText(i.getStringExtra(Consts.MONITORING_LEQ_TWELVE_HOUR));
		if (i.getStringExtra(Consts.MONITORING_ALERT).equals(Consts.MONITORING_ALERT_YES)) {
			((View) findViewById(R.id.view1)).setBackgroundColor(Color.parseColor("#FF0000"));
			((TextView) findViewById(R.id.tvCurrentDBA)).setTextColor(Color.parseColor("#FF0000"));
		}
		lvHistory = (ListView) findViewById(R.id.lvHistory);
		supportInvalidateOptionsMenu();
		getHistory();
		getThreshold();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		case R.id.menu_map:
			Intent in = new Intent(HistoryActivity.this, SiteLocationActivity.class);
			in.putExtra(Consts.MONITORING_DEVICE_ID, i.getStringExtra(Consts.MONITORING_DEVICE_ID).toString());
			in.putExtra(Consts.MONITORING_DATE_TIME, i.getStringExtra(Consts.MONITORING_DATE_TIME).toString());
			in.putExtra(Consts.MONITORING_LOCATION, i.getStringExtra(Consts.MONITORING_LOCATION).toString());
			in.putExtra(Consts.MONITORING_LEQ_FIVE_MINUTES, i.getStringExtra(Consts.MONITORING_LEQ_FIVE_MINUTES).toString());
			in.putExtra(Consts.MONITORING_LEQ_ONE_HOUR, i.getStringExtra(Consts.MONITORING_LEQ_ONE_HOUR).toString());
			in.putExtra(Consts.MONITORING_LEQ_TWELVE_HOUR, i.getStringExtra(Consts.MONITORING_LEQ_TWELVE_HOUR).toString());
			in.putExtra(Consts.MONITORING_LOCATION_LAT, i.getStringExtra(Consts.MONITORING_LOCATION_LAT).toString());
			in.putExtra(Consts.MONITORING_LOCATION_LONG, i.getStringExtra(Consts.MONITORING_LOCATION_LONG).toString());
			in.putExtra(Consts.MONITORING_ALERT, i.getStringExtra(Consts.MONITORING_ALERT).toString());
			startActivity(in);
			break;
		case R.id.menu_graph:
			Intent i = new Intent(HistoryActivity.this, GraphActivity.class);
			ArrayList<Number> mo = new WebRequestAPI(HistoryActivity.this).getHistoryNumbersOnly(historyData);
			ArrayList<String> co = new WebRequestAPI(HistoryActivity.this).getHistoryXAxisOnly(historyData);
			i.putExtra("data", new DataWrapper(mo, co));
			startActivity(i);
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.history_menu, menu);
		itemGraph = menu.findItem(R.id.menu_graph);
		if (!canShowGraphMenu) {
			itemGraph.setEnabled(false);
		}
		MenuItem item = menu.findItem(R.id.menu_refresh);
		mRefreshActionItem = (RefreshActionItem) item.getActionView();
		mRefreshActionItem.setMenuItem(item);
		mRefreshActionItem.setProgressIndicatorType(ProgressIndicatorType.INDETERMINATE);
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
								getHistory();
								getThreshold();
							}
						});
					}

				}).start();

			}
		});
		return true;
	}

	private void getThreshold() {
		new AsyncTask<Void, Void, Void>() {
			ArrayList<String> data;

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				HistoryActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if (data != null) {
							if (data.size() > 0) {
								((TextView) findViewById(R.id.tvThresholdOne)).setText(data.get(0));
								((TextView) findViewById(R.id.tvThresholdTwo)).setText(data.get(1));
								((TextView) findViewById(R.id.tvThresholdThree)).setText(data.get(2));
							} else {
								Toast.makeText(HistoryActivity.this, "Unable to retreive data", Toast.LENGTH_SHORT).show();
							}
						}
						try {
							mRefreshActionItem.showProgress(false);
						} catch (Exception e) {
						}
						canShowGraphMenu = true;
						itemGraph.setEnabled(true);
						supportInvalidateOptionsMenu();
					}
				});
			}

			@Override
			protected Void doInBackground(Void... params) {
				try {
					data = new WebRequestAPI(HistoryActivity.this).getThreshold(deviceID);
					//Log.e("DEVICE ID", deviceID);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		}.execute(null, null, null);
	}

	private void getHistory() {
		new AsyncTask<Void, Void, Void>() {
			HistoryAdapter adapter;

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				HistoryActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if (historyData != null) {
							if (historyData.size() > 0) {
								lvHistory.setAdapter(adapter);
							} else {
								Toast.makeText(HistoryActivity.this, "Unable to retreive data", Toast.LENGTH_SHORT).show();
							}
						}
						try {
							mRefreshActionItem.showProgress(false);
						} catch (Exception e) {
						}
					}
				});
			}

			@Override
			protected Void doInBackground(Void... params) {
				try {
					// ArrayList<HashMap<String, String>> data = new WebRequestAPI(HistoryActivity.this).getDevices(new Utils(HistoryActivity.this).getUDID());
					historyData = new WebRequestAPI(HistoryActivity.this).getHistory(deviceID);
					Log.e("DEVICE ID", deviceID);
					adapter = new HistoryAdapter(HistoryActivity.this, historyData);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		}.execute(null, null, null);
	}
}
