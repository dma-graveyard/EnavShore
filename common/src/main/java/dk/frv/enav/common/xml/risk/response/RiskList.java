package dk.frv.enav.common.xml.risk.response;

import java.util.ArrayList;
import java.util.List;

public class RiskList {

	private Integer mmsi;
	private List<Risk> risks = new ArrayList<Risk>();
	
	public List<Risk> getRisks() {
		return risks;
	}

	public void setRisks(List<Risk> risks) {
		this.risks = risks;
	}

	public boolean addRisk(Risk arg0) {
		return risks.add(arg0);
	}
	
	
	public Integer getMmsi() {
		return mmsi;
	}

	public void setMmsi(Integer mmsi) {
		this.mmsi = mmsi;
	}
}
