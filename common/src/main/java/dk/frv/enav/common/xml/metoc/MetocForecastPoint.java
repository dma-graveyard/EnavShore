package dk.frv.enav.common.xml.metoc;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * Class containing METOC information for one specific lat/lon position
 * 
 * @author obo
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "metocForecastPoint", propOrder = {})
public class MetocForecastPoint implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Decimal degree lat and lon
	 */
	private double lat;
	private double lon;
	/**
	 * The forecast is for this time
	 */
	private Date time;
	/**
	 * The forecast expires at this time
	 */
	private Date expires;
	/**
	 * Wind speed in m/s
	 */
	private MetocForecastTriplet windSpeed;
	/**
	 * Wind from direction in degrees clockwise from north
	 */
	private MetocForecastTriplet windDirection;
	/**
	 * Current speed in m/s
	 */
	private MetocForecastTriplet currentSpeed;
	/**
	 * Current towards direction in degrees from north 
	 */
	private MetocForecastTriplet currentDirection;
	/**
	 * Mean wave height in meters
	 */
	private MetocForecastTriplet meanWaveHeight;
	/**
	 * Mean wave period in seconds
	 */
	private MetocForecastTriplet meanWavePeriod;
	/**
	 * Mean wave from direction in degrees from north 
	 */	
	private MetocForecastTriplet meanWaveDirection;
	/**
	 * Sea level in meters
	 */
	private MetocForecastTriplet seaLevel;
	/**
	 * Density in kg/m^3
	 */
	private MetocForecastTriplet density;
	
	public MetocForecastPoint() {
		
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

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}
	
	public Date getExpires() {
		return expires;
	}
	
	public void setExpires(Date expires) {
		this.expires = expires;
	}

	public MetocForecastTriplet getWindSpeed() {
		return windSpeed;
	}

	public void setWindSpeed(MetocForecastTriplet windSpeed) {
		this.windSpeed = windSpeed;
	}

	public MetocForecastTriplet getWindDirection() {
		return windDirection;
	}

	public void setWindDirection(MetocForecastTriplet windDirection) {
		this.windDirection = windDirection;
	}

	public MetocForecastTriplet getCurrentSpeed() {
		return currentSpeed;
	}

	public void setCurrentSpeed(MetocForecastTriplet currentSpeed) {
		this.currentSpeed = currentSpeed;
	}

	public MetocForecastTriplet getCurrentDirection() {
		return currentDirection;
	}

	public void setCurrentDirection(MetocForecastTriplet currentDirection) {
		this.currentDirection = currentDirection;
	}

	public MetocForecastTriplet getMeanWaveHeight() {
		return meanWaveHeight;
	}

	public void setMeanWaveHeight(MetocForecastTriplet meanWaveHeight) {
		this.meanWaveHeight = meanWaveHeight;
	}
	
	public MetocForecastTriplet getMeanWavePeriod() {
		return meanWavePeriod;
	}
	
	public void setMeanWavePeriod(MetocForecastTriplet meanWavePeriod) {
		this.meanWavePeriod = meanWavePeriod;
	}

	public MetocForecastTriplet getMeanWaveDirection() {
		return meanWaveDirection;
	}

	public void setMeanWaveDirection(MetocForecastTriplet meanWaveDirection) {
		this.meanWaveDirection = meanWaveDirection;
	}

	public MetocForecastTriplet getSeaLevel() {
		return seaLevel;
	}

	public void setSeaLevel(MetocForecastTriplet seaLevel) {
		this.seaLevel = seaLevel;
	}

	public MetocForecastTriplet getDensity() {
		return density;
	}

	public void setDensity(MetocForecastTriplet density) {
		this.density = density;
	}
	
}
