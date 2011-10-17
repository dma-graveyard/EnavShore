package dk.frv.enav.common.xml.nogo.request;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {

    public ObjectFactory() {
    }

    public NogoRequest createNogoRequest() {
        return new NogoRequest();
    }
    
}
