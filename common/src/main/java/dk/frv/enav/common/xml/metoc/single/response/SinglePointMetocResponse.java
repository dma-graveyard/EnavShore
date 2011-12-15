package dk.frv.enav.common.xml.metoc.single.response;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import dk.frv.enav.common.xml.ShoreServiceResponse;
import dk.frv.enav.common.xml.metoc.MetocForecast;
import dk.frv.enav.common.xml.metoc.MetocForecastPoint;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "singlePointMetocResponse", propOrder = {})
public class SinglePointMetocResponse extends ShoreServiceResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private MetocForecastPoint metocPoint = new MetocForecastPoint();
	
	public SinglePointMetocResponse() {
		
	}

	public MetocForecastPoint getMetocPoint() {
		return metocPoint;
	
	}

	public void setMetocPoint(MetocForecastPoint metocPoint) {
		this.metocPoint = metocPoint;
	}
	
	
	
}
