package dk.frv.enav.common.xml.metoc.request;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import dk.frv.enav.common.xml.ShoreServiceRequest;
import dk.frv.enav.common.xml.metoc.MetocDataTypes;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "metocForecastRequest", propOrder = {})
public class MetocForecastRequest extends ShoreServiceRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Set of data types to request
	 */
	private Set<MetocDataTypes> dataTypes = new HashSet<MetocDataTypes>();

	/**
	 * Interval between points in minutes
	 */	
	private int dt;
	
	private Integer t0;
	
	private Integer t1;
	
	/**
	 * List of waypoints
	 */
	private List<MetocForecastRequestWp> waypoints = new ArrayList<MetocForecastRequestWp>();
	
	public MetocForecastRequest() {
		
	}

	public Set<MetocDataTypes> getDataTypes() {
		return dataTypes;
	}

	public void setDataTypes(Set<MetocDataTypes> dataTypes) {
		this.dataTypes = dataTypes;
	}

	public int getDt() {
		return dt;
	}

	public void setDt(int dt) {
		this.dt = dt;
	}

	public Integer getT0() {
		return t0;
	}

	public void setT0(Integer t0) {
		this.t0 = t0;
	}

	public Integer getT1() {
		return t1;
	}

	public void setT1(Integer t1) {
		this.t1 = t1;
	}

	public List<MetocForecastRequestWp> getWaypoints() {
		return waypoints;
	}

	public void setWaypoints(List<MetocForecastRequestWp> waypoints) {
		this.waypoints = waypoints;
	}
	
	
}
