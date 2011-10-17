package dk.frv.enav.shore.core.services;

public class ServiceException extends Exception {

	private static final long serialVersionUID = 1019081461518422727L;

	private int errorCode = Errorcodes.SYSTEM_ERROR;
	private String intErrorMsg = "";
	private String extErrorMsg = "";
	
	public ServiceException(int errorCode, String intErrorMsg, String extErrorMsg) {
		this.errorCode = errorCode;
		this.intErrorMsg = intErrorMsg;
		this.extErrorMsg = extErrorMsg;
	}
	
	public ServiceException(int errorCode, String message) {
		this(errorCode, message, Errorcodes.getErrorMessage(errorCode));
	}

	public ServiceException(String message) {
		this(Errorcodes.SYSTEM_ERROR, message);
	}
	
	public ServiceException(int errorCode) {
		this(errorCode, Errorcodes.getErrorMessage(errorCode));
	}
	
	public String getErrorReply() {
		return "error=" + errorCode + " message=" + extErrorMsg;
	}
	
	public int getErrorCode() {
		return errorCode;
	}
	
	public String getExtErrorMsg() {
		return extErrorMsg;
	}
	
	@Override
	public String toString() {
		return "ServiceException errorCode = " + errorCode + " intErrorMsg = " + intErrorMsg + " extErrorMsg = " + extErrorMsg;
	}

}
