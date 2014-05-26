package dk.frv.enav.common.xml.nogoslices.response;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import dk.frv.enav.common.xml.ShoreServiceResponse;
import dk.frv.enav.common.xml.nogo.response.NogoResponse;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "nogoResponseSlices", propOrder = {})
public class NogoResponseSlices extends ShoreServiceResponse {
    private static final long serialVersionUID = 1L;

    /**
     * TODO Some form of representation of no go
     */
    private List<NogoResponse> responses;
    private Date validFrom;
    private Date validTo;
    private int noGoErrorCode;
    private String noGoMessage;

    public NogoResponseSlices() {
        responses = new ArrayList<NogoResponse>();
    }

    public int getNoGoErrorCode() {
        return noGoErrorCode;
    }

    public void setNoGoErrorCode(int noGoErrorCode) {
        this.noGoErrorCode = noGoErrorCode;
    }

    public String getNoGoMessage() {
        return noGoMessage;
    }

    public void setNoGoMessage(String nogoMessage) {
        this.noGoMessage = nogoMessage;
    }

    public void addResponse(NogoResponse response) {
        responses.add(response);
    }

    /**
     * @return the responses
     */
    public List<NogoResponse> getResponses() {
        return responses;
    }

    /**
     * @param responses
     *            the responses to set
     */
    public void setResponses(List<NogoResponse> responses) {
        this.responses = responses;
    }

    /**
     * @return the serialversionuid
     */
    public static long getSerialversionuid() {
        return serialVersionUID;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }

    public Date getValidFrom() {
        return validFrom;
    }

    public Date getValidTo() {
        return validTo;
    }

}
