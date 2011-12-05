package dk.frv.enav.shore.core.services.ais;

public class OverviewAisTarget extends AisTarget {
	
	public OverviewAisTarget() {
		super();
	}

	public OverviewAisTarget(long lastReceived, double lat, double lon, double cog, byte navStatus, String vesselType,
			String vesselCargo, short length, byte width, double sog) {
		super(lastReceived, lat, lon, cog, navStatus, vesselType, vesselCargo, length, width, sog);
	}

}
