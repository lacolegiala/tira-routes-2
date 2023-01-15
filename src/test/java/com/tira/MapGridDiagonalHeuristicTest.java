package com.tira;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class MapGridDiagonalHeuristicTest {

    private static final org.apache.logging.log4j.Logger log
            = org.apache.logging.log4j.LogManager.getLogger(MapGridDiagonalHeuristicTest.class);

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
        Assertions.assertNotNull (mapGrid);
    }

    @Test
    void getNode() {
    }

    @Test
    void setNodeType() {
    }

    @Test
    void getGridNodes() {
    }

    @Test
    void getGridNodesAllNeighbours() {
    }

    @Test
    void getGridNodesNonBlockedNeighbours() {
        GridNode node = new GridNode(15, 27, NodeType.FREE);
        List<GridNode> neighbours = mapGrid.getGridNodesNonBlockedNeighbours(node);
        log.debug("non blocked neighbours: {}", neighbours);
        Assertions.assertNotNull(neighbours);
        Assertions.assertEquals(8, neighbours.size(), "Should have 8 neighbours");
    }

    @Test
    void getSizeX() {
    }

    @Test
    void getSizeY() {
    }

    @Test
    void getGridNodesSortedByDistance() {
        GridNode node1 = new GridNode(15, 27, NodeType.FREE);
        GridNode node2 = new GridNode(15, 500, NodeType.FREE);
        List<GridNode> neighbours = mapGrid.getGridNodesSortedByDistanceToTarget(node1, node2);
        log.debug("sorted neighbours: {}", neighbours);
        Assertions.assertNotNull(neighbours);
    }
}