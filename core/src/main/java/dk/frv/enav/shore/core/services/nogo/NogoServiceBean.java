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
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.joda.time.DateTime;

import dk.dma.enav.model.geometry.CoordinateSystem;
import dk.dma.enav.model.geometry.Position;
import dk.frv.ais.geo.GeoLocation;
import dk.frv.enav.common.xml.nogo.request.NogoRequest;
import dk.frv.enav.common.xml.nogo.response.NogoResponse;
import dk.frv.enav.common.xml.nogo.types.BoundingBoxPoint;
import dk.frv.enav.common.xml.nogo.types.NogoPoint;
import dk.frv.enav.common.xml.nogo.types.NogoPolygon;
import dk.frv.enav.common.xml.nogoslices.request.NogoRequestSlices;
import dk.frv.enav.common.xml.nogoslices.response.NogoResponseSlices;
import dk.frv.enav.shore.core.domain.DepthDenmark;
import dk.frv.enav.shore.core.domain.TideDenmark;
import dk.frv.enav.shore.core.services.Errorcodes;
import dk.frv.enav.shore.core.services.ServiceException;

@Stateless
public class NogoServiceBean implements NogoService {

    @PersistenceContext(unitName = "enav")
    private EntityManager entityManager;

    public enum WorkerType {
        DEPTHPOINT, TIDEPOINT, DEPTHDATA, TIDEDATA, MAXTIDE, HUMBERTIDE;
    }

    public enum DataType {
        SYDKATTEGAT, NORDKATTEGAT, SF_BAY, HUMBER, UNKNOWN;
    }

    int errorCode = 0;
    DataType type;

    double latOffset;
    double lonOffset;

    NogoWorker nogoWorkerFirstPointDepth = null;

    NogoWorker nogoWorkerSecondPointDepth = null;

    NogoWorker nogoWorkerFirstPointTide = null;

    NogoWorker nogoWorkerSecondPointTide = null;

    NogoWorker nogoWorkerDepthData = null;

    NogoWorker nogoWorkerTideData = null;

    // old test static values
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

