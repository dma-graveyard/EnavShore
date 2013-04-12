package dk.frv.enav.shore.core.metoc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import dk.frv.ais.geo.GeoLocation;
import dk.frv.enav.common.FormatException;
import dk.frv.enav.common.jboss.JbossProperties;
import dk.frv.enav.common.net.http.HttpClient;
import dk.frv.enav.common.net.http.HttpParams;
import dk.frv.enav.common.util.DateUtils;
import dk.frv.enav.common.util.ParseUtils;
import dk.frv.enav.common.xml.metoc.MetocDataTypes;
import dk.frv.enav.common.xml.metoc.MetocForecast;
import dk.frv.enav.common.xml.metoc.MetocForecastPoint;
import dk.frv.enav.common.xml.metoc.MetocForecastTriplet;
import dk.frv.enav.common.xml.metoc.request.MetocForecastRequest;
import dk.frv.enav.common.xml.metoc.request.MetocForecastRequestWp;
import dk.frv.enav.common.xml.metoc.response.MetocForecastResponse;

public class MetocHttpInvoker implements MetocInvoker{
	
	private static Logger LOG = Logger.getLogger(MetocHttpInvoker.class);
	
	private String metocUrl;
	private MetocForecastRequest metocRequest;
	private StringBuilder buf = new StringBuilder();
	
	public MetocHttpInvoker(MetocForecastRequest metocRequest) { 
		this.metocRequest = metocRequest;
		this.metocUrl = JbossProperties.get("metoc_url", "http://linux06/cgi-bin/metoc_route_cgi.cgi");
		LOG.debug("metocUrl: " + metocUrl);
	}
	
	public MetocForecastResponse makeRequest() throws MetocInvokerException {
		buildRequest();
		HttpClient httpClient = new HttpClient();
		httpClient.setUrl(metocUrl);
		HttpParams params = new HttpParams();
		params.put("request", buf.toString());
		httpClient.setRequestParams(params);
		LOG.debug("Making METOC HTTP request to: " + metocUrl + " with request: " + buf.toString());
		LOG.debug("request:\n" + buf.toString());

		int responseCode = -1;
		try {
            responseCode = httpClient.post();
        } catch (IOException e) {
        	
            LOG.error("Failed to make HTTP request: " + e.getMessage());
            throw new MetocInvokerException("Failed to call METOC service");
        }
        String response = httpClient.getResponseString();
        httpClient.releaseConnection();
        if (responseCode != 200) {
        	LOG.error("Error from METOC service: " + response);
        	throw new MetocInvokerException("METOC service failed");
        }
        
        LOG.debug("Response:\n" + response);

        return parseResponse(response);
	}
	
	private MetocForecastResponse parseResponse(String str) throws MetocInvokerException {
		MetocForecastResponse response = new MetocForecastResponse();		
		MetocForecast forecast = new MetocForecast();
		List<MetocForecastPoint> points = new ArrayList<MetocForecastPoint>();		
		forecast.setForecasts(points);
		forecast.setCreated(new Date());
		response.setMetocForecast(forecast);
		
		String[] lines = str.split("\n");
				
		for (String line : lines) {
			if (line.length() > 10) {
				points.add(parseLine(line));
			}
		}
		
		return response;
	}
	
	private MetocForecastPoint parseLine(String line) throws MetocInvokerException {
		MetocForecastPoint point = new MetocForecastPoint();
		String[] fields = line.split(",");
		if (fields.length < 3) {
			LOG.error("Error in METOC response line: " + line);
			throw new MetocInvokerException("Error in METOC response");
		}
		// Time
		try {
			point.setTime(ParseUtils.parseIso8602(fields[0] + ":00"));
		} catch (FormatException e) {
			LOG.error("Date error in METOC response line: " + line + ": " + e.getMessage());
			throw new MetocInvokerException("Error in METOC response");
		}
		// Expires
		try {
			point.setExpires(ParseUtils.parseIso8602(fields[1] + ":00"));
		} catch (FormatException e) {
			LOG.error("Date error in METOC response line: " + line + ": " + e.getMessage());
			throw new MetocInvokerException("Error in METOC response");
		}
		// Lat and lon
		try {
			point.setLat(Double.parseDouble(fields[2]));
			point.setLon(Double.parseDouble(fields[3]));
		} catch (NumberFormatException e) {
			LOG.error("Pos error in METOC response line: " + line + ": " + e.getMessage());
			throw new MetocInvokerException("Error in METOC response");
		}
		
		int offset = 4;
		
		while (offset < fields.length) {
			String id = fields[offset].substring(0,2);
			fields[offset] = fields[offset].substring(3);			
			List<MetocForecastTriplet> triplets = parseTriplets(fields[offset]);
			if (triplets == null) {
				offset++;
				continue;
			}
			if (id.equals("WI")) {
				if (triplets.size() != 2) {
					LOG.error("Wind error in METOC response line: " + line);
					throw new MetocInvokerException("Error in METOC response");
				}
				point.setWindDirection(triplets.get(0));
				point.setWindSpeed(triplets.get(1));
			} else if (id.equals("CU")) {
				if (triplets.size() != 2) {
					LOG.error("Current error in METOC response line: " + line);
					throw new MetocInvokerException("Error in METOC response");
				}
				point.setCurrentDirection(triplets.get(0));
				point.setCurrentSpeed(triplets.get(1));
			} else if (id.equals("WA")) {
				if (triplets.size() != 3) {
					LOG.error("Wave error in METOC response line: " + line);
					throw new MetocInvokerException("Error in METOC response");
				}
				point.setMeanWaveHeight(triplets.get(0));
				point.setMeanWavePeriod(triplets.get(1));
				point.setMeanWaveDirection(triplets.get(2));
			} else if (id.equals("SE")) {
				if (triplets.size() != 1) {
					LOG.error("Sea level error in METOC response line: " + line);
					throw new MetocInvokerException("Error in METOC response");
				}
				point.setSeaLevel(triplets.get(0));
			}
			offset++;
		}
		
		
		return point;
	}
	
