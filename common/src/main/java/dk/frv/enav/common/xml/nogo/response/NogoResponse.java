package dk.frv.enav.common.xml.nogo.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import dk.frv.enav.common.xml.ShoreServiceResponse;
import dk.frv.enav.common.xml.nogo.types.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "nogoResponse", propOrder = {})
public class NogoResponse extends ShoreServiceResponse {
	private static final long serialVersionUID = 1L;
	
	/**
	 * TODO Some form of representation of no go
	 */
	private List<NogoPolygon> polygons;
	private Date validFrom;
	private Date validTo;
	private int noGoErrorCode;
	private String noGoMessage;
	
	public NogoResponse() {
		polygons = new ArrayList<NogoPolygon>();
	}

	public int getNoGoErrorCode() {
		return noGoErrorCode;
	}



	public void setNoGoErrorCode(int noGoErrorCode) {
		this.noGoErrorCode = noGoErrorCode;
	}



	public String getNoGoMessage() {
		return noGoMessage;
	}



	public void setNoGoMessage(String nogoMessage) {
		this.noGoMessage = nogoMessage;
	}



	public void addPolygon(NogoPolygon polygon){
		polygons.add(polygon);
	}

	public List<NogoPolygon> getPolygons(){
		return polygons;
	}

	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}
	
	public void setValidTo(Date validTo) {
		this.validTo = validTo;
	}

	public Date getValidFrom() {
		return validFrom;
	}

	public Date getValidTo() {
		return validTo;
	}	
	
	
}
