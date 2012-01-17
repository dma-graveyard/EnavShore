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
@Table(name = "risks")
public class Risk implements Serializable {
	private static final long serialVersionUID = 1L;

	public dk.frv.enav.common.xml.risk.response.Risk getCommonRiskObject() {
		dk.frv.enav.common.xml.risk.response.Risk risk = new dk.frv.enav.common.xml.risk.response.Risk();
		risk.setAccidentType(accidentType);
		risk.setProbability(probability);
		risk.setConsequence(consequence);
		risk.setRiskNorm(riskNorm);
//		risk.setDraught(draught);
//		risk.setLength(length);
		return risk;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private Integer mmsi;

	@Column(name = "accident_type")
	private String accidentType;

	@Column(name = "risk_index_normalized")
	private Double riskNorm;

	@Column(name = "probability")
	private Double probability;

	@Column(name = "consequence")
	private Double consequence;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "time_created")
	private Date dateCreated;
	
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAccidentType() {
		return accidentType;
	}

	public void setAccidentType(String accidentType) {
		this.accidentType = accidentType;
	}

	public Double getRiskNorm() {
		return riskNorm;
	}

	public void setRiskNorm(Double riskProba) {
		this.riskNorm = riskProba;
	}

	public Double getProbability() {
		return probability;
	}

	public void setProbability(Double consequenceIndex) {
		this.probability = consequenceIndex;
	}

	public Integer getMmsi() {
		return mmsi;
	}

	public void setMmsi(Integer mmsi) {
		this.mmsi = mmsi;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

}
