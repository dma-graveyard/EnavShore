package dk.frv.enav.shore.core.services.nogo;

import java.util.ArrayList;
import java.util.List;

import dk.frv.enav.common.xml.nogo.types.NogoPoint;
import dk.frv.enav.common.xml.nogo.types.NogoPolygon;
import dk.frv.enav.shore.core.domain.DepthDenmark;

public class connectNeighbourLines {

	static List<NogoPolygon> connectFindValidNeighbours(List<DepthDenmark> line, List<List<DepthDenmark>> nextLines) {

		double latOffset = 0.00055504;
		double lonOffset = 0.00055504;

		List<NogoPolygon> polygon = new ArrayList<NogoPolygon>();

		if (line.size() == 1) {
			System.out.println("singleton shit");
		} else {

			int selfLeft = line.get(0).getN();
			int selfRight = line.get(1).getN();

//			System.out.println("left end point is: " + selfLeft + " lon is: " + line.get(0).getLon() + " lat is: "
//					+ line.get(0).getLat());
//			System.out.println("right end point is: " + selfRight + " lon is: " + line.get(1).getLon() + " lat is: "
//					+ line.get(1).getLat());

			// Look at each of the lines in the nextLines list
			for (int i = 0; i < nextLines.size(); i++) {
				// System.out.println("Investigating the line: " +
				// nextLines.get(i));

				if (nextLines.get(i).size() == 1) {
					System.out.println("singleton shit");
				} else {

					int leftPoint = nextLines.get(i).get(0).getN();
					int rightPoint = nextLines.get(i).get(1).getN();
//
//					System.out.println("left end point investigated: " + leftPoint + " lon is: "
//							+ nextLines.get(i).get(0).getLon() + " lat is: " + nextLines.get(i).get(0).getLat());
//					System.out.println("right end point investigated: " + rightPoint + " lon is: "
//							+ nextLines.get(i).get(1).getLon() + " lat is: " + nextLines.get(i).get(0).getLat());

					// Outward left
					if (leftPoint < selfLeft
							&& ((rightPoint < selfRight && rightPoint >= selfLeft) || rightPoint >= selfLeft)) {
						// System.out.println("Outward left neighbour");

						NogoPolygon triangle = new NogoPolygon();

						// Three nogo points make a triangle
						NogoPoint rightTop = new NogoPoint(line.get(0).getLat() + latOffset, line.get(0).getLon()
								- lonOffset);
						NogoPoint rightBottom = new NogoPoint(nextLines.get(i).get(0).getLat() + latOffset, line.get(0)
								.getLon() - lonOffset);

						NogoPoint leftBottom = new NogoPoint(nextLines.get(i).get(0).getLat() + latOffset, nextLines
								.get(i).get(0).getLon()
								- lonOffset);

						triangle.getPolygon().add(rightTop);
						triangle.getPolygon().add(rightBottom);
						triangle.getPolygon().add(leftBottom);

						polygon.add(triangle);

						// Create the needed polygon triangle thingy?

						// Create the data points, add them seperately?

						// Connet to m +/-1

						// Change nogopoints type to include n, m

					}

					// Inward left

					if (leftPoint > selfLeft && leftPoint < selfRight) {
						// System.out.println("Inwarde left neighbour");

						NogoPolygon triangle = new NogoPolygon();

						// Three nogo points make a triangle

						NogoPoint leftTop = new NogoPoint(line.get(0).getLat() - latOffset, line.get(0).getLon()
								- lonOffset);
						NogoPoint rightTop = new NogoPoint(line.get(0).getLat() - latOffset, nextLines.get(i).get(0)
								.getLon()
								- lonOffset);
						NogoPoint rightBottom = new NogoPoint(nextLines.get(i).get(0).getLat() - latOffset, nextLines
								.get(i).get(0).getLon()
								- lonOffset);
//						NogoPoint middlePoint = new NogoPoint(line.get(0).getLat() - (1.5) * latOffset, nextLines
//								.get(i).get(0).getLon()
//								- (1.5) * lonOffset);

						// triangle.getPolygon().add(middlePoint);
						triangle.getPolygon().add(leftTop);
						triangle.getPolygon().add(rightTop);
						triangle.getPolygon().add(rightBottom);

						polygon.add(triangle);

					}

					// Outward right
					if (rightPoint > selfRight
							&& ((leftPoint < selfLeft && leftPoint <= selfRight) || leftPoint <= selfRight)) {
//						System.out.println("Outward right neighbour");

						NogoPolygon triangle = new NogoPolygon();

						// Three nogo points make a triangle
						NogoPoint leftTop = new NogoPoint(line.get(1).getLat() + latOffset, line.get(1).getLon()
								+ lonOffset);

						NogoPoint leftBottom = new NogoPoint(nextLines.get(i).get(1).getLat() + latOffset, line.get(1)
								.getLon() + lonOffset);
						NogoPoint rightBottom = new NogoPoint(nextLines.get(i).get(1).getLat() + latOffset, nextLines
								.get(i).get(1).getLon()
								+ lonOffset);

						triangle.getPolygon().add(leftTop);
						triangle.getPolygon().add(leftBottom);
						triangle.getPolygon().add(rightBottom);

						polygon.add(triangle);

					}

					// Inward right

					if (rightPoint < selfRight && rightPoint > selfLeft) {

						NogoPolygon triangle = new NogoPolygon();

						// System.out.println("Inwarde Right neighbour");

						// Three nogo points make a triangle

						NogoPoint leftTop = new NogoPoint(line.get(1).getLat() - latOffset, line.get(1).getLon()
								+ lonOffset);
						NogoPoint rightTop = new NogoPoint(line.get(1).getLat() - latOffset, nextLines.get(i).get(1)
								.getLon()
								+ lonOffset);
						NogoPoint rightBottom = new NogoPoint(nextLines.get(i).get(1).getLat() - latOffset, nextLines
								.get(i).get(1).getLon()
								+ lonOffset);

						// triangle.getPolygon().add(middlePoint);
						triangle.getPolygon().add(leftTop);
						triangle.getPolygon().add(rightTop);
						triangle.getPolygon().add(rightBottom);

						polygon.add(triangle);

					}

				}

			}
		}

		return polygon;
	}

	
	static List<NogoPolygon> triangleOverlap(List<NogoPolygon> triangles){
		List<NogoPolygon> result = new ArrayList<NogoPolygon>();
	
		
		//If there's only one triangle, it can't overlap with any other triangles
		if (triangles.size() > 1){
		
//			System.out.println("We have a total of: " + triangles.size());
			
		for (int i = 0; i < triangles.size(); i++) {
			
			//Does it clash with any triangle within the triangles list that's not itself
			//If it doesn't add it to our result
			NogoPolygon currentTriangle = triangles.get(i);
			
			if (!overlap(currentTriangle, triangles)){
				result.add(currentTriangle);
			}
			
			
		}
		}else{
			result.addAll(triangles);
		}
		
		return result;
		
	}
	
	
	static boolean overlap(NogoPolygon triangle, List<NogoPolygon> triangles){
		
		double leftLon = getLeft(triangle);
		double rightLon = getRight(triangle);
		
//		double latTop = Math.floor(getLatMax(triangle)*1000);
		double latBot = Math.floor(getLatMax(triangle)*1000);
		
//		System.out.println("Triangle at top: " + latTop);
//		System.out.println("Triangle at bot: " + latBot);
		
		for (int i = 0; i < triangles.size(); i++) {
			NogoPolygon currentTriangle = triangles.get(i);
			double leftLonCurrent = getLeft(currentTriangle);
			double rightLonCurrent = getRight(currentTriangle);
			
//			double latTopCurrent = Math.floor(getLatMax(currentTriangle)*1000);
			double latBotCurrent = Math.floor(getLatMax(currentTriangle)*1000);

//			System.out.println("Triangle current at top: " + latTopCurrent);
//			System.out.println("Triangle current at bot: " + latBotCurrent);
			
//			
//			System.out.println(latTop == latTopCurrent && latBot == latBotCurrent);
			

			if (currentTriangle != triangle && latBot == latBotCurrent){
//				System.out.println("It's not current");
				
				
				if (
						leftLon < leftLonCurrent && leftLonCurrent < rightLon
						||
						leftLonCurrent > leftLon && rightLon  > rightLonCurrent
						||
						leftLon > leftLonCurrent && leftLon < rightLonCurrent
						
//						||
//						leftLon > leftLonCurrent && rightLon < rightLonCurrent
//						||
//						leftLon < leftLonCurrent && rightLon > rightLonCurrent
						
						||
						leftLon == leftLonCurrent && rightLon == rightLonCurrent
						
						||
						leftLon == leftLonCurrent && rightLon < rightLonCurrent
						||
						leftLon == leftLonCurrent && rightLon > rightLonCurrent
						||
						leftLon == leftLonCurrent && rightLonCurrent > rightLon
						
						
//					leftLon < leftLonCurrent && rightLon > leftLonCurrent
//					||
//					rightLon > rightLonCurrent && leftLon > rightLonCurrent
					
						
//						
//						
//||				   leftLon < leftLonCurrent && rightLon < leftLonCurrent
//				|| rightLon < rightLonCurrent && rightLon > leftLonCurrent	
//				|| leftLon > leftLonCurrent && rightLon
//				
				){
//					System.out.println("We have an overlap of :");
//					System.out.println("Left : " + leftLon + " vs. " + leftLonCurrent);
//					System.out.println("Right : " + rightLon + " vs. " + rightLonCurrent);
//					System.out.println("Lat: " + lat +" vs" + currentLat);
					
					return true;
				}
				
			}
					
//					&&
//					(
//			
//							(leftLon > leftLonCurrent && rightLon < rightLonCurrent)
//			||
//			(leftLon < leftLonCurrent && rightLon > rightLonCurrent)
//			)
//			){
				
//			}
			
			
		}
		
		return false;
	}
	
	
	/**
	 * 
	 * @param triangle
	 * @param object
	 * @return
	 */
	static boolean doesOverlap(NogoPolygon triangle, List<List<DepthDenmark>> line) {

		double lonOffset = 0.00055504;

		double leftLon = getLeft(triangle);
		double rightLon = getRight(triangle);

		System.out.println("Triangle left: " +leftLon);
		System.out.println("Triangle right: " +rightLon);
		
		double LineLeftLon;
		double LineRightLon;

		for (int i = 0; i < line.size(); i++) {
			
			

			// If not singleton
			if (line.get(i).size() != 1) {
				LineLeftLon = line.get(i).get(0).getLon() - lonOffset;
				LineRightLon = line.get(i).get(1).getLon() + lonOffset;

				System.out.println("Does the triangle overlap with left: " + LineLeftLon + " and right: " + LineRightLon);
				
//				if (leftLon < LineLeftLon && rightLon > LineLeftLon){
//					System.out.println("Overlap!");
//					return true;
//				}
//				
//				if (leftLon < LineRightLon && rightLon > LineRightLon ){
//					System.out.println("Overlap!");
//					return true;
//				}
//				
//				if (leftLon == LineLeftLon && rightLon > LineLeftLon){
//					System.out.println("Overlap!");
//					return true;
//				}
//				
//				if (leftLon == LineRightLon && rightLon > LineRightLon ){
//					System.out.println("Overlap!");
//					return true;
//				}
//				
//				if (leftLon < LineLeftLon && rightLon == LineRightLon ){
//					System.out.println("Overlap!");
//					return true;
//				}
//				
//				if (leftLon < LineLeftLon && rightLon > LineRightLon){
//					System.out.println("Overlap!");
//					return true;
//				}
//				
//				if (leftLon < LineLeftLon && rightLon < LineRightLon && rightLon > LineLeftLon){
//					System.out.println("Overlap!");
//					return true;
//				}


				
			}

		}

		return false;
	}

	static double getLatMin(NogoPolygon triangle){
		
		double min = 999.0;
		for (int i = 0; i < triangle.getPolygon().size(); i++) {
			if (min > triangle.getPolygon().get(i).getLat()) {
				min = triangle.getPolygon().get(i).getLat();
			}
		}
		return min + 0.00055504;
	
	}
	
	static double getLatMax(NogoPolygon triangle) {
		double max = -999.0;

		for (int i = 0; i < triangle.getPolygon().size(); i++) {
			if (max < triangle.getPolygon().get(i).getLat()) {
				max = triangle.getPolygon().get(i).getLat();
			}
		}

		return max;
	}
	
	
	
	static double getLeft(NogoPolygon triangle) {
		double left = 999.0;
		for (int i = 0; i < triangle.getPolygon().size(); i++) {
			if (left > triangle.getPolygon().get(i).getLon()) {
				left = triangle.getPolygon().get(i).getLon();
			}
		}
		return left;
	}

	static double getRight(NogoPolygon triangle) {
		double right = -999.0;

		for (int i = 0; i < triangle.getPolygon().size(); i++) {
			if (right < triangle.getPolygon().get(i).getLon()) {
				right = triangle.getPolygon().get(i).getLon();
			}
		}

		return right;
	}

}
