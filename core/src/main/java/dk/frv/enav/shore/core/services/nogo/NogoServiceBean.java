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
import dk.frv.enav.common.xml.nogo.request.NogoRequest;
import dk.frv.enav.common.xml.nogo.response.NogoResponse;
import dk.frv.enav.common.xml.nogo.types.BoundingBoxPoint;
import dk.frv.enav.common.xml.nogo.types.NogoPoint;
import dk.frv.enav.common.xml.nogo.types.NogoPolygon;
import dk.frv.enav.shore.core.domain.DepthDenmark;
import dk.frv.enav.shore.core.domain.TideDenmark;
import dk.frv.enav.shore.core.services.ServiceException;

@Stateless
public class NogoServiceBean implements NogoService {

	@PersistenceContext(unitName = "enav")
	private EntityManager entityManager;
	
	
	public enum WorkerType {
		 DEPTHPOINT, TIDEPOINT, DEPTHDATA, TIDEDATA, MAXTIDE;
		}
	

	@Override
	public NogoResponse nogoPoll(NogoRequest nogoRequest)
			throws ServiceException {

		
		
		System.out.println("NoGo request recieved");
		
		NogoWorker nogoWorkerFirstPointDepth = new NogoWorker(entityManager, WorkerType.DEPTHPOINT);
		
		NogoWorker nogoWorkerSecondPointDepth = new NogoWorker(entityManager, WorkerType.DEPTHPOINT);
		
		
		NogoWorker nogoWorkerFirstPointTide = new NogoWorker(entityManager, WorkerType.TIDEPOINT);
		
		NogoWorker nogoWorkerSecondPointTide = new NogoWorker(entityManager, WorkerType.TIDEPOINT);
				
		
		NogoWorker nogoWorkerThirdMaxTide = new NogoWorker(entityManager, WorkerType.MAXTIDE);

		
		
		
		nogoWorkerFirstPointDepth.setPos(new GeoLocation(nogoRequest
				.getNorthWestPointLat(), nogoRequest.getNorthWestPointLon()));

		nogoWorkerSecondPointDepth.setPos(new GeoLocation(nogoRequest
				.getSouthEastPointLat(), nogoRequest.getSouthEastPointLon()));
		

		nogoWorkerFirstPointTide.setPos(new GeoLocation(nogoRequest
				.getNorthWestPointLat(), nogoRequest.getNorthWestPointLon()));

		nogoWorkerSecondPointTide.setPos(new GeoLocation(nogoRequest
				.getSouthEastPointLat(), nogoRequest.getSouthEastPointLon()));
		
		
		
		
		
		//Get the grid position of the data in the depth database
		nogoWorkerFirstPointDepth.start();
		nogoWorkerSecondPointDepth.start();
		
		//Get the grid position of the data in the tide database
		nogoWorkerFirstPointTide.start();
		nogoWorkerSecondPointTide.start();
		
		//Find max change in depth database
		nogoWorkerThirdMaxTide.start();
		
		//nogoRequest.getStartDate();

	
		
		
//		BoundingBoxPoint firstPos = getAreaDepthDenmark(
//				nogoRequest.getNorthWestPointLat(),
//				nogoRequest.getNorthWestPointLon());

		// BoundingBoxPoint secondPos = getAreaDepthDenmark(
		// nogoRequest.getSouthEastPointLat(),
		// nogoRequest.getSouthEastPointLon());



		try {
			nogoWorkerFirstPointDepth.join();
			System.out.println("First depth point found");
			nogoWorkerSecondPointDepth.join();
			System.out.println("Second depth point found");
			nogoWorkerThirdMaxTide.join();
			System.out.println("MaxTide found");
			
			nogoWorkerFirstPointTide.join();
			System.out.println("First tide point found");
			nogoWorkerSecondPointTide.join();
			System.out.println("Second tide point found");
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		BoundingBoxPoint firstPosDepth = nogoWorkerFirstPointDepth.getPoint();
		BoundingBoxPoint secondPosDepth = nogoWorkerSecondPointDepth.getPoint();
		
		BoundingBoxPoint firstPosTide = nogoWorkerFirstPointTide.getPoint();
		BoundingBoxPoint secondPosTide = nogoWorkerSecondPointTide.getPoint();
		
		double depthOffset = nogoWorkerThirdMaxTide.getMaxDepth();
		
		//System.out.println(nogoRequest.getDraught());
		//System.out.println(depthOffset);
		
		// firstPos = getArea(55.070, 11.668);
		// secondPos = getArea(55.170, 11.868);

		// BoundingBoxPoint firstPos = getArea(56.1106, 12.1290);
		// BoundingBoxPoint secondPos = getArea(55.2878, 12.955);

		List<NogoPolygon> polyArea = new ArrayList<NogoPolygon>();

		if (firstPosDepth != null && secondPosDepth != null) {
			System.out.println("Bounding Box found - requesting data");
			
			
			NogoWorker nogoWorkerDepthData = new NogoWorker(entityManager, WorkerType.DEPTHDATA);
			
			NogoWorker nogoWorkerTideData = new NogoWorker(entityManager, WorkerType.TIDEDATA);
			
			nogoWorkerDepthData.setFirstPos(firstPosDepth);
			nogoWorkerDepthData.setSecondPos(secondPosDepth);
			nogoWorkerDepthData.setDraught(nogoRequest.getDraught() + depthOffset);
			
			nogoWorkerTideData.setFirstPos(firstPosTide);
			nogoWorkerTideData.setSecondPos(secondPosTide);
			
			nogoWorkerDepthData.start();
			
			nogoWorkerTideData.start();
			
			try {
				nogoWorkerDepthData.join();
				System.out.println("Depth data thread joined");
				nogoWorkerTideData.join();
				System.out.println("Tide data thread joined");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			polyArea = parseResult(nogoWorkerDepthData.getDepthDatabaseResult()
					, nogoWorkerTideData.getTideDatabaseResult(), nogoRequest.getDraught());
			
			
			//Add the offset to draught, note draught is negative while offset is positive
			//We want to increase the area that is being searched for
			//So we add it, ie instead of only allowing search on 7, we allow search from 5
//			polyArea = getNogoArea(firstPosDepth, secondPosDepth,
//					nogoRequest.getDraught() + depthOffset);
//			

			// polyArea = getNogoArea(firstPos, secondPos, -7);
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
	public BoundingBoxPoint getAreaDepthDenmark(double lat, double lon) {

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
	public BoundingBoxPoint getTideDenmark(double lat, double lon) {
		GeoLocation pos1 = new GeoLocation(lat, lon);

		Query query = entityManager
				.createQuery("SELECT dd.n, dd.m, dd.lat, dd.lon "
						+ "FROM TideDenmark dd "
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

		Query query = entityManager.createQuery("SELECT dd "
				+ "FROM DepthDenmark dd " + "WHERE dd.n between :n1 AND :n2 "
				+ "AND dd.m between :m1 AND :m2 "
				+ "AND dd.depth > :d1 ORDER BY M, N");

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
			// What is the index, n
			if (depthDenmark.getM() > m) {
				line = new ArrayList<DepthDenmark>();
				lines.add(line);
				m = depthDenmark.getM();
			}
			line.add(depthDenmark);

		}
		// System.out.println("There are: " + lines.size() + " lines");


		System.out.println("Query executed! - parsing");

		// double lonOffset = 0.0007854;
		// The difference between each point / 2. This is used in calculating
		// the polygons surrounding the lines

		// 100m spacing
		double latOffset = 0.00055504;

		// 50m spacing
		// double latOffset = 0.000290;

		System.out.println("Parsing Query");

		ParseData parseData = new ParseData();

		List<List<DepthDenmark>> parsed = parseData.getParsed(lines);

		// parsed = lines;

//		System.out.println(lines.size());
//		System.out.println(parsed.size());
//
//		for (int j = 0; j < parsed.size(); j++) {
//			System.out.println(parsed.get(j).size());
//		}

		NogoPolygon polygon;
		NogoPolygon temp;

		for (List<DepthDenmark> splittedLines : parsed) {

			if (splittedLines.size() == 1) {
				NogoPoint point = new NogoPoint(splittedLines.get(0).getLat(),
						splittedLines.get(0).getLon());
				temp = new NogoPolygon();
				temp.getPolygon().add(point);
				temp.getPolygon().add(point);
			} else {
				temp = new NogoPolygon();
				for (DepthDenmark dataEntries : splittedLines) {
					NogoPoint point = new NogoPoint(dataEntries.getLat(),
							dataEntries.getLon());
					temp.getPolygon().add(point);
				}
			}

			NogoPoint westPoint = temp.getPolygon().get(0);
			NogoPoint eastPoint = temp.getPolygon().get(1);

			NogoPoint northWest = new NogoPoint(westPoint.getLat() + latOffset,
					westPoint.getLon());

			NogoPoint northEast = new NogoPoint(eastPoint.getLat() + latOffset,
					eastPoint.getLon());

			NogoPoint southWest = new NogoPoint(westPoint.getLat() - latOffset,
					westPoint.getLon());

			NogoPoint southEast = new NogoPoint(eastPoint.getLat() - latOffset,
					eastPoint.getLon());

			polygon = new NogoPolygon();

			polygon.getPolygon().add(northWest);
			polygon.getPolygon().add(southWest);
			polygon.getPolygon().add(southEast);
			polygon.getPolygon().add(northEast);

			res.add(polygon);
		}

		System.out.println(res.size());

		return res;
	}

	@Override
	public double maxTideDepth() {
		
		Double maxDepth = 0.0;

		Query query = entityManager
				.createQuery("SELECT max(td.depth) "
						+ "FROM TideDenmark td ");
		
		Object queryResult = query.getSingleResult();
		
		if (queryResult != null){
		maxDepth = (Double) queryResult;	
		}

		System.out.println(maxDepth);
		
		return maxDepth;
	}
	
	@Override
	public List<NogoPolygon> parseResult(List<DepthDenmark> result, List<TideDenmark> resultTide, double depth){
		
		
		
		
		
		// This is where we store our result
		List<NogoPolygon> res = new ArrayList<NogoPolygon>();
		
		
		
		//Seperate it into lines - depth
		List<List<DepthDenmark>> lines = new ArrayList<List<DepthDenmark>>();
		int m = -1;
		List<DepthDenmark> line = null;
		for (DepthDenmark depthDenmark : result) {
			// What is the index, n
			if (depthDenmark.getM() > m) {
				line = new ArrayList<DepthDenmark>();
				lines.add(line);
				m = depthDenmark.getM();
			}
			line.add(depthDenmark);

		}

		
		
		
		//Seperate it into lines - depth
		List<List<TideDenmark>> linesTide = new ArrayList<List<TideDenmark>>();
		int mT = -1;
		List<TideDenmark> lineTide = null;
		for (TideDenmark tideDenmark : resultTide) {
			// What is the index, n
			if (tideDenmark.getM() > mT) {
				lineTide = new ArrayList<TideDenmark>();
				linesTide.add(lineTide);
				mT = tideDenmark.getM();
			}
			lineTide.add(tideDenmark);

		}		
		
		System.out.println("Lines depth has " + lines.size() + " lines");
		System.out.println("Lines tide has " + linesTide.size() + " lines");
		
		//Combine the two into one result
		
		int j = 0;
		
		for (int i = 0; i < linesTide.size(); i++) {
			
			List<TideDenmark> currentTideLine = linesTide.get(i);
			
			//Not always divisible by 5
			
			//Function takes the lines and j, does it on 5 at a time
			//Also takes currentTideLine
			//Find the remainder, do it until j + 5 is equal or less than lines.size()
			
			//Then special cases for rest
			
			
			
			//Not always divisble by 8
			//Function that takes a single line and q, and does it on 8 points at a time
			//Also takes currentTideDepth to apply to each
			//Find the remainder, do it until q+8 is equal or less than lines.size()
			
			//Then special cases for rest
			
			
			
			
			//Going down in the tide db is the same as going down 5 times in depth db
			List<DepthDenmark> firstDepthLine = lines.get(j);
			List<DepthDenmark> secondDepthLine = lines.get(j+1);
			List<DepthDenmark> thirdDepthLine = lines.get(j+2);
			List<DepthDenmark> fourthDepthLine = lines.get(j+3);
			List<DepthDenmark> fifthDepthLine = lines.get(j+4);

			for (int k = 0; k < currentTideLine.size(); k++) {
				double currentTideDepth = 0.0;
				if (currentTideLine.get(k).getDepth() != null){
					currentTideDepth = currentTideLine.get(k).getDepth();
				}
				
				int q = 0;
				
				if (firstDepthLine.size() > 8)
				for (int l = 0; l < firstDepthLine.size()/8; l++) {
					//Do for 8 points
					
					//First subtract the value
					firstDepthLine.get(q).setDepth(firstDepthLine.get(q).getDepth() - currentTideDepth);
					//Should it still be a nogo?
					if (firstDepthLine.get(q).getDepth() > depth){
						firstDepthLine.remove(q);
					}
					
					//First subtract the value
					firstDepthLine.get(q+1).setDepth(firstDepthLine.get(q+1).getDepth() - currentTideDepth);
					//Should it still be a nogo?
					if (firstDepthLine.get(q+1).getDepth() > depth){
						firstDepthLine.remove(q+1);
					}

					//First subtract the value
					firstDepthLine.get(q+2).setDepth(firstDepthLine.get(q+2).getDepth() - currentTideDepth);
					//Should it still be a nogo?
					if (firstDepthLine.get(q+2).getDepth() > depth){
						firstDepthLine.remove(q+2);
					}									
					
					//First subtract the value
					firstDepthLine.get(q+3).setDepth(firstDepthLine.get(q+1).getDepth() - currentTideDepth);
					//Should it still be a nogo?
					if (firstDepthLine.get(q+3).getDepth() > depth){
						firstDepthLine.remove(q+3);
					}
					
					//First subtract the value
					firstDepthLine.get(q+4).setDepth(firstDepthLine.get(q+1).getDepth() - currentTideDepth);
					//Should it still be a nogo?
					if (firstDepthLine.get(q+4).getDepth() > depth){
						firstDepthLine.remove(q+4);
					}
	
					
					//First subtract the value
					firstDepthLine.get(q+5).setDepth(firstDepthLine.get(q+1).getDepth() - currentTideDepth);
					//Should it still be a nogo?
					if (firstDepthLine.get(q+5).getDepth() > depth){
						firstDepthLine.remove(q+5);
					}
					
					//First subtract the value
					firstDepthLine.get(q+6).setDepth(firstDepthLine.get(q+1).getDepth() - currentTideDepth);
					//Should it still be a nogo?
					if (firstDepthLine.get(q+6).getDepth() > depth){
						firstDepthLine.remove(q+6);
					}
					
					//First subtract the value
					firstDepthLine.get(q+7).setDepth(firstDepthLine.get(q+1).getDepth() - currentTideDepth);
					//Should it still be a nogo?
					if (firstDepthLine.get(q+7).getDepth() > depth){
						firstDepthLine.remove(q+7);
					}
					
					
				}
				
				
				
				
				
			}
			
			j =+5;
			System.out.println("j is now: " + j);
			
			
		}
		

		System.out.println("Query executed! - parsing");

		// double lonOffset = 0.0007854;
		// The difference between each point / 2. This is used in calculating
		// the polygons surrounding the lines

		// 100m spacing
		double latOffset = 0.00055504;

		// 50m spacing
		// double latOffset = 0.000290;

		System.out.println("Parsing Query");

		ParseData parseData = new ParseData();

		List<List<DepthDenmark>> parsed = parseData.getParsed(lines);

		// parsed = lines;

//		System.out.println(lines.size());
//		System.out.println(parsed.size());
//
//		for (int j = 0; j < parsed.size(); j++) {
//			System.out.println(parsed.get(j).size());
//		}

		NogoPolygon polygon;
		NogoPolygon temp;

		for (List<DepthDenmark> splittedLines : parsed) {

			if (splittedLines.size() == 1) {
				NogoPoint point = new NogoPoint(splittedLines.get(0).getLat(),
						splittedLines.get(0).getLon());
				temp = new NogoPolygon();
				temp.getPolygon().add(point);
				temp.getPolygon().add(point);
			} else {
				temp = new NogoPolygon();
				for (DepthDenmark dataEntries : splittedLines) {
					NogoPoint point = new NogoPoint(dataEntries.getLat(),
							dataEntries.getLon());
					temp.getPolygon().add(point);
				}
			}

			NogoPoint westPoint = temp.getPolygon().get(0);
			NogoPoint eastPoint = temp.getPolygon().get(1);

			NogoPoint northWest = new NogoPoint(westPoint.getLat() + latOffset,
					westPoint.getLon());

			NogoPoint northEast = new NogoPoint(eastPoint.getLat() + latOffset,
					eastPoint.getLon());

			NogoPoint southWest = new NogoPoint(westPoint.getLat() - latOffset,
					westPoint.getLon());

			NogoPoint southEast = new NogoPoint(eastPoint.getLat() - latOffset,
					eastPoint.getLon());

			polygon = new NogoPolygon();

			polygon.getPolygon().add(northWest);
			polygon.getPolygon().add(southWest);
			polygon.getPolygon().add(southEast);
			polygon.getPolygon().add(northEast);

			res.add(polygon);
		}

		System.out.println(res.size());

		return res;
	}

}
