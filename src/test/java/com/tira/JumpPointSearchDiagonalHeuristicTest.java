package com.tira;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Queue;

class JumpPointSearchDiagonalHeuristicTest {

    private static final org.apache.logging.log4j.Logger log
            = org.apache.logging.log4j.LogManager.getLogger(JumpPointSearchDiagonalHeuristicTest.class);

    private class DummyUpdater implements IUpdateView {

        @Override
        public void updateView() {
            return;
        }
    }

    private MapGrid mapGrid;
    private static String fileName = "London_2_512.map";

    JumpPointSearch jumpPointSearch;

    DummyUpdater dummyUpdater = new DummyUpdater();

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
        jumpPointSearch = new JumpPointSearch(mapGrid, dummyUpdater, 1.0, 1.4142135623731);
    }

    @Test
    void search() {
    }

    @Test
    void findPathToEast() {
        GridNode start = new GridNode(100, 100);
        GridNode goal = new GridNode(110, 100);
        Queue<GridNode> path = jumpPointSearch.search(start, goal);
        log.debug("Result is now {}", path);
        Assertions.assertNotNull(path);
    }

    @Test
    void jpsWithDifferentHeuristicValues() {
        DummyUpdater dummyUpdater = new DummyUpdater();
        GridNode startNode = new GridNode(22, 31);
        GridNode endNode = new GridNode(49, 31);
        Long startStamp = System.nanoTime();
        Queue<GridNode> path = jumpPointSearch.search(startNode, endNode);
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
            runOneTest(0.99, 1.4142135623731, diagonalHeuristic, startNode, endNode);
            runOneTest(1.1, 1.4142135623731, diagonalHeuristic, startNode, endNode);
            runOneTest(1.0, 1.0, diagonalHeuristic, startNode, endNode);
            runOneTest(0.8, 1.4142135623731, diagonalHeuristic, startNode, endNode);
        }
    }

    @Test
    void jpsWithDifferentHeuristicValuesShortPath() {
        DummyUpdater dummyUpdater = new DummyUpdater();
        GridNode startNode = new GridNode(414, 245);
        GridNode endNode = new GridNode(408, 236);
        jumpPointSearch = new JumpPointSearch(mapGrid, dummyUpdater, 1.0, 1.4142135623731);
        Long startStamp = System.nanoTime();
        Queue<GridNode> path = jumpPointSearch.search(startNode, endNode);
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
            runOneTest(0.99, 1.4142135623731, diagonalHeuristic, startNode, endNode);
            runOneTest(1.1, 1.4142135623731, diagonalHeuristic, startNode, endNode);
            runOneTest(1.0, 1.0, diagonalHeuristic, startNode, endNode);
            runOneTest(0.8, 1.4142135623731, diagonalHeuristic, startNode, endNode);
        }
    }

    private void runOneTest(Double D, Double D2, DiagonalHeuristic diagonalHeuristic, GridNode startNode, GridNode endNode) {
        diagonalHeuristic.setD(D);
        diagonalHeuristic.setD2(D2);
        mapGrid.clearVisited();
        Long startStamp = System.nanoTime();
        Queue<GridNode> path = jumpPointSearch.search(startNode, endNode);
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
    void jpsWithDifferentHeuristicValuesLongPath() {
        DummyUpdater dummyUpdater = new DummyUpdater();
        GridNode startNode = new GridNode(55, 2);
        GridNode endNode = new GridNode(442, 355);
        jumpPointSearch = new JumpPointSearch(mapGrid, dummyUpdater, 1.0, 1.4142135623731);
        Long startStamp = System.nanoTime();
        Queue<GridNode> path = jumpPointSearch.search(startNode, endNode);
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
            runOneTest(0.99, 1.4142135623731, diagonalHeuristic, startNode, endNode);
            runOneTest(1.1, 1.4142135623731, diagonalHeuristic, startNode, endNode);
            runOneTest(1.0, 1.0, diagonalHeuristic, startNode, endNode);
            runOneTest(0.8, 1.4142135623731, diagonalHeuristic, startNode, endNode);
        }
    }
}
