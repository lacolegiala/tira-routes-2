package com.tira;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MapGridTest {

    private static final org.apache.logging.log4j.Logger log
            = org.apache.logging.log4j.LogManager.getLogger(MapGridTest.class);

    private MapGrid mapGrid;
    private static String fileName ="London_2_512.map";

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
    void getGrid() {
        Assertions.assertNotNull(mapGrid.getGrid());
    }

    @Test
    void getNode() {
        // 100, 100 should be traversable (=free node) in London_2_512.map
        Assertions.assertNotNull(mapGrid.getNode(100, 100));
    }

    @Test
    void getTraversableNode() {
        // 100, 100 should be traversable (=free node) in London_2_512.map
        Assertions.assertNotNull(mapGrid.getNode(100, 100));
    }

    @Test
    void isTraversableNode() {
        // 100, 100 should be traversable (=free node) in London_2_512.map
        Assertions.assertEquals(mapGrid.isTraversableNode(100, 100), true);
    }

    @Test
    void setNodeType() {
        mapGrid.setNodeType(100, 101, NodeType.BLOCKED);
        Assertions.assertEquals(mapGrid.isTraversableNode(100, 101), false);
    }

    @Test
    void getGridNodes() {
        Assertions.assertEquals(mapGrid.getGridNodes().size(), 512*512);
    }

    @Test
    void getGridNodesNonBlockedNeighbours() {
    }

    @Test
    void getNeighbours() {
    }

    @Test
    void getGridNodesSortedByDistance() {
    }

    @Test
    void getSizeX() {
        Assertions.assertEquals(mapGrid.getSizeX(), 512);
    }

    @Test
    void getSizeY() {
        Assertions.assertEquals(mapGrid.getSizeY(), 512);
    }

    @Test
    void step() {
        // 100, 100 should have traversable nodes all around
        GridNode node = new GridNode(100, 100);
        // Vertical and horizontal directions
        GridNode e = mapGrid.step(node, Direction.DIRECTION.E);
        Assertions.assertEquals(e.getX(), 101);
        GridNode w = mapGrid.step(node, Direction.DIRECTION.W);
        Assertions.assertEquals(w.getX(), 99);
        GridNode n = mapGrid.step(node, Direction.DIRECTION.N);
        Assertions.assertEquals(n.getY(), 99);
        GridNode s = mapGrid.step(node, Direction.DIRECTION.S);
        Assertions.assertEquals(s.getY(), 101);
        // Diagonal directions
        GridNode ne = mapGrid.step(node, Direction.DIRECTION.NE);
        Assertions.assertEquals(ne.getX(), 101);
        Assertions.assertEquals(ne.getY(), 99);
        GridNode se = mapGrid.step(node, Direction.DIRECTION.SE);
        Assertions.assertEquals(se.getX(), 101);
        Assertions.assertEquals(se.getY(), 101);
        GridNode sw = mapGrid.step(node, Direction.DIRECTION.SW);
        Assertions.assertEquals(sw.getX(), 99);
        Assertions.assertEquals(sw.getY(), 101);
        GridNode nw = mapGrid.step(node, Direction.DIRECTION.NW);
        Assertions.assertEquals(nw.getX(), 99);
        Assertions.assertEquals(nw.getY(), 99);
    }
}