package dk.frv.enav.shore.core.services.risk;

import java.util.List;

import javax.ejb.Local;

import dk.frv.ais.geo.GeoLocation;
import dk.frv.enav.common.xml.risk.request.RiskRequest;
import dk.frv.enav.common.xml.risk.response.RiskResponse;
import dk.frv.enav.shore.core.domain.RiskIndexes;
import dk.frv.enav.shore.core.services.ServiceException;

@Local
public interface RiskIndexService {
	
	/**
	 * Get the risk indexes for ais target
	 * @param msiPollRequest
	 * @return
	 */
	public RiskResponse getRiskIndexes(RiskRequest req) throws ServiceException;
	
}
