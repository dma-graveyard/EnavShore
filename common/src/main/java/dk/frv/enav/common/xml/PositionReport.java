package dk.frv.enav.common.xml;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "positionReport", propOrder = {})
public class PositionReport implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Mandatory latitude
	 */
	private double latitude;
	
	/**
	 * Mandatory longitude
	 */
	private double longitude;
	
	/**
	 * Speed over ground
	 */
	private Double sog;
	
	/**
	 * Heading
	 */
	private Double heading;
	
	/**
	 * Course over ground
	 */
	private Double cog;
	
	/**
	 * Rate of turn
	 */
	private Double rot;
	
	public PositionReport() {
		
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

	public Double getSog() {
		return sog;
	}

	public void setSog(Double sog) {
		this.sog = sog;
	}

	public Double getHeading() {
		return heading;
	}

	public void setHeading(Double heading) {
		this.heading = heading;
	}

	public Double getCog() {
		return cog;
	}

	public void setCog(Double cog) {
		this.cog = cog;
	}

	public Double getRot() {
		return rot;
	}

	public void setRot(Double rot) {
		this.rot = rot;
	}
	
}
