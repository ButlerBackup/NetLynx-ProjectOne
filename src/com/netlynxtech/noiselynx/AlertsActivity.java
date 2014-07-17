package com.netlynxtech.noiselynx;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.manuelpeinado.refreshactionitem.ProgressIndicatorType;
import com.manuelpeinado.refreshactionitem.RefreshActionItem;
import com.manuelpeinado.refreshactionitem.RefreshActionItem.RefreshActionListener;
import com.netlynxtech.noiselynx.adapter.AlertsAdapter;
import com.netlynxtech.noiselynx.classes.SQLFunctions;
import com.netlynxtech.noiselynx.classes.Utils;
import com.netlynxtech.noiselynx.classes.WebRequestAPI;

public class AlertsActivity extends SherlockActivity {
	private RefreshActionItem mRefreshActionItem;
	ListView lvAlerts;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setTitle(getResources().getString(R.string.alert_name));
		setContentView(R.layout.alert_activity); // same layout, different list item
		lvAlerts = (ListView) findViewById(R.id.lvAlerts);
		getAlerts();
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
		getSupportMenuInflater().inflate(R.menu.alerts_menu, menu);
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
								getAlerts();
							}
						});
					}

				}).start();

			}
		});
		return true;
	}

	private void getAlerts() {
		new AsyncTask<Void, Void, Void>() {
			AlertsAdapter adapter;
			ArrayList<HashMap<String, String>> data;

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				AlertsActivity.this.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						try {
							adapter = new AlertsAdapter(AlertsActivity.this, data);
							lvAlerts.setAdapter(adapter);
							mRefreshActionItem.showProgress(false);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}

			@Override
			protected Void doInBackground(Void... params) {
				try {
					new WebRequestAPI(AlertsActivity.this).getAlerts(new Utils(AlertsActivity.this).getUDID());
					SQLFunctions sql = new SQLFunctions(AlertsActivity.this);
					sql.open();
					data = sql.loadMessagesNoHousekeep(); // get new messages, which insert the nwe get from DB
					sql.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		}.execute(null, null, null);
	}
}
