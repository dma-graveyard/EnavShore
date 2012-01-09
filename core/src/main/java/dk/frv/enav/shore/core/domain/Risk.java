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
import javax.persistence.criteria.CriteriaBuilder.In;

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
		risk.setRiskProba(riskProba);
		risk.setConsequenceIndex(consequenceIndex);
		

		return risk;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	private Integer mmsi;
	
	@Column(name = "accident_type")
	private String accidentType;
	
	@Column(name = "risk_proba")
	private Double riskProba;
	
	@Column(name = "consequence_index")
	private Double consequenceIndex;
	
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
	public Double getRiskProba() {
		return riskProba;
	}
	public void setRiskProba(Double riskProba) {
		this.riskProba = riskProba;
	}
	public Double getConsequenceIndex() {
		return consequenceIndex;
	}
	public void setConsequenceIndex(Double consequenceIndex) {
		this.consequenceIndex = consequenceIndex;
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