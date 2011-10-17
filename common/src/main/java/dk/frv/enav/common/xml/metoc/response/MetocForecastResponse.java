package dk.frv.enav.common.xml.metoc.response;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import dk.frv.enav.common.xml.ShoreServiceResponse;
import dk.frv.enav.common.xml.metoc.MetocForecast;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "metocForecastResponse", propOrder = {
    "metocForecast"
})
public class MetocForecastResponse extends ShoreServiceResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private MetocForecast metocForecast = null;
	
	public MetocForecastResponse() {
		
	}
	
	public MetocForecast getMetocForecast() {
		return metocForecast;
	}
	
	public void setMetocForecast(MetocForecast metocForecast) {
		this.metocForecast = metocForecast;
	}
	
}
