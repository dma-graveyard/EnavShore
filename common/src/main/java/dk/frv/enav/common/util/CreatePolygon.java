package dk.frv.enav.common.util;
import java.util.ArrayList;
import java.util.List;


public class CreatePolygon {
	List<Point> V = new ArrayList<Point>();
	List<Point> K = new ArrayList<Point>();
	Point current;
	boolean done = false;
	
	public CreatePolygon(List<Point> V){
		this.V = V;
		
		//Reset all flags
		for (int i = 0; i < V.size(); i++) {
			V.get(i).isProcessed = false;
		}
		
		current = V.get(0);
		
		System.out.println("The current is set as: "+ current);
		
		while(!done){
				parsePoints();				
		}
	System.out.println("We are done");
	System.out.println(K);

	
	//System.out.println(K);
		
	}
	
	public void parsePoints(){
		current.isProcessed = true;
		K.add(current);
		
		List<Point> neighbours = findNeighbours(current, V);
		
		if (neighbours.size() == 1){
			current = neighbours.get(0);
		//	parsePoints();
		}
		
		if (neighbours.size() == 0){
			K.add(current);
			done = true;
			//We are done!
			//System.out.println("We are done");
			//System.out.println(K);

		}
	
		//Add current to K
		//Find currents neighbour
		//Set it as current
		
		//If no neighbours -> We are done
	}
	
	  public List<Point> getK() {
		return K;
	}

	public List<Point> findNeighbours(Point point, List<Point> V){
		 // System.out.println("Searching for neighbours");
		  List<Point> neighbours = new ArrayList<Point>();
		  
		  Point neighbour1 = new Point(point.n +1,point.m);
		  Point neighbour2 = new Point(point.n ,point.m -1);
		  Point neighbour3 = new Point(point.n -1,point.m);
		  Point neighbour4 = new Point(point.n ,point.m +1);

		  //System.out.println(neighbour1);
		  //System.out.println(point);
		  
		  
		  //First go right
		  //Then go down
		  //Then go left
		  //Then go up
		  
		  //We can go right
		  for (int i = 0; i < V.size(); i++) {
			if (V.get(i).isProcessed == false){
				  
			if ( (V.get(i).n == neighbour1.n && V.get(i).m == neighbour1.m)){
				System.out.println("We'er going right toward point: " + V.get(i));
				neighbours.add(V.get(i));
				break;
			}
		
			//We can go down
			if ( (V.get(i).n == neighbour2.n && V.get(i).m == neighbour2.m)){
				System.out.println("We'er going down toward point: " + V.get(i));
				neighbours.add(V.get(i));
				break;
			}		

			//We can go left
			if ( (V.get(i).n == neighbour3.n && V.get(i).m == neighbour3.m)){
				System.out.println("We'er going left toward point: " + V.get(i));
				neighbours.add(V.get(i));
				break;
			}		

				//We can go up
			if ( (V.get(i).n == neighbour4.n && V.get(i).m == neighbour4.m)){
				System.out.println("We'er going up toward point: " + V.get(i));
				neighbours.add(V.get(i));
				break;
			}		
																	
				//System.out.println("A neighbour was found, adding: " + V.get(i));
			}
		
		  }
		  return neighbours;
		  
	  }
}
