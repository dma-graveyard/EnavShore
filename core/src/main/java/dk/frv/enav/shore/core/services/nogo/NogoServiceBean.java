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

import java.sql.Timestamp;
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
import dk.frv.enav.shore.core.services.Errorcodes;
import dk.frv.enav.shore.core.services.ServiceException;

@Stateless
public class NogoServiceBean implements NogoService {

	@PersistenceContext(unitName = "enav")
	private EntityManager entityManager;

	public enum WorkerType {
		DEPTHPOINT, TIDEPOINT, DEPTHDATA, TIDEDATA, MAXTIDE;
	}

	public enum DataType {
		SYDKATTEGAT, NORDKATTEGAT;
	}

	int errorCode = 0;
	
	@SuppressWarnings("deprecation")
	@Override
	public NogoResponse nogoPoll(NogoRequest nogoRequest) throws ServiceException {

		
		// System.out.println("NoGo request recieved");

		// First identify which area we are searching in

		GeoLocation northWest = new GeoLocation(nogoRequest.getNorthWestPointLat(), nogoRequest.getNorthWestPointLon());
		GeoLocation SouthEast = new GeoLocation(nogoRequest.getSouthEastPointLat(), nogoRequest.getSouthEastPointLon());

		NogoWorker nogoWorkerFirstPointDepth = null;

		NogoWorker nogoWorkerSecondPointDepth = null;

		NogoWorker nogoWorkerFirstPointTide = null;

		NogoWorker nogoWorkerSecondPointTide = null;

		NogoWorker nogoWorkerDepthData = null;

		NogoWorker nogoWorkerTideData = null;

		// Sydkattegat data
		if (northWest.getLatitude() > 54.36294 && northWest.getLatitude() < 56.36316
				&& northWest.getLongitude() > 9.419409 && northWest.getLongitude() < 13.149009
				&& SouthEast.getLatitude() > 54.36294 && SouthEast.getLatitude() < 56.36316
				&& SouthEast.getLongitude() > 9.419409 && SouthEast.getLongitude() < 13.149009) {

			nogoWorkerFirstPointDepth = new NogoWorker(entityManager, WorkerType.DEPTHPOINT, DataType.SYDKATTEGAT);
			nogoWorkerSecondPointDepth = new NogoWorker(entityManager, WorkerType.DEPTHPOINT, DataType.SYDKATTEGAT);
			nogoWorkerFirstPointTide = new NogoWorker(entityManager, WorkerType.TIDEPOINT, DataType.SYDKATTEGAT);
			nogoWorkerSecondPointTide = new NogoWorker(entityManager, WorkerType.TIDEPOINT, DataType.SYDKATTEGAT);

			nogoWorkerDepthData = new NogoWorker(entityManager, WorkerType.DEPTHDATA, DataType.SYDKATTEGAT);
			nogoWorkerTideData = new NogoWorker(entityManager, WorkerType.TIDEDATA, DataType.SYDKATTEGAT);

		} else {
			// Nordkattegat data
			if (northWest.getLatitude() > 56.34096 && northWest.getLatitude() < 58.26237
					&& northWest.getLongitude() > 9.403869 && northWest.getLongitude() < 12.148899
					&& SouthEast.getLatitude() > 56.34096 && SouthEast.getLatitude() < 58.26237
					&& SouthEast.getLongitude() > 9.403869 && SouthEast.getLongitude() < 12.148899) {
				// System.out.println("Valid nordkattegat point");

				nogoWorkerFirstPointDepth = new NogoWorker(entityManager, WorkerType.DEPTHPOINT, DataType.NORDKATTEGAT);
				nogoWorkerSecondPointDepth = new NogoWorker(entityManager, WorkerType.DEPTHPOINT, DataType.NORDKATTEGAT);
				nogoWorkerFirstPointTide = new NogoWorker(entityManager, WorkerType.TIDEPOINT, DataType.NORDKATTEGAT);
				nogoWorkerSecondPointTide = new NogoWorker(entityManager, WorkerType.TIDEPOINT, DataType.NORDKATTEGAT);

				nogoWorkerDepthData = new NogoWorker(entityManager, WorkerType.DEPTHDATA, DataType.NORDKATTEGAT);
				nogoWorkerTideData = new NogoWorker(entityManager, WorkerType.TIDEDATA, DataType.NORDKATTEGAT);

			}

		}

		if (northWest.getLatitude() > 58.26237 || northWest.getLatitude() < 54.36294
				|| northWest.getLongitude() > 13.149009 || northWest.getLongitude() < 9.403869
				|| SouthEast.getLatitude() > 58.26237 || SouthEast.getLatitude() < 54.36294
				|| SouthEast.getLongitude() > 13.149009 || SouthEast.getLongitude() < 9.403869) {
			System.out.println("No data available");
			NogoResponse res = new NogoResponse();
			
			res.setNoGoErrorCode(17);
			res.setNoGoMessage(Errorcodes.getErrorMessage(17));
				
			return res;
		}

		// NogoWorker nogoWorkerThirdMaxTide = new NogoWorker(entityManager,
		// WorkerType.MAXTIDE);

		nogoWorkerFirstPointDepth.setPos(new GeoLocation(nogoRequest.getNorthWestPointLat(), nogoRequest
				.getNorthWestPointLon()));

		nogoWorkerSecondPointDepth.setPos(new GeoLocation(nogoRequest.getSouthEastPointLat(), nogoRequest
				.getSouthEastPointLon()));

		nogoWorkerFirstPointTide.setPos(new GeoLocation(nogoRequest.getNorthWestPointLat(), nogoRequest
				.getNorthWestPointLon()));

		nogoWorkerSecondPointTide.setPos(new GeoLocation(nogoRequest.getSouthEastPointLat(), nogoRequest
				.getSouthEastPointLon()));

		// firstPos = getArea(55.070, 11.668);
		// secondPos = getArea(55.170, 11.868)

		// Testing stuff
		// nogoWorkerFirstPointDepth.setPos(new GeoLocation(55.070, 11.668));
		//
		// nogoWorkerSecondPointDepth.setPos(new GeoLocation(55.170, 11.868));
		//
		// nogoWorkerFirstPointTide.setPos(new GeoLocation(55.070, 11.668));
		//
		// nogoWorkerSecondPointTide.setPos(new GeoLocation(55.170, 11.868));

		// Get the grid position of the data in the depth database
		nogoWorkerFirstPointDepth.start();
		nogoWorkerSecondPointDepth.start();

		// Get the grid position of the data in the tide database
		nogoWorkerFirstPointTide.start();
		nogoWorkerSecondPointTide.start();

		// Find max change in depth database - not needed anymore
		// nogoWorkerThirdMaxTide.start();

		// nogoRequest.getStartDate();

		try {
			nogoWorkerFirstPointDepth.join();
			// System.out.println("First depth point found");
			nogoWorkerSecondPointDepth.join();
			// System.out.println("Second depth point found");
			// nogoWorkerThirdMaxTide.join();
			// System.out.println("MaxTide found");

			nogoWorkerFirstPointTide.join();
			// System.out.println("First tide point found");
			nogoWorkerSecondPointTide.join();
			// System.out.println("Second tide point found");

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		BoundingBoxPoint firstPosDepth = nogoWorkerFirstPointDepth.getPoint();
		BoundingBoxPoint secondPosDepth = nogoWorkerSecondPointDepth.getPoint();

		// System.out.println("depth points are " +
		// nogoWorkerFirstPointDepth.getPoint() + ", "
		// + nogoWorkerSecondPointDepth.getPoint());

		BoundingBoxPoint firstPosTide = nogoWorkerFirstPointTide.getPoint();
		BoundingBoxPoint secondPosTide = nogoWorkerSecondPointTide.getPoint();

		// System.out.println("tide points are " +
		// nogoWorkerFirstPointTide.getPoint() + ", "
		// + nogoWorkerSecondPointTide.getPoint());

		List<NogoPolygon> polyArea = new ArrayList<NogoPolygon>();

		if (firstPosDepth != null && secondPosDepth != null) {
			// System.out.println("Bounding Box found - requesting data");

			nogoWorkerDepthData.setFirstPos(firstPosDepth);
			nogoWorkerDepthData.setSecondPos(secondPosDepth);

			nogoWorkerDepthData.setDraught(nogoRequest.getDraught());

			// Testing
			// nogoWorkerDepthData.setDraught(-7);

			nogoWorkerTideData.setFirstPos(firstPosTide);
			nogoWorkerTideData.setSecondPos(secondPosTide);

			// Use 01-05 until we get better database setup
			// 2012-01-05 22:00:00
			java.sql.Timestamp timeStart = new Timestamp(112, 0, 5, 0, 0, 0, 0);
			java.sql.Timestamp timeEnd = new Timestamp(112, 0, 5, 0, 0, 0, 0);

			timeStart.setHours(nogoRequest.getStartDate().getHours());
			timeEnd.setHours(nogoRequest.getEndDate().getHours());

			nogoWorkerTideData.setTimeStart(timeStart);
			nogoWorkerTideData.setTimeEnd(timeEnd);

			// System.out.println("StartTime is: " + timeStart);
			//
			// System.out.println("EndTime is: " + timeEnd);

			nogoWorkerDepthData.start();

			nogoWorkerTideData.start();

			try {
				nogoWorkerDepthData.join();
				// System.out.println("Depth data thread joined");
				nogoWorkerTideData.join();
				// System.out.println("Tide data thread joined");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			if (nogoWorkerDepthData.getDepthDatabaseResult().size() != 0) {
				polyArea = parseResult(nogoWorkerDepthData.getDepthDatabaseResult(),
						nogoWorkerTideData.getTideDatabaseResult(), nogoRequest.getDraught());
			}
			// polyArea = getNogoArea(firstPos, secondPos, -7);
			// System.out.println("Data recieved and parsed");
		}

		NogoResponse res = new NogoResponse();

		for (int i = 0; i < polyArea.size(); i++) {
			res.addPolygon(polyArea.get(i));
		}

		Date requestStart = nogoRequest.getStartDate();
		requestStart.setMinutes(0);
		requestStart.setSeconds(0);

		Date requestEnd = nogoRequest.getEndDate();
		requestEnd.setMinutes(0);
		requestEnd.setSeconds(0);

		// Date currentDate = new Date();
		// long futureDate = currentDate.getTime() + 7200000;

		res.setValidFrom(requestStart);
		res.setValidTo(requestEnd);
		
		res.setNoGoErrorCode(errorCode);
		res.setNoGoMessage(Errorcodes.getErrorMessage(errorCode));

		// System.out.println("Sending data");

		return res;
	}

	@Override
	@SuppressWarnings("unchecked")
	public BoundingBoxPoint getAreaDepthDenmark(double lat, double lon) {

		GeoLocation pos1 = new GeoLocation(lat, lon);

		Query query = entityManager.createQuery("SELECT dd.n, dd.m, dd.lat, dd.lon " + "FROM DepthDenmark dd "
				+ "where dd.lat between :lat1 AND :lat1range " + "AND " + "dd.lon between :lon1 AND :lon1range");

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
				GeoLocation pos = new GeoLocation((Double) objects[2], (Double) objects[3]);
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
			BoundingBoxPoint point = new BoundingBoxPoint((Integer) bestMatch[0], (Integer) bestMatch[1]);

			return point;

		}
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public BoundingBoxPoint getTideDenmark(double lat, double lon) {
		GeoLocation pos1 = new GeoLocation(lat, lon);

		Query query = entityManager.createQuery("SELECT dd.n, dd.m, dd.lat, dd.lon " + "FROM TideDenmark dd "
				+ "where dd.lat between :lat1 AND :lat1range " + "AND " + "dd.lon between :lon1 AND :lon1range");

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
				GeoLocation pos = new GeoLocation((Double) objects[2], (Double) objects[3]);
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
			BoundingBoxPoint point = new BoundingBoxPoint((Integer) bestMatch[0], (Integer) bestMatch[1]);

			return point;

		}
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<NogoPolygon> getNogoArea(BoundingBoxPoint firstPos, BoundingBoxPoint secondPos, double draught) {

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

		// System.out.println(n1);
		// System.out.println(n2);
		// System.out.println(m1);
		// System.out.println(m2);
		// System.out.println(draught);

		Query query = entityManager.createQuery("SELECT dd " + "FROM DepthDenmark dd "
				+ "WHERE dd.n between :n1 AND :n2 " + "AND dd.m between :m1 AND :m2 "
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

		// System.out.println("Query executed! - parsing");

		// double lonOffset = 0.0007854;
		// The difference between each point / 2. This is used in calculating
		// the polygons surrounding the lines

		// 100m spacing
		double latOffset = 0.00055504;

		// 50m spacing
		// double latOffset = 0.000290;

		// System.out.println("Parsing Query");

		ParseData parseData = new ParseData();

		List<List<DepthDenmark>> parsed = parseData.getParsed(lines);

		// parsed = lines;

		// System.out.println(lines.size());
		// System.out.println(parsed.size());
		//
		// for (int j = 0; j < parsed.size(); j++) {
		// System.out.println(parsed.get(j).size());
		// }

		NogoPolygon polygon;
		NogoPolygon temp;


		
		for (List<DepthDenmark> splittedLines : parsed) {

			if (splittedLines.size() == 1) {
				NogoPoint point = new NogoPoint(splittedLines.get(0).getLat(), splittedLines.get(0).getLon());
				temp = new NogoPolygon();
				temp.getPolygon().add(point);
				temp.getPolygon().add(point);
			} else {
				temp = new NogoPolygon();
				for (DepthDenmark dataEntries : splittedLines) {
					NogoPoint point = new NogoPoint(dataEntries.getLat(), dataEntries.getLon());
					temp.getPolygon().add(point);
				}
			}

			NogoPoint westPoint = temp.getPolygon().get(0);
			NogoPoint eastPoint = temp.getPolygon().get(1);

			NogoPoint northWest = new NogoPoint(westPoint.getLat() + latOffset, westPoint.getLon());

			NogoPoint northEast = new NogoPoint(eastPoint.getLat() + latOffset, eastPoint.getLon());

			NogoPoint southWest = new NogoPoint(westPoint.getLat() - latOffset, westPoint.getLon());

			NogoPoint southEast = new NogoPoint(eastPoint.getLat() - latOffset, eastPoint.getLon());

			polygon = new NogoPolygon();

			polygon.getPolygon().add(northWest);
			polygon.getPolygon().add(southWest);
			polygon.getPolygon().add(southEast);
			polygon.getPolygon().add(northEast);

			res.add(polygon);
		}

		// System.out.println(res.size());

		return res;
	}

	@Override
	public double maxTideDepth() {

		Double maxDepth = 0.0;

		Query query = entityManager.createQuery("SELECT max(td.depth) " + "FROM TideDenmark td ");

		Object queryResult = query.getSingleResult();

		if (queryResult != null) {
			maxDepth = (Double) queryResult;
		}

		// System.out.println(maxDepth);

		return maxDepth;
	}

	@Override
	public List<NogoPolygon> parseResult(List<DepthDenmark> result, List<TideDenmark> resultTide, double depth) {

		// System.out.println("Query executed! - parsing");

		// This is where we store our result
		List<NogoPolygon> res = new ArrayList<NogoPolygon>();

		// Seperate it into lines - depth
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


		if (resultTide == null){
			errorCode = 18;	
		}

		// Seperate it into lines - tide - if we got em
		if (resultTide != null) {
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

			// Identify how many similar we have
			int n = linesTide.get(0).get(0).getN();
			int nCount = 0;
			for (int j = 0; j < linesTide.get(0).size(); j++) {

				if (n != -1 && linesTide.get(0).get(j).getN() != n) {
					break;
				}
				nCount++;

			}

			// System.out.println("We have: " + nCount + " that are equal");
			// System.out.println("The size of linesTide first line is: " +
			// linesTide.get(0).size());
			// We have a broad time spand
			if (nCount != 1) {
				List<List<TideDenmark>> linesTideParsed = new ArrayList<List<TideDenmark>>();
				// We need to take nCount out and compare, and return the
				// highest
				for (int i = 0; i < linesTide.size(); i++) {
					List<TideDenmark> parsedLine = compareTideLines(linesTide.get(i), nCount);
					linesTideParsed.add(parsedLine);
				}
				// Overwrite the old one
				linesTide = linesTideParsed;
			}

			// System.out.println("The size of linesTideParsed first line is: "
			// + linesTide.get(0).size());

			// Combine the two into one result
			int j = 0;
			for (int i = 0; i < linesTide.size(); i++) {
				List<TideDenmark> currentTideLine = linesTide.get(i);
				combineVertical(currentTideLine, lines, j);
				j = j + 5;
			}

		}
		// Remove invalid positions
		for (int i = 0; i < lines.size(); i++) {

			for (int k = 0; k < lines.get(i).size(); k++) {
				if (lines.get(i).get(k).getDepth() == null || lines.get(i).get(k).getDepth() < depth) {
					lines.get(i).remove(k);
				}
			}

		}

		// double lonOffset = 0.0007854;
		// The difference between each point / 2. This is used in calculating
		// the polygons surrounding the lines

		// 100m spacing
		double latOffset = 0.00055504;
		
		double lonOffset = 0.00055504;

		// 50m spacing
		// double latOffset = 0.000290;

		// System.out.println("Parsing Query");

		ParseData parseData = new ParseData();

		// System.out.println("Lines is: " + lines.size());

		List<List<DepthDenmark>> parsed = parseData.getParsed(lines);

		// System.out.println("Parsed is: " + parsed.size());

		// parsed = lines;

		// System.out.println(lines.size());
		// System.out.println(parsed.size());
		//
		// for (int j = 0; j < parsed.size(); j++) {
		// System.out.println(parsed.get(j).size());
		// }

		NogoPolygon polygon;
		NogoPolygon temp;

		for (List<DepthDenmark> splittedLines : parsed) {

			if (splittedLines.size() == 1) {
				NogoPoint point = new NogoPoint(splittedLines.get(0).getLat(), splittedLines.get(0).getLon());
				temp = new NogoPolygon();
				temp.getPolygon().add(point);
				temp.getPolygon().add(point);
			} else {
				temp = new NogoPolygon();
				for (DepthDenmark dataEntries : splittedLines) {
					NogoPoint point = new NogoPoint(dataEntries.getLat(), dataEntries.getLon());
					temp.getPolygon().add(point);
				}
			}

			NogoPoint westPoint = temp.getPolygon().get(0);
			NogoPoint eastPoint = temp.getPolygon().get(1);

			NogoPoint northWest = new NogoPoint(westPoint.getLat() + latOffset, westPoint.getLon());

			NogoPoint northEast = new NogoPoint(eastPoint.getLat() + latOffset, eastPoint.getLon());

			NogoPoint southWest = new NogoPoint(westPoint.getLat() - latOffset, westPoint.getLon());

			NogoPoint southEast = new NogoPoint(eastPoint.getLat() - latOffset, eastPoint.getLon());

			polygon = new NogoPolygon();

			polygon.getPolygon().add(northWest);
			polygon.getPolygon().add(southWest);
			polygon.getPolygon().add(southEast);
			polygon.getPolygon().add(northEast);

			res.add(polygon);
		}

		// System.out.println(res.size());

		return res;
	}

	private List<TideDenmark> compareTideLines(List<TideDenmark> list, int nCount) {

		List<TideDenmark> parsedList = new ArrayList<TideDenmark>();
		// Take nCount out
		// Compare them
		for (int i = 0; i < list.size(); i = i + nCount) {

			// take all the elements
			List<TideDenmark> tempList = new ArrayList<TideDenmark>();
			for (int j = 0; j < nCount; j++) {
				tempList.add(list.get(j + i));
			}

			// find lowest in tempList
			TideDenmark lowestTide = getLowestTide(tempList);
			// add it to parsedList
			parsedList.add(lowestTide);
		}

		return parsedList;
	}

	private TideDenmark getLowestTide(List<TideDenmark> tempList) {
		TideDenmark current = tempList.get(0);

		for (int i = 0; i < tempList.size(); i++) {
			if (current.getDepth() != null && tempList.get(i).getDepth() != null) {
				// Take the lowest
				if (current.getDepth() > tempList.get(i).getDepth()) {
					current = tempList.get(i);
				}
			}
			// if current is null and the other isn't, take the none null one.
			// Is this the correct approach?
			if (current.getDepth() == null && tempList.get(i).getDepth() != null) {
				// System.out.println("Strangeness");
				current = tempList.get(i);
			}

		}

		return current;
	}

	private void combineVertical(List<TideDenmark> currentTideLine, List<List<DepthDenmark>> lines, int k) {

		// How many entries does lines has, is k + 5 > than lines.size then
		// treat then special - end of shit

		if (k + 5 > lines.size() - 1) {

			for (int i = k + 1; i < lines.size(); i++) {
				// System.out.println("We must work on " + k);
				combineHorizontal(currentTideLine, lines.get(k));
			}
			// System.out.println("Do something else");

		} else {

			// We have the line, work on the depth database part
			for (int j = 0; j < 4; j++) {
				// Five lines has to use the currentTideLine
				// Each line now has to iterate through the tideline
				combineHorizontal(currentTideLine, lines.get(k + j));
				// System.out.println("Currently working on: " + (k+j));
			}

		}

	}

	private void combineHorizontal(List<TideDenmark> currentTideLine, List<DepthDenmark> currentDepthList) {

		// Gets two lines - depth and tide
		int j = 0;

		// For each tidePoint, apply it's depth to all element in the depth
		for (int i = 0; i < currentTideLine.size(); i++) {
			// System.out.println("Current tide size is: "
			// + currentTideLine.size());
			// Apply this depth to some elements - 8 f them
			double currentDepth = 0;

			if (currentTideLine.get(i).getDepth() != null) {
				currentDepth = currentTideLine.get(i).getDepth();
			}
			// System.out.println("currentDepth is: " + currentDepth);

			// System.out.println("Depth something is: " +
			// currentTideLine.get(0).getId());

			combineDepth(currentDepth, currentDepthList, j);

			// Take element from

			j += 8;
		}

	}

	private void combineDepth(double currentDepth, List<DepthDenmark> currentDepthList, int j) {

		if (j + 8 > currentDepthList.size() - 1) {

			for (int i = j + 1; i < currentDepthList.size(); i++) {

				// It's null, screw it
				if (currentDepthList.get(i).getDepth() != null) {
					double newDepth = currentDepthList.get(i).getDepth() - currentDepth;
					currentDepthList.get(i).setDepth(newDepth);
					// System.out.println("Depth is: " +
					// currentDepthList.get(i+j).getDepth());
				}

				// System.out.println("We must work on " + i);

			}
			// System.out.println("Do something else");

		}

		if (j + 8 > currentDepthList.size() - 1) {
			// System.out.println("Do something else - depth line version");
		} else {

			// j is the current position, so we need to take that + 7
			for (int i = 0; i < 7; i++) {

				// It's null, screw it
				if (currentDepthList.get(i + j).getDepth() != null) {
					double newDepth = currentDepthList.get(i + j).getDepth() - currentDepth;
					currentDepthList.get(i + j).setDepth(newDepth);
					// System.out.println("Depth is: " +
					// currentDepthList.get(i+j).getDepth());
				}

			}

		}

	}

}
