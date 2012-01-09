package dk.frv.enav.shore.api.xml;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dk.frv.enav.common.xml.risk.request.RiskRequest;
import dk.frv.enav.common.xml.risk.response.RiskResponse;
import dk.frv.enav.shore.core.domain.ServiceLog;
import dk.frv.enav.shore.core.services.Errorcodes;
import dk.frv.enav.shore.core.services.ServiceException;
import dk.frv.enav.shore.core.services.log.LogService;
import dk.frv.enav.shore.core.services.risk.RiskIndexService;

public class RiskIndexServlet extends XmlApiServlet {
	
	private static final long serialVersionUID = 1L;
	
	@EJB
	RiskIndexService riskService;
	
	@EJB
	LogService logService;

	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RiskResponse resXml = new RiskResponse();
		resXml.setErrorCode(Errorcodes.OK);
		
		ServiceLog logEntry = logService.createLog("RISK_XML", request);
		
		try {
			RiskRequest reqXml = (RiskRequest)getRequestXml(request, "dk.frv.enav.common.xml.risk.request", logEntry);
			//logEntry.setMmsi(reqXml.getMmsi());
			 resXml= riskService.getRiskIndexes(reqXml);
			 logEntry.markCompleted();
		} catch (ServiceException e) {
			resXml.setErrorCode(e.getErrorCode());
			resXml.setErrorMessage(e.getExtErrorMsg());
			logEntry.markFailed(e);
		}
		
		sendResponse(response, "dk.frv.enav.common.xml.risk.response", resXml);
		
		logService.endLog(logEntry);
	}

}
