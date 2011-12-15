package dk.frv.enav.common.xml;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "shoreServiceResponse", propOrder = {})
public abstract class ShoreServiceResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private int shoreMmsi;
	private int errorCode;
	private String errorMessage;

	public ShoreServiceResponse() {

	}

	public int getShoreMmsi() {
		return shoreMmsi;
	}

	public void setShoreMmsi(int shoreMmsi) {
		this.shoreMmsi = shoreMmsi;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
