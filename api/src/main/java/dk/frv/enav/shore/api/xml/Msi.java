package dk.frv.enav.shore.api.xml;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dk.frv.enav.common.xml.msi.request.MsiPollRequest;
import dk.frv.enav.common.xml.msi.response.MsiResponse;
import dk.frv.enav.shore.core.domain.ServiceLog;
import dk.frv.enav.shore.core.services.Errorcodes;
import dk.frv.enav.shore.core.services.ServiceException;
import dk.frv.enav.shore.core.services.log.LogService;
import dk.frv.enav.shore.core.services.msi.MsiService;

public class Msi extends XmlApiServlet {
	
	private static final long serialVersionUID = 1L;
	
	@EJB
	MsiService msiService;
	
	@EJB
	LogService logService;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		MsiResponse resXml = new MsiResponse();
		resXml.setErrorCode(Errorcodes.OK);
		
		ServiceLog logEntry = logService.createLog("MSI_XML", request);
		
		try {
			MsiPollRequest reqXml = (MsiPollRequest)getRequestXml(request, "dk.frv.enav.common.xml.msi.request", logEntry);
			logEntry.setMmsi(reqXml.getMmsi());
			resXml = msiService.msiPoll(reqXml);
			logEntry.markCompleted();
		} catch (ServiceException e) {
			resXml.setErrorCode(e.getErrorCode());
			resXml.setErrorMessage(e.getExtErrorMsg());
			logEntry.markFailed(e);
		}
		
		sendResponse(response, "dk.frv.enav.common.xml.msi.response", resXml);
		
		logService.endLog(logEntry);
	}

}
