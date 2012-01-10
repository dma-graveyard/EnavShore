package dk.frv.enav.shore.core.services.nogo;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import dk.frv.ais.geo.GeoLocation;
import dk.frv.enav.common.xml.nogo.types.BoundingBoxPoint;
import dk.frv.enav.shore.core.domain.DepthDenmark;
import dk.frv.enav.shore.core.domain.TideDenmark;
import dk.frv.enav.shore.core.services.nogo.NogoServiceBean.WorkerType;

public class NogoWorker extends Thread {
	public final static int THREADPASS = 0;
	public final static int THREADFAIL = 1;
	int _status;
	int type;
	private GeoLocation pos;
	private BoundingBoxPoint point;
	private WorkerType workerType;
	private double maxDepth;
	private List<DepthDenmark> depthDatabaseResult;
	private List<TideDenmark> tideDatabaseResult;
	private EntityManager entityManager;

	private BoundingBoxPoint firstPos;
	private BoundingBoxPoint secondPos;
	private double draught;
	private Timestamp timeStart;
	private Timestamp timeEnd;
	
	
	public void setTimeStart(Timestamp timeStart) {
		this.timeStart = timeStart;
	}

	public void setTimeEnd(Timestamp timeEnd) {
		this.timeEnd = timeEnd;
	}



	public NogoWorker(EntityManager entityManager, WorkerType workerType) {
		this.entityManager = entityManager;
		this.workerType = workerType;
		_status = THREADFAIL;
	}

	public void setFirstPos(BoundingBoxPoint firstPos) {
		this.firstPos = firstPos;
	}

	public void setSecondPos(BoundingBoxPoint secondPos) {
		this.secondPos = secondPos;
	}

	public void setDraught(double draught) {
		this.draught = draught;
	}

	public List<DepthDenmark> getDepthDatabaseResult() {
		return depthDatabaseResult;
	}

	public List<TideDenmark> getTideDatabaseResult() {
		return tideDatabaseResult;
	}

	public double getMaxDepth() {
		return maxDepth;
	}

	public int status() {
		return _status;
	}

	public void setType(int i) {
		this.type = i;
	}

	public void setPos(GeoLocation pos) {
		this.pos = pos;
	}

	public BoundingBoxPoint getPoint() {
		return this.point;
	}

	public void run() {
		// System.out.print("Thread " + getName() + ": Entered\n");
		// System.out.print("Thread " + getName() + ": Working\n");

		// Do the task that type specifies
		if (workerType == WorkerType.DEPTHPOINT) {
			System.out.println("Thread " + getName()
					+ " i'm a depthPoint worker");
			if (pos != null) {
				point = getAreaDepthDenmark();
			}
		}

		if (workerType == WorkerType.TIDEPOINT) {
			System.out.println("Thread " + getName()
					+ " i'm a tidePoint worker");
			if (pos != null) {
				point = getAreaTideDenmark();
			}
		}

		if (workerType == WorkerType.MAXTIDE) {
			System.out.println("Thread " + getName() + " i'm a maxTide worker");
			maxDepth = maxTideDepth();
		}

		if (workerType == WorkerType.DEPTHDATA) {
			System.out.println("Thread " + getName()
					+ " i'm a depthData worker");
			depthDatabaseResult = getNogoArea(firstPos, secondPos, draught);
		}

		if (workerType == WorkerType.TIDEDATA) {
			System.out
					.println("Thread " + getName() + " i'm a tideData worker");
			tideDatabaseResult = getTideArea(firstPos, secondPos);
		}

		// BoundingBoxPoint firstPos,
		// BoundingBoxPoint secondPos,
		// double draught
		//
		//
		//
		// System.out.print("Thread " + getName() + " \n");
		System.out.print("Thread " + getName() + ": Done with work\n");
		_status = THREADPASS;
	}

