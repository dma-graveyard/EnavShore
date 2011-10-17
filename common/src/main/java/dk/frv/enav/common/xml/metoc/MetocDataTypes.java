package dk.frv.enav.common.xml.metoc;

import org.apache.commons.lang.StringUtils;

/**
 * Enumeration describing the different METOC data types
 * 
 */
public enum MetocDataTypes {
	WI, CU, WA, SE, DE;
	
	public static MetocDataTypes parseType(String type) throws Exception {
		if (type.equalsIgnoreCase("WI")) {
			return WI;
		} else if (type.equalsIgnoreCase("CU")) {
			return CU;
		} else if (type.equalsIgnoreCase("WA")) {
			return WA;
		} else if (type.equalsIgnoreCase("SE")) {
			return SE;
		} else if (type.equalsIgnoreCase("DE")) {
			return DE;
		} else {
			throw new Exception("Unknown METOC type " + type);
		}
	}
	
	public static MetocDataTypes[] parseTypes(String data) throws Exception {
		String[] elements = StringUtils.split(data, ',');
		MetocDataTypes[] types = new MetocDataTypes[elements.length];
		for (int i=0; i < elements.length; i++) {
			types[i] = parseType(elements[i]);
		}
		return types;
	}
	
	public static MetocDataTypes[] allTypes() {
		return MetocDataTypes.values();
	}

}