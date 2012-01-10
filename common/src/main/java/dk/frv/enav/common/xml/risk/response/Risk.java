package dk.frv.enav.common.xml.risk.response;

import java.io.Serializable;

/**
 *
 * 
 */
public class Risk implements Serializable {
	private static final long serialVersionUID = 1L;


	
	private String accidentType;
	private Double riskProba;
	private Double consequenceIndex;

	public Risk() {
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

	
	

}