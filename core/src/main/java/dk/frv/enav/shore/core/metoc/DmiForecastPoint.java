package dk.frv.enav.shore.core.metoc;

import java.util.Date;

import com.google.gson.annotations.SerializedName;

import dk.frv.enav.common.util.DateUtils;

public class DmiForecastPoint {
    private String time;
    private Double lat;
    private Double lon;
    
    
    public static class Forecast {
        private Double forecast;
        
        public Double getValue() {
            return forecast;
        }
    }
    
    @SerializedName("current-dir")
    private Forecast currentDir = null;
    
    @SerializedName("current-speed")
    private Forecast currentSpeed = null;
    
    @SerializedName("wind-dir")
    private Forecast windDir = null;
    
    @SerializedName("wind-speed")
    private Forecast windSpeed = null;
    
    @SerializedName("wave-dir")
    private Forecast waveDir = null;
    
    @SerializedName("wave-height")
    private Forecast waveHeight = null;
    
    @SerializedName("wave-period")
    private Forecast wavePeriod = null;
    
    @SerializedName("sealevel")
    private Forecast sealevel = null;
    
    @SerializedName("density")
    private Forecast density = null;
            
    public Double getLat() {
        return lat;
    }
    
    public Double getLon() {
        return lon;
    }
    
    public Forecast getCurrentDir() {
        return currentDir;
    }
    
    public Forecast getCurrentSpeed() {
        return currentSpeed;
    }
    
    public Forecast getWindSpeed() {
        return windSpeed;
    }
    
    public Forecast getWindDir() {
        return windDir;
    }
    
    public Forecast getWaveDir() {
        return waveDir;
    }
    
    public Forecast getWaveHeight() {
        return waveHeight;
    }
    
    public Forecast getWavePeriod() {
        return wavePeriod;
    }
    
    public Forecast getSealevel() {
        return sealevel;
    }
    
    public Forecast getDensity() {
        return density;
    }
       
    public Date getTime() {
        return DateUtils.getISO8620SSSZZ(time);
    }    
}