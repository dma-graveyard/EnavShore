package dk.frv.enav.shore.core.services.metoc;

import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import dk.frv.enav.common.xml.metoc.request.MetocForecastRequest;
import dk.frv.enav.common.xml.metoc.request.MetocForecastRequestWp;
import dk.frv.enav.common.xml.metoc.response.MetocForecastResponse;
import dk.frv.enav.shore.core.metoc.DmiMetocClient;
import dk.frv.enav.shore.core.metoc.MetocHttpInvoker;
import dk.frv.enav.shore.core.metoc.MetocInvoker;
import dk.frv.enav.shore.core.metoc.MetocInvokerException;
import dk.frv.enav.shore.core.services.Errorcodes;
import dk.frv.enav.shore.core.services.ServiceException;
import dk.frv.enav.shore.core.services.vessel.VesselService;

@Stateless
public class MetocServiceBean implements MetocService {

	@EJB
	VesselService vesselService;

	@Override
	public MetocForecastResponse getMetoc(MetocForecastRequest request) throws ServiceException {
		// Validate the request
		if (request.getWaypoints() == null || request.getWaypoints().size() == 0) {
			throw new ServiceException(Errorcodes.EMPTY_ROUTE);
		}
		// Filter out waypoints in the past
		Date now = new Date();
		int startIndex;
		for (startIndex = 1; startIndex < request.getWaypoints().size(); startIndex++) {
			MetocForecastRequestWp endWp = request.getWaypoints().get(startIndex);
			if (endWp.getEta() == null) {
				throw new ServiceException(Errorcodes.MISSING_ROUTE_DATA);
			}
			if (endWp.getEta().after(now)) {
				// In the future
				startIndex--;
				break;
			}
		}

		if (startIndex == request.getWaypoints().size() - 1) {
			throw new ServiceException(Errorcodes.ROUTE_IN_PAST);
		}
		for (int i = 0; i < startIndex; i++) {
			request.getWaypoints().remove(0);
		}

		String provider = request.getProvider().toLowerCase();
		MetocInvoker invoker;
		if (provider.equals("dmi")) {
		     invoker = new DmiMetocClient(request);    
		} else if (provider.equals("fco")) {
		    invoker = new MetocHttpInvoker(request);
		} else {
		    invoker = new DmiMetocClient(request);
		}
		
		//invoker = new MetocHttpInvoker(request);
		

		// Make the request
		MetocForecastResponse res;
		try {
			res = invoker.makeRequest();
		} catch (MetocInvokerException e) {
			throw new ServiceException(Errorcodes.METOC_UNAVAILABLE);
		}


		return res;
	}

}
