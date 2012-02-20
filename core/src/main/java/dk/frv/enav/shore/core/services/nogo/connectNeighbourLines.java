package dk.frv.enav.shore.core.services.nogo;

import java.util.ArrayList;
import java.util.List;

import dk.frv.enav.common.xml.nogo.types.NogoPoint;
import dk.frv.enav.common.xml.nogo.types.NogoPolygon;
import dk.frv.enav.shore.core.domain.DepthDenmark;

public class connectNeighbourLines {

	public static void main(String args[]) {

		System.out.println("Starting to connect neighbours!");

		// Polygons = new ArrayList<List<Point>>();
		// List<Point> V = new ArrayList<Point>();
		// List<Point> K = new ArrayList<Point>();

		// We have the main line consisting of 2 points
		//
		// Point point0 = new Point(0,2);
		// Point point1 = new Point(0,5);
		//
		//
		// List<Point> line = new ArrayList<Point>();
		// line.add(point0);
		// line.add(point1);
		//
		//
		// List<List<Point>> nextLines = new ArrayList<List<Point>>();
		// //We have a list of lines, ie. all the lines located at m+1 from
		// original
		//
		// List<Point> line1 = new ArrayList<Point>();
		//
		// line1.add(new Point(1,0));
		// line1.add(new Point(1,3));
		// // line1.add(new Point(1,5));
		// // line1.add(new Point(1,8));
		//
		//
		// List<Point> line2 = new ArrayList<Point>();
		//
		// line2.add(new Point(1,1));
		// line2.add(new Point(1,6));
		//
		//
		// List<Point> line3 = new ArrayList<Point>();
		//
		// line3.add(new Point(1,3));
		// line3.add(new Point(1,8));
		//
		//
		// List<Point> line4 = new ArrayList<Point>();
		//
		// line4.add(new Point(1,0));
		// line4.add(new Point(1,2));
		//
		//
		// nextLines.add(line1);
		// nextLines.add(line2);
		// nextLines.add(line3);
		// nextLines.add(line4);
		//
		// System.out.println("The line are are investigating is: " + line);
		// System.out.println("The next lines one down is: " + nextLines);
		//
		// List<List<Point>> possibleNeighbours =
		// connectFindValidNeighbours(line, nextLines);
		//
		//
	}

	static List<NogoPolygon> connectFindValidNeighbours(List<DepthDenmark> line, List<List<DepthDenmark>> nextLines) {

		double latOffset = 0.00055504;
		double lonOffset = 0.00055504;

		List<NogoPolygon> polygon = new ArrayList<NogoPolygon>();

		if (line.size() == 1) {
//			System.out.println("singleton shit");
		} else {

			int selfLeft = line.get(0).getN();
			int selfRight = line.get(1).getN();

			// System.out.println("left end point is: " + selfLeft + " lon is: "
			// + line.get(0).getLon() + " lat is: " + line.get(0).getLat() );
			// System.out.println("right end point is: " + selfRight +
			// " lon is: " + line.get(1).getLon() + " lat is: " +
			// line.get(1).getLat());

			// Look at each of the lines in the nextLines list
			for (int i = 0; i < nextLines.size(); i++) {
				// System.out.println("Investigating the line: " +
				// nextLines.get(i));

				if (nextLines.get(i).size() == 1) {
//					System.out.println("singleton shit");
				} else {

					int leftPoint = nextLines.get(i).get(0).getN();
					int rightPoint = nextLines.get(i).get(1).getN();
					//
					// System.out.println("left end point investigated: " +
					// leftPoint + " lon is: " +
					// nextLines.get(i).get(0).getLon() + " lat is: " +
					// nextLines.get(i).get(0).getLat());
					// System.out.println("right end point investigated: " +
					// rightPoint + " lon is: " +
					// nextLines.get(i).get(1).getLon() + " lat is: " +
					// nextLines.get(i).get(0).getLat());

					// Outward left
					if (leftPoint < selfLeft
							&& ((rightPoint < selfRight && rightPoint >= selfLeft) || rightPoint >= selfLeft)) {
//						System.out.println("Outward left neighbour");

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
//						System.out.println("Inwarde left neighbour");

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
						NogoPoint middlePoint = new NogoPoint(line.get(0).getLat() - (1.5) * latOffset, nextLines
								.get(i).get(0).getLon()
								- (1.5) * lonOffset);

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
								.getLon()
								+ lonOffset);
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
						
//						System.out.println("Inwarde Right neighbour");

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

}
