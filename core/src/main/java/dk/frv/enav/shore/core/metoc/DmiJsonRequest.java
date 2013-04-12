package dk.frv.enav.shore.core.metoc;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import dk.frv.enav.common.util.DateUtils;
import dk.frv.enav.common.xml.metoc.MetocDataTypes;
import dk.frv.enav.common.xml.metoc.request.MetocForecastRequest;
import dk.frv.enav.common.xml.metoc.request.MetocForecastRequestWp;

public class DmiJsonRequest {
    //fields are used by GSON
    @SuppressWarnings("unused")
    private Long mmsi;
    private List<String> datatypes;
    @SuppressWarnings("unused")
    private Integer dt;
    private List<JsonWaypoint> waypoints;
    @SuppressWarnings("unused")
    private String heading;

    /*
     * Static conversion from DMA-style to DMI-style
     */
    private static final Map<String, String> metocToDmiType;
    static {
        final Map<String, String> aMap = new HashMap<String,String>();
        aMap.put("WA", "wave");
        aMap.put("WI", "wind");
        aMap.put("DE", "density");
        aMap.put("SE", "sealevel");
        aMap.put("CU", "current");
        metocToDmiType = Collections.unmodifiableMap(aMap);
    } 
    
    
    public DmiJsonRequest(MetocForecastRequest mfr) {
        mmsi = mfr.getMmsi();
        datatypes = new LinkedList<String>();
        dt = -1;
        waypoints = new LinkedList<JsonWaypoint>();
         
        for (MetocDataTypes metocType: mfr.getDataTypes()) {
            this.datatypes.add(metocTypeToString(metocType));
        }
        
        dt = mfr.getDt();
        
        for ( MetocForecastRequestWp wp: mfr.getWaypoints()) {
            String eta = DateUtils.getISO8620SSSZZ(wp.getEta());
            
            String heading = wp.getHeading();
            JsonWaypoint jsonwp = new JsonWaypoint(eta, heading, wp.getLat(), wp.getLon());
            waypoints.add(jsonwp);
        }
                
                
    }
    
    public String metocTypeToString(MetocDataTypes dataType) {
        return metocToDmiType.get(dataType.toString());
    }
    
    /*@Override 
    public String toString() {
        
    }*/
}
