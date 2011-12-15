package dk.frv.enav.common.xml.risk.response;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {

    public ObjectFactory() {
    }

    public RiskResponse createRiskResponse() {
        return new RiskResponse();
    }
    
}
