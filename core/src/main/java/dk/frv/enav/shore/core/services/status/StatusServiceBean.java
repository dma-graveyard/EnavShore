package dk.frv.enav.shore.core.services.status;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import dk.frv.enav.shore.core.domain.ServiceLog;
import dk.frv.enav.shore.core.services.log.LogService;

@Stateless
public class StatusServiceBean implements StatusService {
	
	private static final int LOOKBACK_COUNT = 3;
	
	@EJB
	LogService logService;

	@Override
	public Map<String, Status> fullStatus() {
		Map<String, Status> status = new HashMap<String, Status>();
				
		// Determine status of XML services
		String[] services = {"MSI_XML", "METOC_XML"};
		for (String service : services) {
			status.put(service, serviceOk(service));
		}
		
		// Determine overall status
		Status overall = Status.OK;
		for (String service : status.keySet()) {
			if (status.get(service) == Status.ERROR) {
				overall = Status.ERROR;
				break;
			}		
		}
		status.put("OVERALL", overall);
		
		return status;				
	}
	
	private Status serviceOk(String name) {
		Status status = Status.ERROR;
		List<ServiceLog> logs = logService.getLast(name, LOOKBACK_COUNT);
		for (ServiceLog serviceLog : logs) {
			if (serviceLog.getStatus() == ServiceLog.Status.COMPLETED) {
				return Status.OK;
			}
		}
		return status;
	}

}
