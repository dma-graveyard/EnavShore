package dk.frv.enav.common.xml.msi.response;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import dk.frv.enav.common.xml.ShoreServiceResponse;
import dk.frv.enav.common.xml.msi.MsiMessage;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "msiResponse", propOrder = {})
public class MsiResponse extends ShoreServiceResponse {
	private static final long serialVersionUID = 1L;
	
	private List<MsiMessage> messages = null;
	
	public MsiResponse() {
		
	}

	public List<MsiMessage> getMessages() {
		return messages;
	}
	
	public void setMessages(List<MsiMessage> messages) {
		this.messages = messages;
	}
	
}
