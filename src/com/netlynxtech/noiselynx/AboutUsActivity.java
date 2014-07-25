package com.netlynxtech.noiselynx;

import android.os.Bundle;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

public class AboutUsActivity extends SherlockActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setTitle(getResources().getString(R.string.about_us_activity));
		setContentView(R.layout.about_us_activity);
		String v = "1";
		try {
			v = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
			((TextView) findViewById(R.id.tvVersion)).setText(AboutUsActivity.this.getResources().getString(R.string.version) + " " + v);
		} catch (Exception e) {
			((TextView) findViewById(R.id.tvVersion)).setText(AboutUsActivity.this.getResources().getString(R.string.version) + " 1.0.0");
		}
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
}
