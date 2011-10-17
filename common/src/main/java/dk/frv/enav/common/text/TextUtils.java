package dk.frv.enav.common.text;

import java.util.Formatter;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;

public class TextUtils {

    public static String className(Class<?> cls) {
        String[] nameParts = StringUtils.split(cls.getName(), '.');
        return nameParts[nameParts.length - 1];
    }

    public static boolean exists(String str) {
        return (str != null && str.length() > 0);
    }
    
	public static String latToPrintable(double lat) {
		String ns = "N";
		if (lat < 0) {
			ns = "S";
			lat *= -1;
		}
		int hours = (int)lat;
		lat -= hours;
		lat *= 60;		
		long p = (long)Math.floor(lat);
		long d = Math.round(((lat - p) * 1000.0));
		StringBuilder sb = new StringBuilder();
		Formatter formatter = new Formatter(sb, Locale.US);
		formatter.format("%02d %02d.%03d%s", hours, p, d, ns);
		return sb.toString();
	}
	
	public static String lonToPrintable(double lon) {
		String ns = "E";
		if (lon < 0) {
			ns = "W";
			lon *= -1;
		}
		int hours = (int)lon;
		lon -= hours;
		lon *= 60;		
		long p = (long)Math.floor(lon);
		long d = Math.round(((lon - p) * 1000.0));
		StringBuilder sb = new StringBuilder();
		Formatter formatter = new Formatter(sb, Locale.US);
		formatter.format("%03d %02d.%03d%s", hours, p, d, ns);
		return sb.toString();
	}
	
}
