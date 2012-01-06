package dk.frv.enav.shore.core.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Date;

@Entity
@Table(name="tide_denmark")
public class TideDenmark implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;
	private double lat;
	private double lon;
	private int n ;
	private int m;
	private Date time;
	private Double depth;

    public TideDenmark() {
    }

	@Id
	@Column(name="id", unique = true, nullable = false, insertable = true, updatable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name="lat", unique = false, nullable = false, insertable = true, updatable = true)
	public double getLat() {
		return this.lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	@Column(name="lon", unique = false, nullable = false, insertable = true, updatable = true)
	public double getLon() {
		return this.lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}	
	
	@Column(name="n", unique = false, nullable = false, insertable = true, updatable = true)
	public int getN() {
		return this.n;
	}

	public void setN(int n) {
		this.n = n;
	}	
	
	@Column(name="m", unique = false, nullable = false, insertable = true, updatable = true)
	public int getM() {
		return this.m;
	}

	public void setM(int m) {
		this.m = m;
	}	
			
	@Column(name="time", unique = false, nullable = false, insertable = true, updatable = true)
	public Date getTime() {
		return this.time;
	}

	public void setTime(Date time) {
		this.time = time;
	}	
	
	@Column(name="depth", unique = false, nullable = true, insertable = true, updatable = true)
	public Double getDepth() {
		return this.depth;
	}

	public void setDepth(Double depth) {
		this.depth = depth;
	}		

}
	