package dk.frv.enav.shore.core.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

@NamedQueries( {
    @NamedQuery(name = "Msi:getMessages", query = "SELECT m FROM Messages m WHERE m.id > :fromMessage AND m.isLatest = true AND ((m.validTo IS NULL) OR (m.validTo > :now))")})
@Entity
@Table(name = "messages")
public class Messages implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private int id;
	private int messageId;
	private int version;
	private String message;
	private String localLanguage;
	private String navtex;
	private String navtexNo;
	private String audio;
	private String encText;
	private String symbol;
	private String reference;
	private String priority;
	private Date validFrom;
	private Date validTo;
	private Locations location;
	private Boolean useDecimalDegrees;
	private Double locationPrecision;
	private Double validForDraughtGT;
	private String validForShiptype;
	private Userregister user;
	private Organisationregister organisation;
	private Date datetimeCreated;
	private Date datetimeUpdated;
	private Date datetimeDeleted;
	private boolean isLatest;
	
	public Messages() {
		
	}

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false, insertable = true, updatable = false)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name="messageId", unique = false, nullable = false, insertable = true, updatable = true)
	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

	@Column(name="version", unique = false, nullable = false, insertable = true, updatable = true)
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	@Column(name="message", unique = false, nullable = true, insertable = true, updatable = true, length = 512)
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Column(name="localLanguage", unique = false, nullable = true, insertable = true, updatable = true, length = 512)
	public String getLocalLanguage() {
		return localLanguage;
	}

	public void setLocalLanguage(String localLanguage) {
		this.localLanguage = localLanguage;
	}

	@Column(name="navtex", unique = false, nullable = true, insertable = true, updatable = true, length = 512)
	public String getNavtex() {
		return navtex;
	}
	
	public void setNavtex(String navtex) {
		this.navtex = navtex;
	}
	
	@Column(name="navtexNo", unique = false, nullable = true, insertable = true, updatable = true, length = 45)
	public String getNavtexNo() {
		return navtexNo;
	}
	
	public void setNavtexNo(String navtexNo) {
		this.navtexNo = navtexNo;
	}

	@Column(name="audio", unique = false, nullable = true, insertable = true, updatable = true, length = 512)
	public String getAudio() {
		return audio;
	}

	public void setAudio(String audio) {
		this.audio = audio;
	}

	@Column(name="ENCtext", unique = false, nullable = true, insertable = true, updatable = true, length = 45)
	public String getEncText() {
		return encText;
	}

	public void setEncText(String encText) {
		this.encText = encText;
	}

	@Column(name="symbol", unique = false, nullable = true, insertable = true, updatable = true, length = 45)
	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	@Column(name="reference", unique = false, nullable = true, insertable = true, updatable = true, length = 45)
	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	@Column(name="priority", unique = false, nullable = true, insertable = true, updatable = true, length = 45)
	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	@Column(name="validFrom", unique = false, nullable = true, insertable = true, updatable = true)
	public Date getValidFrom() {
		return validFrom;
	}

	public void setValidFrom(Date validFrom) {
		this.validFrom = validFrom;
	}

	@Column(name="validTo", unique = false, nullable = true, insertable = true, updatable = true)
	public Date getValidTo() {
		return validTo;
	}

	public void setValidTo(Date validTo) {
		this.validTo = validTo;
	}

	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "locationId", unique = false, nullable = false, insertable = true, updatable = true)
	public Locations getLocation() {
		return location;
	}

	public void setLocation(Locations location) {
		this.location = location;
	}

	@Column(name="useDecimalDegrees", unique = false, nullable = true, insertable = true, updatable = true)
	public Boolean isUseDecimalDegrees() {
		return useDecimalDegrees;
	}

	public void setUseDecimalDegrees(Boolean useDecimalDegrees) {
		this.useDecimalDegrees = useDecimalDegrees;
	}

	@Column(name="locationPrecision", unique = false, nullable = true, insertable = true, updatable = true)
	public Double getLocationPrecision() {
		return locationPrecision;
	}

	public void setLocationPrecision(Double loactionPrecision) {
		this.locationPrecision = loactionPrecision;
	}

	@Column(name="validForDraughtGT", unique = false, nullable = true, insertable = true, updatable = true)
	public Double getValidForDraughtGT() {
		return validForDraughtGT;
	}

	public void setValidForDraughtGT(Double validForDraughtGT) {
		this.validForDraughtGT = validForDraughtGT;
	}

	@Column(name="validForShiptype", unique = false, nullable = true, insertable = true, updatable = true, length = 45)
	public String getValidForShiptype() {
		return validForShiptype;
	}

	public void setValidForShiptype(String validForShiptype) {
		this.validForShiptype = validForShiptype;
	}
	
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "usernameId", unique = false, nullable = true, insertable = true, updatable = true)
	public Userregister getUser() {
		return user;
	}
	
	public void setUser(Userregister user) {
		this.user = user;
	}
	
	@ManyToOne(cascade = {}, fetch = FetchType.LAZY)
	@JoinColumn(name = "organisationId", unique = false, nullable = true, insertable = true, updatable = true)
	public Organisationregister getOrganisation() {
		return organisation;
	}
	
	public void setOrganisation(Organisationregister organisation) {
		this.organisation = organisation;
	}

	@Column(name = "datetimeCreated", unique = false, nullable = false, insertable = false, updatable = false)
	public Date getDatetimeCreated() {
		return datetimeCreated;
	}

	public void setDatetimeCreated(Date datetimeCreated) {
		this.datetimeCreated = datetimeCreated;
	}

	@Column(name = "datetimeUpdated", unique = false, nullable = true, insertable = false, updatable = false)
	public Date getDatetimeUpdated() {
		return datetimeUpdated;
	}

	public void setDatetimeUpdated(Date datetimeUpdated) {
		this.datetimeUpdated = datetimeUpdated;
	}

	@Column(name = "dateTimeDeleted", unique = false, nullable = true, insertable = false, updatable = false)
	public Date getDatetimeDeleted() {
		return datetimeDeleted;
	}
	
	@Transient
	public boolean isDeleted() {
		return (getDatetimeDeleted() != null);
	}

	public void setDatetimeDeleted(Date datetimeDeleted) {
		this.datetimeDeleted = datetimeDeleted;
	}

	@Column(name = "isLatest", unique = false, nullable = true, insertable = false, updatable = false)
	public boolean isIsLatest() {
		return isLatest;
	}

	public void setIsLatest(boolean isLatest) {
		this.isLatest = isLatest;
	}
	
}
