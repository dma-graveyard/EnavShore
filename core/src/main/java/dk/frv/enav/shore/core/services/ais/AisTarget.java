package dk.frv.enav.shore.core.services.ais;

import java.util.Date;

public abstract class AisTarget {

	protected long id;
	protected long lastReceived;
	protected long currentTime;
	protected double lat;
	protected double lon;
	protected double cog;
	protected boolean moored;
	protected String vesselType;
	protected String vesselCargo;
	protected short length;
	protected byte width;
	protected double sog;

	public AisTarget() {
		currentTime = System.currentTimeMillis();
	}

	public AisTarget(long lastReceived, double lat, double lon, double cog, byte navStatus, String vesselType, String vesselCargo,
			short length, byte width, double sog) {
		this();
		Date elapsed = new Date(currentTime - lastReceived);
		this.lastReceived = elapsed.getTime() / 1000;
		this.lat = lat;
		this.lon = lon;
		this.cog = cog;
		moored = false;
		if (navStatus == 1 || navStatus == 5) {
			moored = true;
		}
		this.vesselType = vesselType;
		this.vesselCargo = vesselCargo;
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

	public String getVesselType() {
		return vesselType;
	}

	public void setVesselType(String vesselType) {
		this.vesselType = vesselType;
	}

	public String getVesselCargo() {
		return vesselCargo;
	}

	public void setVesselCargo(String vesselCargo) {
		this.vesselCargo = vesselCargo;
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
