package dk.frv.enav.shore.core.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "points")
public class Points implements Serializable, Comparable<Points> {
	private static final long serialVersionUID = 1L;
	
	private int id;
	private Locations location;
	private int ptnNo;
	private double latitude;
	private double longitude;
	private double radius;
	
	public Points() {
		
	}

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false, insertable = true, updatable = false)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "locationId", unique = false, nullable = false, insertable = true, updatable = true)
	public Locations getLocation() {
		return location;
	}

	public void setLocation(Locations location) {
		this.location = location;
	}

	@Column(name="ptnNo", unique = false, nullable = false, insertable = true, updatable = true)
	public int getPtnNo() {
		return ptnNo;
	}

	public void setPtnNo(int ptnNo) {
		this.ptnNo = ptnNo;
	}

	@Column(name="latitude", unique = false, nullable = false, insertable = true, updatable = true)
	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	@Column(name="longitude", unique = false, nullable = false, insertable = true, updatable = true)
	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	@Column(name="radius", unique = false, nullable = false, insertable = true, updatable = true)
	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}

	@Transient
	@Override
	public int compareTo(Points p2) {
		if (ptnNo == p2.ptnNo) return 0;
		return ((ptnNo < p2.ptnNo) ? -1 : 1);
	}

}
