package dk.frv.enav.shore.core.services.nogo;

import java.util.Date;
import java.util.List;

import javax.ejb.Local;

import dk.frv.ais.geo.GeoLocation;
import dk.frv.enav.common.xml.nogo.request.NogoRequest;
import dk.frv.enav.common.xml.nogo.response.NogoResponse;
import dk.frv.enav.common.xml.nogo.types.NogoPolygon;
import dk.frv.enav.common.xml.nogoslices.request.NogoRequestSlices;
import dk.frv.enav.common.xml.nogoslices.response.NogoResponseSlices;
import dk.frv.enav.shore.core.domain.DepthDenmark;
import dk.frv.enav.shore.core.domain.TideDenmark;
import dk.frv.enav.shore.core.services.ServiceException;

@Local
public interface NogoService {

    /**
     * Calculate no go area from no go request
     * 
     * @param nogoRequest
     * @return
     * @throws ServiceException
     */
    NogoResponse nogoPoll(NogoRequest nogoRequest) throws ServiceException;

    /**
     * Calculate no go area from no go request
     * 
     * @param nogoRequest
     * @return
     * @throws ServiceException
     */
    NogoResponseSlices nogoPoll(NogoRequestSlices nogoRequest) throws ServiceException;

    public List<NogoPolygon> parseResult(List<DepthDenmark> result, List<TideDenmark> resultTide, double depth);

    void createBathymetryWorkers(GeoLocation northWest, GeoLocation southEast);

    void createTidalWorkers(GeoLocation northWest, GeoLocation southEast, Date timeStart, Date TimeEnd);

    void getDataRegion(GeoLocation northWest, GeoLocation southEast);

}
