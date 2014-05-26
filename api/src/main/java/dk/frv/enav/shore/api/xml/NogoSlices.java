package dk.frv.enav.shore.api.xml;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dk.frv.enav.common.xml.nogoslices.request.NogoRequestSlices;
import dk.frv.enav.common.xml.nogoslices.response.NogoResponseSlices;
import dk.frv.enav.shore.core.domain.ServiceLog;
import dk.frv.enav.shore.core.services.Errorcodes;
import dk.frv.enav.shore.core.services.ServiceException;
import dk.frv.enav.shore.core.services.log.LogService;
import dk.frv.enav.shore.core.services.nogo.NogoService;

public class NogoSlices extends XmlApiServlet {
	
	private static final long serialVersionUID = 1L;
	
	@EJB
	LogService logService;

	@EJB
	NogoService nogoService;
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		NogoResponseSlices resXml = new NogoResponseSlices();
		resXml.setErrorCode(Errorcodes.OK);
		
		ServiceLog logEntry = logService.createLog("NOGO_XML", request);
		
		try {
			NogoRequestSlices reqXml = (NogoRequestSlices)getRequestXml(request, "dk.frv.enav.common.xml.nogoslices.request", logEntry);
			logEntry.setMmsi(reqXml.getMmsi());
			resXml = nogoService.nogoPoll(reqXml);
			logEntry.markCompleted();
		} catch (ServiceException e) {
			resXml.setErrorCode(e.getErrorCode());
			resXml.setErrorMessage(e.getExtErrorMsg());
			logEntry.markFailed(e);
		}
		
		sendResponse(response, "dk.frv.enav.common.xml.nogoslices.response", resXml);
		
		logService.endLog(logEntry);
	}
		
}
