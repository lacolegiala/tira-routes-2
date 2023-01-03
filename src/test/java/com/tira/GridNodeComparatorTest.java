package com.tira;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GridNodeComparatorTest {

    @Test
    void compare() {
        IHeuristic iHeuristic = new ManhattanHeuristic();
        GridNode target = new GridNode(20,20,NodeType.FREE);
        GridNodeComparator gridNodeComparator = new GridNodeComparator(target, iHeuristic);
        GridNode closer = new GridNode(25,25,NodeType.FREE);
        GridNode further = new GridNode(125,125,NodeType.FREE);

        Assertions.assertEquals(gridNodeComparator.compare(closer, further), -1);
        Assertions.assertEquals(gridNodeComparator.compare(closer, closer), 0);
        Assertions.assertEquals(gridNodeComparator.compare(further, closer), 1);

    }
}