package dk.frv.enav.common.xml.msi.response;

import javax.xml.bind.annotation.XmlRegistry;

@XmlRegistry
public class ObjectFactory {

	public ObjectFactory() {
    }

    public MsiResponse createMsiResponse() {
        return new MsiResponse();
    }
    
}
