package dev.blacksheep.netlynx;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.dd.processbutton.iml.ActionProcessButton;

import dev.blacksheep.netlynx.classes.ProgressGenerator;
import dev.blacksheep.netlynx.classes.Utils;
import dev.blacksheep.netlynx.classes.ProgressGenerator.OnCompleteListener;

public class SiteLocationActivity extends SherlockActivity {
	ActionProcessButton bUpdateLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);
		setContentView(R.layout.site_location_activity); // same layout, different list item
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
