package dk.frv.enav.shore.core.services.status;

import java.util.Map;

import javax.ejb.Local;

@Local
public interface StatusService {
	
	public enum Status {
		OK, ERROR, UNKNOWN
	}
	
	/**
	 * Get a map of service descriptors and status OK/FAIL
	 * @return
	 */
	Map<String, Status> fullStatus(); 

}
