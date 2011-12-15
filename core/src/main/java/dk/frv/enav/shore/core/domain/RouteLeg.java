package dk.frv.enav.shore.core.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import dk.frv.enav.common.util.ParseUtils;
import dk.frv.enav.common.xml.Waypoint;

@Entity
@Table(name="route_leg")
public class RouteLeg implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int id;
	// Meters
	private double portSpace;
	private Waypoint.Heading sailHeading;
	// nm / hour (knots)
	private double speed;
	// Meters
	private double stbSpace;
	private Route route;
	private RouteWaypoint startWp;
	private RouteWaypoint endWp;

    public RouteLeg() {
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
	
	@Column(name="stb_space", unique = false, nullable = true, insertable = true, updatable = true)
	public double getStbSpace() {
		return this.stbSpace;
	}

	public void setStbSpace(double stbSpace) {
		this.stbSpace = stbSpace;
	}

	@Column(name="port_space", unique = false, nullable = true, insertable = true, updatable = true)
	public double getPortSpace() {
		return this.portSpace;
	}

	public void setPortSpace(double portSpace) {
		this.portSpace = portSpace;
	}

	@Enumerated(EnumType.STRING)
    @Column(name = "sail_heading", unique = false, nullable = false, insertable = true, updatable = true)
	public Waypoint.Heading getSailHeading() {
		return this.sailHeading;
	}

	public void setSailHeading(Waypoint.Heading sailHeading) {
		this.sailHeading = sailHeading;
	}

	@Column(name="speed", unique = false, nullable = false, insertable = true, updatable = true)	
	public double getSpeed() {
		return this.speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

    @ManyToOne(cascade = {}, fetch = FetchType.LAZY)
    @JoinColumn(name = "route", unique = false, nullable = false, insertable = true, updatable = true)
	public Route getRoute() {
		return this.route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}
	
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name="start_wp", unique = false, nullable = false, insertable = true, updatable = true)
	public RouteWaypoint getStartWp() {
		return startWp;
	}

	public void setStartWp(RouteWaypoint startWp) {
		this.startWp = startWp;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name="end_wp", unique = false, nullable = false, insertable = true, updatable = true)
	public RouteWaypoint getEndWp() {
		return endWp;
	}

	public void setEndWp(RouteWaypoint endWp) {
		this.endWp = endWp;
	}
	
	/**
	 * Distance in meters based on sail heading type
	 * @return
	 */
	@Transient
	public double getDistance() {
		if (sailHeading == Waypoint.Heading.GC) {
			return getGeodesicDistance();
		} else {
			return getRhumbLineDistance();
		}
	}
	
	/**
	 * Distance in meters
	 * @return
	 */
	@Transient
	public double getGeodesicDistance() {
		return startWp.getGeodesicDistance(endWp);
	}
	
	/**
	 * Distance in meters
	 * @return
	 */
	@Transient
	public double getRhumbLineDistance() {
		return startWp.getRhumbLineDistance(endWp);
	}
	
	/**
	 * Get the time to travel segment 
	 * @return time in milliseconds
	 */
	@Transient
	public long getTravelTime() {
		// Distance in nm
		double dist = getDistance() / ParseUtils.METERS_PER_NM;
		// time in ms
		return (long)((dist / getSpeed()) * 60 * 60 * 1000);		
	}
	
	/**
	 * Calculate eta at end waypoint
	 */
	@Transient
	public void calculateEndEta() {
		long t = getTravelTime();
		if (getStartWp() != null && getStartWp().getEta() != null) {
			Date eta = new Date(getStartWp().getEta().getTime() + t);
			getEndWp().setEta(eta);
		}
	}
	
	@Transient
	public boolean isInPast(Date d) {
		Date startEta = getStartWp().getEta();
		Date endEta = getEndWp().getEta();		
		if (startEta == null || endEta == null) {
			return false;
		}
		return (startEta.before(d) && endEta.before(d));
	}
	
	 @Transient
	 public boolean isInPast() {
		 return isInPast(new Date());
	 }
	 
}