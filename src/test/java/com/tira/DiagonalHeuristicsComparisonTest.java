package com.tira;

import javafx.util.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

class DiagonalHeuristicsComparisonTest {
    private static final org.apache.logging.log4j.Logger log
            = org.apache.logging.log4j.LogManager.getLogger(DiagonalHeuristicsComparisonTest.class);

    private class DummyUpdater implements IUpdateView {

        @Override
        public void updateView() {
            return;
        }
    }

    private MapGrid mapGrid;

    private static String fileName = "London_2_512.map";

    @BeforeEach
    void setUp() {
        OctileHeader octileHeader = new OctileHeader();
        IHeuristic iHeuristic = new DiagonalHeuristic();
        MapFileReader mapFileReader = new MapFileReader(
                fileName,
                octileHeader,
                iHeuristic);
        mapGrid = mapFileReader.getMapGrid();
        // log.debug("Read the file {}", fileName);
    }

    @Test
    void runTestsInOrder() {
        horizontalTestRight();
        horizontalTestLeft();
        verticalTestDown();
        verticalTestUp();
        diagonalTestDown();
        diagonalTestUp();
        complicatedTestOne();
        complicatedTestBack();
    }

    void horizontalTestRight() {
        log.debug("horizontalTestRight");
        runTheTests(100, 80, 170, 80);
    }

    void horizontalTestLeft() {
        log.debug("horizontalTestLeft");
        runTheTests(170, 80, 100, 80);
    }

    void verticalTestDown() {
        log.debug("verticalTestDown");
        runTheTests(265, 50, 265, 200);
    }

    void verticalTestUp() {
        log.debug("verticalTestUp");
        runTheTests(265, 200, 265, 50);
    }

    void diagonalTestDown() {
        log.debug("diagonalTestDown");
        runTheTests(265, 200, 465, 450);
    }

    void diagonalTestUp() {
        log.debug("diagonalTestUp");
        runTheTests(465, 450, 265, 200);
    }

    void complicatedTestOne() {
        log.debug("complicatedTestOne");
        runTheTests(320, 220, 150, 401);
    }

    void complicatedTestBack() {
        log.debug("complicatedTestBack");
        runTheTests(175, 500, 400, 285);
    }

    void runTheTests(Integer sX, Integer sY, Integer gX, Integer gY) {
        DummyUpdater dummyUpdater = new DummyUpdater();
        GridNode startNode = new GridNode(sX, sY);
        GridNode endNode = new GridNode(gX, gY);

        IHeuristic heuristic = mapGrid.getHeuristic();
        Long aStarMinimum = Long.MAX_VALUE;
        Double aStarMinimumD = 0.10;
        Double aStarMinimumD2 = 0.10;
        List<Pair<Double, Double>> otherAstar = new ArrayList<>();

        if (heuristic instanceof DiagonalHeuristic) {
            AStar aStar = new AStar(mapGrid, dummyUpdater, 1.0, 1.4142135623731);
            // Try different values for D and D2
            DiagonalHeuristic diagonalHeuristic = (DiagonalHeuristic) heuristic;
            Stack<GridNode> path;
            // change only the D value
            for (Double d = 0.90; d < 2.1; d+=0.1) {
                // for (Double d2 = 1.2142135623731; d2 < 1.8; d2+=0.1) {
                for (Double d2 = 1.2; d2 < 1.8; d2+=0.1) {
                    path = runOneAstarTest(d, d2, aStar, diagonalHeuristic, startNode, endNode);
                    if (path.size() < aStarMinimum) {
                        aStarMinimum = Long.valueOf(path.size());
                        aStarMinimumD = d;
                        aStarMinimumD2 = d2;
                    } else if (path.size() == aStarMinimum) {
                        otherAstar.add(new Pair(d, d2));
                    }
                }
            }
        }
        Long jpsMinimum = Long.MAX_VALUE;
        Double jpsMinimumD = 0.90;
        Double jpsMinimumD2 = 0.90;
        List<Pair<Double, Double>> otherJps = new ArrayList<>();

        if (heuristic instanceof DiagonalHeuristic) {
            JumpPointSearch jps = new JumpPointSearch(mapGrid, dummyUpdater, 1.0, 1.4142135623731);
            // Try different values for D and D2
            DiagonalHeuristic diagonalHeuristic = (DiagonalHeuristic) heuristic;
            Queue<GridNode> path;
            // change only the D value
            for (Double d = 0.90; d < 2.1; d+=0.1) {
                for (Double d2 = 1.2; d2 < 1.8; d2 += 0.1) {
                    path = runOneJpsTest(d, d2, jps, diagonalHeuristic, startNode, endNode);
                    if (path != null && path.size() < jpsMinimum) {
                        jpsMinimum = Long.valueOf(path.size());
                        jpsMinimumD = d;
                        jpsMinimumD2 = d2;
                    } else if (path.size() == jpsMinimum) {
                        otherJps.add(new Pair(d, d2));
                    }
                }
            }
        }
        log.debug("AStar ({},{}) -> ({},{}) minimum {} with D: {} and D2 {} (others: {})", sX, sY, gX, gY, aStarMinimum, aStarMinimumD, aStarMinimumD2, otherAstar);
        log.debug("JPS ({},{}) -> ({},{}) minimum {} with D: {} and D2 {} (others: {}", sX, sY, gX, gY, jpsMinimum, jpsMinimumD, jpsMinimumD2, otherJps);
    }

    private Stack<GridNode> runOneAstarTest(Double D, Double D2, AStar aStar, DiagonalHeuristic diagonalHeuristic, GridNode startNode, GridNode goal) {
        diagonalHeuristic.setD(D);
        diagonalHeuristic.setD2(D2);
        mapGrid.clearVisited();

        Stack<GridNode> path = aStar.a_star(startNode, goal, 0.0);
        Assertions.assertNotNull(path);
        return path;
    }

    private Queue<GridNode> runOneJpsTest(Double D, Double D2, JumpPointSearch jps, DiagonalHeuristic diagonalHeuristic, GridNode startNode, GridNode goal) {
        diagonalHeuristic.setD(D);
        diagonalHeuristic.setD2(D2);
        mapGrid.clearVisited();

        Queue<GridNode> path = jps.search(startNode, goal);
        return path;
    }
}