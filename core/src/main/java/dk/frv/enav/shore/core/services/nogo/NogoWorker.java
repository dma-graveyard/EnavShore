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
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import dk.dma.enav.model.geometry.Position;
import dk.frv.ais.geo.GeoLocation;
import dk.frv.enav.common.xml.nogo.types.BoundingBoxPoint;
import dk.frv.enav.shore.core.domain.DepthDenmark;
import dk.frv.enav.shore.core.domain.DepthDenmarkNord;
import dk.frv.enav.shore.core.domain.Humber;
import dk.frv.enav.shore.core.domain.HumberTideTabels;
import dk.frv.enav.shore.core.domain.TideDenmark;
import dk.frv.enav.shore.core.domain.sfBay;
import dk.frv.enav.shore.core.services.nogo.NogoServiceBean.DataType;
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
    private DataType dataType;

    private List<HumberTidePoint> humberTidalPoints;

    public void setTimeStart(Timestamp timeStart) {
        this.timeStart = timeStart;
    }

    public void setTimeEnd(Timestamp timeEnd) {
        this.timeEnd = timeEnd;
    }

    public NogoWorker(EntityManager entityManager, WorkerType workerType, DataType dataType) {
        this.entityManager = entityManager;
        this.workerType = workerType;
        this.dataType = dataType;
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
            // System.out.println("Thread " + getName() + " i'm a depthPoint worker");
            if (pos != null) {
                point = getAreaDepthDenmark();
            }
        }

        if (workerType == WorkerType.TIDEPOINT) {
            // System.out.println("Thread " + getName() + " i'm a tidePoint worker");
            if (pos != null) {
                point = getAreaTideDenmark();
            }
        }

        if (workerType == WorkerType.MAXTIDE) {
            // System.out.println("Thread " + getName() + " i'm a maxTide worker");
            maxDepth = maxTideDepth();
        }

        if (workerType == WorkerType.DEPTHDATA) {
            // System.out.println("Thread " + getName() + " i'm a depthData worker");
            if (firstPos != null && secondPos != null) {
                depthDatabaseResult = getNogoArea(firstPos, secondPos, draught);
            } else {
                depthDatabaseResult = null;
            }
        }

        if (workerType == WorkerType.TIDEDATA) {
            // System.out.println("Thread " + getName() + " i'm a tideData worker");
            if (firstPos != null && secondPos != null) {
                tideDatabaseResult = getTideArea(firstPos, secondPos);
            }

        }

        if (workerType == WorkerType.HUMBERTIDE) {
            System.out.println("Humber Tide Worker");
            if (timeStart != null && timeEnd != null) {
                getHumberTidalValues();
            }

        }

        // System.out.print("Thread " + getName() + ": Done with work\n");
        _status = THREADPASS;
    }

    @SuppressWarnings("unchecked")
    private void getHumberTidalValues() {

        humberTidalPoints = new ArrayList<HumberTidePoint>();

        // Retrieve list of buoys
        Query query = null;
        query = entityManager.createQuery("SELECT dd FROM HumberTideTabels dd");

        List<HumberTideTabels> results = query.getResultList();

        for (int i = 0; i < results.size(); i++) {
            // System.out.println(results.get(i).getDbName());
            double minimumDepth = retrieveHumberTidalValue(results.get(i).getDbName());
            Position position = Position.create(results.get(i).getLatitude(), results.get(i).getLongitude());
            humberTidalPoints.add(new HumberTidePoint(position, minimumDepth));

        }

        // For each buyo retrieve the data in the time section
        // List of strings

        // Hashmap of
        // Buyoy custom class with NAME and Lat/Lon
        // Each tidal gauge have a depth value

        // retrieveHumberTidalValue("SpurnTide2014");
    }

    private double retrieveHumberTidalValue(String dbname) {

        // SELECT MIN(depth) FROM enav_p.spurn_tide_2014 where dateTime >= '2014-01-02 00:00:00' AND dateTime < '2014-01-02
        // 23:53:00'

        Query query = null;
        query = entityManager.createQuery("SELECT MIN(dd.depth) " + "FROM " + dbname + " dd "
                + "where dd.dateTime >= :time1 AND dd.dateTime <= :time2");
        query.setParameter("time1", timeStart);
        query.setParameter("time2", timeEnd);

        Double result = (Double) query.getSingleResult();
        return result;
        // System.out.println("Minimum depth for spurn is " + result);

        // List<Object[]> lines = query.getResultList();
        // System.out.println("Found " + lines.size() + " tidal results of depth stuff Humber");
        // for (int i = 0; i < lines.size(); i++) {
        // System.out.println(lines.get(i));
        //
        // }
    }

    @SuppressWarnings("unchecked")
    public BoundingBoxPoint getAreaDepthDenmark() {

        // GeoLocation pos1 = new GeoLocation(lat, lon);

        Query query = null;

        if (dataType == DataType.SYDKATTEGAT) {
            query = entityManager.createQuery("SELECT dd.n, dd.m, dd.lat, dd.lon " + "FROM DepthDenmark dd "
                    + "where dd.lat between :lat1 AND :lat1range " + "AND " + "dd.lon between :lon1 AND :lon1range");
        }

        if (dataType == DataType.NORDKATTEGAT) {
            query = entityManager.createQuery("SELECT dd.n, dd.m, dd.lat, dd.lon " + "FROM DepthDenmarkNord dd "
                    + "where dd.lat between :lat1 AND :lat1range " + "AND " + "dd.lon between :lon1 AND :lon1range");
        }

        if (dataType == DataType.SF_BAY) {
            query = entityManager.createQuery("SELECT dd.n, dd.m, dd.lat, dd.lon " + "FROM sfBay dd "
                    + "where dd.lat between :lat1 AND :lat1range " + "AND " + "dd.lon between :lon1 AND :lon1range");
        }
        if (dataType == DataType.HUMBER) {
            query = entityManager.createQuery("SELECT dd.n, dd.m, dd.lat, dd.lon " + "FROM Humber dd "
                    + "where dd.lat between :lat1 AND :lat1range " + "AND " + "dd.lon between :lon1 AND :lon1range");
        }

        if (dataType == DataType.SF_BAY) {
            query.setParameter("lat1", pos.getLatitude());
            query.setParameter("lat1range", pos.getLatitude() + 0.001);
            query.setParameter("lon1", pos.getLongitude());
            query.setParameter("lon1range", pos.getLongitude() + 0.001);
        } else {

            if (dataType == DataType.HUMBER) {
                query.setParameter("lat1", pos.getLatitude());
                query.setParameter("lat1range", pos.getLatitude() + 0.0005);
                query.setParameter("lon1", pos.getLongitude());
                query.setParameter("lon1range", pos.getLongitude() + 0.0005);
            } else {
                query.setParameter("lat1", pos.getLatitude());
                query.setParameter("lat1range", pos.getLatitude() + 0.01);
                query.setParameter("lon1", pos.getLongitude());
                query.setParameter("lon1range", pos.getLongitude() + 0.01);
            }
        }

        List<Object[]> lines = query.getResultList();

        // System.out.println("How many possible point matches did we get: " + lines.size());

        if (lines.size() == 0) {
            query.setParameter("lat1", pos.getLatitude());
            query.setParameter("lat1range", pos.getLatitude() + 0.002);
            query.setParameter("lon1", pos.getLongitude());
            query.setParameter("lon1range", pos.getLongitude() + 0.001);

            lines = query.getResultList();
        }

        // System.out.println(lines.size());

        if (lines.size() != 0) {
            double distance = 9999999;

            Object[] bestMatch = null;

            for (Object[] objects : lines) {
                // Minimum distance
                GeoLocation pos2 = new GeoLocation((Double) objects[2], (Double) objects[3]);
                double distancePoint = pos2.getGeodesicDistance(pos);
                // System.out.println("Comparing distance: " + distancePoint);
                // System.out.println(distancePoint + " for " + (Integer)
                // objects[0] + " , " + (Integer) objects[1] + " at " + pos);
                if (distancePoint < distance) {
                    distance = distancePoint;
                    bestMatch = objects;

                }
            }
            // System.out.println("Best distance is " + distance);
            // System.out.println("The point we're looking for is at " +
            // bestMatch[0] +" , " + bestMatch[1]);
            BoundingBoxPoint point = new BoundingBoxPoint((Integer) bestMatch[0], (Integer) bestMatch[1]);

            return point;

        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public BoundingBoxPoint getAreaTideDenmark() {

        // GeoLocation pos1 = new GeoLocation(lat, lon);

        Query query = entityManager.createQuery("SELECT DISTINCT td.n, td.m, td.lat, td.lon " + "FROM TideDenmark td "
                + "where td.lat between :lat1 AND :lat1range " + "AND " + "td.lon between :lon1 AND :lon1range");

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
                GeoLocation pos = new GeoLocation((Double) objects[2], (Double) objects[3]);
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
            BoundingBoxPoint point = new BoundingBoxPoint((Integer) bestMatch[0], (Integer) bestMatch[1]);

            return point;

        }
        return null;
    }

    public double maxTideDepth() {

        Double maxDepth = 0.0;

        Query query = entityManager.createQuery("SELECT max(td.depth) " + "FROM TideDenmark td ");

        Object queryResult = query.getSingleResult();

        if (queryResult != null) {
            maxDepth = (Double) queryResult;
            // System.out.println("Max depth is now: " + maxDepth);
        }

        return maxDepth;
    }

    @SuppressWarnings("unchecked")
    public List<DepthDenmark> getNogoArea(BoundingBoxPoint firstPos, BoundingBoxPoint secondPos, double draught) {

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

        Query query = null;

        if (dataType == DataType.SYDKATTEGAT) {
            query = entityManager.createQuery("SELECT dd " + "FROM DepthDenmark dd " + "WHERE dd.n between :n1 AND :n2 "
                    + "AND dd.m between :m1 AND :m2 " + "ORDER BY M, N");
        }

        if (dataType == DataType.NORDKATTEGAT) {
            query = entityManager.createQuery("SELECT dd " + "FROM DepthDenmarkNord dd " + "WHERE dd.n between :n1 AND :n2 "
                    + "AND dd.m between :m1 AND :m2 " + "ORDER BY M, N");
        }

        if (dataType == DataType.SF_BAY) {
            query = entityManager.createQuery("SELECT dd " + "FROM sfBay dd " + "WHERE dd.n between :n1 AND :n2 "
                    + "AND dd.m between :m1 AND :m2 " + "ORDER BY M, N");
        }
        if (dataType == DataType.HUMBER) {
            query = entityManager.createQuery("SELECT dd " + "FROM Humber dd " + "WHERE dd.n between :n1 AND :n2 "
                    + "AND dd.m between :m1 AND :m2 " + "ORDER BY M, N");
        }

        query.setParameter("n1", n1);
        query.setParameter("n2", n2);
        query.setParameter("m1", m1);
        query.setParameter("m2", m2);
        // query.setParameter("d1", draught);

        List<DepthDenmark> result = null;

        if (dataType == DataType.SYDKATTEGAT) {
            result = query.getResultList();
        }

        if (dataType == DataType.NORDKATTEGAT) {
            List<DepthDenmarkNord> resultNord = query.getResultList();
            result = new ArrayList<DepthDenmark>();

            for (int i = 0; i < resultNord.size(); i++) {
                DepthDenmarkNord currentNord = resultNord.get(i);
                DepthDenmark converted = new DepthDenmark();
                converted.setId(currentNord.getId());
                converted.setLat(currentNord.getLat());
                converted.setLon(currentNord.getLon());
                converted.setN(currentNord.getN());
                converted.setM(currentNord.getM());
                converted.setDepth(currentNord.getDepth());
                result.add(converted);
            }
        }

        if (dataType == DataType.SF_BAY) {
            List<sfBay> resultSf = query.getResultList();
            result = new ArrayList<DepthDenmark>();

            for (int i = 0; i < resultSf.size(); i++) {
                sfBay currentSf = resultSf.get(i);
                DepthDenmark converted = new DepthDenmark();
                converted.setId(currentSf.getId());
                converted.setLat(currentSf.getLat());
                converted.setLon(currentSf.getLon());
                converted.setN(currentSf.getN());
                converted.setM(currentSf.getM());
                converted.setDepth(currentSf.getDepth());
                result.add(converted);
            }
        }

        if (dataType == DataType.HUMBER) {
            System.out.println("Retrieving Humber");
            List<Humber> resultHumber = query.getResultList();
            result = new ArrayList<DepthDenmark>();
            System.out.println("Retrieving Humber " + result.size());
            for (int i = 0; i < resultHumber.size(); i++) {
                Humber currentSf = resultHumber.get(i);
                DepthDenmark converted = new DepthDenmark();
                converted.setId(currentSf.getId());
                converted.setLat(currentSf.getLat());
                converted.setLon(currentSf.getLon());
                converted.setN(currentSf.getN());
                converted.setM(currentSf.getM());
                converted.setDepth(currentSf.getDepth());
                result.add(converted);
            }
        }

        // List<DepthDenmarkNord>

        // System.out.println("Results generated");
        return result;
    }

    @SuppressWarnings("unchecked")
    public List<TideDenmark> getTideArea(BoundingBoxPoint firstPos, BoundingBoxPoint secondPos) {

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

        Query query = entityManager.createQuery("SELECT td " + "FROM TideDenmark td " + "WHERE td.n between :n1 AND :n2 "
                + "AND td.m between :m1 AND :m2 "
                // + "AND td.depth is NOT NULL "
                // +
                // "AND td.time between '2012-01-05 22:00:00'  and '2012-01-05 23:00:00' "
                + "AND td.time between :t1  and :t2 " + "ORDER BY M, N");

        query.setParameter("n1", n1);
        query.setParameter("n2", n2);
        query.setParameter("m1", m1);
        query.setParameter("m2", m2);
        query.setParameter("t1", timeStart);
        query.setParameter("t2", timeEnd);

        List<TideDenmark> result = query.getResultList();

        return result;
    }

    /**
     * @return the humberTidalPoints
     */
    public List<HumberTidePoint> getHumberTidalPoints() {
        return humberTidalPoints;
    }

    
    
}
