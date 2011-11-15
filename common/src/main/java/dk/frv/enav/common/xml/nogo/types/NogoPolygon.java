package dk.frv.enav.common.xml.nogo.types;

import java.util.ArrayList;
import java.util.List;

public class NogoPolygon {
	private List<NogoPoint> polygon = new ArrayList<NogoPoint>();
	
	public List<NogoPoint> getPolygon() {
		return polygon;
	}
	public void setPolygon(List<NogoPoint> polygon) {
		this.polygon = polygon;
	}
	
	public NogoPolygon(){

	}
	
	/**
	
	public void addPoint(NogoPoint point)
	{
		this.points.add(point);
	}

	public List<NogoPoint> getPolygon(){
		return points;
	}
	**/
	
}
