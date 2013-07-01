package dk.frv.enav.shore.core.metoc;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.Gson;

import dk.frv.enav.common.util.DateUtils;
import dk.frv.enav.common.xml.metoc.MetocForecast;
import dk.frv.enav.common.xml.metoc.MetocForecastPoint;
import dk.frv.enav.common.xml.metoc.MetocForecastTriplet;
import dk.frv.enav.common.xml.metoc.response.MetocForecastResponse;


public class DmiJsonResponse {
    class DmiMetocForecast {
        public String created = "0";
        public List<DmiForecastPoint> forecasts = new LinkedList<>();
        
    }
    
    private Integer error = 0;
    private String errorMsg = "";
    
    private DmiMetocForecast metocForecast = new DmiMetocForecast();
    
    public List<DmiForecastPoint> getForecasts() {
        if (metocForecast != null) {
            return metocForecast.forecasts;
        } 
        
        return new LinkedList<DmiForecastPoint>();
        
    }
    
    public Date getCreated() {
        try {
            return DateUtils.getISO8620SSSZZ(metocForecast.created);
        } catch (NullPointerException e) {
            return new Date(0);
        }
    }

    public Integer getError() {
        return error;
    }
    
    public String getErrorMsg() {
        return errorMsg;
    }
    
    public static MetocForecastResponse metocFromJson(String jsonString) {
        Gson gson = new Gson();
        
        DmiJsonResponse dmiJsonResponse = gson.fromJson(jsonString, DmiJsonResponse.class);
        
        MetocForecast returnedMetocForecast = new MetocForecast();
        
        returnedMetocForecast.setCreated(dmiJsonResponse.getCreated());
        
        List<MetocForecastPoint> forecasts = new LinkedList<MetocForecastPoint>();
        for (DmiForecastPoint dfcp: dmiJsonResponse.getForecasts()) {
            MetocForecastPoint mfp = new MetocForecastPoint();
            mfp.setLat(dfcp.getLat());
            mfp.setLon(dfcp.getLon());
            
            if (dfcp.getCurrentDir() != null) {
                mfp.setCurrentDirection(new MetocForecastTriplet(dfcp.getCurrentDir().getValue()));    
            }
            if (dfcp.getCurrentSpeed() != null) {
                mfp.setCurrentSpeed(new MetocForecastTriplet(dfcp.getCurrentSpeed().getValue()));
            } 
            if (dfcp.getWindSpeed() != null) {
                mfp.setWindSpeed(new MetocForecastTriplet(dfcp.getWindSpeed().getValue()));
            } 
            if (dfcp.getWindDir() != null) {
                mfp.setWindDirection(new MetocForecastTriplet(dfcp.getWindDir().getValue()));
            } 
            if (dfcp.getWaveDir() != null) {
                mfp.setMeanWaveDirection(new MetocForecastTriplet(dfcp.getWaveDir().getValue()));
            }
            if (dfcp.getWaveHeight() != null) {
                mfp.setMeanWaveHeight(new MetocForecastTriplet(dfcp.getWaveHeight().getValue()));
            }
            if (dfcp.getWavePeriod() != null) {
                mfp.setMeanWavePeriod(new MetocForecastTriplet(dfcp.getWavePeriod().getValue()));
            }
            if (dfcp.getSealevel() != null) {
                mfp.setSeaLevel(new MetocForecastTriplet(dfcp.getSealevel().getValue()));
            }
            if (dfcp.getDensity() != null) {
                mfp.setDensity(new MetocForecastTriplet(dfcp.getDensity().getValue()));
            } else {
                mfp.setDensity(new MetocForecastTriplet(1000.0));
            }
            
            mfp.setTime(dfcp.getTime());
            
            //2 hours later
            /*
            Calendar cal = Calendar.getInstance();
            cal.setTime(dfcp.getTime());
            cal.add(Calendar.HOUR,2);
            
            mfp.setExpires(cal.getTime());
            */
            forecasts.add(mfp);
        }
        
        returnedMetocForecast.setForecasts(forecasts);
        
        MetocForecastResponse metocForecastResponse = new MetocForecastResponse();
        metocForecastResponse.setMetocForecast(returnedMetocForecast);
        metocForecastResponse.setErrorCode(dmiJsonResponse.getError());
        metocForecastResponse.setErrorMessage(dmiJsonResponse.getErrorMsg());
        
        return metocForecastResponse;
    }
    

}
