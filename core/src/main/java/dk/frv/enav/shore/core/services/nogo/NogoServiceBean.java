package dk.frv.enav.shore.core.services.nogo;

import javax.ejb.Stateless;

import dk.frv.enav.common.xml.nogo.request.NogoRequest;
import dk.frv.enav.common.xml.nogo.response.NogoResponse;
import dk.frv.enav.shore.core.services.ServiceException;

@Stateless
public class NogoServiceBean implements NogoService {

	@Override
	public NogoResponse makeNogo(NogoRequest nogoRequest) throws ServiceException {
		NogoResponse res = new NogoResponse();
		
		// TODO
		
		return res;
	}

}
