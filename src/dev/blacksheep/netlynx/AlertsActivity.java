package dev.blacksheep.netlynx;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.manuelpeinado.refreshactionitem.ProgressIndicatorType;
import com.manuelpeinado.refreshactionitem.RefreshActionItem;
import com.manuelpeinado.refreshactionitem.RefreshActionItem.RefreshActionListener;

import dev.blacksheep.netlynx.adapter.AlertsAdapter;
import dev.blacksheep.netlynx.classes.WebRequestAPI;

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
						if (data != null) {
							if (data.size() > 0) {
								lvAlerts.setAdapter(adapter);
							} else {
								Toast.makeText(AlertsActivity.this, "Unable to retreive data", Toast.LENGTH_SHORT).show();
							}
						}
						mRefreshActionItem.showProgress(false);
					}
				});
			}

			@Override
			protected Void doInBackground(Void... params) {
				try {
					// ArrayList<HashMap<String, String>> data = new WebRequestAPI(MonitoringSitesActivity.this).getDevices(new Utils(MonitoringSitesActivity.this).getUDID());
					data = new WebRequestAPI(AlertsActivity.this).getAlerts("123456");
					adapter = new AlertsAdapter(AlertsActivity.this, data);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}
		}.execute(null, null, null);
	}
}
