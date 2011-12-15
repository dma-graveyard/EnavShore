package dk.frv.enav.common.xml.msi;

import java.io.Serializable;
import java.util.List;

import dk.frv.ais.geo.GeoLocation;

public class MsiLocation implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public enum LocationType {
		POINT, POLYGON, POINTS, POLYLINE;		
		public static LocationType getType(String type) {
			if (type == null) {
				return null;
			}
			if (type.equalsIgnoreCase("POINT")) {
				return POINT;
			}
			if (type.equalsIgnoreCase("POLYGON")) {
				return POLYGON;
			}
			if (type.equalsIgnoreCase("POINTS")) {
				return POINTS;
			}
			if (type.equalsIgnoreCase("POLYLINE")) {
				return POLYLINE;
			}
			return null;
		}		
	}
	
	private LocationType locationType;
	private String name;
	private String area;
	private String subArea;
	private List<MsiPoint> points;
	
	public MsiLocation() {
		
	}

	public LocationType getLocationType() {
		return locationType;
	}

	public void setLocationType(LocationType locationType) {
		this.locationType = locationType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getSubArea() {
		return subArea;
	}

	public void setSubArea(String subArea) {
		this.subArea = subArea;
	}
	
	public List<MsiPoint> getPoints() {
		return points;
	}
	
	public void setPoints(List<MsiPoint> points) {
		this.points = points;
	}
	
	public GeoLocation getCenter() {
		if (points == null || points.size() == 0) {
			return null;
		}
		double minLat = 90;
		double maxLat = -90;
		double minLon = 180;
		double maxLon = -180;
		for (MsiPoint msiPoint : points) {
			if (msiPoint.getLatitude() < minLat) {
				minLat = msiPoint.getLatitude();
			}
			if (msiPoint.getLatitude() > maxLat) {
				maxLat = msiPoint.getLatitude();
			}
			if (msiPoint.getLongitude() < minLon) {
				minLon = msiPoint.getLongitude();
			}
			if (msiPoint.getLongitude() > maxLon) {
				maxLon = msiPoint.getLongitude();
			}
		}
		
		if (getLocationType() != LocationType.POLYLINE) {
			return new GeoLocation((minLat + maxLat) / 2, (minLon + maxLon) / 2);
		}
		
		// Polyline center will be middle point
		MsiPoint msiPoint = points.get(points.size() / 2);
		return new GeoLocation(msiPoint.getLatitude(), msiPoint.getLongitude());
	}
	
}
