package dk.frv.enav.shore.core.services.ais;

import java.util.List;

import javax.ejb.Local;

import dk.frv.enav.shore.core.domain.AisVesselTarget;

@Local
public interface AisService {
	
	
	public List<AisVesselTarget> getAisTargets(double swLat,double swLon, double neLat, double neLon);
}
