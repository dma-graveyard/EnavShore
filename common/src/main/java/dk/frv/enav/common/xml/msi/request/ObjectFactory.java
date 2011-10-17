package dk.frv.enav.common.xml.msi.request;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {

    public ObjectFactory() {
    }

    public MsiPollRequest createMsiPollRequest() {
        return new MsiPollRequest();
    }
    
}
