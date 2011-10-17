package dk.frv.enav.shore.core.services.ais;

import java.util.Date;

import dk.frv.enav.shore.core.domain.AisVesselStatic.VesselType;

public class PublicAisTarget {
	private long lastReceived;
	private long currentTime = System.currentTimeMillis();
	private double lat;
	private double lon;
	private double cog;
	private boolean moored;
	private VesselType vesselType;
	private short length;
	private byte width;
	private double sog;
	
	public PublicAisTarget(){
		
	}
	
	public PublicAisTarget(long lastReceived, double lat, double lon, double cog, byte navStatus,
			VesselType vesselType, short length, byte width, double sog) {
		Date elapsed = new Date(currentTime - lastReceived);;
		this.lastReceived = elapsed.getTime()/1000;
		this.lat = lat;
		this.lon = lon;
		this.cog = cog;
		moored = false;
		if(navStatus == 1 || navStatus == 5) {
			moored = true;
		}
		this.vesselType = vesselType;
		this.length = length;
		this.width = width;
		this.sog = sog;
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

	public double getCog() {
		return cog;
	}

	public void setCog(double hdg) {
		this.cog = hdg;
	}

	public boolean isMoored() {
		return moored;
	}

	public VesselType getVesselType() {
		return vesselType;
	}

	public void setVesselType(VesselType vesselType) {
		this.vesselType = vesselType;
	}

	public short getLength() {
		return length;
	}

	public void setLength(short length) {
		this.length = length;
	}

	public double getSog() {
		return sog;
	}

	public void setSog(double sog) {
		this.sog = sog;
	}

	public long getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(long currentTime) {
		this.currentTime = currentTime;
	}

	public long getLastReceived() {
		return lastReceived;
	}

	public void setLastReceived(long lastReceived) {
		this.lastReceived = lastReceived;
	}

	public byte getWidth() {
		return width;
	}

	public void setWidth(byte width) {
		this.width = width;
	}

}
