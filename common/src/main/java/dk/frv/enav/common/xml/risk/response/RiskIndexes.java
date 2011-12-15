package dk.frv.enav.common.xml.risk.response;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * 
 */
public class RiskIndexes implements Serializable {
	private static final long serialVersionUID = 1L;


	private Integer cog;

	private Double collision;

	private Double cpaDist;

	private Integer cpaTargetMmsi;

	private Double cpaTime;

	private Date datetimeCreated;

	private Double draught;

	private Double fireExplosion;

	private String flag;

	private Double foundering;

	private Double hullFailure;

	private Double length;

	private Double machineryFailure;

	private Integer mmsi;

	private String shipType;

	private Integer sog;

	private Double strandedByMachineFailure;

	private Double strandedByNavigationError;

	private Integer yearOfBuild;

	public RiskIndexes() {
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