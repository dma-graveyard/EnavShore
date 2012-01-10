package dk.frv.enav.common.xml.metoc.single.request;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {

    public ObjectFactory() {
    }

    public SinglePointMetocRequest createSinglePointMetocRequest() {
        return new SinglePointMetocRequest();
    }
    
}
