package dk.frv.enav.shore.api.http;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import dk.frv.enav.common.net.http.HttpParams;
import dk.frv.enav.shore.core.services.ais.AisRequest;
import dk.frv.enav.shore.core.services.ais.AisService;
import dk.frv.enav.shore.core.services.ais.PublicAisTarget;

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
        
        double swLat = new Double(params.getFirst("swLat"));
        double swLon = new Double(params.getFirst("swLon"));
        double neLat = new Double(params.getFirst("neLat"));
        double neLon = new Double(params.getFirst("neLon"));
        
        AisRequest aisRequest = new AisRequest(swLat, swLon, neLat, neLon);
        List<PublicAisTarget> aisTargets = aisService.getPublicAisTargets(aisRequest);
        
        String json = gson.toJson(aisTargets);
        out.print(json);
	}
}
