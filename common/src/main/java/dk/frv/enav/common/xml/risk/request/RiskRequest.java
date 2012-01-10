package dk.frv.enav.common.xml.risk.request;

import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import dk.frv.enav.common.xml.ShoreServiceRequest;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "riskRequest", propOrder = {})
public class RiskRequest extends ShoreServiceRequest {
	private static final long serialVersionUID = 1L;
	
	
	private double latMin;
	private double lonMin;
	private double latMax;
	private double lonMax;
	
	private Collection<Long> mmsiList;
	
	public RiskRequest() {
	}


	


	public RiskRequest(double latMin, double lonMin, double latMax, double lonMax) {
		super();
		this.latMin = latMin;
		this.lonMin = lonMin;
		this.latMax = latMax;
		this.lonMax = lonMax;
	
	}


	public boolean add(Long arg0) {
		return mmsiList.add(arg0);
	}


	public Collection<Long> getMmsiList() {
		return mmsiList;
	}


	public void setMmsiList(Collection<Long> mmsiList) {
		this.mmsiList = mmsiList;
	}





	public double getLatMin() {
		return latMin;
	}





	public void setLatMin(double latMin) {
		this.latMin = latMin;
	}





	public double getLonMin() {
		return lonMin;
	}





	public void setLonMin(double lonMin) {
		this.lonMin = lonMin;
	}





	public double getLatMax() {
		return latMax;
	}





	public void setLatMax(double latMax) {
		this.latMax = latMax;
	}





	public double getLonMax() {
		return lonMax;
	}





	public void setLonMax(double lonMax) {
		this.lonMax = lonMax;
	}
	
	
	
	
}
