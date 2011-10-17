package dk.frv.enav.shore.core.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="vessel_position")
public class VesselPosition implements Serializable {
	private static final long serialVersionUID = 1L;

	private int vesselId;
	private double cog;
	private double heading;
	private double latitude;
	private double longitude;
	private int navStatus;
	private byte posAccuracy;
	private double rot;
	private double sog;
	private Date updated;
	private Vessel vessel;

    public VesselPosition() {
    }

	@Id
	@Column(name="vessel", unique = true, nullable = false, insertable = true, updatable = false)
	public int getVesselId() {
		return this.vesselId;
	}

	public void setVesselId(int vesselId) {
		this.vesselId = vesselId;
	}
	
	@OneToOne
	@JoinColumn(name = "vessel")
	public Vessel getVessel() {
		return this.vessel;
	}

	public void setVessel(Vessel vessel) {
		this.vessel = vessel;
	}

	@Column(name="cog", unique = false, nullable = true, insertable = true, updatable = true)
	public double getCog() {
		return this.cog;
	}

	public void setCog(double cog) {
		this.cog = cog;
	}

	@Column(name="heading", unique = false, nullable = true, insertable = true, updatable = true)
	public double getHeading() {
		return this.heading;
	}

	public void setHeading(double heading) {
		this.heading = heading;
	}

	@Column(name="latitude", unique = false, nullable = true, insertable = true, updatable = true)
	public double getLatitude() {
		return this.latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	@Column(name="longitude", unique = false, nullable = true, insertable = true, updatable = true)
	public double getLongitude() {
		return this.longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	@Column(name="nav_status", unique = false, nullable = true, insertable = true, updatable = true)
	public int getNavStatus() {
		return this.navStatus;
	}

	public void setNavStatus(int navStatus) {
		this.navStatus = navStatus;
	}

	@Column(name="pos_accuracy", unique = false, nullable = true, insertable = true, updatable = true)
	public byte getPosAccuracy() {
		return this.posAccuracy;
	}

	public void setPosAccuracy(byte posAccuracy) {
		this.posAccuracy = posAccuracy;
	}

	@Column(name="rot", unique = false, nullable = true, insertable = true, updatable = true)
	public double getRot() {
		return this.rot;
	}

	public void setRot(double rot) {
		this.rot = rot;
	}

	@Column(name="sog", unique = false, nullable = true, insertable = true, updatable = true)
	public double getSog() {
		return this.sog;
	}

	public void setSog(double sog) {
		this.sog = sog;
	}

	@Column(name="updated", unique = false, nullable = false, insertable = true, updatable = true)
	public Date getUpdated() {
		return this.updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

}
	