package dk.frv.enav.shore.core.services.ais;

import java.util.List;

import javax.ejb.Local;

@Local
public interface AisService {
	
	int getVesselCount();
	
	public List<PublicAisTarget> getPublicAisTargets(AisRequest aisRequest);
}
