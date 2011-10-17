package dk.frv.enav.shore.core.domain;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;

/**
 * The persistent class for the ais_vessel_static database table.
 * 
 */
@Entity
@Table(name = "ais_vessel_static")
public class AisVesselStatic implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public enum VesselType {PASSENGER_VESSEL, CARGO_VESSEL, TANKER, HSC, TUG, PILOT, YACHT, NAVIGATION_AID, UNSPECIFIED};
	private int mmsi;
	private String callsign;
	private Date created;
	private short dimBow;
	private byte dimPort;
	private byte dimStarboard;
	private short dimStern;
	private String name;
	private Date received;
	private Byte shipType;
	private AisClassAStatic aisClassAStatic;
	private AisVesselTarget aisVesselTarget;

	public AisVesselStatic() {
		
	}

	@Transient
	public static VesselType getVesselType(byte shipType) {
		switch(shipType/10) {
			case 6: return VesselType.PASSENGER_VESSEL;
			case 7: return VesselType.CARGO_VESSEL;
			case 8: return VesselType.TANKER;
			case 4: return VesselType.HSC;
			case 5:
				switch(shipType) {
					case 52: return VesselType.TUG;
					case 50: return VesselType.PILOT;
					default: break;
				}
			case 3:
				switch(shipType) {
					case 36: return VesselType.YACHT;
					case 37: return VesselType.YACHT;
					default: break;
				}
			default: return VesselType.UNSPECIFIED;
		}
	}
	
	@Id
	@Column(unique = true, nullable = false)
	public int getMmsi() {
		return this.mmsi;
	}

	public void setMmsi(int mmsi) {
		this.mmsi = mmsi;
	}

	@Column(nullable = true, length = 8)
	public String getCallsign() {
		return this.callsign;
	}

	public void setCallsign(String callsign) {
		this.callsign = callsign;
	}

	@Column(nullable = false)
	public Date getCreated() {
		return this.created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@Column(name = "dim_bow", nullable = true)
	public short getDimBow() {
		return this.dimBow;
	}

	public void setDimBow(short dimBow) {
		this.dimBow = dimBow;
	}

	@Column(name = "dim_port", nullable = true)
	public byte getDimPort() {
		return this.dimPort;
	}

	public void setDimPort(byte dimPort) {
		this.dimPort = dimPort;
	}

	@Column(name = "dim_starboard", nullable = true)
	public byte getDimStarboard() {
		return this.dimStarboard;
	}

	public void setDimStarboard(byte dimStarboard) {
		this.dimStarboard = dimStarboard;
	}

	@Column(name = "dim_stern", nullable = true)
	public short getDimStern() {
		return this.dimStern;
	}

	public void setDimStern(short dimStern) {
		this.dimStern = dimStern;
	}

	@Column(nullable = true, length = 32)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(nullable = false)
	public Date getReceived() {
		return this.received;
	}

	public void setReceived(Date received) {
		this.received = received;
	}

	@Column(name = "ship_type", nullable = true)
	public Byte getShipType() {
		return this.shipType;
	}

	public void setShipType(Byte shipType) {
		this.shipType = shipType;
	}

	// bi-directional one-to-one association to AisClassAStatic
	@OneToOne(mappedBy = "aisVesselStatic")
	public AisClassAStatic getAisClassAStatic() {
		return this.aisClassAStatic;
	}

	public void setAisClassAStatic(AisClassAStatic aisClassAStatic) {
		this.aisClassAStatic = aisClassAStatic;
	}

	// bi-directional one-to-one association to AisVesselTarget
	@OneToOne
	@JoinColumn(name = "mmsi", nullable = false, insertable = false, updatable = false)
	public AisVesselTarget getAisVesselTarget() {
		return this.aisVesselTarget;
	}

	public void setAisVesselTarget(AisVesselTarget aisVesselTarget) {
		this.aisVesselTarget = aisVesselTarget;
	}

}