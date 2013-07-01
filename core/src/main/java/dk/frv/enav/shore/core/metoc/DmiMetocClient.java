package dk.frv.enav.shore.core.metoc;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

import dk.frv.enav.common.net.http.HttpClient;
import dk.frv.enav.common.net.http.HttpParams;
import dk.frv.enav.common.xml.metoc.request.MetocForecastRequest;
import dk.frv.enav.common.xml.metoc.response.MetocForecastResponse;

public class DmiMetocClient implements MetocInvoker {

    private static Logger LOG = Logger.getLogger(DmiMetocClient.class);

    private final MetocForecastRequest metocRequest;

    public DmiMetocClient(MetocForecastRequest metocRequest) {
        this.metocRequest = metocRequest;
        
    }

    public MetocForecastResponse makeRequest() {

        LOG.info("Calling DMI METOC service");
        
        Gson gson = new Gson();
        
        String jsonS = gson.toJson(new DmiJsonRequest(metocRequest));
        System.out.println("DMI Request: "+jsonS);


        HttpClient httpClient = new HttpClient();
        httpClient.setUrl("http://sejlrute.dmi.dk/SejlRute/SR");

        HttpParams params = new HttpParams();
        
        String encoded = "";
        try {
            encoded = URLEncoder.encode(jsonS, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            LOG.error("Error in DMI request encoding with UTF-8");
            e1.printStackTrace();
        }
        params.addParams("req="+encoded);
        
        httpClient.setRequestParams(params);
        
        int responseCode = -1;
        try {
            responseCode = httpClient.post();
        } catch (IOException e) {
            
            LOG.error("Failed to make HTTP request: " + e.getMessage());
        }
        
        String responseString = httpClient.getResponseString();
        LOG.debug("Response:\n" + responseString);
        
        MetocForecastResponse response;
        try {
            response = DmiJsonResponse.metocFromJson(responseString);
        } catch (Exception e) {
            response = new MetocForecastResponse();
            response.setErrorCode(responseCode);
            response.setErrorMessage(responseString);
        }

        httpClient.releaseConnection();
        if (responseCode != 200) {
            LOG.error("Error from METOC service: " + responseString);
        }
        
        return response;
    }
    

    public static void main(String[] args) throws JAXBException {
        MetocForecastRequest metocRequest;
        JAXBContext jc = JAXBContext.newInstance("dk.frv.enav.common.xml.metoc.request");
        Unmarshaller u = jc.createUnmarshaller();
        metocRequest = (MetocForecastRequest) u.unmarshal(new File("core/src/main/resources/metoc_req.xml"));
        DmiMetocClient client = new DmiMetocClient(metocRequest);

        MetocForecastResponse response = client.makeRequest();
        jc = JAXBContext.newInstance("dk.frv.enav.common.xml.metoc.response");
        Marshaller m = jc.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
        m.marshal(response, System.out);
    }

}
