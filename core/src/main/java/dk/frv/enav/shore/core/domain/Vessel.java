package dk.frv.enav.shore.core.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@NamedQueries( {
    @NamedQuery(name = "Vessel:getByMMSI", query = "SELECT v FROM Vessel v WHERE v.mmsi=:mmsi")})
@Entity
@Table(name = "vessel")
public class Vessel implements Serializable {
	private static final long serialVersionUID = 1L;

	private int id;
	private String callSign;
	private Integer imoNo;
	private int mmsi;
	private String name;
	private Date updated;
	private Date created;
	private Set<Route> routes;
	private VesselPosition vesselPosition;

	public Vessel() {
	}

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false, insertable = true, updatable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name="call_sign", unique = false, nullable = true, insertable = true, updatable = true, length = 10)
	public String getCallSign() {
		return this.callSign;
	}

	public void setCallSign(String callSign) {
		this.callSign = callSign;
	}

	@Column(name="imo_no", unique = false, nullable = true, insertable = true, updatable = true)
	public Integer getImoNo() {
		return this.imoNo;
	}

	public void setImoNo(Integer imoNo) {
		this.imoNo = imoNo;
	}

	@Column(name="mmsi", unique = true, nullable = false, insertable = true, updatable = true)
	public int getMmsi() {
		return this.mmsi;
	}

	public void setMmsi(int mmsi) {
		this.mmsi = mmsi;
	}

	@Column(name="name", unique = false, nullable = true, insertable = true, updatable = true, length = 128)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name="updated", unique = false, nullable = true, insertable = true, updatable = true)
	public Date getUpdated() {
		return this.updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	@GeneratedValue
	@Column(name = "created", unique = false, nullable = false, insertable = false, updatable = false)
	public Date getCreated() {
		return this.created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}


	@OneToMany(mappedBy = "vessel", cascade = {})
	public Set<Route> getRoutes() {
		return this.routes;
	}

	public void setRoutes(Set<Route> routes) {
		this.routes = routes;
	}

	@OneToOne(mappedBy = "vessel", cascade = {CascadeType.ALL})
	public VesselPosition getVesselPosition() {
		return this.vesselPosition;
	}

	public void setVesselPosition(VesselPosition vesselPosition) {
		this.vesselPosition = vesselPosition;
	}

}