package dk.frv.enav.common.xml.msi.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import dk.frv.enav.common.xml.ShoreServiceRequest;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "msiPollRequest", propOrder = {})
public class MsiPollRequest extends ShoreServiceRequest {
	private static final long serialVersionUID = 1L;
	
	/**
	 * The incremental ID of the last received message
	 */
	private int lastMessage;
	
	public MsiPollRequest() {
		
	}
	
	public int getLastMessage() {
		return lastMessage;
	}
	
	public void setLastMessage(int lastMessage) {
		this.lastMessage = lastMessage;
	}

}
