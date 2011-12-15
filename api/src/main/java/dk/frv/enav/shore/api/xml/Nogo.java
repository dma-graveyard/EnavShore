package dk.frv.enav.shore.api.xml;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dk.frv.enav.common.xml.nogo.request.NogoRequest;
import dk.frv.enav.common.xml.nogo.response.NogoResponse;
import dk.frv.enav.shore.core.domain.ServiceLog;
import dk.frv.enav.shore.core.services.Errorcodes;
import dk.frv.enav.shore.core.services.ServiceException;
import dk.frv.enav.shore.core.services.log.LogService;
import dk.frv.enav.shore.core.services.nogo.NogoService;

public class Nogo extends XmlApiServlet {
	
	private static final long serialVersionUID = 1L;
	
	@EJB
	LogService logService;

	@EJB
	NogoService nogoService;
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		NogoResponse resXml = new NogoResponse();
		resXml.setErrorCode(Errorcodes.OK);
		
		ServiceLog logEntry = logService.createLog("NOGO_XML", request);
		
		try {
			NogoRequest reqXml = (NogoRequest)getRequestXml(request, "dk.frv.enav.common.xml.nogo.request", logEntry);
			logEntry.setMmsi(reqXml.getMmsi());
			resXml = nogoService.makeNogo(reqXml);
			logEntry.markCompleted();
		} catch (ServiceException e) {
			resXml.setErrorCode(e.getErrorCode());
			resXml.setErrorMessage(e.getExtErrorMsg());
			logEntry.markFailed(e);
		}
		
		sendResponse(response, "dk.frv.enav.common.xml.nogo.response", resXml);
		
		logService.endLog(logEntry);
	}
		
}
