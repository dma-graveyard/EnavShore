package dk.frv.enav.shore.core.webservices;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import org.jboss.wsf.spi.annotation.WebContext;

import dk.frv.enav.shore.core.services.ais.AisRequest;
import dk.frv.enav.shore.core.services.ais.AisService;
import dk.frv.enav.shore.core.services.ais.PublicAisTarget;

@WebService(serviceName = "AisService", targetNamespace = "http://enav.frv.dk/api/ws/ais")
@SOAPBinding(style = SOAPBinding.Style.RPC)
@WebContext(contextRoot = "/api/ws", urlPattern = "/ais")
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class AisWS {
	
	@EJB
	AisService aisService;
	
	@WebMethod
	public int getVesselCount(){
		return aisService.getVesselCount();
	}
	
	@WebMethod
	public List<PublicAisTarget> getTargetsInArea(double toplat, double toplon, double bottomlat, double bottomlon) {
		AisRequest aisRequest = new AisRequest(toplat, toplon, bottomlat, bottomlon);
		return aisService.getPublicAisTargets(aisRequest);
	}
	
	public List<PublicAisTarget> getTargetsInAreaPojo(@WebParam(name="aisRequest") AisRequest aisRequest) {
		return aisService.getPublicAisTargets(aisRequest);
	}
}
