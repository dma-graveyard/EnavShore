package dk.frv.enav.shore.core.services.nogo;

import javax.ejb.Local;

import dk.frv.enav.common.xml.nogo.request.NogoRequest;
import dk.frv.enav.common.xml.nogo.response.NogoResponse;
import dk.frv.enav.common.xml.nogo.types.BoundingBoxPoint;
import dk.frv.enav.common.xml.nogo.types.NogoPolygon;
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

	
	int getDataCount();
	
	void getData();
	
	BoundingBoxPoint getArea(double lat1, double lon1);
	
	NogoPolygon getNogoArea(BoundingBoxPoint point1, BoundingBoxPoint point2);
}
