package dk.frv.enav.shore.core.services.nogo;

import java.util.List;

import javax.ejb.Local;

import dk.frv.enav.common.xml.nogo.request.NogoRequest;
import dk.frv.enav.common.xml.nogo.response.NogoResponse;
import dk.frv.enav.common.xml.nogo.types.NogoPolygon;
import dk.frv.enav.shore.core.domain.DepthDenmark;
import dk.frv.enav.shore.core.domain.TideDenmark;
import dk.frv.enav.shore.core.services.ServiceException;

@Local
public interface NogoService {

	/**
	 * Calculate no go area from no go request
	 * @param nogoRequest
	 * @return
	 * @throws ServiceException
	 */
	NogoResponse nogoPoll(NogoRequest nogoRequest) throws ServiceException;
	
	
	List<NogoPolygon> parseResult(List<DepthDenmark> result, List<TideDenmark> resultTide, double depth);
	
}
