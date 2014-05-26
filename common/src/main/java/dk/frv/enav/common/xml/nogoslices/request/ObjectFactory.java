package dk.frv.enav.common.xml.nogoslices.request;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {

    public ObjectFactory() {
    }

    public NogoRequestSlices createNogoRequest() {
        return new NogoRequestSlices();
    }

}
