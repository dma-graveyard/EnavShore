package dk.frv.enav.common.xml.risk.response;

import java.io.Serializable;

/**
 *
 * 
 */
public class Risk implements Serializable {
	private static final long serialVersionUID = 1L;

	private String accidentType;
	private Double riskNorm;
	private Double probability;
	private Double consequence;
	private Double draught;
	private Double length;

	public Risk() {
	}

	public String getAccidentType() {
		return accidentType;
	}

	public void setAccidentType(String accidentType) {
		this.accidentType = accidentType;
	}

	public Double getProbability() {
		return probability;
	}

	public void setProbability(Double riskProba) {
		this.probability = riskProba;
	}

	public Double getConsequence() {
		return consequence;
	}

	public void setConsequence(Double consequenceIndex) {
		this.consequence = consequenceIndex;
	}

	public Double getRiskNorm() {
		return riskNorm;
	}

	public void setRiskNorm(Double riskNorm) {
		this.riskNorm = riskNorm;
	}

	public Double getDraught() {
		return draught;
	}

	public void setDraught(Double draught) {
		this.draught = draught;
	}

	public Double getLength() {
		return length;
	}

	public void setLength(Double length) {
		this.length = length;
	}

}
