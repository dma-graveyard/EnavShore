package dk.frv.enav.common.jboss;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.log4j.Logger;


public class JbossProperties {

	private static JbossProperties instance = null;
	private static Logger LOG = Logger.getLogger(JbossProperties.class);

	private java.util.Properties properties;

	public static JbossProperties getInstance() {
        if (instance == null) {
            synchronized (JbossProperties.class) {
                try {
                	instance = new JbossProperties();                	
                } catch (IOException e) {
                	LOG.error("Failed to load properties: " + e);
				}
            }
        }
        return instance;
	}
	
	public static String get(String key, String defaultVal) {
        JbossProperties props = getInstance();
        if (props == null) {
            return defaultVal;
        }
        if (props.properties == null) {
            return defaultVal;
        }
        return props.properties.getProperty(key, defaultVal);
    }
	
	public static String get(String key) {
		return get(key, null);
	}

	private JbossProperties() throws IOException {
		// Load property file
		// Try ${user.home}/.enavshore.xml first, then
		// ${jboss.server.home.dir}/conf/enavshore.xml
		String localFilename = System.getProperty("user.home") + "/.enavshore.xml";
		String masterFilename = System.getProperty("jboss.server.home.dir") + "/conf/enavshore.xml";
		String filename = null;
		LOG.info("Looking for local properties file: " + localFilename);
		File f = new File(localFilename);
		if (f.exists()) {
			filename = localFilename;
		} else {
			filename = masterFilename;
		}
		LOG.info("Creating new JbossProperties instance from property file: " + filename);
		FileInputStream fis = new FileInputStream(filename);
		properties = new java.util.Properties();
		properties.loadFromXML(fis);
	}

}
