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
	private double point1;
	private double point2;
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

	public double getPoint1() {
		return point1;
	}

	public void setPoint1(double point1) {
		this.point1 = point1;
	}

	public double getPoint2() {
		return point2;
	}

	public void setPoint2(double point2) {
		this.point2 = point2;
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
