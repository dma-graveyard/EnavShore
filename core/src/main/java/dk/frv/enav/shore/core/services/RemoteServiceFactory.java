package dk.frv.enav.shore.core.services;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import dk.frv.msiedit.core.services.message.MessageService;

public class RemoteServiceFactory {

	private static RemoteServiceFactory instance;
	private InitialContext context;

	private RemoteServiceFactory() {
		Hashtable<String, String> env = new Hashtable<String, String>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
		env.put(Context.PROVIDER_URL, "localhost:1099");
		env.put(Context.URL_PKG_PREFIXES, "org.jboss.naming:org.jnp.interfaces");
		try {
			context = new InitialContext(env);
		} catch (NamingException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public static MessageService getMessageService() {
		return (MessageService) getInstance().getService("msiEAR/MessageServiceBean/remote");
	}

	private Object getService(String name) {
		try {
			return context.lookup(name);
		} catch (NamingException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	private static RemoteServiceFactory getInstance() {
		if (instance == null) {
			instance = new RemoteServiceFactory();
		}
		return instance;
	}

}
