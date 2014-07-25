package com.netlynxtech.noiselynx;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.dd.processbutton.iml.ActionProcessButton;
import com.netlynxtech.noiselynx.classes.ProgressGenerator;
import com.netlynxtech.noiselynx.classes.ProgressGenerator.OnCompleteListener;
import com.netlynxtech.noiselynx.classes.Utils;
import com.securepreferences.SecurePreferences;

public class MainActivity extends SherlockActivity {

	EditText etPhoneNo;
	ActionProcessButton bRequestPin;
	TextView tvError;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		getSupportActionBar().setTitle("NoiseLynx Registration");
		super.onCreate(savedInstanceState);
		if (new Utils(MainActivity.this).checkIfLoggedIn()) {
			startActivity(new Intent(MainActivity.this, PasswordActivity.class));
			finish();
		}
		setContentView(R.layout.main);
		tvError = (TextView) findViewById(R.id.tvError);
		etPhoneNo = (EditText) findViewById(R.id.etPhoneNo);
		bRequestPin = (ActionProcessButton) findViewById(R.id.bRequestPin);
		bRequestPin.setMode(ActionProcessButton.Mode.ENDLESS);
		etPhoneNo.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (s.toString().length() > 0) {
					bRequestPin.setEnabled(true);
				} else {
					bRequestPin.setEnabled(false);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		final ProgressGenerator progressGenerator = new ProgressGenerator(new OnCompleteListener() {

			@Override
			public void onComplete() {
				if (!bRequestPin.getText().toString().equals("Success")) {
					tvError.setText(bRequestPin.getText().toString());
					etPhoneNo.setEnabled(true);
					bRequestPin.setEnabled(true);
					bRequestPin.setProgress(0);
					tvError.setVisibility(View.VISIBLE);
				} else {
					SecurePreferences sp = new SecurePreferences(MainActivity.this);
					sp.edit().putString(Consts.PREFERENCES_PHONE_NO, etPhoneNo.getText().toString()).commit();
					startActivity(new Intent(MainActivity.this, CheckPinActivity.class));
					finish();
				}
			}
		});

		bRequestPin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.e("Button", "Click");
				etPhoneNo.setEnabled(false);
				bRequestPin.setEnabled(false);
				progressGenerator.requestPin(bRequestPin, etPhoneNo.getText().toString(), MainActivity.this);
			}
		});

	}

}
