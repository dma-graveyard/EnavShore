package dk.frv.enav.shore.core.services.ais;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class AisRequest {
	private double southWestLat;
	private double southWestLon;
	private double northEastLat;
	private double northEastLon;
	
	
	public AisRequest(double southWestLat, double southWestLon, double northEastLat, double northEastLon) {
		this.southWestLat = southWestLat;
		this.southWestLon = southWestLon;
		this.northEastLat = northEastLat;
		this.northEastLon = northEastLon;
	}


	public double getSouthWestLat() {
		return southWestLat;
	}

	public void setSouthWestLat(double southWestLat) {
		this.southWestLat = southWestLat;
	}


	public double getSouthWestLon() {
		return southWestLon;
	}


	public void setSouthWestLon(double southWestLon) {
		this.southWestLon = southWestLon;
	}


	public double getNorthEastLat() {
		return northEastLat;
	}


	public void setNorthEastLat(double northEastLat) {
		this.northEastLat = northEastLat;
	}


	public double getNorthEastLon() {
		return northEastLon;
	}


	public void setNorthEastLon(double northEastLon) {
		this.northEastLon = northEastLon;
	}	
	
}
