package dk.frv.enav.shore.api.xml;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import dk.frv.enav.common.text.TextUtils;
import dk.frv.enav.common.xml.ShoreServiceResponse;
import dk.frv.enav.shore.core.domain.ServiceLog;
import dk.frv.enav.shore.core.services.Errorcodes;
import dk.frv.enav.shore.core.services.ServiceException;

public abstract class XmlApiServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private static Logger LOG = Logger.getLogger(XmlApiServlet.class);
	
	public XmlApiServlet() {
		super();
	}
	
	protected static Object getRequestXml(HttpServletRequest request, String contextPath, ServiceLog logEntry) throws ServiceException {
		try {
			String xmlRequest;
			// Determine if XML is in request parameter or body
			if (request.getParameterMap().containsKey("xml")) {
				// From parameter
				xmlRequest = request.getParameter("xml");
			} else {
				// From request body
				Writer writer = new StringWriter();
				char[] buffer = new char[1024];
				Reader reader = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
				xmlRequest = writer.toString();
			}
			
			LOG.debug("XML request:\n" + xmlRequest);

			// Unmarshall
			ByteArrayInputStream input = new ByteArrayInputStream (xmlRequest.getBytes("UTF-8")); 
			JAXBContext jc = JAXBContext.newInstance(contextPath);
			Unmarshaller u = jc.createUnmarshaller();
			return u.unmarshal(input);
		} catch (JAXBException e) {
			if (!logEntry.isSilent()) {
				LOG.error("Failed to parse XML content: " + e.getMessage());
			}
			throw new ServiceException(Errorcodes.INVALID_XML_REQUEST);
		} catch (IOException e) {
			if (!logEntry.isSilent()) {
				LOG.error("Failed to read XML request: " + e.getMessage());
			}
			throw new ServiceException(Errorcodes.FAILED_TO_READ_XML);
		}
	}
	
	protected static void sendResponse(HttpServletResponse res, String contextPath, ShoreServiceResponse resObj) {		
		res.setStatus(HttpServletResponse.SC_OK);
		res.setContentType("text/xml");
        res.setCharacterEncoding("UTF-8");
        
        // TODO where should this come from
        resObj.setShoreMmsi(0);
        
		try {
			JAXBContext jc = JAXBContext.newInstance(contextPath);
			Marshaller m = jc.createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			m.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
			m.marshal(resObj, res.getWriter());
			return;
		} catch (JAXBException e) {
			e.printStackTrace();
			LOG.error("Failed to marshal object: " + e.getMessage());			
		} catch (IOException e) {
			LOG.error("Failed send XML response: " + e.getMessage());
		}
		// Error
		res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		res.setContentType("text/plain");		
		try {
			PrintWriter p = res.getWriter();
			p.print("Internal Server Error");
		} catch (IOException e) { }		
	}
	
	@Override
    public void init() throws ServletException {
        super.init();
	}
	
	protected static void apiLogReq(Class<?> cls, HttpServletRequest request, String reqStr) {
        LOG.debug(String.format("%-19s (req): (%s) %s", TextUtils.className(cls), request.getRemoteAddr(), reqStr));
    }
    
    protected static void apiLogRes(Class<?> cls, String replyStr) {
        LOG.debug(String.format("%-19s (res):\n%s", TextUtils.className(cls), replyStr));
    }

}
