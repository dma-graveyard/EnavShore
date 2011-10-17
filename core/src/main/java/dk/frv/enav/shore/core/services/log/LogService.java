package dk.frv.enav.shore.core.services.log;

import java.util.List;

import javax.ejb.Local;
import javax.servlet.http.HttpServletRequest;

import dk.frv.enav.shore.core.domain.ServiceLog;

@Local
public interface LogService {
	
	/**
	 * Create a new log entry
	 *  
	 * @param service name
	 * @param servlet request
	 * @return
	 */
	ServiceLog createLog(String serviceName, HttpServletRequest request);
	
	/**
	 * End the log entry
	 * 
	 * @param entry
	 */
	void endLog(ServiceLog entry);
	
	/**
	 * Get the last count logs for service with service name
	 * @param serviceName
	 * @param count
	 * @return
	 */
	List<ServiceLog> getLast(String serviceName, int count);

}
