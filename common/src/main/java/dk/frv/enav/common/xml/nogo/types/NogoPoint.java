package dk.frv.enav.common.xml.nogo.types;

public class NogoPoint {
private double lat;
private double lon;
	public NogoPoint(double lat, double lon){
		this.lat = lat;
		this.lon = lon;
	}
	
	public NogoPoint(){
		this.lat = 0;
		this.lon = 0;
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
	
	 @Override public String toString() {
		 return ("Lat: " + lat + " Long: " + lon);
	 }
	
	
}
