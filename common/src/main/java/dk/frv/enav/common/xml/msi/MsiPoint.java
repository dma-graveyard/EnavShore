package dk.frv.enav.common.xml.msi;

import java.io.Serializable;

public class MsiPoint implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private double latitude;
	private double longitude;
	private double radius;
	
	public MsiPoint() {
		
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}
	
}
