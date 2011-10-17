package dk.frv.enav.common.xml.metoc.request;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "metocForecastRequestWp", propOrder = {
    "eta",
    "heading",
    "lat",
    "lon"
})
public class MetocForecastRequestWp implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private double lat;
	private double lon;
	private Date eta;
	/**
	 * Heading on outgoing leg
	 */
	private String heading; // GC or RL
	
	public MetocForecastRequestWp() {
		
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public Date getEta() {
		return eta;
	}

	public void setEta(Date eta) {
		this.eta = eta;
	}

	public String getHeading() {
		return heading;
	}

	public void setHeading(String heading) {
		this.heading = heading;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MetocForecastRequestWp [eta=");
		builder.append(eta);
		builder.append(", heading=");
		builder.append(heading);
		builder.append(", lat=");
		builder.append(lat);
		builder.append(", lon=");
		builder.append(lon);
		builder.append("]");
		return builder.toString();
	}
	
}
