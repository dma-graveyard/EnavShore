package dk.frv.enav.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateUtils {

	private static final TimeZone tzGMT = TimeZone.getTimeZone("GMT+0000");

	public static String getISO8620(Date date) {
		SimpleDateFormat iso8601gmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		iso8601gmt.setTimeZone(tzGMT);
		return iso8601gmt.format(date);
	}

}
