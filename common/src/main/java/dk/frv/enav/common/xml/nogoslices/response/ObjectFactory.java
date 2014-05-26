package dk.frv.enav.common.xml.nogoslices.response;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {

    public ObjectFactory() {
    }

    public NogoResponseSlices createNogoResponse() {
        return new NogoResponseSlices();
    }
    
}