	@SuppressWarnings("unchecked")
	public BoundingBoxPoint getAreaDepthDenmark() {

		// GeoLocation pos1 = new GeoLocation(lat, lon);

		Query query = entityManager
				.createQuery("SELECT dd.n, dd.m, dd.lat, dd.lon "
						+ "FROM DepthDenmark dd "
						+ "where dd.lat between :lat1 AND :lat1range " + "AND "
						+ "dd.lon between :lon1 AND :lon1range");

		query.setParameter("lat1", pos.getLatitude());
		query.setParameter("lat1range", pos.getLatitude() + 0.01);

		query.setParameter("lon1", pos.getLongitude());
		query.setParameter("lon1range", pos.getLongitude() + 0.01);

		List<Object[]> lines = query.getResultList();

		if (lines.size() != 0) {
			double distance = 9999999;

			Object[] bestMatch = null;

			for (Object[] objects : lines) {
				// Minimum distance
				GeoLocation pos = new GeoLocation((Double) objects[2],
						(Double) objects[3]);
				double distancePoint = pos.getGeodesicDistance(pos);
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

	@SuppressWarnings("unchecked")
	public BoundingBoxPoint getAreaTideDenmark() {

		// GeoLocation pos1 = new GeoLocation(lat, lon);

		Query query = entityManager
				.createQuery("SELECT DISTINCT td.n, td.m, td.lat, td.lon "
						+ "FROM TideDenmark td "
						+ "where td.lat between :lat1 AND :lat1range " + "AND "
						+ "td.lon between :lon1 AND :lon1range");

		query.setParameter("lat1", pos.getLatitude());
		query.setParameter("lat1range", pos.getLatitude() + 0.01);

		query.setParameter("lon1", pos.getLongitude());
		query.setParameter("lon1range", pos.getLongitude() + 0.01);

		List<Object[]> lines = query.getResultList();

		if (lines.size() != 0) {
			double distance = 9999999;

			Object[] bestMatch = null;

			for (Object[] objects : lines) {
				// Minimum distance
				GeoLocation pos = new GeoLocation((Double) objects[2],
						(Double) objects[3]);
				double distancePoint = pos.getGeodesicDistance(pos);
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

	public double maxTideDepth() {

		Double maxDepth = 0.0;

		Query query = entityManager.createQuery("SELECT max(td.depth) "
				+ "FROM TideDenmark td ");

		Object queryResult = query.getSingleResult();

		if (queryResult != null) {
			maxDepth = (Double) queryResult;
			// System.out.println("Max depth is now: " + maxDepth);
		}

		return maxDepth;
	}

	@SuppressWarnings("unchecked")
	public List<DepthDenmark> getNogoArea(BoundingBoxPoint firstPos,
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

		Query query = entityManager.createQuery("SELECT dd "
				+ "FROM DepthDenmark dd " + "WHERE dd.n between :n1 AND :n2 "
				+ "AND dd.m between :m1 AND :m2 "
				// + "AND dd.depth > :d1 ORDER BY M, N");
				+ "ORDER BY M, N");

		query.setParameter("n1", n1);
		query.setParameter("n2", n2);
		query.setParameter("m1", m1);
		query.setParameter("m2", m2);
		// query.setParameter("d1", draught);

		List<DepthDenmark> result = query.getResultList();

		return result;
	}

	@SuppressWarnings("unchecked")
	public List<TideDenmark> getTideArea(BoundingBoxPoint firstPos,
			BoundingBoxPoint secondPos) {

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

		Query query = entityManager
				.createQuery("SELECT td "
						+ "FROM TideDenmark td "
						+ "WHERE td.n between :n1 AND :n2 "
						+ "AND td.m between :m1 AND :m2 "
						// + "AND td.depth is NOT NULL "
						//+ "AND td.time between '2012-01-05 22:00:00'  and '2012-01-05 23:00:00' "
						+ "AND td.time between :t1  and :t2 "
						+ "ORDER BY M, N");

		query.setParameter("n1", n1);
		query.setParameter("n2", n2);
		query.setParameter("m1", m1);
		query.setParameter("m2", m2);
		query.setParameter("t1", timeStart);
		query.setParameter("t2", timeEnd);
		

		List<TideDenmark> result = query.getResultList();

		return result;
	}

}
