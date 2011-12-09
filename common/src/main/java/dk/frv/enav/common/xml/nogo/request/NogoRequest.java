package dk.frv.enav.common.xml.nogo.request;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import dk.frv.enav.common.xml.Route;
import dk.frv.enav.common.xml.ShoreServiceRequest;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "nogoRequest", propOrder = {})
public class NogoRequest extends ShoreServiceRequest {
	private static final long serialVersionUID = 1L;

	/**
	 * Optional vessel route to use for calculating no go
	 */
	private double draught;
	private double northWestPointLat;
	private double northWestPointLon;
	private double southEastPointLat;
	private double southEastPointLon;
	private Date startDate;
	private Date endDate;

	public NogoRequest() {
	}

	public double getDraught() {
		return draught;
	}

	public void setDraught(double draught) {
		this.draught = draught;
	}

	public double getNorthWestPointLat() {
		return northWestPointLat;
	}

	public void setNorthWestPointLat(double northWestPointLat) {
		this.northWestPointLat = northWestPointLat;
	}

	public double getNorthWestPointLon() {
		return northWestPointLon;
	}

	public void setNorthWestPointLon(double northWestPointLon) {
		this.northWestPointLon = northWestPointLon;
	}

	public double getSouthEastPointLat() {
		return southEastPointLat;
	}

	public void setSouthEastPointLat(double southEastPointLat) {
		this.southEastPointLat = southEastPointLat;
	}

	public double getSouthEastPointLon() {
		return southEastPointLon;
	}

	public void setSouthEastPointLon(double southEastPointLon) {
		this.southEastPointLon = southEastPointLon;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}
