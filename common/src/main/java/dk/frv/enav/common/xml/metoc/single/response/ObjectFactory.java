package dk.frv.enav.common.xml.metoc.single.response;

import javax.xml.bind.annotation.XmlRegistry;

import dk.frv.enav.common.xml.metoc.response.MetocForecastResponse;

@XmlRegistry
public class ObjectFactory {

    public ObjectFactory() {
    }
    
    public SinglePointMetocResponse createSinglePointMetocResponse() {
        return new SinglePointMetocResponse();
    }

}
