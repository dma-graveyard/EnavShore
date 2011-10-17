package dk.frv.enav.shore.core.services;

import java.util.HashMap;

public class Errorcodes {

	public static final int OK = 0;
	public static final int SYSTEM_ERROR = 1;
	public static final int WRONG_DATE_FORMAT = 2;
	public static final int WRONG_ROUTE_FORMAT = 3;
	public static final int WRONG_MMSI_FORMAT = 4;
	public static final int WRONG_METOC_TYPE = 5;
	public static final int METOC_NOT_MODIFIED = 6;
	public static final int NO_VOYAGE_FOR_VESSEL = 7;
	public static final int MISSING_START_TIME = 8;
	public static final int MISSING_MMSI = 9;
	public static final int MISSING_DATA = 10;
	public static final int MISSING_ROUTE_DATA = 11;
	public static final int ROUTE_IN_PAST = 12;
	public static final int EMPTY_ROUTE = 13;
	public static final int INVALID_XML_REQUEST = 14;
	public static final int FAILED_TO_READ_XML = 15;
	public static final int METOC_UNAVAILABLE = 16;

	private static Errorcodes instance = null;

	private HashMap<Integer, String> errorMsg = new HashMap<Integer, String>();

	private Errorcodes() {
		errorMsg.put(OK, "OK");
		errorMsg.put(SYSTEM_ERROR, "Internal system error");
		errorMsg.put(WRONG_DATE_FORMAT, "Wrong date format");
		errorMsg.put(WRONG_ROUTE_FORMAT, "Error in route format");
		errorMsg.put(WRONG_MMSI_FORMAT, "Wrong MMSI format");
		errorMsg.put(WRONG_METOC_TYPE, "Wrong METOC type");
		errorMsg.put(METOC_NOT_MODIFIED, "METOC data not modified");
		errorMsg.put(NO_VOYAGE_FOR_VESSEL, "No voyage for vessel");
		errorMsg.put(MISSING_START_TIME, "Missing start time");
		errorMsg.put(MISSING_MMSI, "Missing MMSI");
		errorMsg.put(MISSING_DATA, "Missing data");
		errorMsg.put(MISSING_ROUTE_DATA, "Missing route data");
		errorMsg.put(ROUTE_IN_PAST, "Route in the past");
		errorMsg.put(EMPTY_ROUTE, "Empty route");
		errorMsg.put(INVALID_XML_REQUEST, "Invalid XML request");
		errorMsg.put(FAILED_TO_READ_XML, "Failed to read XML");
		errorMsg.put(METOC_UNAVAILABLE, "METOC unavailable");
	}

	public static String getErrorMessage(int errorCode) {
		Errorcodes codes = getInstance();
		if (codes.errorMsg.containsKey(errorCode)) {
			return codes.errorMsg.get(errorCode);
		}
		return "Unknown error";
	}

	public static Errorcodes getInstance() {
		synchronized (Errorcodes.class) {
			if (instance == null) {
				instance = new Errorcodes();
			}
			return instance;
		}
	}

}
