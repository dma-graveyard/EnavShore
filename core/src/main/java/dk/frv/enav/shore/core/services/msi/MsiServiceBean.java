package dk.frv.enav.shore.core.services.msi;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;

import dk.frv.enav.common.xml.msi.MsiLocation;
import dk.frv.enav.common.xml.msi.MsiLocation.LocationType;
import dk.frv.enav.common.xml.msi.MsiMessage;
import dk.frv.enav.common.xml.msi.MsiPoint;
import dk.frv.enav.common.xml.msi.request.MsiPollRequest;
import dk.frv.enav.common.xml.msi.response.MsiResponse;
import dk.frv.enav.shore.core.services.ServiceException;
import dk.frv.enav.shore.core.services.msi.generated.MsiDto;
import dk.frv.enav.shore.core.services.msi.generated.PointDto;
import dk.frv.enav.shore.core.services.msi.generated.WarningService;

@Stateless
public class MsiServiceBean implements MsiService {

	@Override
	public MsiResponse msiPoll(MsiPollRequest msiPollRequest) throws ServiceException {
		MsiResponse msiResponse = new MsiResponse();
		
		/*
		 * call Msi webservice - client generated in core with wsimport (see webservice_client.readme in source root folder)
		 */
		
		List<MsiDto> list = new WarningService().getMsiServiceBeanPort().getWarningAfterId(msiPollRequest.getLastMessage()).getItem();

		/*
		 * convert to enav.common object 
		 */
		List<MsiMessage> msgList = new ArrayList<MsiMessage>();
		for (MsiDto msiDto : list) {
			msgList.add(createFromMsiDto(msiDto));
		}
		msiResponse.setMessages(msgList);
		
		return msiResponse;
	}

	private MsiMessage createFromMsiDto(MsiDto msi) {

		MsiMessage msg = new MsiMessage();
		msg.setCreated(msi.getCreated().getTime());
		msg.setDeleted(msi.getDeleted()==null?null:msi.getDeleted().getTime());
		msg.setUpdated(msi.getUpdated().getTime());
		msg.setEncText(msi.getEncText());
		msg.setId(msi.getId());

		MsiLocation loc = new MsiLocation();
		loc.setArea(msi.getAreaEnglish());
		loc.setLocationType(LocationType.getType(msi.getLocationType()));
		loc.setSubArea(msi.getSubarea());
		List<MsiPoint> msiPoints = new ArrayList<MsiPoint>();
		for (PointDto pt : msi.getPoints().getPoint()) {
			MsiPoint p = new MsiPoint();
			p.setLatitude(pt.getLatitude());
			p.setLongitude(pt.getLongitude());
			msiPoints.add(p);
		}
		loc.setPoints(msiPoints);
		msg.setLocation(loc);

		msg.setMessage(msi.getNavWarning());
		msg.setMessageId(msi.getMessageId());
		msg.setNavtex(msi.getNavtex());
		msg.setNavtexNo(msi.getNavtexNo());
		msg.setOrganisation(msi.getOrganisation());
		msg.setPriority(msi.getPriority());
		
		msg.setUsername(msi.getUsername());
		msg.setValidFrom(msi.getValidFrom().getTime());
		msg.setValidTo(msi.getValidTo()==null?null:msi.getValidTo().getTime());
		msg.setVersion(msi.getVersion());

		return msg;
	}
}
