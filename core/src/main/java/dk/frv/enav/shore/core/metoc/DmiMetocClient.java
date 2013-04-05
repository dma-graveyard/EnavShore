package dk.frv.enav.shore.core.metoc;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import dk.frv.enav.common.xml.metoc.request.MetocForecastRequest;
import dk.frv.enav.common.xml.metoc.response.MetocForecastResponse;

public class DmiMetocClient {

    private static Logger LOG = Logger.getLogger(DmiMetocClient.class);

    private final MetocForecastRequest metocRequest;

    public DmiMetocClient(MetocForecastRequest metocRequest) {
        this.metocRequest = metocRequest;
    }

    public MetocForecastResponse makeRequest() {

        LOG.info("Calling DMI METOC service");
        System.out.println(metocRequest);

        MetocForecastResponse response = new MetocForecastResponse();
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
