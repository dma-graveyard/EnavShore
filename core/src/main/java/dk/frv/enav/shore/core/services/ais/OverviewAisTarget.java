package dk.frv.enav.shore.core.services.ais;

public class OverviewAisTarget {
	
	private long id;
	private int cog;
	private double lat;
	private double lon;
	private String vc;
	private int vt;
	
	public OverviewAisTarget() {
		
	}

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public int getCog() {
		return cog;
	}

	public void setCog(int cog) {
		this.cog = cog;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public String getVc() {
		return vc;
	}

	public void setVc(String vc) {
		this.vc = vc;
	}

	public int getVt() {
		return vt;
	}

	public void setVt(int vt) {
		this.vt = vt;
	}
	
}
