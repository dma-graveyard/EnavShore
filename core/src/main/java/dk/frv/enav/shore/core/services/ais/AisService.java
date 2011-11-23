package dk.frv.enav.shore.core.services.ais;

import java.util.List;

import javax.ejb.Local;

import dk.frv.enav.shore.core.domain.AisVesselTarget;

@Local
public interface AisService {
	
	int getVesselCount();
	
	public List<PublicAisTarget> getPublicAisTargets(AisRequest aisRequest);
	
	public List<AisVesselTarget> getAisTargets(AisRequest aisRequest);
}
