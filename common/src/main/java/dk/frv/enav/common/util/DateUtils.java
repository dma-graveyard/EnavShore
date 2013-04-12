package dk.frv.enav.common.util;

import java.text.ParseException;
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
	
	public static String getISO8620SSSZZ(Date date) {
	    SimpleDateFormat iso8601gmtz = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");
	    iso8601gmtz.setTimeZone(tzGMT);
	    return iso8601gmtz.format(date);
	}
	
	public static Date getISO8620SSSZZ(String date)  {
	    SimpleDateFormat iso8601gmtz = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");
	    try {
            return iso8601gmtz.parse(date);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
	}

}
