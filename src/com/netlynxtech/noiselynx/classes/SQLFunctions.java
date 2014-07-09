package com.netlynxtech.noiselynx.classes;

import java.util.ArrayList;
import java.util.HashMap;

import com.netlynxtech.noiselynx.Consts;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLFunctions {
	public static final String TAG = "NetLynx[SQLi]";
	public static final String GLOBAL_ROWID = "_id";

	private static final String DATABASE_NAME = "noiselynx";
	private static final String TABLE_MESSAGES = "messages";

	private static final int DATABASE_VERSION = 2;

	private DbHelper ourHelper;
	private final Context ourContext;
	private SQLiteDatabase ourDatabase;

	private static class DbHelper extends SQLiteOpenHelper {

		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + TABLE_MESSAGES + " (" + GLOBAL_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + Consts.MESSAGES_MESSAGE_ID + " TEXT NOT NULL, "
					+ Consts.MESSAGES_MESSAGE_TIMESTAMP + " TEXT NOT NULL, " + Consts.MESSAGES_MESSAGE_SUBJECT + " TEXT NOT NULL, " + Consts.MESSAGES_MESSAGE_BODY + " TEXT NOT NULL, "
					+ Consts.MESSAGES_MESSAGE_PRIORITY + " TEXT NOT NULL, " + Consts.DATABASE_COLUMN_UNIX + " TEXT NOT NULL);");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
			onCreate(db);
		}

	}

	public SQLFunctions(Context c) {
		ourContext = c;
	}

	public SQLFunctions open() throws SQLException {
		ourHelper = new DbHelper(ourContext);
		ourDatabase = ourHelper.getWritableDatabase();
		return null;
	}

	public void close() {
		if (ourHelper != null) {
			ourHelper.close();
		} else {
			Log.e(TAG, "You did not open your database. Null error");
		}
	}

	public long unixTime() {
		return System.currentTimeMillis() / 1000L;
	}

	public boolean longerThanTwoHours(String pTime) {
		int prevTime = Integer.parseInt(pTime);
		int currentTime = (int) (System.currentTimeMillis() / 1000L);
		int seconds = currentTime - prevTime;
		int how_many;
		if (seconds > 3600 && seconds < 86400) {
			how_many = (int) seconds / 3600;
			if (how_many >= 2) { // 2 hours
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	public String getLastRowId() {
		String sql = "SELECT * FROM " + TABLE_MESSAGES + " ORDER BY " + GLOBAL_ROWID + " DESC LIMIT 1";
		Cursor cursor = ourDatabase.rawQuery(sql, null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				String id = cursor.getString(cursor.getColumnIndex(GLOBAL_ROWID));
				cursor.close();
				Log.e("LATEST SQL ROW", id);
				return id;
			}
		}
		cursor.close();
		return "";
	}

	public HashMap<String, String> getItemInformation(String name) {
		HashMap<String, String> map = new HashMap<String, String>();
		String sql = "SELECT * FROM " + TABLE_MESSAGES + " WHERE " + Consts.MESSAGES_MESSAGE_ID + " = ?";
		Cursor cursor = ourDatabase.rawQuery(sql, new String[] { name });
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				map.put(Consts.MESSAGES_MESSAGE_BODY, cursor.getString(cursor.getColumnIndex(Consts.MESSAGES_MESSAGE_BODY)));
			}
		}
		cursor.close();
		return map;
	}

	public ArrayList<HashMap<String, String>> loadMessages() {
		ArrayList<HashMap<String, String>> map = new ArrayList<HashMap<String, String>>();
		Cursor cursor = ourDatabase.rawQuery("SELECT * FROM " + TABLE_MESSAGES + " ORDER BY " + GLOBAL_ROWID + " DESC", null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				while (cursor.isAfterLast() == false) {
					HashMap<String, String> hash = new HashMap<String, String>();
					hash.put(Consts.MESSAGES_MESSAGE_ID, cursor.getString(cursor.getColumnIndex(Consts.MESSAGES_MESSAGE_ID)));
					hash.put(Consts.MESSAGES_MESSAGE_TIMESTAMP, cursor.getString(cursor.getColumnIndex(Consts.MESSAGES_MESSAGE_TIMESTAMP)));
					hash.put(Consts.MESSAGES_MESSAGE_SUBJECT, cursor.getString(cursor.getColumnIndex(Consts.MESSAGES_MESSAGE_SUBJECT)));
					hash.put(Consts.MESSAGES_MESSAGE_BODY, cursor.getString(cursor.getColumnIndex(Consts.MESSAGES_MESSAGE_BODY)));
					hash.put(Consts.MESSAGES_MESSAGE_PRIORITY, cursor.getString(cursor.getColumnIndex(Consts.MESSAGES_MESSAGE_PRIORITY)));
					map.add(hash);
					cursor.moveToNext();
				}
			}
		}
		cursor.close();
		return map;
	}
	
	public void insertMessage(String id, String timestamp, String subject, String body, String priority) {
		ContentValues cv = new ContentValues();
		String sql = "SELECT * FROM " + TABLE_MESSAGES + " WHERE " + Consts.MESSAGES_MESSAGE_ID + " = ?";
		Cursor cursor = ourDatabase.rawQuery(sql, new String[] { id });
		if (cursor.moveToFirst()) {
			Log.e(TAG, "Subject ID exist");
		} else {
			cv.put(Consts.MESSAGES_MESSAGE_ID, id);
			cv.put(Consts.MESSAGES_MESSAGE_TIMESTAMP, timestamp);
			cv.put(Consts.MESSAGES_MESSAGE_SUBJECT, subject);
			cv.put(Consts.MESSAGES_MESSAGE_BODY, body);
			cv.put(Consts.MESSAGES_MESSAGE_PRIORITY, priority);
			cv.put(Consts.DATABASE_COLUMN_UNIX, new Utils(ourContext).convertTimetoUnix(timestamp));
			try {
				ourDatabase.insert(TABLE_MESSAGES, null, cv);
			} catch (Exception e) {
				Log.e(TAG, "Error creating message entry", e);
			}
		}
		cursor.close();
	}

	/*public String getMessageData(String id, String timestamp, String subject, String body, String priority) {
		Cursor cursor = ourDatabase.rawQuery("SELECT * FROM " + TABLE_MESSAGES + " WHERE " + Consts.MESSAGES_MESSAGE_ID + " = '" + id + "'", null);
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				String value = cursor.getString(cursor.getColumnIndex(MARKET_PRICE));
				cursor.close();
				return value;
			}
		}
		cursor.close();
		return Consts.MARKET_ITEM_NOT_FOUND;
	}*/
}