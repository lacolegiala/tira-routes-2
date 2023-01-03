package com.tira;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Queue;

@Log4j2
class JumpPointSearchDiagonalHeuristicTest {
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
        jumpPointSearch = new JumpPointSearch(mapGrid, dummyUpdater, 1.5, 1.5);
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
        GridNode startNode = new GridNode(360, 500);
        GridNode endNode = new GridNode(100, 100);
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
            runOneTest(1.2, 1.2, diagonalHeuristic, startNode, endNode);
            runOneTest(1.5, 1.5, diagonalHeuristic, startNode, endNode);
            runOneTest(2.0, 2.0, diagonalHeuristic, startNode, endNode);
            runOneTest(3.0, 2.0, diagonalHeuristic, startNode, endNode);
        }
    }

    @Test
    void jpsWithDifferentHeuristicValuesShortPath() {
        DummyUpdater dummyUpdater = new DummyUpdater();
        GridNode startNode = new GridNode(220, 120);
        GridNode endNode = new GridNode(100, 100);
        jumpPointSearch = new JumpPointSearch(mapGrid, dummyUpdater, 1.5, 1.5);
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
            runOneTest(1.2, 1.2, diagonalHeuristic, startNode, endNode);
            runOneTest(1.5, 1.5, diagonalHeuristic, startNode, endNode);
            runOneTest(2.0, 2.0, diagonalHeuristic, startNode, endNode);
            runOneTest(3.0, 2.0, diagonalHeuristic, startNode, endNode);
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
        GridNode startNode = new GridNode(10, 20);
        GridNode endNode = new GridNode(505, 505);
        jumpPointSearch = new JumpPointSearch(mapGrid, dummyUpdater, 1.5, 1.5);
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
            runOneTest(1.2, 1.2, diagonalHeuristic, startNode, endNode);
            runOneTest(1.5, 1.5, diagonalHeuristic, startNode, endNode);
            runOneTest(2.0, 2.0, diagonalHeuristic, startNode, endNode);
            runOneTest(3.0, 2.0, diagonalHeuristic, startNode, endNode);
        }
    }
}
