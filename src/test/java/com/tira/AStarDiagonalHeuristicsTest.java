package com.tira;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Stack;

class AStarDiagonalHeuristicsTest {

    private static final org.apache.logging.log4j.Logger log
            = org.apache.logging.log4j.LogManager.getLogger(AStarDiagonalHeuristicsTest.class);

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
        log.debug("Read the file {}", fileName);
    }

    @Test
    void aStar() {
        DummyUpdater dummyUpdater = new DummyUpdater();
        GridNode startNode = new GridNode(360, 500);
        GridNode endNode = new GridNode(100, 100);
        AStar aStar = new AStar(mapGrid, dummyUpdater, 1.0, 1.4142135623731);
        Long startStamp = System.nanoTime();
        Stack<GridNode> path = aStar.a_star(startNode, endNode, 0.0);
        Long endStamp = System.nanoTime();
        Long duration = endStamp - startStamp;
        Assertions.assertNotNull(path);
        if (path != null) {
            log.debug("Found in {} ms : path len {} ({} ns)", duration / 1000000, path.size(), duration);
        } else {
            log.debug("Failed in {} : path len {} ms ({})", duration / 1000000, path.size(), duration);
        }
    }

    @Test
    void aStarWithDifferentHeuristicValues() {
        DummyUpdater dummyUpdater = new DummyUpdater();
        GridNode startNode = new GridNode(22, 31);
        GridNode endNode = new GridNode(49, 31);
        AStar aStar = new AStar(mapGrid, dummyUpdater, 1.0, 1.4142135623731);
        Long startStamp = System.nanoTime();
        Stack<GridNode> path = aStar.a_star(startNode, endNode, 0.0);
        Long endStamp = System.nanoTime();
        Long duration = endStamp - startStamp;
        Assertions.assertNotNull(path);
        if (path != null) {
            log.debug("Found in {} ms : path len {} ({} ns)", duration / 1000000, path.size(), duration);
        } else {
            log.debug("Failed in {} : path len {} ms ({})", duration / 1000000, path.size(), duration);
        }
        IHeuristic heuristic = mapGrid.getHeuristic();
        if (heuristic instanceof DiagonalHeuristic) {
            // Try different values for D and D2
            DiagonalHeuristic diagonalHeuristic = (DiagonalHeuristic) heuristic;
            runOneTest(0.99, 1.4142135623731, aStar, diagonalHeuristic, startNode, endNode);
            runOneTest(1.1, 1.4142135623731, aStar, diagonalHeuristic, startNode, endNode);
            runOneTest(1.0, 1.0, aStar, diagonalHeuristic, startNode, endNode);
            runOneTest(0.8, 1.4142135623731, aStar, diagonalHeuristic, startNode, endNode);
        }
    }

    @Test
    void aStarWithDifferentHeuristicValuesShortPath() {
        DummyUpdater dummyUpdater = new DummyUpdater();
        GridNode startNode = new GridNode(414, 245);
        GridNode endNode = new GridNode(408, 236);
        AStar aStar = new AStar(mapGrid, dummyUpdater, 1.0, 1.4142135623731);
        Long startStamp = System.nanoTime();
        Stack<GridNode> path = aStar.a_star(startNode, endNode, 0.0);
        Long endStamp = System.nanoTime();
        Long duration = endStamp - startStamp;
        Assertions.assertNotNull(path);
        if (path != null) {
            log.debug("Found in {} ms : path len {} ({} ns)", duration / 1000000, path.size(), duration);
        } else {
            log.debug("Failed in {} : path len {} ms ({})", duration / 1000000, path.size(), duration);
        }
        IHeuristic heuristic = mapGrid.getHeuristic();
        if (heuristic instanceof DiagonalHeuristic) {
            // Try different values for D and D2
            DiagonalHeuristic diagonalHeuristic = (DiagonalHeuristic) heuristic;
            runOneTest(0.99, 1.4142135623731, aStar, diagonalHeuristic, startNode, endNode);
            runOneTest(1.1, 1.4142135623731, aStar, diagonalHeuristic, startNode, endNode);
            runOneTest(1.0, 1.0, aStar, diagonalHeuristic, startNode, endNode);
            runOneTest(0.8, 1.4142135623731, aStar, diagonalHeuristic, startNode, endNode);
        }
    }


    private void runOneTest(Double D, Double D2, AStar aStar, DiagonalHeuristic diagonalHeuristic, GridNode startNode, GridNode goal) {
        diagonalHeuristic.setD(D);
        diagonalHeuristic.setD2(D2);
        mapGrid.clearVisited();
        Long startStamp = System.nanoTime();
        Stack<GridNode> path = aStar.a_star(startNode, goal, 0.0);
        Long endStamp = System.nanoTime();
        Long duration = endStamp - startStamp;
        Assertions.assertNotNull(path);
        if (path != null) {
            log.debug("D {}, D2 {} Found in {} ms : path len {} ({} ns)", D, D2, duration / 1000000, path.size(), duration);
        } else {
            log.debug("D {}, D2 {} Failed in {} ms : path len {} ({} ns)", D, D2, duration / 1000000, path.size(), duration);
        }

    }

    @Test
    void aStarWithDifferentHeuristicValuesLongPath() {
        DummyUpdater dummyUpdater = new DummyUpdater();
        GridNode startNode = new GridNode(55, 2);
        GridNode endNode = new GridNode(442, 355);
        AStar aStar = new AStar(mapGrid, dummyUpdater, 1.0, 1.4142135623731);
        Long startStamp = System.nanoTime();
        Stack<GridNode> path = aStar.a_star(startNode, endNode, 0.0);
        Long endStamp = System.nanoTime();
        Long duration = endStamp - startStamp;
        Assertions.assertNotNull(path);
        if (path != null) {
            log.debug("Found in {} ms : path len {} ({} ns)", duration / 1000000, path.size(), duration);
        } else {
            log.debug("Failed in {} : path len {} ms ({})", duration / 1000000, path.size(), duration);
        }
        IHeuristic heuristic = mapGrid.getHeuristic();
        if (heuristic instanceof DiagonalHeuristic) {
            // Try different values for D and D2, there is difference in speed and the length of the route found
            DiagonalHeuristic diagonalHeuristic = (DiagonalHeuristic) heuristic;
            runOneTest(0.99, 1.4142135623731, aStar, diagonalHeuristic, startNode, endNode);
            runOneTest(1.1, 1.4142135623731, aStar, diagonalHeuristic, startNode, endNode);
            runOneTest(1.0, 1.0, aStar, diagonalHeuristic, startNode, endNode);
            runOneTest(0.8, 1.4142135623731, aStar, diagonalHeuristic, startNode, endNode);
        }
    }

    @Test
    void aStarWithDifferentHeuristicValuesLongPathBack() {
        DummyUpdater dummyUpdater = new DummyUpdater();
        GridNode startNode = new GridNode(505, 505);
        GridNode endNode = new GridNode(10, 20);
        AStar aStar = new AStar(mapGrid, dummyUpdater, 1.0, 1.4142135623731);
        Long startStamp = System.nanoTime();
        Stack<GridNode> path = aStar.a_star(startNode, endNode, 0.0);
        Long endStamp = System.nanoTime();
        Long duration = endStamp - startStamp;
        Assertions.assertNotNull(path);
        if (path != null) {
            log.debug("Found in {} ms : path len {} ({} ns)", duration / 1000000, path.size(), duration);
        } else {
            log.debug("Failed in {} : path len {} ms ({})", duration / 1000000, path.size(), duration);
        }
        IHeuristic heuristic = mapGrid.getHeuristic();
        if (heuristic instanceof DiagonalHeuristic) {
            // Try different values for D and D2, there is difference in speed and the length of the route found
            DiagonalHeuristic diagonalHeuristic = (DiagonalHeuristic) heuristic;
            runOneTest(0.99, 1.4142135623731, aStar, diagonalHeuristic, startNode, endNode);
            runOneTest(1.1, 1.4142135623731, aStar, diagonalHeuristic, startNode, endNode);
            runOneTest(1.0, 1.0, aStar, diagonalHeuristic, startNode, endNode);
            runOneTest(0.8, 1.4142135623731, aStar, diagonalHeuristic, startNode, endNode);
        }
    }

}