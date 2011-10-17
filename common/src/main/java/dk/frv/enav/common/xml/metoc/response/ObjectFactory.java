package dk.frv.enav.common.xml.metoc.response;

import javax.xml.bind.annotation.XmlRegistry;

import dk.frv.enav.common.xml.metoc.response.MetocForecastResponse;

@XmlRegistry
public class ObjectFactory {

    public ObjectFactory() {
    }
    
    public MetocForecastResponse createMetocForecastResponse() {
        return new MetocForecastResponse();
    }

}
