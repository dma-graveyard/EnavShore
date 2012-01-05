package dk.frv.enav.shore.core.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;

/**
 * The persistent class for the ais_class_a_static database table.
 * 
 */
@Entity
@Table(name = "ais_class_a_static")
public class AisClassAStatic implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	private Integer mmsi;
	private String destination;
	private Short draught;
	private Byte dte;
	private Date eta;
	private Integer imo;
	private Byte posType;
	private Byte version;
	private AisVesselStatic aisVesselStatic;

	public AisClassAStatic() {
	}
	
	@Id
	@Column(unique = true, nullable = false)
	public Integer getMmsi() {
		return this.mmsi;
	}

	public void setMmsi(Integer mmsi) {
		this.mmsi = mmsi;
	}

	@Column(length = 32)
	public String getDestination() {
		return this.destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	@Column(nullable = false)
	public Short getDraught() {
		return this.draught;
	}

	public void setDraught(Short draught) {
		this.draught = draught;
	}

	@Column(nullable = false)
	public Byte getDte() {
		return this.dte;
	}

	public void setDte(Byte dte) {
		this.dte = dte;
	}

	public Date getEta() {
		return this.eta;
	}

	public void setEta(Date eta) {
		this.eta = eta;
	}

	public Integer getImo() {
		return this.imo;
	}

	public void setImo(Integer imo) {
		this.imo = imo;
	}

	@Column(name = "pos_type", nullable = false)
	public Byte getPosType() {
		return this.posType;
	}

	public void setPosType(Byte posType) {
		this.posType = posType;
	}

	@Column(nullable = false)
	public Byte getVersion() {
		return this.version;
	}

	public void setVersion(Byte version) {
		this.version = version;
	}

	// bi-directional one-to-one association to AisVesselStatic
	@OneToOne
	@JoinColumn(name = "mmsi", nullable = false, insertable = false, updatable = false)
	public AisVesselStatic getAisVesselStatic() {
		return this.aisVesselStatic;
	}

	public void setAisVesselStatic(AisVesselStatic aisVesselStatic) {
		this.aisVesselStatic = aisVesselStatic;
	}

}