package com.netlynxtech.noiselynx;

import android.os.Bundle;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class AboutUsActivity extends SherlockActivity {
	AdView adView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setTitle(getResources().getString(R.string.about_us_activity));
		setContentView(R.layout.about_us_activity);
		adView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
		String v = "1";
		try {
			v = getPackageManager().getPackageInfo(getPackageName(), 0).versionName; // get version of this package (in manifest)
			((TextView) findViewById(R.id.tvVersion)).setText(AboutUsActivity.this.getResources().getString(R.string.version) + " " + v);
		} catch (Exception e) {
			((TextView) findViewById(R.id.tvVersion)).setText(AboutUsActivity.this.getResources().getString(R.string.version) + " 1.0.0"); // else v1.0.0
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
	
    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
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
