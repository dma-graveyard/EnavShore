package dk.frv.enav.shore.core.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import dk.frv.ais.geo.GeoLocation;

@Entity
@Table(name="route_waypoint")
public class RouteWaypoint implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;
	private Date eta;
	// -90 ... 90 decimal degrees
	private double latitude;
	// -180 ... 180 decimal degrees
	private double longitude;
	private String name;
	private int no;
	// Meters
	private double turnRad;
	// deg / min
	private double rot;
	private Route route;

    public RouteWaypoint() {
    }

    @Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false, insertable = true, updatable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name="eta", unique = false, nullable = true, insertable = true, updatable = true)
	public Date getEta() {
		return this.eta;
	}

	public void setEta(Date eta) {
		this.eta = eta;
	}

	@Column(name="latitude", unique = false, nullable = false, insertable = true, updatable = true)
	public double getLatitude() {
		return this.latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	@Column(name="longitude", unique = false, nullable = false, insertable = true, updatable = true)
	public double getLongitude() {
		return this.longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	@Column(name="name", unique = false, nullable = true, insertable = true, updatable = true, length = 256)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name="no", unique = false, nullable = false, insertable = true, updatable = true)
	public int getNo() {
		return this.no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	@Column(name="turn_rad", unique = false, nullable = true, insertable = true, updatable = true)
	public double getTurnRad() {
		return this.turnRad;
	}

	public void setTurnRad(double turnRad) {
		this.turnRad = turnRad;
	}
	
	@Column(name="rot", unique = false, nullable = true, insertable = true, updatable = true)
	public double getRot() {
		return rot;
	}
	
	public void setRot(double rot) {
		this.rot = rot;
	}
	
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "route", unique = false, nullable = false, insertable = true, updatable = true)
	public Route getRoute() {
		return this.route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}
	
	@Transient
	public GeoLocation getGeoLocation() {
		return new GeoLocation(latitude, longitude);
	}
	
	@Transient
	public double getGeodesicDistance(RouteWaypoint endWp) {
		GeoLocation start = getGeoLocation();
		GeoLocation end = endWp.getGeoLocation();
		return start.getGeodesicDistance(end);
	}
	
	@Transient
	public double getRhumbLineDistance(RouteWaypoint endWp) {
		GeoLocation start = getGeoLocation();
		GeoLocation end = endWp.getGeoLocation();
		return start.getRhumbLineDistance(end);
	}
	
	@Transient
	public double getPositiveLongitude() {
		return (getLongitude() + 360) % 360;
	}
	
}