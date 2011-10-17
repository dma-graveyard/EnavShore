package dk.frv.enav.shore.core.services.vessel;

import javax.ejb.Local;

import dk.frv.enav.shore.core.domain.Vessel;

@Local
public interface VesselService {

	/**
	 * Get vessel by mmsi number
	 * @param mmsi
	 * @return
	 */
	Vessel getVessel(int mmsi);
	
	/**
	 * Get existing vessel entity or create a new one
	 * @param mmsi
	 * @return
	 */
	Vessel getOrCreate(int mmsi);
	
}
