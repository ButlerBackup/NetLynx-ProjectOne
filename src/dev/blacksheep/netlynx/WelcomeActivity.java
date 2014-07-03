package dev.blacksheep.netlynx;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.dd.processbutton.iml.ActionProcessButton;

public class WelcomeActivity extends SherlockActivity {
	ActionProcessButton bNext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome_activity);
		Intent i = getIntent();
		TextView tvMessage = (TextView) findViewById(R.id.tvMessage);
		tvMessage.setText(i.getStringExtra("message"));
		bNext = (ActionProcessButton) findViewById(R.id.bNext);
		bNext.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(WelcomeActivity.this, MonitoringSitesActivity.class));
				finish();
			}
		});
	}

}
