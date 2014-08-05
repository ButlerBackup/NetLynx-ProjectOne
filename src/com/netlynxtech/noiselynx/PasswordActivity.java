package com.netlynxtech.noiselynx;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.netlynxtech.noiselynx.classes.Utils;

public class PasswordActivity extends SherlockActivity {
	String password, message;
	Button bSkip, bLogin;
	EditText etPassword, etPasswordAgain;
	TextView tvPasswordInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Intent i = getIntent();
		message = i.getStringExtra("message");
		setContentView(R.layout.password_activity);
		bSkip = (Button) findViewById(R.id.bSkip);
		bLogin = (Button) findViewById(R.id.bLogin);
		etPassword = (EditText) findViewById(R.id.etPassword);
		etPasswordAgain = (EditText) findViewById(R.id.etPasswordAgain);
		tvPasswordInfo = (TextView) findViewById(R.id.tvPasswordInfo);

		password = new Utils(PasswordActivity.this).getPassword();
		if (!password.equals("0")) { // already set password
			if (password.equals("")) { // dont want password // skip for now
				startActivity(new Intent(PasswordActivity.this, MonitoringSitesActivity.class));
				finish();
			} else { // user set a password, now prompt them!
				etPasswordAgain.setVisibility(View.GONE);
				bSkip.setVisibility(View.GONE);
				tvPasswordInfo.setText(PasswordActivity.this.getResources().getString(R.string.password_info_text_existing));
			}
		} else {
			bLogin.setText(PasswordActivity.this.getResources().getString(R.string.password_activity_set_password));
		}

		bLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (password.equals("0")) { // new user
					if (etPassword.getText().toString().trim().length() > 0 && etPasswordAgain.getText().toString().trim().length() > 0) {
						if (etPassword.getText().toString().trim().equals(etPasswordAgain.getText().toString().trim())) {
							new Utils(PasswordActivity.this).setPassword(etPassword.getText().toString().trim());
							startActivity(new Intent(PasswordActivity.this, WelcomeActivity.class).putExtra("message", message));
							finish();
						} else {
							tvPasswordInfo.setText(PasswordActivity.this.getResources().getString(R.string.password_info_password_notequal));
						}
					} else {
						tvPasswordInfo.setText(PasswordActivity.this.getResources().getString(R.string.password_info_password_null));
					}
				} else {
					if (etPassword.getText().toString().equals(password)) {
						startActivity(new Intent(PasswordActivity.this, MonitoringSitesActivity.class));
						finish();
					} else {
						tvPasswordInfo.setText(PasswordActivity.this.getResources().getString(R.string.password_info_text_wrong));
					}
				}
			}
		});
		bSkip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new Utils(PasswordActivity.this).setPassword("");
				startActivity(new Intent(PasswordActivity.this, WelcomeActivity.class).putExtra("message", message));
				finish();
			}
		});
	}

}
