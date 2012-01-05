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
package dk.frv.enav.shore.core.services.nogo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import dk.frv.ais.geo.GeoLocation;
import dk.frv.enav.common.util.Point;
import dk.frv.enav.common.xml.nogo.request.NogoRequest;
import dk.frv.enav.common.xml.nogo.response.NogoResponse;
import dk.frv.enav.common.xml.nogo.types.BoundingBoxPoint;
import dk.frv.enav.common.xml.nogo.types.NogoPoint;
import dk.frv.enav.common.xml.nogo.types.NogoPolygon;
import dk.frv.enav.shore.core.domain.DepthDenmark;
import dk.frv.enav.shore.core.services.ServiceException;

@Stateless
public class NogoServiceBean implements NogoService {

	@PersistenceContext(unitName = "enav")
	private EntityManager entityManager;

	@Override
	public NogoResponse nogoPoll(NogoRequest nogoRequest)
			throws ServiceException {

		

		System.out.println("NoGo request recieved");
		BoundingBoxPoint firstPos = getArea(nogoRequest.getNorthWestPointLat(),
				nogoRequest.getNorthWestPointLon());
		System.out.println("First point found");
		BoundingBoxPoint secondPos = getArea(
				nogoRequest.getSouthEastPointLat(),
				nogoRequest.getSouthEastPointLon());


//firstPos = getArea(55.070, 11.668);
//secondPos = getArea(55.170, 11.868);

		// BoundingBoxPoint firstPos = getArea(56.1106, 12.1290);
		// BoundingBoxPoint secondPos = getArea(55.2878, 12.955);

		List<NogoPolygon> polyArea = new ArrayList<NogoPolygon>();

		if (firstPos != null && secondPos != null) {
			System.out.println("Bounding Box found - requesting data");
			polyArea = getNogoArea(firstPos, secondPos, nogoRequest.getDraught());
//			polyArea = getNogoArea(firstPos, secondPos, -7);
			System.out.println("Data recieved and parsed");
		}

		NogoResponse res = new NogoResponse();

		for (int i = 0; i < polyArea.size(); i++) {
			res.addPolygon(polyArea.get(i));
		}

		Date currentDate = new Date();
		long futureDate = currentDate.getTime() + 7200000;

		res.setValidFrom(new Date());
		res.setValidTo(new Date(futureDate));

		System.out.println("Sending data");

		return res;
	}

