package com.netlynxtech.noiselynx.classes;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.netlynxtech.noiselynx.Consts;

public class WebRequestAPI {
	Context context;
	ArrayList<String> locationList = new ArrayList<String>();

	public WebRequestAPI(Context context) {
		this.context = context;
	}

	public ArrayList<String> getLocationList() {
		return locationList;
	}

	public String requestPin(String mobileNumber) {
		SoapObject rpc = new SoapObject(Consts.NAMESPACE, Consts.NOISELYNX_API_REQUESTPIN_METHOD_NAME);
		rpc.addProperty("mobile_no", mobileNumber);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(rpc);
		HttpTransportSE ht = new HttpTransportSE(Consts.NOISELYNX_API_URL);
		ht.debug = true;
		try {
			Log.e("WebRequest", "TRY!");
			ht.call(Consts.NOISELYNX_API_REQUESTPIN_SOAP_ACTION, envelope);
			System.err.println(ht.responseDump);
			SoapObject result = (SoapObject) envelope.getResponse();
			Log.e("RESULT", result.toString());
			// Log.e("COUNT", result.getPropertyCount() + "");
			// Log.e("COUNT", result.getProperty(0).toString());
			if (result.getProperty(0).toString().equals("1")) {
				return "success";
			} else {
				return result.getProperty(1).toString();
			}
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			return "Timed out. Please try again.";
		} catch (HttpResponseException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	public String checkPin(String mobileNumber, String pin, String gcm_id, String udid) {
		SoapObject rpc = new SoapObject(Consts.NAMESPACE, Consts.NOISELYNX_API_CHECKPIN_METHOD_NAME);
		rpc.addProperty("mobile_no", mobileNumber);
		rpc.addProperty("pin", pin);
		rpc.addProperty("GCM_ID", gcm_id);
		rpc.addProperty("UDID", udid);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(rpc);
		HttpTransportSE ht = new HttpTransportSE(Consts.NOISELYNX_API_URL);
		ht.debug = true;
		try {
			Log.e("WebRequest", "TRY!");
			ht.call(Consts.NOISELYNX_API_CHECKPIN_SOAP_ACTION, envelope);
			System.err.println(ht.responseDump);
			SoapObject result = (SoapObject) envelope.getResponse();
			// Log.e("COUNT", result.getPropertyCount() + "");
			// Log.e("COUNT", result.getProperty(0).toString());
			if (result.getProperty(0).toString().equals("1")) {
				return "success|" + result.getProperty(1).toString();
			} else {
				return result.getProperty(1).toString();
			}
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			return "Timed out. Please try again.";
		} catch (HttpResponseException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			return e.getMessage();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	public ArrayList<HashMap<String, String>> getDevices(String udid) {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		SoapObject rpc = new SoapObject(Consts.NAMESPACE, Consts.NOISELYNX_API_GETDEVICES_METHOD_NAME);
		rpc.addProperty("UDID", udid);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(rpc);
		HttpTransportSE ht = new HttpTransportSE(Consts.NOISELYNX_API_URL);
		ht.debug = true;
		try {
			Log.e("WebRequest", "TRY!");
			ht.call(Consts.NOISELYNX_API_GETDEVICES_SOAP_ACTION, envelope);
			System.err.println(ht.responseDump);
			SoapObject result = (SoapObject) envelope.getResponse();
			HashMap<String, String> map;
			for (int i = 0; i < result.getPropertyCount(); i++) {
				SoapObject object = (SoapObject) result.getProperty(i);
				map = new HashMap<String, String>();
				// Log.e("Location", object.getProperty(Consts.MONITORING_LOCATION) + "");
				locationList.add(object.getProperty(Consts.MONITORING_LOCATION).toString());
				map.put(Consts.MONITORING_DEVICE_ID, object.getProperty(Consts.MONITORING_DEVICE_ID).toString());
				map.put(Consts.MONITORING_DATE_TIME, object.getProperty(Consts.MONITORING_DATE_TIME).toString());
				map.put(Consts.MONITORING_LOCATION, object.getProperty(Consts.MONITORING_LOCATION).toString());
				map.put(Consts.MONITORING_LEQ_FIVE_MINUTES, object.getProperty(Consts.MONITORING_LEQ_FIVE_MINUTES).toString());
				map.put(Consts.MONITORING_LEQ_ONE_HOUR, object.getProperty(Consts.MONITORING_LEQ_ONE_HOUR).toString());
				map.put(Consts.MONITORING_LEQ_TWELVE_HOUR, object.getProperty(Consts.MONITORING_LEQ_TWELVE_HOUR).toString());
				map.put(Consts.MONITORING_LOCATION_LAT, object.getProperty(Consts.MONITORING_LOCATION_LAT).toString());
				map.put(Consts.MONITORING_LOCATION_LONG, object.getProperty(Consts.MONITORING_LOCATION_LONG).toString());
				map.put(Consts.MONITORING_ALERT, object.getProperty(Consts.MONITORING_ALERT).toString());
				list.add(map);
			}

		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			// Toast.makeText(context, "Timed out. Please try again", Toast.LENGTH_SHORT).show();
			// return "Timed out. Please try again.";
		} catch (HttpResponseException e) {
			e.printStackTrace();
			// return e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			// return e.getMessage();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			// return e.getMessage();
		}
		return list;
	}

	public ArrayList<HashMap<String, String>> getAlerts(String udid) {
		SQLFunctions sql = new SQLFunctions(context);
		sql.open();
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		SoapObject rpc = new SoapObject(Consts.NAMESPACE, Consts.NOISELYNX_API_GETMESSAGES_METHOD_NAME);
		rpc.addProperty("UDID", udid);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(rpc);
		HttpTransportSE ht = new HttpTransportSE(Consts.NOISELYNX_API_URL);
		ht.debug = true;
		try {
			Log.e("WebRequest", "TRY!");
			ht.call(Consts.NOISELYNX_API_GETMESSAGES_SOAP_ACTION, envelope);
			System.err.println(ht.responseDump);
			SoapObject result = (SoapObject) envelope.getResponse();
			HashMap<String, String> map;
			for (int i = 0; i < result.getPropertyCount(); i++) {
				SoapObject object = (SoapObject) result.getProperty(i);
				map = new HashMap<String, String>();
				// Log.e("MESSAGE", object.getProperty(Consts.MESSAGES_MESSAGE_BODY).toString());
				// Log.e("Location", object.getProperty(Consts.MONITORING_LOCATION) + "");
				map.put(Consts.MESSAGES_MESSAGE_ID, object.getProperty(Consts.MESSAGES_MESSAGE_ID).toString());
				map.put(Consts.MESSAGES_MESSAGE_TIMESTAMP, object.getProperty(Consts.MESSAGES_MESSAGE_TIMESTAMP).toString());
				map.put(Consts.MESSAGES_MESSAGE_SUBJECT, object.getProperty(Consts.MESSAGES_MESSAGE_SUBJECT).toString());
				map.put(Consts.MESSAGES_MESSAGE_BODY, object.getProperty(Consts.MESSAGES_MESSAGE_BODY).toString());
				map.put(Consts.MESSAGES_MESSAGE_PRIORITY, object.getProperty(Consts.MESSAGES_MESSAGE_PRIORITY).toString());
				list.add(map);
				Log.e("MESSAGE ID", object.getProperty(Consts.MESSAGES_MESSAGE_ID).toString());
				sql.insertMessage(object.getProperty(Consts.MESSAGES_MESSAGE_ID).toString(), object.getProperty(Consts.MESSAGES_MESSAGE_TIMESTAMP).toString(),
						object.getProperty(Consts.MESSAGES_MESSAGE_SUBJECT).toString(), object.getProperty(Consts.MESSAGES_MESSAGE_BODY).toString(),
						object.getProperty(Consts.MESSAGES_MESSAGE_PRIORITY).toString());
			}
			sql.close();
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			list = sql.loadMessages();
			sql.close();
			return list;
		}
	}

	public ArrayList<HashMap<String, String>> getHistory(String deviceID) {
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		SoapObject rpc = new SoapObject(Consts.NAMESPACE, Consts.NOISELYNX_API_GETHISTORY_METHOD_NAME);
		rpc.addProperty("deviceID", Integer.parseInt(deviceID));
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(rpc);
		HttpTransportSE ht = new HttpTransportSE(Consts.NOISELYNX_API_URL, Consts.WEBREQUEST_TIMEOUT); // 20 seconds timeout
		ht.debug = true;
		try {
			Log.e("WebRequest", "TRY!");
			ht.call(Consts.NOISELYNX_API_GETHISTORY_SOAP_ACTION, envelope);
			System.err.println(ht.responseDump);
			SoapObject result = (SoapObject) envelope.getResponse();
			HashMap<String, String> map;
			for (int i = 0; i < result.getPropertyCount(); i++) {
				SoapObject object = (SoapObject) result.getProperty(i);
				map = new HashMap<String, String>();
				map.put(Consts.HISTORY_DATETIMESTAMP, object.getProperty(Consts.HISTORY_DATETIMESTAMP).toString());
				map.put(Consts.MONITORING_LEQ_FIVE_MINUTES, object.getProperty(Consts.MONITORING_LEQ_FIVE_MINUTES).toString());
				map.put(Consts.MONITORING_ALERT, object.getProperty(Consts.MONITORING_ALERT).toString());
				list.add(map);
			}

		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			// return "Timed out. Please try again.";
		} catch (HttpResponseException e) {
			e.printStackTrace();
			// return e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			// return e.getMessage();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			// return e.getMessage();
		}
		return list;
	}

	public ArrayList<String> getThreshold(String deviceID) {
		ArrayList<String> list = new ArrayList<String>();
		SoapObject rpc = new SoapObject(Consts.NAMESPACE, Consts.NOISELYNX_API_GETTHRESHOLD_METHOD_NAME);
		rpc.addProperty("UDID", new Utils(context).getDeviceId());
		rpc.addProperty("deviceID", deviceID);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(rpc);
		HttpTransportSE ht = new HttpTransportSE(Consts.NOISELYNX_API_URL);
		ht.debug = true;
		try {
			Log.e("WebRequest", "TRY!");
			ht.call(Consts.NOISELYNX_API_GETTHRESHOLD_SOAP_ACTION, envelope);
			System.err.println(ht.responseDump);
			SoapObject result = (SoapObject) envelope.getResponse();
			for (int i = 0; i < result.getPropertyCount(); i++) {
				SoapObject object = (SoapObject) result.getProperty(i);
				// String timestamp = new Utils(context).parseTimeOnly(object.getProperty(Consts.THRESHOLD_TIMESPAN).toString());
				String timestamp = object.getProperty(Consts.THRESHOLD_TIMESPAN).toString();
				Log.e("THRESHOLD", object.getProperty(Consts.THRESHOLD_TIMESPAN).toString() + "|" + object.getProperty(Consts.THRESHOLD_THRESHOLD).toString());
				list.add(timestamp + " : " + object.getProperty(Consts.THRESHOLD_THRESHOLD).toString() + " dBA");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public String updateLocation(String deviceID, String latitude, String longitude) {
		SoapObject rpc = new SoapObject(Consts.NAMESPACE, Consts.NOISELYNX_API_UPDATELATLONG_METHOD_NAME);
		rpc.addProperty("UDID", new Utils(context).getUDID());
		rpc.addProperty("deviceID", Integer.parseInt(deviceID));
		rpc.addProperty("latitude", latitude);
		rpc.addProperty("longitude", longitude);
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(rpc);
		HttpTransportSE ht = new HttpTransportSE(Consts.NOISELYNX_API_URL, Consts.WEBREQUEST_TIMEOUT); // 20 seconds timeout
		ht.debug = true;
		try {
			Log.e("WebRequest", "TRY!");
			ht.call(Consts.NOISELYNX_API_UPDATELATLONG_SOAP_ACTION, envelope);
			System.err.println(ht.responseDump);
			SoapObject result = (SoapObject) envelope.getResponse();
			if (result.getProperty(0).toString().equals("1")) {
				return "success|" + result.getProperty(1).toString();
			} else {
				return result.getProperty(1).toString();
			}
		} catch (SocketTimeoutException e) {
			e.printStackTrace();
			// return "Timed out. Please try again.";
		} catch (HttpResponseException e) {
			e.printStackTrace();
			// return e.getMessage();
		} catch (IOException e) {
			e.printStackTrace();
			// return e.getMessage();
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			// return e.getMessage();
		}
		return "failed";
	}

}
