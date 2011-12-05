package dk.frv.enav.shore.core.services.ais;

import dk.frv.enav.shore.core.domain.AisClassAPosition;
import dk.frv.enav.shore.core.domain.AisVesselPosition;
import dk.frv.enav.shore.core.domain.AisVesselStatic;
import dk.frv.enav.shore.core.domain.AisVesselTarget;


public abstract class AisTarget {

	protected long mmsi;
	protected String vesselClass;
	protected long lastReceived;
	protected long currentTime;
	protected double lat;
	protected double lon;
	protected double cog;
	protected boolean moored;
	protected String vesselType;
	protected short length;
	protected byte width;
	protected double sog;

	public AisTarget() {
		
	}
	
	public void init(AisVesselTarget aisVessel, AisVesselPosition aisVesselPosition, AisVesselStatic aisVesselStatic, AisClassAPosition aisClassAPosition) {
		Double heading = null;
		if((heading = aisVesselPosition.getCog()) == null) {
			if((heading = aisVesselPosition.getHeading()) == null)
				heading = 0d;
		}

		byte navStatus = -1;
		if(aisClassAPosition != null) {
			navStatus = aisClassAPosition.getNavStatus();
		}
		
		
		short length = (short) (aisVesselStatic.getDimBow() + aisVesselStatic.getDimStern());
		byte width = (byte) (aisVesselStatic.getDimPort() + aisVesselStatic.getDimStarboard());
		
		Double sog;
		if((sog = aisVesselPosition.getSog()) == null) {
			sog = 0d;
		}
		
		currentTime = System.currentTimeMillis();
		setMmsi(aisVessel.getMmsi());
		setVesselClass(aisVessel.getVesselClass());
		setLastReceived((currentTime - aisVessel.getLastReceived().getTime()) / 1000);
		setLat(aisVesselPosition.getLat());
		setLon(aisVesselPosition.getLon());
		setCog(heading);
		setSog(sog);
		setMoored(navStatus == 1 || navStatus == 5);
		setVesselType(aisVesselStatic.getShipTypeCargo().prettyType());
		setWidth(width);
		setLength(length);
	}
	
	public long getMmsi() {
		return mmsi;
	}
	
	public void setMmsi(long mmsi) {
		this.mmsi = mmsi;
	}
	
	public String getVesselClass() {
		return vesselClass;
	}
	
	public void setVesselClass(String vesselClass) {
		this.vesselClass = vesselClass;
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
	
	public void setMoored(boolean moored) {
		this.moored = moored;
	}

	public String getVesselType() {
		return vesselType;
	}

	public void setVesselType(String vesselType) {
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
