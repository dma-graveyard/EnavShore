package dk.frv.enav.shore.core.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="route")
public class Route implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;
	private String name;
	private Date startTime;
	private Date created;
	private Vessel vessel;
	private List<RouteLeg> routeLegs;
	private List<RouteWaypoint> routeWaypoints;

    public Route() {
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

	@Column(name="name", unique = false, nullable = true, insertable = true, updatable = true, length = 256)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name="start_time", unique = false, nullable = false, insertable = true, updatable = true)
	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "vessel", unique = false, nullable = false, insertable = true, updatable = true)
	public Vessel getVessel() {
		return this.vessel;
	}

	public void setVessel(Vessel vessel) {
		this.vessel = vessel;
	}
	
	@OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy="route")
	public List<RouteLeg> getRouteLegs() {
		return this.routeLegs;
	}

	public void setRouteLegs(List<RouteLeg> routeLegs) {
		this.routeLegs = routeLegs;
	}
	
	@OneToMany(cascade = {}, fetch = FetchType.LAZY, mappedBy="route")
	public List<RouteWaypoint> getRouteWaypoints() {
		return this.routeWaypoints;
	}

	public void setRouteWaypoints(List<RouteWaypoint> routeWaypoints) {
		this.routeWaypoints = routeWaypoints;
	}
	
	@GeneratedValue
	@Column(name = "created", unique = false, nullable = false, insertable = false, updatable = false)
	public Date getCreated() {
		return this.created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

}	