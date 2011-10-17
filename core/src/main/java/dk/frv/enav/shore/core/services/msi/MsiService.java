package dk.frv.enav.shore.core.services.msi;

import javax.ejb.Local;

import dk.frv.enav.common.xml.msi.request.MsiPollRequest;
import dk.frv.enav.common.xml.msi.response.MsiResponse;
import dk.frv.enav.shore.core.services.ServiceException;

@Local
public interface MsiService {
	
	/**
	 * Get MSI response from poll request
	 * @param msiPollRequest
	 * @return
	 */
	MsiResponse msiPoll(MsiPollRequest msiPollRequest) throws ServiceException;
	
}
