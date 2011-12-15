package dk.frv.enav.common.xml;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "waypoint", propOrder = {})
public class Waypoint implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public enum Heading {
		RL, GC
	}

	// Waypoint related
	
	/**
	 * Mandatory latitude
	 */
	private double lat;
	
	/**
	 * Mandatory longitude
	 */
	private double lon;
	
	/**
	 * Waypoint turn radius in nautical miles
	 */
	private Double turnRad;
	
	/**
	 * Rate of turn
	 */
	private Double rot;
	
	/**
	 * ETA at waypoint
	 */
	private Date eta;
	
	// Leaving leg related
	
	/**
	 * Speed in knots
	 */
	private Double speed;
	
	/**
	 * Heading for leg rhumb line or great circle
	 */
	private Heading heading;
	
	/**
	 * Starboard XTD
	 */
	private Double xtdStarboard;
	
	/**
	 * Port XTD
	 */
	private Double xtdPort;
	
	public Waypoint() {
		
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public Double getTurnRad() {
		return turnRad;
	}

	public void setTurnRad(Double turnRad) {
		this.turnRad = turnRad;
	}

	public Double getRot() {
		return rot;
	}

	public void setRot(Double rot) {
		this.rot = rot;
	}

	public Date getEta() {
		return eta;
	}

	public void setEta(Date eta) {
		this.eta = eta;
	}

	public Double getSpeed() {
		return speed;
	}

	public void setSpeed(Double speed) {
		this.speed = speed;
	}

	public Heading getHeading() {
		return heading;
	}

	public void setHeading(Heading heading) {
		this.heading = heading;
	}

	public Double getXtdStarboard() {
		return xtdStarboard;
	}

	public void setXtdStarboard(Double xtdStarboard) {
		this.xtdStarboard = xtdStarboard;
	}

	public Double getXtdPort() {
		return xtdPort;
	}

	public void setXtdPort(Double xtdPort) {
		this.xtdPort = xtdPort;
	}
	
}
