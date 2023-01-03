package com.tira;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Stack;

@Log4j2
class IterativeDeepeningAStarDiagonalHeuristicsTest {

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
    void idaStar() {
        DummyUpdater dummyUpdater = new DummyUpdater();
        GridNode startNode = new GridNode(360, 500);
        GridNode endNode = new GridNode(100, 100);
        IterativeDeepeningAStar iterativeDeepeningAStar = new IterativeDeepeningAStar(endNode, mapGrid, dummyUpdater, 1.5, 1.5);
        Long startStamp = System.nanoTime();
        Stack<GridNode> path = iterativeDeepeningAStar.idaStar(startNode);
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
    void idaStarWithDifferentHeuristicValues() {
        DummyUpdater dummyUpdater = new DummyUpdater();
        GridNode startNode = new GridNode(360, 500);
        GridNode endNode = new GridNode(100, 100);
        IterativeDeepeningAStar iterativeDeepeningAStar = new IterativeDeepeningAStar(endNode, mapGrid, dummyUpdater, 1.5, 1.5);
        Long startStamp = System.nanoTime();
        Stack<GridNode> path = iterativeDeepeningAStar.idaStar(startNode);
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
            runOneTest(1.2, 1.2, iterativeDeepeningAStar, diagonalHeuristic, startNode);
            runOneTest(1.5, 1.5, iterativeDeepeningAStar, diagonalHeuristic, startNode);
            runOneTest(2.0, 2.0, iterativeDeepeningAStar, diagonalHeuristic, startNode);
            runOneTest(3.0, 2.0, iterativeDeepeningAStar, diagonalHeuristic, startNode);
        }
    }

    @Test
    void idaStarWithDifferentHeuristicValuesShortPath() {
        DummyUpdater dummyUpdater = new DummyUpdater();
        GridNode startNode = new GridNode(220, 120);
        GridNode endNode = new GridNode(100, 100);
        IterativeDeepeningAStar iterativeDeepeningAStar = new IterativeDeepeningAStar(endNode, mapGrid, dummyUpdater, 1.5, 1.5);
        Long startStamp = System.nanoTime();
        Stack<GridNode> path = iterativeDeepeningAStar.idaStar(startNode);
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
            runOneTest(1.2, 1.2, iterativeDeepeningAStar, diagonalHeuristic, startNode);
            runOneTest(1.5, 1.5, iterativeDeepeningAStar, diagonalHeuristic, startNode);
            runOneTest(2.0, 2.0, iterativeDeepeningAStar, diagonalHeuristic, startNode);
            runOneTest(3.0, 2.0, iterativeDeepeningAStar, diagonalHeuristic, startNode);
        }
    }


    private void runOneTest(Double D, Double D2, IterativeDeepeningAStar iterativeDeepeningAStar, DiagonalHeuristic diagonalHeuristic, GridNode startNode) {
        diagonalHeuristic.setD(D);
        diagonalHeuristic.setD2(D2);
        mapGrid.clearVisited();
        Long startStamp = System.nanoTime();
        Stack<GridNode> path = iterativeDeepeningAStar.idaStar(startNode);
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
    void idaStarWithDifferentHeuristicValuesLongPath() {
        DummyUpdater dummyUpdater = new DummyUpdater();
        GridNode startNode = new GridNode(10, 20);
        GridNode endNode = new GridNode(505, 505);
        IterativeDeepeningAStar iterativeDeepeningAStar = new IterativeDeepeningAStar(endNode, mapGrid, dummyUpdater, 1.5, 1.5);
        Long startStamp = System.nanoTime();
        Stack<GridNode> path = iterativeDeepeningAStar.idaStar(startNode);
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
            runOneTest(1.5, 1.5, iterativeDeepeningAStar, diagonalHeuristic, startNode);
            runOneTest(2.0, 2.0, iterativeDeepeningAStar, diagonalHeuristic, startNode);
            runOneTest(3.0, 2.0, iterativeDeepeningAStar, diagonalHeuristic, startNode);
            runOneTest(3.5, 3.0, iterativeDeepeningAStar, diagonalHeuristic, startNode);
        }
    }


}