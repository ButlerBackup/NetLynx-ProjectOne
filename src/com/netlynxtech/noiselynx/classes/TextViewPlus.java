package com.netlynxtech.noiselynx.classes;

import java.util.Hashtable;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.netlynxtech.noiselynx.R;

public class TextViewPlus extends TextView {
	private static final String TAG = "TextView";
	private static final Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();

	public TextViewPlus(Context context) {
		super(context);
	}

	public TextViewPlus(Context context, AttributeSet attrs) {
		super(context, attrs);
		setCustomFont(context, attrs);
	}

	public TextViewPlus(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setCustomFont(context, attrs);
	}

	private void setCustomFont(Context ctx, AttributeSet attrs) {
		TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.TextViewPlus);
		String customFont = a.getString(R.styleable.TextViewPlus_customFont);
		setCustomFont(ctx, customFont);
		a.recycle();
	}

	public boolean setCustomFont(Context ctx, String asset) {
		Typeface tf = null;
		try {
			//tf = Typeface.createFromAsset(ctx.getAssets(), asset);
			tf = get(ctx, asset);
		} catch (Exception e) {
			Log.e(TAG, "Could not get typeface: " + e.getMessage());
			return false;
		}

		setTypeface(tf);
		return true;
	}

	public static Typeface get(Context c, String assetPath) {
		synchronized (cache) {
			if (!cache.containsKey(assetPath)) {
				try {
					Typeface t = Typeface.createFromAsset(c.getAssets(), assetPath);
					cache.put(assetPath, t);
				} catch (Exception e) {
					Log.e(TAG, "Could not get typeface '" + assetPath + "' because " + e.getMessage());
					return null;
				}
			}
			return cache.get(assetPath);
		}
	}
}
