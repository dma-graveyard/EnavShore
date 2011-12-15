package dk.frv.enav.common.xml.metoc.single.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import dk.frv.ais.geo.GeoLocation;
import dk.frv.enav.common.xml.ShoreServiceRequest;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "singlePointMetocRequest", propOrder = {})
public class SinglePointMetocRequest extends ShoreServiceRequest {

	
	Double lat;
	Double lon;
	
	
	public SinglePointMetocRequest() {
		super();
	
	}

	public SinglePointMetocRequest(Double lat, Double lon) {
		super();
		this.lat = lat;
		this.lon = lon;
	}
	
	public SinglePointMetocRequest(GeoLocation pos) {
		super();
		this.lat = pos.getLatitude();
		this.lon = pos.getLongitude();
	}


	public Double getLat() {
		return lat;
	}


	public void setLat(Double lat) {
		this.lat = lat;
	}


	public Double getLon() {
		return lon;
	}


	public void setLon(Double lon) {
		this.lon = lon;
	}

	
	
	
}
