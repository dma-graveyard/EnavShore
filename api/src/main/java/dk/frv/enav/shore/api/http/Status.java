package dk.frv.enav.shore.api.http;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import dk.frv.enav.shore.core.services.status.StatusService;

public class Status extends HttpApiServlet {
	
	private static final long serialVersionUID = 1L;
	
	@EJB
	StatusService statusService;
	
	public void init() throws ServletException {
        super.init();
    }
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();        
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");        
        
        Map<String, StatusService.Status> fullStatus = statusService.fullStatus();
        List<String> resLines = new ArrayList<String>();
        for (String service : fullStatus.keySet()) {
			resLines.add(service + "=" + fullStatus.get(service).name());
		}
        String res = StringUtils.join(resLines.iterator(), "\r\n"); 
        
        response.setStatus(HttpServletResponse.SC_OK);        
        out.print(res);        
	}

}