	@Override
	@SuppressWarnings("unchecked")
	public BoundingBoxPoint getArea(double lat, double lon) {

		GeoLocation pos1 = new GeoLocation(lat, lon);


		Query query = entityManager
				.createQuery("SELECT dd.n, dd.m, dd.lat, dd.lon "
						+ "FROM DepthDenmark dd "
						+ "where dd.lat between :lat1 AND :lat1range " + "AND "
						+ "dd.lon between :lon1 AND :lon1range");

		
		query.setParameter("lat1", lat);
		query.setParameter("lat1range", lat + 0.01);

		query.setParameter("lon1", lon);
		query.setParameter("lon1range", lon + 0.01);

		List<Object[]> lines = query.getResultList();

		if (lines.size() != 0) {
			double distance = 9999999;

			Object[] bestMatch = null;

			for (Object[] objects : lines) {
				// Minimum distance
				GeoLocation pos = new GeoLocation((Double) objects[2],
						(Double) objects[3]);
				double distancePoint = pos.getGeodesicDistance(pos1);
				// System.out.println(distancePoint + " for " + (Integer)
				// objects[0] + " , " + (Integer) objects[1] + " at " + pos);
				if (distancePoint < distance) {
					distance = distancePoint;
					bestMatch = objects;
				}
			}
			// System.out.println("The point we're looking for is at " +
			// bestMatch[0] +" , " + bestMatch[1]);
			BoundingBoxPoint point = new BoundingBoxPoint(
					(Integer) bestMatch[0], (Integer) bestMatch[1]);

			return point;

		}
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<NogoPolygon> getNogoArea(BoundingBoxPoint firstPos,
			BoundingBoxPoint secondPos, double draught) {

		int n1 = 0;
		int m1 = 0;

		int n2 = 0;
		int m2 = 0;

		if (firstPos.getN() < secondPos.getN()) {
			// firstPos n is at the top
			n1 = firstPos.getN();
			n2 = secondPos.getN();
		} else {
			n1 = secondPos.getN();
			n2 = firstPos.getN();
		}

		if (firstPos.getM() < secondPos.getM()) {
			// firstPos m is to the far left
			m1 = firstPos.getM();
			m2 = secondPos.getM();
		} else {
			m1 = secondPos.getM();
			m2 = firstPos.getM();
		}

		// This is where we store our result
		List<NogoPolygon> res = new ArrayList<NogoPolygon>();
		
		System.out.println(n1);
		System.out.println(n2);
		System.out.println(m1);
		System.out.println(m2);
		System.out.println(draught);
		
		
		Query query = entityManager.createQuery("SELECT dd " +
				"FROM DepthDenmark dd " +
				"WHERE dd.n between :n1 AND :n2 " +
				"AND dd.m between :m1 AND :m2 " +
				"AND dd.depth > :d1 ORDER BY M, N");

			query.setParameter("n1", n1);
			query.setParameter("n2", n2);
			query.setParameter("m1", m1);
			query.setParameter("m2", m2);
			query.setParameter("d1", draught);			
			

			List<DepthDenmark> result = query.getResultList();
		
			
			List<List<DepthDenmark>> lines = new ArrayList<List<DepthDenmark>>();
			int m = -1;
			List<DepthDenmark> line = null;
			for (DepthDenmark depthDenmark : result) {
			//What is the index, n
			if(depthDenmark.getM() > m){
			line = new ArrayList<DepthDenmark>();
			lines.add(line);
			m = depthDenmark.getM();
			}
			line.add(depthDenmark);
			
			}
//			System.out.println("There are: " + lines.size() + " lines");
			
			/**
			
		// Full query
		String sqlQuery =
			"SELECT lat,lon from (" +
			"SELECT * from depth_denmark100m master "+
			"where n between :n1 and :n2 "+
			"and m between :m1 and :m2 "+
			"and depth > :d1 "+
			"and not exists "+
			"(select 1 from depth_denmark100m s  "+
			"where n between :n1 and :n2 "+
			"and m between :m1 and :m2 "+
			"and s.m=master.m "+
			"and s.n=master.n-1 "+
			"and depth > :d1 "+
			") "+
			"union all "+
			"select * from depth_denmark100m master2 "+
			"where n between :n1 and :n2 "+
			"and m between :m1 and :m2 "+
			"and depth > :d1 "+
			"and not exists "+
			"(select 1 from depth_denmark100m s2  "+
			"where n between :n1 and :n2 "+
			"and m between :m1 and :m2 "+
			"and s2.m=master2.m "+
			"and s2.n=master2.n+1 "+
			"and depth > :d1) "+
			") a "+
			"order by m,n; "
			;

		System.out.println("Executing query");
		
		Query query = entityManager.createNativeQuery(sqlQuery);


		query.setParameter("n1", n1);
		query.setParameter("n2", n2);
		query.setParameter("m1", m1);
		query.setParameter("m2", m2);
		query.setParameter("d1", draught);

		List<Object[]> result = query.getResultList();

		
		**/
		
		System.out.println("Query executed! - parsing");
				


		// double lonOffset = 0.0007854;
		//The difference between each point / 2. This is used in calculating the polygons surrounding the lines
		
		//100m spacing
		double latOffset = 0.00055504;
		
		//50m spacing
		//double latOffset = 0.000290;
		
		System.out.println("Parsing Query");

		int i = 0;

		ParseData parseData = new ParseData();
		
		
		
		List<List<DepthDenmark>> parsed = parseData.getParsed(lines);
		
		//parsed = lines;
		
		System.out.println(lines.size());
		System.out.println(parsed.size());
		
		for (int j = 0; j < parsed.size(); j++) {
			System.out.println(parsed.get(j).size());
		}
		
		NogoPolygon polygon;
		NogoPolygon temp;
		
		for (List<DepthDenmark> splittedLines : parsed) {

			if (splittedLines.size() == 1){
				NogoPoint point = new NogoPoint( splittedLines.get(0).getLat(), splittedLines.get(0).getLon() );
				temp = new NogoPolygon();
				temp.getPolygon().add(point);
				temp.getPolygon().add(point);
			}else{
				temp = new NogoPolygon();
			for (DepthDenmark dataEntries : splittedLines) {
				NogoPoint point = new NogoPoint( dataEntries.getLat(), dataEntries.getLon() );
				temp.getPolygon().add(point);
			}
			}
			
			NogoPoint westPoint = temp.getPolygon().get(0);
			NogoPoint eastPoint = temp.getPolygon().get(1);
			
			NogoPoint northWest = new NogoPoint(westPoint.getLat() + latOffset, westPoint.getLon());

			NogoPoint northEast = new NogoPoint(eastPoint.getLat() + latOffset, eastPoint.getLon());

			NogoPoint southWest = new NogoPoint(westPoint.getLat() - latOffset, westPoint.getLon());

			NogoPoint southEast= new NogoPoint(eastPoint.getLat() - latOffset, eastPoint.getLon());


			polygon  = new NogoPolygon();
			
			polygon.getPolygon().add(northWest);
			polygon.getPolygon().add(southWest);
			polygon.getPolygon().add(southEast);
			polygon.getPolygon().add(northEast);
			
			
			res.add(polygon);
		}
		
		System.out.println(res.size());
		/**
		
		for (DepthDenmark objects : result) {
		NogoPoint point = new NogoPoint( objects.getLat(), objects.getLon() );
		if (polygon.getPolygon().size() == 1){
		polygon.getPolygon().add(point);

		temp = new NogoPolygon();
		//The line is complete - time to process it.
		NogoPoint westPoint = polygon.getPolygon().get(0);
		NogoPoint eastPoint = polygon.getPolygon().get(1);

		NogoPoint northWest = new NogoPoint(westPoint.getLat() + latOffset, westPoint.getLon());

		NogoPoint northEast = new NogoPoint(eastPoint.getLat() + latOffset, eastPoint.getLon());

		NogoPoint southWest = new NogoPoint(westPoint.getLat() - latOffset, westPoint.getLon());

		NogoPoint southEast= new NogoPoint(eastPoint.getLat() - latOffset, eastPoint.getLon());


		temp.getPolygon().add(northWest);
		temp.getPolygon().add(southWest);
		temp.getPolygon().add(southEast);
		temp.getPolygon().add(northEast);

		res.add(temp);
		polygon = new NogoPolygon();
		}else{
		polygon.getPolygon().add(point);
		}
		i++;

		}
		**/

			
		return res;
			}

	public boolean parsedPointsContain(List<Point> list, Point point) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).n == point.n && list.get(i).m == point.m) {
				return true;
			}
		}

		return false;

	}

	@Override
	public int getDataCount() {
		Query query = entityManager
				.createQuery("SELECT COUNT(*) FROM DepthDenmark");
		Long count = (Long) query.getSingleResult();
		return count.intValue();
	}

}
