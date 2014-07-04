package dev.blacksheep.netlynx;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.manuelpeinado.refreshactionitem.ProgressIndicatorType;
import com.manuelpeinado.refreshactionitem.RefreshActionItem;
import com.manuelpeinado.refreshactionitem.RefreshActionItem.RefreshActionListener;

import dev.blacksheep.netlynx.adapter.HistoryAdapter;
import dev.blacksheep.netlynx.classes.WebRequestAPI;

public class HistoryActivity extends SherlockActivity {
	private RefreshActionItem mRefreshActionItem;
	ListView lvHistory;
	String deviceID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setTitle("12 Hour History");
		setContentView(R.layout.history_activity_layout);
		Intent i = getIntent();
		deviceID = i.getStringExtra(Consts.MONITORING_DEVICE_ID);
<<<<<<< HEAD
=======
		((TextView) findViewById(R.id.tvTime)).setText(i.getStringExtra(Consts.MONITORING_DATE_TIME));
		((TextView) findViewById(R.id.tvDevice)).setText(i.getStringExtra(Consts.MONITORING_LOCATION));
		((TextView) findViewById(R.id.tvCurrentDBA)).setText(i.getStringExtra(Consts.MONITORING_LEQ_FIVE_MINUTES));
		((TextView) findViewById(R.id.tvLEQ1hour)).setText(i.getStringExtra(Consts.MONITORING_LEQ_ONE_HOUR));
		((TextView) findViewById(R.id.tvLEQ12hour)).setText(i.getStringExtra(Consts.MONITORING_LEQ_TWELVE_HOUR));
		
>>>>>>> parent of 4fc2eb0... update
		lvHistory = (ListView) findViewById(R.id.lvHistory);
		getHistory();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		case R.id.menu_map:
			startActivity(new Intent(HistoryActivity.this, SiteLocationActivity.class));
			break;

		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.history_menu, menu);
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
							}
						});
					}

				}).start();

			}
		});
		return true;
	}

	private void getHistory() {
		new AsyncTask<Void, Void, Void>() {
			HistoryAdapter adapter;
			ArrayList<HashMap<String, String>> data;

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				HistoryActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if (data.size() > 0) {
							lvHistory.setAdapter(adapter);
						} else {
							Toast.makeText(HistoryActivity.this, "Unable to retreive data", Toast.LENGTH_SHORT).show();
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
					data = new WebRequestAPI(HistoryActivity.this).getHistory(deviceID);
					Log.e("DEVICE ID", deviceID);
					adapter = new HistoryAdapter(HistoryActivity.this, data);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		}.execute(null, null, null);
	}
}
