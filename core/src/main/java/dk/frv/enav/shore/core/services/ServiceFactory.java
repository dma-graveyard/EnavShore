package dk.frv.enav.shore.core.services;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import dk.frv.enav.shore.core.services.msi.MsiService;

public class ServiceFactory {
    
    private static final String JNDI_CONTEXT = "enavshore";
    private static Logger LOG = Logger.getLogger(ServiceFactory.class);
    
    private static ServiceFactory instance;
    
    public ServiceFactory() {       
    }
    
    public static MsiService getMsiService() {
        return (MsiService) getInstance().getService("MsiServiceBean");
    }

    protected static ServiceFactory getInstance() {
        if (instance == null) {
            instance = new ServiceFactory();
        }
        return instance;
    }

    protected Object getService(String name) {
        return getService(name, false);
    }

    private Object getService(String name, boolean remote) {
        try {
            String jndiName = JNDI_CONTEXT + "/" + name + (remote ? "/remote" : "/local");
            Context ctx = new InitialContext();
            return ctx.lookup(jndiName);
        } catch (NamingException e) {
            e.printStackTrace();
            LOG.error("Could not get service with name " + name);
            return null;
        }
    }

}
