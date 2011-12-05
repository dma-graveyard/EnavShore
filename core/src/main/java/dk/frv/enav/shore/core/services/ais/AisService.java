package dk.frv.enav.shore.core.services.ais;

import java.util.List;

import javax.ejb.Local;

@Local
public interface AisService {
	
	/**
	 * Get total active vessel count
	 * @return
	 */
	int getVesselCount();
	
	/**
	 * Get list of AIS targets in the form of overview targets
	 * @param aisRequest
	 * @return
	 */
	public List<OverviewAisTarget> getAisTargets(AisRequest aisRequest);
	
	/**
	 * Get details for MMSI target
	 * @param mmsi
	 * @return
	 */
	public DetailedAisTarget getTargetDetails(int mmsi);
	
}
