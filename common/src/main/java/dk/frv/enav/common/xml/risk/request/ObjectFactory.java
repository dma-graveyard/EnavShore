package dk.frv.enav.common.xml.risk.request;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {

    public ObjectFactory() {
    }

    public RiskRequest createRiskRequest() {
        return new RiskRequest();
    }
    
}
