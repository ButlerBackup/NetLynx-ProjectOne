package com.netlynxtech.noiselynx;

public class Consts {

	final static public int WEBREQUEST_TIMEOUT = 20000;
	// http://192.168.10.8/wsNoiseLynx/noiselynx.asmx'
	final static public String NOISELYNX_API_URL = "http://192.168.10.8/wsNoiseLynx/noiselynx.asmx";
	final static public String NOISELYNX_API_REQUESTPIN_SOAP_ACTION = "http://NetlynxTech.com/RequestPIN";
	final static public String NOISELYNX_API_REQUESTPIN_METHOD_NAME = "RequestPIN";
	final static public String NOISELYNX_API_CHECKPIN_SOAP_ACTION = "http://NetlynxTech.com/CheckPIN";
	final static public String NOISELYNX_API_CHECKPIN_METHOD_NAME = "CheckPIN";
	final static public String NOISELYNX_API_GETDEVICES_SOAP_ACTION = "http://NetlynxTech.com/GetDevices";
	final static public String NOISELYNX_API_GETDEVICES_METHOD_NAME = "GetDevices";
	final static public String NOISELYNX_API_GETHISTORY_SOAP_ACTION = "http://NetlynxTech.com/GetHistory";
	final static public String NOISELYNX_API_GETHISTORY_METHOD_NAME = "GetHistory";
	final static public String NOISELYNX_API_GETMESSAGES_SOAP_ACTION = "http://NetlynxTech.com/GetNewMessages";
	final static public String NOISELYNX_API_GETMESSAGES_METHOD_NAME = "GetNewMessages";
	final static public String NOISELYNX_API_GETTHRESHOLD_SOAP_ACTION = "http://NetlynxTech.com/GetThreshold";
	final static public String NOISELYNX_API_GETTHRESHOLD_METHOD_NAME = "GetThreshold";
	final static public String NOISELYNX_API_UPDATELATLONG_SOAP_ACTION = "http://NetlynxTech.com/UpdateLatLong";
	final static public String NOISELYNX_API_UPDATELATLONG_METHOD_NAME = "UpdateLatLong";
	final static public String NAMESPACE = "http://NetlynxTech.com/";

	final static public String PREFERENCES_PHONE_NO = "phone";
	final static public String PREFERENCES_UDID = "udid";
	final static public String PREFERENCES_GCMID = "gcmid";

	final static public String MONITORING_DEVICE_ID = "DeviceID";
	final static public String MONITORING_DATE_TIME = "DataDateTime";
	final static public String MONITORING_LOCATION = "Location";
	final static public String MONITORING_LEQ_FIVE_MINUTES = "Leq5";
	final static public String MONITORING_LEQ_ONE_HOUR = "Leq1";
	final static public String MONITORING_LEQ_TWELVE_HOUR = "Leq12";
	final static public String MONITORING_LOCATION_LAT = "Latitude";
	final static public String MONITORING_LOCATION_LONG = "Longitude";
	final static public String MONITORING_ALERT = "Alert";
	final static public String MONITORING_ALERT_YES = "true";
	final static public String MONITORING_ALERT_NO = "false";

	final static public String HISTORY_DATETIMESTAMP = "DataTimestamp";

	final static public String MESSAGES_MESSAGE_ID = "MessageID";
	final static public String MESSAGES_MESSAGE_TIMESTAMP = "MessageTimestamp";
	final static public String MESSAGES_MESSAGE_SUBJECT = "Subject";
	final static public String MESSAGES_MESSAGE_BODY = "Message";
	final static public String MESSAGES_MESSAGE_PRIORITY = "Priority";

	final static public String THRESHOLD_TIMESPAN = "TimeSpan";
	final static public String THRESHOLD_THRESHOLD = "Threhold"; // GRAMMAR ERROR CAREFUL
	final static public String DATABASE_COLUMN_UNIX = "unixTimestamp";

}
