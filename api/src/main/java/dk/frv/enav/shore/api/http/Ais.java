package dk.frv.enav.shore.api.http;

import java.io.IOException;
import java.io.PrintWriter;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import dk.frv.enav.common.net.http.HttpParams;
import dk.frv.enav.shore.core.services.ais.AisService;
import dk.frv.enav.shore.core.services.ais.DetailedAisTarget;
import dk.frv.enav.shore.core.services.ais.OverviewRequest;
import dk.frv.enav.shore.core.services.ais.OverviewResponse;

public class Ais extends HttpApiServlet {

	private static final long serialVersionUID = 1L;
	private Gson gson = new Gson();
	
	@EJB
	AisService aisService;
	
	public void init() throws ServletException {
		super.init();
	}
	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");        
        HttpParams params = new HttpParams(request.getParameterMap());
        String json = "";
        
        // Determine method
        String method = (params.containsKey("method") ? params.getFirst("method") : "overview");
        
        if (method.equalsIgnoreCase("overview")) {
        	OverviewRequest overviewRequest = new OverviewRequest(params);
            OverviewResponse overviewResponse = aisService.getOverview(overviewRequest);
            json = gson.toJson(overviewResponse);
        }
        else if (method.equalsIgnoreCase("details")) {
        	Integer id = Integer.parseInt(params.getFirst("id"));
        	DetailedAisTarget detailedAisTarget = aisService.getTargetDetails(id);
        	json = gson.toJson(detailedAisTarget);
        }
        
        out.print(json);
	}
}
