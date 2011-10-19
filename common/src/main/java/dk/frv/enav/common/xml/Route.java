package dk.frv.enav.common.xml;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "route", propOrder = {})
public class Route implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * List of waypoints for route
	 */
	private List<Waypoint> waypoints;
	
	/**
	 * Optional active waypoint index
	 */
	private Integer activeWaypoint;
	
	public Route() {
		
	}

	public List<Waypoint> getWaypoints() {
		return waypoints;
	}

	public void setWaypoints(List<Waypoint> waypoints) {
		this.waypoints = waypoints;
	}

	public Integer getActiveWaypoint() {
		return activeWaypoint;
	}

	public void setActiveWaypoint(Integer activeWaypoint) {
		this.activeWaypoint = activeWaypoint;
	}
	
}
