package dk.frv.enav.shore.core.webservices;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

import org.apache.log4j.Logger;
import org.jboss.wsf.spi.annotation.WebContext;

@WebService(serviceName = "ExampleService", targetNamespace = "http://enav.frv.dk/api/ws/example")
@SOAPBinding(style = SOAPBinding.Style.RPC)
@WebContext(contextRoot = "/api/ws", urlPattern = "/example")
@Stateless
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class ExampleWS {

	private static final Logger LOG = Logger.getLogger(ExampleWS.class);
	
	@WebMethod
	public String helloWorld(
			@WebParam(name = "mmsi")
			String mmsi,
			@WebParam(name = "message")
			String message			
			) {
		LOG.debug("Example WS called with arguments mmsi=" + mmsi + " message=" + message);
		
		// See soapUI for WS testing
		// See: http://localhost:8080/jbossws/services for list of services installed
		
		return "Hello world";
	}
	
}
