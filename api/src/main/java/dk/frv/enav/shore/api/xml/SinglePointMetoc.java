package dk.frv.enav.shore.api.xml;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dk.frv.ais.geo.GeoLocation;
import dk.frv.enav.common.xml.Waypoint.Heading;
import dk.frv.enav.common.xml.metoc.MetocForecastPoint;
import dk.frv.enav.common.xml.metoc.request.MetocForecastRequest;
import dk.frv.enav.common.xml.metoc.request.MetocForecastRequestWp;
import dk.frv.enav.common.xml.metoc.response.MetocForecastResponse;
import dk.frv.enav.common.xml.metoc.single.request.SinglePointMetocRequest;
import dk.frv.enav.common.xml.metoc.single.response.SinglePointMetocResponse;
import dk.frv.enav.shore.core.domain.ServiceLog;
import dk.frv.enav.shore.core.services.Errorcodes;
import dk.frv.enav.shore.core.services.ServiceException;
import dk.frv.enav.shore.core.services.log.LogService;
import dk.frv.enav.shore.core.services.metoc.MetocService;

/**
 * request metoc info for a single point.
 * @author rch
 *
 */
public class SinglePointMetoc extends XmlApiServlet {
	
	private static final long serialVersionUID = 1L;
	
	@EJB
	MetocService metocService;
	
	@EJB
	LogService logService;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		SinglePointMetocResponse respXml = new SinglePointMetocResponse();
		respXml.setErrorCode(Errorcodes.OK);
		
		ServiceLog logEntry = logService.createLog("METOC_XML", request);
		
		try {
			SinglePointMetocRequest reqXml = (SinglePointMetocRequest)getRequestXml(request, "dk.frv.enav.common.xml.metoc.single.request", logEntry);
			//GeoLocation pos = reqXml.getPos();
			GeoLocation pos = new GeoLocation(reqXml.getLat(), reqXml.getLon());
			/*
			 * Create a Metoc forecast  request 
			 */
			MetocForecastRequest metocReq = new MetocForecastRequest();
			
			/*
			 * First waypoint is position
			 */
			MetocForecastRequestWp reqWp = new MetocForecastRequestWp();
			reqWp.setEta(new Date());
			reqWp.setHeading(Heading.RL.name());
			reqWp.setLat(pos.getLatitude());
			reqWp.setLon(pos.getLongitude());
			metocReq.getWaypoints().add(reqWp);
			/*
			 *  Second wayPoint is dummy 
			 */
			MetocForecastRequestWp reqWp2 = new MetocForecastRequestWp();
			Calendar cal = new GregorianCalendar();
			cal.add(Calendar.MINUTE, 10);
			reqWp2.setEta(cal.getTime());
			reqWp2.setHeading(Heading.RL.name());
			reqWp2.setLat(pos.getLatitude()+0.001);
			reqWp2.setLon(pos.getLongitude()+0.001);
			metocReq.getWaypoints().add(reqWp2);
			
			metocReq.setDt(15);
			logEntry.setMmsi(-1l);
			
			/*
			 * request service and create response
			 */
			MetocForecastResponse metocResp= metocService.getMetoc(metocReq);
			Iterator<MetocForecastPoint> iter = metocResp.getMetocForecast().getForecasts().iterator();
			
			if(iter.hasNext()){
				respXml.setMetocPoint(iter.next());				
			}
			logEntry.markCompleted();
		} catch (ServiceException e) {
			respXml.setErrorCode(e.getErrorCode());
			respXml.setErrorMessage(e.getExtErrorMsg());
			logEntry.markFailed(e);
		}
		
		sendResponse(response, "dk.frv.enav.common.xml.metoc.single.response", respXml);
		
		logService.endLog(logEntry);
	}

}
