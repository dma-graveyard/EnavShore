package dk.frv.enav.common.xml.msi;

import java.io.Serializable;
import java.util.Date;

public class MsiMessage implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int id;
	private int messageId;
	private int version;
	private String message;
	private String navtex;
	private String navtexNo;
	private String encText;
	private String symbol;
	private String reference;
	private String priority;
	private Date validFrom;
	private Date validTo;
	private Double locationPrecision;
	private Double validForDraugth;
	private String validForShipType;
	private String username;
	private String organisation;
	private Date created;
	private Date updated;
	private Date deleted;	
	private MsiLocation location;
	
	public MsiMessage() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getEncText() {
		return encText;
	}

	public void setEncText(String encText) {
		this.encText = encText;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public Date getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}

	public Date getValidTo() {
		return validTo;
	}

	public void setValidTo(Date validTo) {
		this.validTo = validTo;
	}

	public Double getLocationPrecision() {
		return locationPrecision;
	}

	public void setLocationPrecision(Double locationPrecision) {
		this.locationPrecision = locationPrecision;
	}

	public Double getValidForDraugth() {
		return validForDraugth;
	}

	public void setValidForDraugth(Double validForDraugth) {
		this.validForDraugth = validForDraugth;
	}

	public String getValidForShipType() {
		return validForShipType;
	}

	public void setValidForShipType(String validForShipType) {
		this.validForShipType = validForShipType;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getOrganisation() {
		return organisation;
	}

	public void setOrganisation(String organisation) {
		this.organisation = organisation;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public Date getDeleted() {
		return deleted;
	}

	public void setDeleted(Date deleted) {
		this.deleted = deleted;
	}
	
	public MsiLocation getLocation() {
		return location;
	}
	
	public void setLocation(MsiLocation location) {
		this.location = location;
	}
	
	public String getNavtex() {
		return navtex;
	}
	
	public void setNavtex(String navtex) {
		this.navtex = navtex;
	}
	
	public String getNavtexNo() {
		return navtexNo;
	}
	
	public void setNavtexNo(String navtexNo) {
		this.navtexNo = navtexNo;
	}
	
}
