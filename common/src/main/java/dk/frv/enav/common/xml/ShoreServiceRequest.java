package dk.frv.enav.common.xml;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "shoreServiceRequest", propOrder = { "mmsi", "positionReport" })
public abstract class ShoreServiceRequest implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * MMSI of requesting ship
	 */
	private Long mmsi;
	
	/**
	 * Optional position report for the ship
	 */
	private PositionReport positionReport = null;

	public ShoreServiceRequest() {

	}

	public Long getMmsi() {
		return mmsi;
	}

	public void setMmsi(Long mmsi) {
		this.mmsi = mmsi;
	}
	
	public PositionReport getPositionReport() {
		return positionReport;
	}
	
	public void setPositionReport(PositionReport positionReport) {
		this.positionReport = positionReport;
	}

}
