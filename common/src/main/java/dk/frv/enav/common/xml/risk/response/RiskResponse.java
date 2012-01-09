package dk.frv.enav.common.xml.risk.response;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import dk.frv.enav.common.xml.ShoreServiceResponse;


@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RiskResponse", propOrder = {})
public class RiskResponse extends ShoreServiceResponse {
	private static final long serialVersionUID = 1L;
	
	
	
	private List<RiskList> list = new ArrayList<RiskList>();
	
	public RiskResponse() {
	}

	
	public List<RiskList> getList() {
		return list;
	}

	public void add(RiskList list) {
		this.list.add(list);
	}

	
	
	

	
	
	

}
