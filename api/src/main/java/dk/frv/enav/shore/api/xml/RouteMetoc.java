package dk.frv.enav.shore.api.xml;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dk.frv.enav.common.xml.metoc.request.MetocForecastRequest;
import dk.frv.enav.common.xml.metoc.response.MetocForecastResponse;
import dk.frv.enav.shore.core.domain.ServiceLog;
import dk.frv.enav.shore.core.services.Errorcodes;
import dk.frv.enav.shore.core.services.ServiceException;
import dk.frv.enav.shore.core.services.log.LogService;
import dk.frv.enav.shore.core.services.metoc.MetocService;

public class RouteMetoc extends XmlApiServlet {
	
	private static final long serialVersionUID = 1L;
	
	@EJB
	MetocService metocService;
	
	@EJB
	LogService logService;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		MetocForecastResponse resXml = new MetocForecastResponse();
		resXml.setErrorCode(Errorcodes.OK);
		
		ServiceLog logEntry = logService.createLog("METOC_XML", request);
		
		try {
			MetocForecastRequest reqXml = (MetocForecastRequest)getRequestXml(request, "dk.frv.enav.common.xml.metoc.request", logEntry);
			logEntry.setMmsi(reqXml.getMmsi());
			resXml = metocService.getMetoc(reqXml);
			logEntry.markCompleted();
		} catch (ServiceException e) {
			resXml.setErrorCode(e.getErrorCode());
			resXml.setErrorMessage(e.getExtErrorMsg());
			logEntry.markFailed(e);
		}
		
		sendResponse(response, "dk.frv.enav.common.xml.metoc.response", resXml);
		
		logService.endLog(logEntry);
	}

}