	private List<MetocForecastTriplet> parseTriplets(String field) throws MetocInvokerException {
		List<MetocForecastTriplet> triplets = new ArrayList<MetocForecastTriplet>();
		String[] trplFields = field.split(":");
		for (String trplField : trplFields) {
			String[] values = trplField.split("/");
			if (values.length != 3) {
				LOG.error("Error in triplet: " + trplField + " length: " + values.length);
				return null;
			}
			MetocForecastTriplet triplet = new MetocForecastTriplet();
			try {
				triplet.setForecast(Double.parseDouble(values[0]));
				triplet.setMin(Double.parseDouble(values[1]));
				triplet.setMax(Double.parseDouble(values[2]));
			} catch (NumberFormatException e) {
				LOG.error("Error in triplet: " + trplField + ": " + e.getMessage());
				return null;
			}
			triplets.add(triplet);
		}
		
		return triplets;
		
	}
	
	private void buildRequest() throws MetocInvokerException {
		// Data types
		Set<MetocDataTypes> types = new HashSet<MetocDataTypes>(); 
		if (metocRequest.getDataTypes() ==  null || metocRequest.getDataTypes().size() == 0) {
			for (MetocDataTypes type : MetocDataTypes.allTypes()) {
				types.add(type);
			}
		} else {			
			for (MetocDataTypes type : metocRequest.getDataTypes()) {
				types.add(type);
			}
		}
		
		// Remove DE as not implemented yet
		types.remove(MetocDataTypes.DE);
		
		buf.append("DATA " + StringUtils.join(types, ",") + "\n");
		
		// Times
		buf.append("TIME " + Integer.toString(metocRequest.getDt() * 60));
		buf.append(",");
		if (metocRequest.getT0() != null) {
			buf.append(Integer.toString(metocRequest.getT0() * 60));
		}
		buf.append(",");
		if (metocRequest.getT1() != null) {
			buf.append(Integer.toString(metocRequest.getT1() * 60));
		}
		buf.append("\n");
				
		if (metocRequest.getWaypoints().size() == 0) {
			return;
		}
		
		// Waypoints
		MetocForecastRequestWp startWp = metocRequest.getWaypoints().get(0);
		buf.append(wpLine(startWp));
		for (int i = 1; i < metocRequest.getWaypoints().size(); i++) {
			MetocForecastRequestWp endWp = metocRequest.getWaypoints().get(i);
			buf.append(legLine(startWp, endWp));			
			buf.append(wpLine(endWp));
			startWp = endWp;
		}
	}
	
	private static String wpLine(MetocForecastRequestWp wp) {
		List<String> fields = new ArrayList<String>();
		fields.add("WP");
		fields.add(String.format(Locale.US, "%.4f", wp.getLat()));
		fields.add(String.format(Locale.US, "%.4f", wp.getLon()));
		fields.add(DateUtils.getISO8620(wp.getEta()));
		return StringUtils.join(fields, ",") + "\n";
	}
	
	private static String legLine(MetocForecastRequestWp startWp, MetocForecastRequestWp endWp) {
		List<String> fields = new ArrayList<String>();
		fields.add("LEG");
		double diffInHours = (double)(endWp.getEta().getTime() - startWp.getEta().getTime()) / 3600000.0;
		GeoLocation startPos = new GeoLocation(startWp.getLat(), startWp.getLon());
		GeoLocation endPos = new GeoLocation(endWp.getLat(), endWp.getLon());
		double distance;
		if (startWp.getHeading().equalsIgnoreCase("GC")) {
			distance = startPos.getGeodesicDistance(endPos);
		} else {
			distance = startPos.getRhumbLineDistance(endPos);
		}		
		double speed = (distance / 1852.0) / diffInHours;
		fields.add(String.format(Locale.US, "%.2f", speed));
		fields.add(startWp.getHeading());
		fields.add(String.format(Locale.US, "%d", Math.round(distance)));
		return StringUtils.join(fields, ",") + "\n";
	}

}
