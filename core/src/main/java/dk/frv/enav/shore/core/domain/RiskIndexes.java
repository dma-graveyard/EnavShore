package dk.frv.enav.shore.core.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the risk_indexes database table.
 * 
 */
@Entity
@Table(name = "risk_indexes")
public class RiskIndexes implements Serializable {
	private static final long serialVersionUID = 1L;

	public dk.frv.enav.common.xml.risk.response.RiskIndexes getCommonRiskObject() {
		dk.frv.enav.common.xml.risk.response.RiskIndexes risk = new dk.frv.enav.common.xml.risk.response.RiskIndexes();
		risk.setCog(cog);
		risk.setCollision(collision);
		risk.setCpaDist(cpaDist);
		risk.setCpaTargetMmsi(cpaTargetMmsi);
		risk.setCpaTime(cpaTime);
		risk.setDatetimeCreated(datetimeCreated);
		risk.setDraught(draught);
		risk.setFireExplosion(fireExplosion);
		risk.setFlag(flag);
		risk.setFoundering(foundering);
		risk.setHullFailure(hullFailure);
		risk.setLength(length);
		risk.setMachineryFailure(machineryFailure);
		risk.setMmsi(mmsi);
		risk.setShipType(shipType);
		risk.setSog(sog);
		risk.setStrandedByMachineFailure(strandedByMachineFailure);
		risk.setStrandedByNavigationError(strandedByNavigationError);
		risk.setYearOfBuild(yearOfBuild);

		return risk;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private Integer cog;

	private Double collision;

	@Column(name = "cpa_dist")
	private Double cpaDist;

	@Column(name = "cpa_target_mmsi")
	private Integer cpaTargetMmsi;

	@Column(name = "cpa_time")
	private Double cpaTime;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "datetime_created")
	private Date datetimeCreated;

	private Double draught;

	@Column(name = "fire_explosion")
	private Double fireExplosion;

	private String flag;

	private Double foundering;

	@Column(name = "hull_failure")
	private Double hullFailure;

	@Column(name = "LENGTH")
	private Double length;

	@Column(name = "machinery_failure")
	private Double machineryFailure;

	private Integer mmsi;

	@Column(name = "ship_type")
	private String shipType;

	private Integer sog;

	@Column(name = "stranded_by_machine_failure")
	private Double strandedByMachineFailure;

	@Column(name = "stranded_by_navigation_error")
	private Double strandedByNavigationError;

	@Column(name = "year_of_build")
	private Integer yearOfBuild;

	public RiskIndexes() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCog() {
		return this.cog;
	}

	public void setCog(Integer cog) {
		this.cog = cog;
	}

	public Double getCollision() {
		return this.collision;
	}

	public void setCollision(Double collision) {
		this.collision = collision;
	}

	public Double getCpaDist() {
		return this.cpaDist;
	}

	public void setCpaDist(Double cpaDist) {
		this.cpaDist = cpaDist;
	}

	public Integer getCpaTargetMmsi() {
		return this.cpaTargetMmsi;
	}

	public void setCpaTargetMmsi(Integer cpaTargetMmsi) {
		this.cpaTargetMmsi = cpaTargetMmsi;
	}

	public Double getCpaTime() {
		return this.cpaTime;
	}

	public void setCpaTime(Double cpaTime) {
		this.cpaTime = cpaTime;
	}

	public Date getDatetimeCreated() {
		return this.datetimeCreated;
	}

	public void setDatetimeCreated(Date datetimeCreated) {
		this.datetimeCreated = datetimeCreated;
	}

	public Double getDraught() {
		return this.draught;
	}

	public void setDraught(Double draught) {
		this.draught = draught;
	}

	public Double getFireExplosion() {
		return this.fireExplosion;
	}

	public void setFireExplosion(Double fireExplosion) {
		this.fireExplosion = fireExplosion;
	}

	public String getFlag() {
		return this.flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public Double getFoundering() {
		return this.foundering;
	}

	public void setFoundering(Double foundering) {
		this.foundering = foundering;
	}

	public Double getHullFailure() {
		return this.hullFailure;
	}

	public void setHullFailure(Double hullFailure) {
		this.hullFailure = hullFailure;
	}

	public Double getLength() {
		return this.length;
	}

	public void setLength(Double length) {
		this.length = length;
	}

	public Double getMachineryFailure() {
		return this.machineryFailure;
	}

	public void setMachineryFailure(Double machineryFailure) {
		this.machineryFailure = machineryFailure;
	}

	public Integer getMmsi() {
		return this.mmsi;
	}

	public void setMmsi(Integer mmsi) {
		this.mmsi = mmsi;
	}

	public String getShipType() {
		return this.shipType;
	}

	public void setShipType(String shipType) {
		this.shipType = shipType;
	}

	public Integer getSog() {
		return this.sog;
	}

	public void setSog(Integer sog) {
		this.sog = sog;
	}

	public Double getStrandedByMachineFailure() {
		return this.strandedByMachineFailure;
	}

	public void setStrandedByMachineFailure(Double strandedByMachineFailure) {
		this.strandedByMachineFailure = strandedByMachineFailure;
	}

	public Double getStrandedByNavigationError() {
		return this.strandedByNavigationError;
	}

	public void setStrandedByNavigationError(Double strandedByNavigationError) {
		this.strandedByNavigationError = strandedByNavigationError;
	}

	public Integer getYearOfBuild() {
		return this.yearOfBuild;
	}

	public void setYearOfBuild(Integer yearOfBuild) {
		this.yearOfBuild = yearOfBuild;
	}

}