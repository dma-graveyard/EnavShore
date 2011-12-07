/*
 * Copyright 2011 Danish Maritime Safety Administration. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   1. Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 *
 *   2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY Danish Maritime Safety Administration ``AS IS'' 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL <COPYRIGHT HOLDER> OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 * The views and conclusions contained in the software and documentation are those
 * of the authors and should not be interpreted as representing official policies,
 * either expressed or implied, of Danish Maritime Safety Administration.
 * 
 */

package dk.frv.enav.common.util;

import java.util.ArrayList;
import java.util.List;

public class PolygonParser {
	private List<List<Point>> Polygons;
	private List<Point> V;
	private List<Point> K;

	public PolygonParser(List<Point> depthDenmarkList) {
		Polygons = new ArrayList<List<Point>>();

		// V = new ArrayList<Point>();
		K = new ArrayList<Point>();

		V = depthDenmarkList;

		//System.out.println("K is " + K.size());
		//System.out.println("V is " + V.size());

		// findEdges(V, K);

		for (int i = 0; i <= V.size(); i++) {
			findEdges();
		}

		Polygons.remove(0);
	}

	public List<List<Point>> getPolygons() {
		return Polygons;
	}

	public void findEdges() {

		Point currentPoint = null;
		// System.out.println("K is " + K.size());
		// System.out.println("V is " + V.size());
		int i;
		// Have we processed all the elements in our current polygon?
		if (allProcessed(K)) {
			//System.out.println("Taking element from V - new polygon found");
			// This means we have found a new polygon.
			// Add old one to a global list of plyons
			// Create a new one
			Polygons.add(K);
			K = new ArrayList<Point>();

			for (i = 0; i < V.size(); i++) {
				if (V.get(i).isProcessed == false) {
					// System.out.println("Element found in V");
					currentPoint = V.get(i);
					currentPoint.isProcessed = true;
					K.add(currentPoint);
					// System.out.println("The element is: " + currentPoint);
					break;
				}
			}
		} else {
			// We still need to process some elements
			// System.out.println("Taking element from K");
			for (i = 0; i < K.size(); i++) {
				if (K.get(i).isProcessed == false) {
					// System.out.println("Element found in K");
					currentPoint = K.get(i);
					// System.out.println("The element is: " + currentPoint);
					currentPoint.isProcessed = true;
					// currentPoint = K.updateProcessed(currentPoint);
					// V.updateProcessed(currentPoint);

					break;
				}
			}
		}
		if (currentPoint != null) {

			// Generate a list of neighbours
			List<Point> neighbours = findNeighbours(currentPoint, V);

			// Add our neighbours to possible edges if not preciously processed

			// Does the neighbour exist?

			for (Point boundingBoxPoint : neighbours) {
				if (boundingBoxPoint.isProcessed == false
						&& !K.contains(boundingBoxPoint)) {
					K.add(boundingBoxPoint);
				}
			}
			// System.out.println("The element had " + neighbours.size()
			// + " neighbours");
			if (neighbours.size() == 4) {
				// System.out.println("Removing the element from the polygon");
				K.remove(currentPoint);
			}
		}
		/**
		 * findEdges(V, K); } else { Polygons.add(K);
		 * System.out.println("Algorithm terminated"); //
		 * System.out.println("The polygon consists of elements: "); }
		 **/
	}

	public static List<Point> findNeighbours(Point point, List<Point> V) {
		// System.out.println("Searching for neighbours");
		List<Point> neighbours = new ArrayList<Point>();

		Point neighbour1 = new Point(point.n, point.m + 1);
		Point neighbour2 = new Point(point.n, point.m - 1);
		Point neighbour3 = new Point(point.n + 1, point.m);
		Point neighbour4 = new Point(point.n - 1, point.m);

		for (int i = 0; i < V.size(); i++) {
			if ((V.get(i).n == neighbour1.n && V.get(i).m == neighbour1.m)
					|| (V.get(i).n == neighbour2.n && V.get(i).m == neighbour2.m)
					|| (V.get(i).n == neighbour3.n && V.get(i).m == neighbour3.m)
					|| (V.get(i).n == neighbour4.n && V.get(i).m == neighbour4.m)) {
				neighbours.add(V.get(i));
			}
		}

		return neighbours;

	}

	public static boolean allProcessed(List<Point> k) {
		for (int i = 0; i < k.size(); i++) {
			if (k.get(i).isProcessed == false) {
				return false;
			}
		}
		return true;
	}

}
