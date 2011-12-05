package dk.frv.enav.shore.core.services.ais;

import java.util.Date;

import dk.frv.ais.country.CountryMapper;
import dk.frv.ais.country.MidCountry;
import dk.frv.ais.message.NavigationalStatus;
import dk.frv.enav.shore.core.domain.AisClassAPosition;
import dk.frv.enav.shore.core.domain.AisClassAStatic;
import dk.frv.enav.shore.core.domain.AisVesselPosition;
import dk.frv.enav.shore.core.domain.AisVesselStatic;
import dk.frv.enav.shore.core.domain.AisVesselTarget;

public class DetailedAisTarget extends AisTarget {
	
	protected String name;
	protected String callsign;
	protected String imoNo;
	protected String cargo;
	protected String country;
	protected Double draught;
	protected Double heading;
	protected Double rot;
	protected String destination;
	protected String navStatus;
	protected Date eta;
	protected byte posAcc;

	public DetailedAisTarget() {

	}

	public void init(AisVesselTarget aisVessel) {
		AisVesselStatic aisVesselStatic = aisVessel.getAisVesselStatic();
		AisVesselPosition aisVesselPosition = aisVessel.getAisVesselPosition();
		AisClassAPosition aisClassAPosition = aisVesselPosition.getAisClassAPosition();
		AisClassAStatic aisClassAStatic = aisVesselStatic.getAisClassAStatic();
		super.init(aisVessel, aisVesselPosition, aisVesselStatic, aisClassAPosition);
		
		this.name = aisVesselStatic.getName();
		this.callsign = aisVesselStatic.getCallsign();
		this.cargo = aisVesselStatic.getShipTypeCargo().prettyCargo();

		// Class A statics
		if (aisClassAStatic != null) {
			this.imoNo = Integer.toString(aisClassAStatic.getImo());
			this.destination = aisClassAStatic.getDestination();
			this.draught = (double)aisClassAStatic.getDraught();
			this.eta = aisClassAStatic.getEta();
		}		
		
		// Class A position
		if (aisClassAPosition != null) {
			NavigationalStatus navigationalStatus = new NavigationalStatus(aisClassAPosition.getNavStatus());
			this.navStatus = navigationalStatus.prettyStatus();
		}
		
		// Determine country
		String str = Long.toString(mmsi);
		if (str.length() > 3) {
			str = str.substring(0, 3);
			MidCountry midCountry = CountryMapper.getInstance().getByMid(Integer.parseInt(str)); 
			if (midCountry != null) {
				country = midCountry.getName();
			}
		}
		
		if (aisVesselPosition.getHeading() != null) {
			this.heading = aisVesselPosition.getHeading();
		}
		
		this.posAcc = aisVesselPosition.getPosAcc();
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCallsign() {
		return callsign;
	}

	public void setCallsign(String callsign) {
		this.callsign = callsign;
	}

	public String getImoNo() {
		return imoNo;
	}

	public void setImoNo(String imoNo) {
		this.imoNo = imoNo;
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Double getDraught() {
		return draught;
	}

	public void setDraught(Double draught) {
		this.draught = draught;
	}

	public Double getHeading() {
		return heading;
	}

	public void setHeading(Double heading) {
		this.heading = heading;
	}

	public Double getRot() {
		return rot;
	}

	public void setRot(Double rot) {
		this.rot = rot;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getNavStatus() {
		return navStatus;
	}

	public void setNavStatus(String navStatus) {
		this.navStatus = navStatus;
	}

	public Date getEta() {
		return eta;
	}

	public void setEta(Date eta) {
		this.eta = eta;
	}

	public byte getPosAcc() {
		return posAcc;
	}

	public void setPosAcc(byte posAcc) {
		this.posAcc = posAcc;
	}
	
}
