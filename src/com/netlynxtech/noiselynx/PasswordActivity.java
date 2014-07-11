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
	String password;
	Button bSkip, bLogin;
	EditText etPassword;
	TextView tvPasswordInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.password_activity);
		bSkip = (Button) findViewById(R.id.bSkip);
		bLogin = (Button) findViewById(R.id.bLogin);
		etPassword = (EditText) findViewById(R.id.etPassword);
		tvPasswordInfo = (TextView) findViewById(R.id.tvPasswordInfo);

		password = new Utils(PasswordActivity.this).getPassword();
		if (!password.equals("0")) { // already set password
			if (password.equals("")) {
				startActivity(new Intent(PasswordActivity.this, MonitoringSitesActivity.class));
				finish();
			} else { // user set a password
				bSkip.setVisibility(View.GONE);
			}
		}

		bLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (password.equals("0")) { // new user
					new Utils(PasswordActivity.this).setPassword(etPassword.getText().toString());
					startActivity(new Intent(PasswordActivity.this, MonitoringSitesActivity.class));
					finish();
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
				startActivity(new Intent(PasswordActivity.this, MonitoringSitesActivity.class));
				finish();
			}
		});
	}

}
