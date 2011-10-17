package dk.frv.enav.common.xml.nogo.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import dk.frv.enav.common.xml.ShoreServiceResponse;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "nogoResponse", propOrder = {
    "nogoShape"
})
public class NogoResponse extends ShoreServiceResponse {
	private static final long serialVersionUID = 1L;
	
	/**
	 * TODO Some form of representation of no go
	 */
	private String nogoShape;
	
	public NogoResponse() {
	}
	
	public String getNogoShape() {
		return nogoShape;
	}
	
	public void setNogoShape(String nogoShape) {
		this.nogoShape = nogoShape;
	}

}
