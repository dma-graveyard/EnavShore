package dk.frv.enav.shore.core.services.msi;

import javax.ejb.Stateless;

import dk.frv.enav.common.xml.msi.request.MsiPollRequest;
import dk.frv.enav.common.xml.msi.response.MsiResponse;
import dk.frv.enav.shore.core.services.RemoteServiceFactory;
import dk.frv.enav.shore.core.services.ServiceException;
import dk.frv.msiedit.core.services.message.MessageService;

@Stateless
public class MsiServiceBean implements MsiService {
	
	@Override
	public MsiResponse msiPoll(MsiPollRequest msiPollRequest) throws ServiceException {
		MsiResponse msiResponse = new MsiResponse();
		MessageService messageService = RemoteServiceFactory.getMessageService();
		msiResponse.setMessages(messageService.messagePoll(msiPollRequest.getLastMessage()));
		return msiResponse;
	}
	
}
