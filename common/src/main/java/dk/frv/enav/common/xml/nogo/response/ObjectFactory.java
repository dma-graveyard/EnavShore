package dk.frv.enav.common.xml.nogo.response;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {

    public ObjectFactory() {
    }

    public NogoResponse createNogoResponse() {
        return new NogoResponse();
    }
    
}
