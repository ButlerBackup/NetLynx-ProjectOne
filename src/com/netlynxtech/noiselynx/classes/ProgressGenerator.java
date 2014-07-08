package com.netlynxtech.noiselynx.classes;

import java.util.Random;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

import com.dd.processbutton.ProcessButton;
import com.netlynxtech.noiselynx.Consts;
import com.securepreferences.SecurePreferences;

public class ProgressGenerator {

	Context context;
	ProcessButton button;
	TextView tvStatusDesc;

	public interface OnCompleteListener {
		public void onComplete();
	}

	private OnCompleteListener mListener;
	private int mProgress = 0;

	public ProgressGenerator(OnCompleteListener listener) {
		mListener = listener;
	}

	public void start(final ProcessButton button) {
		final Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				mProgress += 10;
				button.setProgress(mProgress);
				if (mProgress < 100) {
					handler.postDelayed(this, generateDelay());
				} else {
					mListener.onComplete();
				}
			}
		}, generateDelay());
	}

	private Random random = new Random();

	private int generateDelay() {
		return random.nextInt(1000);
	}

	private class requestPin extends AsyncTask<String, Void, Void> {
		String res = "";

		@Override
		protected Void doInBackground(String... params) {
			res = new WebRequestAPI(context).requestPin(params[0]);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (res.equals("success")) {
				Log.e("onPostExecute", "success");
				button.setProgress(100);
			} else {
				mProgress = 0;
				button.setProgress(-1);
				button.setText(res);
				Log.e("onPostExecute", "failed");
			}
			mListener.onComplete();
		}

	}

	public void requestPin(final ProcessButton button, final String phoneNo, final Context context) {
		this.button = button;
		this.context = context;

		final Handler handler = new Handler();
		handler.post(new Runnable() {
			@Override
			public void run() {
				mProgress += 25;
				button.setProgress(mProgress);
				if (mProgress < 74) {
					handler.postDelayed(this, generateDelay());
				} else {
					new requestPin().execute(phoneNo);
				}
			}
		});
	}

	private class checkPin extends AsyncTask<String, Void, Void> {
		String res = "";

		@Override
		protected Void doInBackground(String... params) {
			SecurePreferences sp = new SecurePreferences(context);
			String phoneNo = sp.getString(Consts.PREFERENCES_PHONE_NO, "0");
			String unique_id = new Utils(context).getDeviceId();
			sp.edit().putString(Consts.PREFERENCES_UDID, unique_id).commit();
			// res = new WebRequestAPI(context).checkPin(phoneNo, params[0], params[1], "123456");
			res = new WebRequestAPI(context).checkPin(phoneNo, params[0], "123456", "123456");
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			if (res.contains("success|")) {
				Log.e("onPostExecute", "success");
				button.setProgress(100);
				tvStatusDesc.setText(res.replace("success|", ""));
			} else {
				mProgress = 0;
				button.setProgress(-1);
				button.setText(res);
				Log.e("onPostExecute", "failed");
			}
			mListener.onComplete();
		}

	}

	public void checkPin(final ProcessButton button, final String pinNo, final String gcm_id, final TextView tvStatusDesc, final Context context) {
		this.button = button;
		this.context = context;
		this.tvStatusDesc = tvStatusDesc;

		final Handler handler = new Handler();
		handler.post(new Runnable() {
			@Override
			public void run() {
				mProgress += 25;
				button.setProgress(mProgress);
				if (mProgress < 74) {
					handler.postDelayed(this, generateDelay());
				} else {
					new checkPin().execute(pinNo, gcm_id);
				}
			}
		});
	}
}