package dk.frv.enav.common.xml.metoc.request;

import javax.xml.bind.annotation.XmlRegistry;

import dk.frv.enav.common.xml.metoc.request.MetocForecastRequest;

@XmlRegistry
public class ObjectFactory {

    public ObjectFactory() {
    }

    public MetocForecastRequest createMetocForecastRequest() {
        return new MetocForecastRequest();
    }
    
    public MetocForecastRequestWp createMetocForecastRequestWp() {
        return new MetocForecastRequestWp();
    }
    
}
