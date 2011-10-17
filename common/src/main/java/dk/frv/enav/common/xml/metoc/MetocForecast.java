package dk.frv.enav.common.xml.metoc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MetocForecast implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Date of creation for this specific forecast
	 */
	private Date created;
	/**
	 * List of forecast points
	 */
	private List<MetocForecastPoint> forecasts = new ArrayList<MetocForecastPoint>();
	
	public MetocForecast() {
		
	}
	
	public MetocForecast(Date now) {
		this.created = now;
	}

	public List<MetocForecastPoint> getForecasts() {
		return forecasts;
	}

	public void setForecasts(List<MetocForecastPoint> forecasts) {
		this.forecasts = forecasts;
	}
	
	public Date getCreated() {
		return created;
	}
	
	public void setCreated(Date created) {
		this.created = created;
	}
	
}
