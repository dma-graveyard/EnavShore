package dk.frv.enav.shore.core.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import dk.frv.enav.shore.core.services.ServiceException;

@NamedQueries( { 
	@NamedQuery(name = "ServiceLog:getLastForService", query = "SELECT sl FROM ServiceLog sl WHERE sl.service=:service ORDER BY sl.id DESC") 
})
@Entity
@Table(name = "service_log")
public class ServiceLog implements Serializable {
	private static final long serialVersionUID = 1L;

	public enum Status {
		STARTED, COMPLETED, FAILED
	};

	private int id;
	private String service;
	private Status status;
	private Long mmsi;
	private String ip;
	private Date started;
	private Date ended;
	private Integer errorcode;
	private String errortext;
	private boolean silent = false;

	public ServiceLog() {
		setStatus(Status.STARTED);
	}

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false, insertable = true, updatable = false)
	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "service", unique = false, nullable = false, insertable = true, updatable = true, length = 32)
	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "status", unique = false, nullable = false, insertable = true, updatable = true)
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	@Column(name = "mmsi", unique = false, nullable = true, insertable = true, updatable = true)
	public Long getMmsi() {
		return mmsi;
	}

	public void setMmsi(Long mmsi) {
		this.mmsi = mmsi;
	}

	@Column(name = "ip", unique = false, nullable = true, insertable = true, updatable = true, length = 16)
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Column(name = "started", unique = false, nullable = false, insertable = true, updatable = true)
	public Date getStarted() {
		return started;
	}

	public void setStarted(Date started) {
		this.started = started;
	}

	@Column(name = "ended", unique = false, nullable = true, insertable = true, updatable = true)
	public Date getEnded() {
		return ended;
	}

	public void setEnded(Date ended) {
		this.ended = ended;
	}

	@Column(name = "errorcode", unique = false, nullable = true, insertable = true, updatable = true)
	public Integer getErrorcode() {
		return errorcode;
	}

	public void setErrorcode(Integer errorcode) {
		this.errorcode = errorcode;
	}

	@Column(name = "errortext", unique = false, nullable = true, insertable = true, updatable = true, length = 255)
	public String getErrortext() {
		return errortext;
	}

	public void setErrortext(String errortext) {
		this.errortext = errortext;
	}

	@Transient
	public void markCompleted() {
		setStatus(Status.COMPLETED);
	}

	@Transient
	public void markFailed(ServiceException e) {
		setStatus(Status.FAILED);
		setErrorcode(e.getErrorCode());
		setErrortext(e.getExtErrorMsg());
	}

	@Transient
	public boolean isSilent() {
		return silent;
	}

	public void setSilent(boolean silent) {
		this.silent = silent;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ServiceLog [ended=");
		builder.append(ended);
		builder.append(", errorcode=");
		builder.append(errorcode);
		builder.append(", errortext=");
		builder.append(errortext);
		builder.append(", id=");
		builder.append(id);
		builder.append(", ip=");
		builder.append(ip);
		builder.append(", mmsi=");
		builder.append(mmsi);
		builder.append(", service=");
		builder.append(service);
		builder.append(", silent=");
		builder.append(silent);
		builder.append(", started=");
		builder.append(started);
		builder.append(", status=");
		builder.append(status);
		builder.append("]");
		return builder.toString();
	}
	
}
