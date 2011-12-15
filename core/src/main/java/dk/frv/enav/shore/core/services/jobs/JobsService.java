package dk.frv.enav.shore.core.services.jobs;

import javax.ejb.Remote;

@Remote
public interface JobsService {
	
	/**
	 * Various cleanup routines
	 */
	void cleanup();

}
