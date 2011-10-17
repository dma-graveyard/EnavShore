package dk.frv.enav.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import dk.frv.enav.common.FormatException;
import dk.frv.enav.common.xml.Waypoint;

/**
 * Utility class different parsing tasks
 * @author obo
 *
 */
public class ParseUtils {
	
	public static final int METERS_PER_NM = 1852;
	
	public static Double nmToMeters(Double nm) {
		if (nm == null) return null;
		return nm * METERS_PER_NM;
	}
	
	public static Double metersToNm(Double meters) {
		if (meters == null) return null;
		return meters / METERS_PER_NM;
	}
	
	public static Double parseDouble(String str) throws FormatException {
		if (str == null || str.length() == 0) return null;
		try {
			return Double.parseDouble(str);
		} catch (NumberFormatException e) {
			throw new FormatException("Could not parse " + str + " as a decimal number");
		}
	}
	
	public static Integer parseInt(String str) throws FormatException {
		if (str == null || str.length() == 0) return null;
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
			throw new FormatException("Could not parse " + str + " as an integer");
		}
	}
	
	public static String parseString(String str) {
		str = str.trim();
		if (str == null || str.length() == 0) return null;
		return str;
	}
	
	public static double parseLatitude(String hours, String minutes, String northSouth) throws FormatException {
		Integer h = parseInt(hours);
		Double m = parseDouble(minutes);
		String ns = parseString(northSouth);		
		if (h == null || m == null || ns == null) {
			throw new FormatException();
		}
		if (!ns.equals("N") && !ns.equals("S")) {
			throw new FormatException();
		}
		double lat = h + m / 60.0; 
		if (ns.equals("S")) {
			lat *= -1;
		}
		return lat;		
	}
	
	public static double parseLongitude(String hours, String minutes, String eastWest) throws FormatException {
		Integer h = parseInt(hours);
		Double m = parseDouble(minutes);
		String ew = parseString(eastWest);		
		if (h == null || m == null || ew == null) {
			throw new FormatException();
		}
		if (!ew.equals("E") && !ew.equals("W")) {
			throw new FormatException();
		}
		double lon = h + m / 60.0; 
		if (ew.equals("W")) {
			lon *= -1;
		}
		return lon;		
	}
	
	public static Waypoint.Heading parseSailHeadingType(String heading) throws FormatException {
		heading = parseString(heading);
		if (heading == null) {
			throw new FormatException("Missing sail field");
		}
		if (heading.equals("RL")) {
			return Waypoint.Heading.RL;
		}
		if (heading.equals("GC")) {
			return Waypoint.Heading.GC;
		}
		throw new FormatException("Unknown sail heading " + heading);
	}
	
	public static String getShortSailHeadingType(Waypoint.Heading st) {
		if (st == Waypoint.Heading.RL) {
			return  "RL";
		}
		return "GC";
	}

	public static TimeZone parseTimeZone(String tz) throws FormatException {
		tz = parseString(tz);
		if (tz == null) return null;
		String[] parts = tz.split(":");
		if (parts.length != 2) {
			throw new FormatException("Error in timezone");
		}
		Integer hours = parseInt(parts[0]);
		Integer mins = parseInt(parts[1]);
		if (hours == null || mins == null) {
			throw new FormatException("Error in timezone");
		}
		String sign = (hours < 0) ? "-" : "+";
		hours = Math.abs(hours);		
		String customTzId = String.format("GMT%s%02d%02d", sign, hours, mins);
		return TimeZone.getTimeZone(customTzId);
	}
	
	public static Date parseIso8602(String str) throws FormatException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+0000"));
		try {
			return dateFormat.parse(str);
		} catch (ParseException e) {
			throw new FormatException(e.getMessage());
		}
	}
	
	

}
