package dk.frv.enav.shore.core.services.metoc;

import javax.ejb.Local;

import dk.frv.enav.common.xml.metoc.request.MetocForecastRequest;
import dk.frv.enav.common.xml.metoc.response.MetocForecastResponse;
import dk.frv.enav.shore.core.services.ServiceException;

@Local
public interface MetocService {
	
	/**
	 * Get METOC 
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	MetocForecastResponse getMetoc(MetocForecastRequest request) throws ServiceException;

}
