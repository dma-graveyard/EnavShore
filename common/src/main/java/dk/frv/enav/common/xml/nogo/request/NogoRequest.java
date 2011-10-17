package dk.frv.enav.common.xml.nogo.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import dk.frv.enav.common.xml.Route;
import dk.frv.enav.common.xml.ShoreServiceRequest;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "nogoRequest", propOrder = {
    "route"
})
public class NogoRequest extends ShoreServiceRequest {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Optional vessel route to use for calculating no go 
	 */
	private Route route;
	
	public NogoRequest() {
	}
	
	public Route getRoute() {
		return route;
	}
	
	public void setRoute(Route route) {
		this.route = route;
	}

}
