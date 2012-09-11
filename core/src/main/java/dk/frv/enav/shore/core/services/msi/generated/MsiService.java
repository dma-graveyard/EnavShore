
package dk.frv.enav.shore.core.services.msi.generated;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.1.6 in JDK 6
 * Generated source version: 2.1
 * 
 */
@WebService(name = "MsiService", targetNamespace = "http://message.webservice.core.msiedit.frv.dk/")
@SOAPBinding(style = SOAPBinding.Style.RPC)
@XmlSeeAlso({
    ObjectFactory.class
})
public interface MsiService {


    /**
     * 
     * @return
     *     returns dk.frv.enav.shore.core.services.msi.generated.MsiDtoLightArray
     */
    @WebMethod
    @WebResult(partName = "return")
    public MsiDtoLightArray getActiveWarning();

    /**
     * 
     * @param arg0
     * @return
     *     returns dk.frv.enav.shore.core.services.msi.generated.MsiDtoArray
     */
    @WebMethod
    @WebResult(partName = "return")
    public MsiDtoArray getWarningAfterId(
        @WebParam(name = "arg0", partName = "arg0")
        int arg0);

}
