package dk.frv.enav.shore.core.services.nogo;

import javax.ejb.Local;

import dk.frv.enav.common.xml.nogo.request.NogoRequest;
import dk.frv.enav.common.xml.nogo.response.NogoResponse;
import dk.frv.enav.shore.core.services.ServiceException;

@Local
public interface NogoService {

	/**
	 * Calculate no go area from no go request
	 * @param nogoRequest
	 * @return
	 * @throws ServiceException
	 */
	NogoResponse makeNogo(NogoRequest nogoRequest) throws ServiceException;

}
