package dk.frv.enav.common.xml.metoc;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "metocForecastTriplet", propOrder = {})
public class MetocForecastTriplet implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Double forecast;
	private Double min;
	private Double max;
	
	public MetocForecastTriplet() {
		
	}
	
	public MetocForecastTriplet(Double forecast, Double min, Double max) {
		this.forecast = forecast;
		this.min = min;
		this.max = max;
	}
	
	public MetocForecastTriplet(Double forecast) {
		this(forecast, null, null);
	}

	public Double getForecast() {
		return forecast;
	}

	public void setForecast(Double forecast) {
		this.forecast = forecast;
	}

	public Double getMin() {
		return min;
	}

	public void setMin(Double min) {
		this.min = min;
	}

	public Double getMax() {
		return max;
	}

	public void setMax(Double max) {
		this.max = max;
	}
	
}