    @SuppressWarnings("deprecation")
    @Override
    public NogoResponse nogoPoll(NogoRequest nogoRequest) throws ServiceException {

        // First identify which area we are searching in

        GeoLocation northWest = new GeoLocation(nogoRequest.getNorthWestPointLat(), nogoRequest.getNorthWestPointLon());
        GeoLocation southEast = new GeoLocation(nogoRequest.getSouthEastPointLat(), nogoRequest.getSouthEastPointLon());

        // Determine the area
        getDataRegion(northWest, southEast);

        // The areas given are not valid
        if (this.type == DataType.UNKNOWN) {

            NogoResponse res = new NogoResponse();

            res.setNoGoErrorCode(17);
            res.setNoGoMessage(Errorcodes.getErrorMessage(17));

            return res;
        }

        // Create the workers
        createBathymetryWorkers(northWest, southEast);

        createTidalWorkers(northWest, southEast, nogoRequest.getStartDate(), nogoRequest.getEndDate());

        // Get the grid position of the data in the depth database
        nogoWorkerFirstPointDepth.start();
        nogoWorkerSecondPointDepth.start();

        // Get the grid position of the data in the tide database
        nogoWorkerFirstPointTide.start();
        nogoWorkerSecondPointTide.start();

        try {
            nogoWorkerFirstPointDepth.join();
            System.out.println("First depth point found");
            nogoWorkerSecondPointDepth.join();
            System.out.println("Second depth point found");
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

        System.out.println("depth points are " + nogoWorkerFirstPointDepth.getPoint() + ", "
                + nogoWorkerSecondPointDepth.getPoint());

        BoundingBoxPoint firstPosTide = nogoWorkerFirstPointTide.getPoint();
        BoundingBoxPoint secondPosTide = nogoWorkerSecondPointTide.getPoint();

        List<NogoPolygon> polyArea = new ArrayList<NogoPolygon>();

        if (firstPosDepth != null && secondPosDepth != null) {
            System.out.println("Bounding Box found - requesting data");

            nogoWorkerDepthData.setFirstPos(firstPosDepth);
            nogoWorkerDepthData.setSecondPos(secondPosDepth);

            nogoWorkerDepthData.setDraught(nogoRequest.getDraught());

            if (this.type != DataType.HUMBER) {
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
            }

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
            System.out.println("Depth database size: " + nogoWorkerDepthData.getDepthDatabaseResult().size());

            if (nogoWorkerDepthData.getDepthDatabaseResult().size() != 0) {

                List<DepthDenmark> depthResult = nogoWorkerDepthData.getDepthDatabaseResult();

                double depth = nogoRequest.getDraught();
                if (this.type == DataType.HUMBER) {
                    depth = -depth;

                    depthResult = combineWithHumberTide(depthResult, nogoWorkerTideData.getHumberTidalPoints());
                }

                System.out.println("Begin parsing into line segments");

                polyArea = parseResult(depthResult, nogoWorkerTideData.getTideDatabaseResult(), depth);
            }
            // polyArea = getNogoArea(firstPos, secondPos, -7);
            System.out.println("Data recieved and parsed");
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

        errorCode = 0;

        System.out.println("Sending data");
        return res;
    }

    @Override
    public void getDataRegion(GeoLocation northWest, GeoLocation southEast) {
        // Sydkattegat data
        if (northWest.getLatitude() > 54.36294 && northWest.getLatitude() < 56.36316 && northWest.getLongitude() > 9.419409
                && northWest.getLongitude() < 13.149009 && southEast.getLatitude() > 54.36294 && southEast.getLatitude() < 56.36316
                && southEast.getLongitude() > 9.419409 && southEast.getLongitude() < 13.149009) {

            latOffset = 0.00055504;
            lonOffset = 0.00055504;

            type = DataType.SYDKATTEGAT;

        }

        // Nordkattegat data
        if (northWest.getLatitude() > 56.34096 && northWest.getLatitude() < 58.26237 && northWest.getLongitude() > 9.403869
                && northWest.getLongitude() < 12.148899 && southEast.getLatitude() > 56.34096 && southEast.getLatitude() < 58.26237
                && southEast.getLongitude() > 9.403869 && southEast.getLongitude() < 12.148899) {
            // System.out.println("Valid nordkattegat point");

            latOffset = 0.00055504;
            lonOffset = 0.00055504;

            type = DataType.NORDKATTEGAT;
        }

        // SF Bay data
        if (northWest.getLatitude() > 37.17 && northWest.getLatitude() < 38.35 && northWest.getLongitude() > -123.21
                && northWest.getLongitude() < -121.32 && southEast.getLatitude() > 37.17 && southEast.getLatitude() < 38.35
                && southEast.getLongitude() > -123.21 && southEast.getLongitude() < -121.32) {

            latOffset = 0.000418;
            lonOffset = latOffset;

            type = DataType.SF_BAY;
        }

        // Humber Data
        if (northWest.getLatitude() > 53.516960327477875 && northWest.getLatitude() < 53.741792160953665
                && northWest.getLongitude() > -0.8661253027560037 && northWest.getLongitude() < 0.24236332125267657
                && southEast.getLatitude() > 53.516960327477875 && southEast.getLatitude() < 53.741792160953665
                && southEast.getLongitude() > -0.8661253027560037 && southEast.getLongitude() < 0.24236332125267657) {
            System.out.println("Valid Humber point");

            latOffset = 0.00011247;
            lonOffset = 0.000554522;

            type = DataType.HUMBER;
        }

        if ((northWest.getLatitude() > 58.26237 || northWest.getLatitude() < 54.36294 || northWest.getLongitude() > 13.149009
                || northWest.getLongitude() < 9.403869

                || southEast.getLatitude() > 58.26237 || southEast.getLatitude() < 54.36294 || southEast.getLongitude() > 13.149009 || southEast
                .getLongitude() < 9.403869)

                && (northWest.getLatitude() > 38.35 || northWest.getLatitude() < 37.16 || northWest.getLongitude() > -121.32
                        || northWest.getLongitude() < -123.21

                        || southEast.getLatitude() > 38.35 || southEast.getLatitude() < 37.16 || southEast.getLongitude() > -121.32 || southEast
                        .getLongitude() < -123.21)

                && (northWest.getLatitude() < 53.516960327477875 || northWest.getLatitude() > 53.741792160953665
                        || northWest.getLongitude() < -0.8661253027560037 || northWest.getLongitude() > 0.24236332125267657
                        || southEast.getLatitude() < 53.516960327477875 || southEast.getLatitude() > 53.741792160953665
                        || southEast.getLongitude() < -0.8661253027560037 || southEast.getLongitude() > 0.24236332125267657)

        ) {
            type = DataType.UNKNOWN;
        }
    }

    @Override
    public void createTidalWorkers(GeoLocation northWest, GeoLocation southEast, Date timeStart, Date timeEnd) {

        // Sydkattegat data
        if (type == DataType.SYDKATTEGAT) {

            nogoWorkerFirstPointTide = new NogoWorker(entityManager, WorkerType.TIDEPOINT, DataType.SYDKATTEGAT);
            nogoWorkerSecondPointTide = new NogoWorker(entityManager, WorkerType.TIDEPOINT, DataType.SYDKATTEGAT);

            nogoWorkerTideData = new NogoWorker(entityManager, WorkerType.TIDEDATA, DataType.SYDKATTEGAT);

        }
        // Nordkattegat data
        if (type == DataType.NORDKATTEGAT) {
            // System.out.println("Valid nordkattegat point");

            nogoWorkerFirstPointTide = new NogoWorker(entityManager, WorkerType.TIDEPOINT, DataType.NORDKATTEGAT);
            nogoWorkerSecondPointTide = new NogoWorker(entityManager, WorkerType.TIDEPOINT, DataType.NORDKATTEGAT);

            nogoWorkerTideData = new NogoWorker(entityManager, WorkerType.TIDEDATA, DataType.NORDKATTEGAT);

        }

        // SF Bay data
        if (type == DataType.SF_BAY) {
            // System.out.println("Valid nordkattegat point");

            nogoWorkerFirstPointTide = new NogoWorker(entityManager, WorkerType.TIDEPOINT, DataType.SF_BAY);
            nogoWorkerSecondPointTide = new NogoWorker(entityManager, WorkerType.TIDEPOINT, DataType.SF_BAY);

            nogoWorkerTideData = new NogoWorker(entityManager, WorkerType.TIDEDATA, DataType.SF_BAY);

        }

        // Humber Data
        if (type == DataType.HUMBER) {

            nogoWorkerFirstPointTide = new NogoWorker(entityManager, WorkerType.TIDEPOINT, DataType.HUMBER);
            nogoWorkerSecondPointTide = new NogoWorker(entityManager, WorkerType.TIDEPOINT, DataType.HUMBER);

            nogoWorkerTideData = new NogoWorker(entityManager, WorkerType.HUMBERTIDE, DataType.HUMBER);

            java.sql.Timestamp timeStartSql = new Timestamp(timeStart.getTime());
            java.sql.Timestamp timeEndSql = new Timestamp(timeEnd.getTime());

            nogoWorkerTideData.setTimeStart(timeStartSql);
            nogoWorkerTideData.setTimeEnd(timeEndSql);
        }

        nogoWorkerFirstPointTide.setPos(northWest);

        nogoWorkerSecondPointTide.setPos(southEast);
    }

    @Override
    public void createBathymetryWorkers(GeoLocation northWest, GeoLocation southEast) {

        // Sydkattegat data
        if (type == DataType.SYDKATTEGAT) {

            nogoWorkerFirstPointDepth = new NogoWorker(entityManager, WorkerType.DEPTHPOINT, DataType.SYDKATTEGAT);
            nogoWorkerSecondPointDepth = new NogoWorker(entityManager, WorkerType.DEPTHPOINT, DataType.SYDKATTEGAT);

            nogoWorkerDepthData = new NogoWorker(entityManager, WorkerType.DEPTHDATA, DataType.SYDKATTEGAT);
        }

        // Nordkattegat data
        if (type == DataType.NORDKATTEGAT) {
            // System.out.println("Valid nordkattegat point");

            nogoWorkerFirstPointDepth = new NogoWorker(entityManager, WorkerType.DEPTHPOINT, DataType.NORDKATTEGAT);
            nogoWorkerSecondPointDepth = new NogoWorker(entityManager, WorkerType.DEPTHPOINT, DataType.NORDKATTEGAT);

            nogoWorkerDepthData = new NogoWorker(entityManager, WorkerType.DEPTHDATA, DataType.NORDKATTEGAT);

        }

        // SF Bay data
        if (type == DataType.SF_BAY) {

            nogoWorkerFirstPointDepth = new NogoWorker(entityManager, WorkerType.DEPTHPOINT, DataType.SF_BAY);
            nogoWorkerSecondPointDepth = new NogoWorker(entityManager, WorkerType.DEPTHPOINT, DataType.SF_BAY);

            nogoWorkerDepthData = new NogoWorker(entityManager, WorkerType.DEPTHDATA, DataType.SF_BAY);

        }

        // Humber Data
        if (type == DataType.HUMBER) {
            System.out.println("Valid Humber point");

            nogoWorkerFirstPointDepth = new NogoWorker(entityManager, WorkerType.DEPTHPOINT, DataType.HUMBER);
            nogoWorkerSecondPointDepth = new NogoWorker(entityManager, WorkerType.DEPTHPOINT, DataType.HUMBER);

            nogoWorkerDepthData = new NogoWorker(entityManager, WorkerType.DEPTHDATA, DataType.HUMBER);

        }

        nogoWorkerFirstPointDepth.setPos(northWest);

        nogoWorkerSecondPointDepth.setPos(southEast);

    }

    private List<DepthDenmark> combineWithHumberTide(List<DepthDenmark> result, List<HumberTidePoint> list) {

        if (list == null || list.size() == 0) {
            errorCode = 18;
            System.out.println("ERROR CODE 18 CANNOT COMBINE");
            return result;
        }

        for (int i = 0; i < result.size(); i++) {

            Position pointPosition = Position.create(result.get(i).getLat(), result.get(i).getLon());

            int closetGauge = -1;
            double smallestDistance = Double.MAX_VALUE;
            for (int j = 0; j < list.size(); j++) {
                double distance = list.get(j).getPosition().distanceTo(pointPosition, CoordinateSystem.CARTESIAN);

                if (distance < smallestDistance) {
                    closetGauge = j;
                    smallestDistance = distance;
                }
            }

//             System.out.println(result.get(i).getDepth());

            if (result.get(i).getDepth() > 0) {
                result.get(i).setDepth(result.get(i).getDepth() + list.get(closetGauge).getMinimumDepth());
            }

            // System.out.println("Using gauage index " + closetGauge + " with min depth" +
            // list.get(closetGauge).getMinimumDepth());

            // Example 10 meters tide ie. risen by 10
            // result.get(i).setDepth(result.get(i).getDepth() + 8);

        }

        return result;
    }

    // private void findNearestTideGauge(Position position) {
    // Position spurnPoint = Position.create(53.582778, 0.116111);
    //
    // }

    @SuppressWarnings("unused")
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

        if (resultTide == null && this.type != DataType.HUMBER) {
            errorCode = 18;
        }

        // Seperate it into lines - tide - if we got em
        if (resultTide != null) {

            // Disable tide for now
            if (true) {
                // if (resultTide.size() == 0){
                // System.out.println("No tide");
                // errorCode = 18;
            } else {

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
        }

        List<List<DepthDenmark>> parsedLines = new ArrayList<List<DepthDenmark>>();

        // Remove invalid positions
        for (int i = 0; i < lines.size(); i++) {
            parsedLines.add(new ArrayList<DepthDenmark>());
            for (int k = 0; k < lines.get(i).size(); k++) {

                if (this.type == DataType.HUMBER) {

                    if (lines.get(i).get(k).getDepth() == null || lines.get(i).get(k).getDepth() < depth) {
                        // System.out.println("Current line depth is: " + lines.get(i).get(k).getDepth());
                        parsedLines.get(i).add(lines.get(i).get(k));

                    }

                } else {
                    if (lines.get(i).get(k).getDepth() == null || lines.get(i).get(k).getDepth() > depth) {
                        // System.out.println("Current line depth is: " + lines.get(i).get(k).getDepth());

                        parsedLines.get(i).add(lines.get(i).get(k));
                    }

                }

            }

        }

        lines = parsedLines;

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

        // All the line component are split into sections, ie. all on same index m are put in a list together
        List<List<List<DepthDenmark>>> lineSection = new ArrayList<List<List<DepthDenmark>>>();
        List<List<DepthDenmark>> tempLine = new ArrayList<List<DepthDenmark>>();

        if (parsed.size() > 0) {

            m = parsed.get(0).get(0).getM();

            // Split the list based on the m index - note the index is opposite of the longitude coordinates
            for (List<DepthDenmark> splittedLines : parsed) {
                if ((splittedLines.get(0).getM()) > m) {
                    // System.out.println("New line detected");
                    lineSection.add(tempLine);
                    tempLine = new ArrayList<List<DepthDenmark>>();
                    tempLine.add(splittedLines);
                    m = splittedLines.get(0).getM();
                } else {
                    tempLine.add(splittedLines);
                }
            }
            lineSection.add(tempLine);

            // Reverse the list
            Collections.reverse(lineSection);

            // Seperate? Find all the required connection things

            List<NogoPolygon> allNeighboursLine = new ArrayList<NogoPolygon>();

            for (int i = 0; i < lineSection.size(); i++) {

                for (int j = 0; j < lineSection.get(i).size(); j++) {

                    List<NogoPolygon> neighbours = new ArrayList<NogoPolygon>();

                    // It has a next line
                    if (i != lineSection.size() - 1) {
                        neighbours = connectNeighbourLines.connectFindValidNeighbours(lineSection.get(i).get(j),
                                lineSection.get(i + 1));

                        allNeighboursLine.addAll(neighbours);

                        //
                        // if (neighbours.size() != 0){
                        // for (int k = 0; k < neighbours.size(); k++) {
                        //
                        // //Check for overlap, first between line + 1 and the triangles
                        // if (!connectNeighbourLines.doesOverlap(neighbours.get(k), lineSection.get(i+1))){
                        // res.add(neighbours.get(k));
                        // }
                        //
                        // //Then for each triangle with the other triangles
                        //
                        // //If no overlap, add it
                        //
                        //
                        // }
                        // }
                    }

                }

                // System.out.println(allNeighboursLine.size());

                // List<NogoPolygon> neighbours = connectNeighbourLines.connectFindValidNeighbours(lineSection.get(0).get(i),
                // lineSection.get(1));

            }

            // We found our neighbours, make sure they don't clash together

            if (type != DataType.SF_BAY || type != DataType.HUMBER) {

                List<NogoPolygon> finalNeighbours = connectNeighbourLines.triangleOverlap(allNeighboursLine);

                for (int k = 0; k < finalNeighbours.size(); k++) {
                    res.add(finalNeighbours.get(k));
                }

            }

        }
        // List<List<DepthDenmark>> neighbours = connectNeighbourLines.connectFindValidNeighbours(lineSection.get(0).get(0),
        // lineSection.get(1));
        // System.out.println(neighbours.size() + " neighbours found");
        //
        // neighbours = connectNeighbourLines.connectFindValidNeighbours(lineSection.get(1).get(0), lineSection.get(2));
        // System.out.println(neighbours.size() + " neighbours found");

        NogoPolygon polygon;
        NogoPolygon temp;

        // System.out.println("splitted lines is: " + parsed.size());

        // double lonOffset = 0.0007854;
        // The difference between each point / 2. This is used in calculating
        // the polygons surrounding the lines

        // 100m spacing
        // double latOffset = 0.00055504;
        // // double latOffset = 0.0;
        //
        // double lonOffset = 0.00055504;
        // double lonOffset = 0.0;

        // 50m spacing
        // double latOffset = 0.000290;

        for (List<DepthDenmark> splittedLines : parsed) {

            // Singleton
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

                /** Add to draw singletons **/
                // }

                NogoPoint westPoint = new NogoPoint(temp.getPolygon().get(0).getLat(), temp.getPolygon().get(0).getLon()
                        - lonOffset);
                NogoPoint eastPoint = new NogoPoint(temp.getPolygon().get(1).getLat(), temp.getPolygon().get(1).getLon()
                        + lonOffset);

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

                /** Remove to draw singletons **/
                // }

            }
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

    @SuppressWarnings("deprecation")
    @Override
    public NogoResponseSlices nogoPoll(NogoRequestSlices nogoRequest) throws ServiceException {

        NogoResponseSlices res = new NogoResponseSlices();

        GeoLocation northWest = new GeoLocation(nogoRequest.getNorthWestPointLat(), nogoRequest.getNorthWestPointLon());
        GeoLocation southEast = new GeoLocation(nogoRequest.getSouthEastPointLat(), nogoRequest.getSouthEastPointLon());

        getDataRegion(northWest, southEast);

        if (this.type == DataType.UNKNOWN) {
            res.setNoGoErrorCode(17);
            res.setNoGoMessage(Errorcodes.getErrorMessage(17));
            return res;
        }

        // Create the workers
        createBathymetryWorkers(northWest, southEast);

        // Only a single slice is requested, just perform normal poll and add result
        if (nogoRequest.getSlices() == 1) {
            System.out.println("Single slice - special case");
            NogoRequest singleSliceRequest = new NogoRequest();
            singleSliceRequest.setDraught(nogoRequest.getDraught());
            singleSliceRequest.setStartDate(nogoRequest.getStartDate());
            singleSliceRequest.setEndDate(nogoRequest.getEndDate());
            singleSliceRequest.setNorthWestPointLat(nogoRequest.getNorthWestPointLat());
            singleSliceRequest.setNorthWestPointLon(nogoRequest.getNorthWestPointLon());
            singleSliceRequest.setSouthEastPointLat(nogoRequest.getSouthEastPointLat());
            singleSliceRequest.setSouthEastPointLon(nogoRequest.getSouthEastPointLon());

            NogoResponse singleResponse = nogoPoll(singleSliceRequest);

            res.setNoGoErrorCode(singleResponse.getNoGoErrorCode());
            res.setNoGoMessage(Errorcodes.getErrorMessage(singleResponse.getNoGoErrorCode()));
            res.setValidFrom(singleResponse.getValidFrom());
            res.setValidTo(singleResponse.getValidTo());

            return res;

        }

        // Retrieve the base data
        // // Get the grid position of the data in the depth database
        nogoWorkerFirstPointDepth.start();
        nogoWorkerSecondPointDepth.start();

        try {
            nogoWorkerFirstPointDepth.join();
            System.out.println("First depth point found");
            nogoWorkerSecondPointDepth.join();
            System.out.println("Second depth point found");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        BoundingBoxPoint firstPosDepth = nogoWorkerFirstPointDepth.getPoint();
        BoundingBoxPoint secondPosDepth = nogoWorkerSecondPointDepth.getPoint();

        if (firstPosDepth != null && secondPosDepth != null) {
            // System.out.println("Bounding Box found - requesting data");
            //
            nogoWorkerDepthData.setFirstPos(firstPosDepth);
            nogoWorkerDepthData.setSecondPos(secondPosDepth);

            nogoWorkerDepthData.setDraught(nogoRequest.getDraught());

            System.out.println("Depth data start");
            nogoWorkerDepthData.start();

            try {
                nogoWorkerDepthData.join();
                System.out.println("Depth data thread joined");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (nogoWorkerDepthData.getDepthDatabaseResult().size() != 0) {

                // This is the raw data from the database. Should be re-used.
                // List<DepthDenmark> depthResult = nogoWorkerDepthData.getDepthDatabaseResult();

                long miliseconds = nogoRequest.getEndDate().getTime() - nogoRequest.getStartDate().getTime();
                long minutes = TimeUnit.MILLISECONDS.toMinutes(miliseconds);
                int timeSlice = (int) (minutes / nogoRequest.getSlices());
                DateTime currentTime = new DateTime(nogoRequest.getStartDate().getTime());
                DateTime nextTime = currentTime.plusMinutes(timeSlice);

                List<DepthDenmark> depthResult = nogoWorkerDepthData.getDepthDatabaseResult();
                
                for (int i = 0; i < nogoRequest.getSlices(); i++) {
                    
                    List<DepthDenmark> depthResultCopy = new ArrayList<DepthDenmark>();
                    
                    for (DepthDenmark entry : depthResult) {
                        depthResultCopy.add(entry.clone());
                    }
                    
//                    System.out.println("Processing slice " + i + " from " + currentTime + " to " + nextTime);
                    // Setup tide worker
                    createTidalWorkers(northWest, southEast, new Date(currentTime.getMillis()), new Date(nextTime.getMillis()));

                    // // Get the grid position of the data in the tide database
                    nogoWorkerFirstPointTide.start();
                    nogoWorkerSecondPointTide.start();

                    try {
                        nogoWorkerFirstPointTide.join();
                        // System.out.println("First tide point found");
                        nogoWorkerSecondPointTide.join();
                        // System.out.println("Second tide point found");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    BoundingBoxPoint firstPosTide = nogoWorkerFirstPointTide.getPoint();
                    BoundingBoxPoint secondPosTide = nogoWorkerSecondPointTide.getPoint();

                    if (this.type != DataType.HUMBER) {
//                        System.out.println("Not humber");
                        nogoWorkerTideData.setFirstPos(firstPosTide);
                        nogoWorkerTideData.setSecondPos(secondPosTide);

                        // Use 01-05 until we get better database setup
                        // 2012-01-05 22:00:00
                        java.sql.Timestamp timeStart = new Timestamp(112, 0, 5, 0, 0, 0, 0);
                        java.sql.Timestamp timeEnd = new Timestamp(112, 0, 5, 0, 0, 0, 0);

                        timeStart.setHours(currentTime.getHourOfDay());
                        timeEnd.setHours(nextTime.getHourOfDay());

                        nogoWorkerTideData.setTimeStart(timeStart);
                        nogoWorkerTideData.setTimeEnd(timeEnd);
                    }

                    nogoWorkerTideData.start();

                    try {
                        nogoWorkerTideData.join();
//                        System.out.println("Tide data thread joined");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // List<DepthDenmark> depthResultCopy = new ArrayList<DepthDenmark>(depthResult);
                    List<NogoPolygon> polyArea = new ArrayList<NogoPolygon>();
                    double depth = nogoRequest.getDraught();

                    if (this.type == DataType.HUMBER) {
//                        System.out.println("Humber combining");
                        depth = -depth;
                        depthResultCopy = combineWithHumberTide(depthResultCopy, nogoWorkerTideData.getHumberTidalPoints());
                    }

                    // Combine into polygon area
                    polyArea = parseResult(depthResultCopy, nogoWorkerTideData.getTideDatabaseResult(), depth);

                    NogoResponse sliceResponse = new NogoResponse();

                    for (int j = 0; j < polyArea.size(); j++) {
                        sliceResponse.addPolygon(polyArea.get(j));
                    }

                    Date requestStart = new Date(currentTime.getMillis());
                    Date requestEnd = new Date(nextTime.getMillis());

                    sliceResponse.setValidFrom(requestStart);
                    sliceResponse.setValidTo(requestEnd);
                    sliceResponse.setNoGoErrorCode(errorCode);
                    sliceResponse.setNoGoMessage(Errorcodes.getErrorMessage(errorCode));
                    res.addResponse(sliceResponse);

                    errorCode = 0;

                    currentTime = nextTime;
                    nextTime = currentTime.plusMinutes(timeSlice);

//                    System.out.println("Result added");
                }

            }
        }

        res.setValidFrom(nogoRequest.getStartDate());
        res.setValidTo(nogoRequest.getEndDate());
        // sliceResponse.setNoGoErrorCode(errorCode);
        res.setNoGoMessage(Errorcodes.getErrorMessage(errorCode));

        System.out.println("Returning " + res.getResponses().size() + " slices");

        //

        return res;
    }

}
