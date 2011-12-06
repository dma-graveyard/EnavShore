package dk.frv.enav.shore.core.services.ais;

import javax.ejb.Local;

import dk.frv.enav.shore.core.domain.AisVesselTarget;

@Local
public interface AisService {
	
	/**
	 * Get total active vessel count
	 * @return
	 */
	int getVesselCount();
	
	/**
	 * Get list of AIS targets in the form of overview targets
	 * @param overviewRequest
	 * @return
	 */
	public OverviewResponse getOverview(OverviewRequest overviewRequest);
	
	/**
	 * Get vessel target by id
	 * @param id
	 * @return
	 */
	public AisVesselTarget getById(int id);
	
	/**
	 * Get details for MMSI target
	 * @param id
	 * @return
	 */
	public DetailedAisTarget getTargetDetails(int id);
	
}
