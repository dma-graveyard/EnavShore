package dk.frv.enav.shore.core.metoc;


public class JsonWaypoint {
    public String eta;
    public String heading;
    public Double lat;
    public Double lon;
    
    
    JsonWaypoint(String eta, String heading, Double lat, Double lon) {
        this.eta = eta;
        this.heading = heading;
        this.lat = lat;
        this.lon = lon;
    }
    
 }