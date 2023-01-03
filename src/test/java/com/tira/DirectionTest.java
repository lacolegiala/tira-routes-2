package com.tira;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DirectionTest {

    @Test
    void direction() {
        GridNode c = new GridNode(100, 100);
        GridNode n = new GridNode(100, 99);
        GridNode s = new GridNode(100, 101);
        GridNode e = new GridNode(101, 100);
        GridNode w = new GridNode(99, 100);
        Assertions.assertEquals(Direction.direction(c, n), Direction.DIRECTION.N);
        Assertions.assertEquals(Direction.direction(c, s), Direction.DIRECTION.S);
        Assertions.assertEquals(Direction.direction(c, e), Direction.DIRECTION.E);
        Assertions.assertEquals(Direction.direction(c, w), Direction.DIRECTION.W);
    }

    @Test
    void directionH() {
        GridNode c = new GridNode(100, 100);
        GridNode ne = new GridNode(101, 99);
        GridNode se = new GridNode(101, 101);
        GridNode nw = new GridNode(99, 99);
        GridNode sw = new GridNode(99, 101);
        Assertions.assertEquals(Direction.directionH(c, ne), Direction.DIRECTION.E);
        Assertions.assertEquals(Direction.directionH(c, se), Direction.DIRECTION.E);
        Assertions.assertEquals(Direction.directionH(c, nw), Direction.DIRECTION.W);
        Assertions.assertEquals(Direction.directionH(c, sw), Direction.DIRECTION.W);
    }

    @Test
    void directionV() {
        GridNode c = new GridNode(100, 100);
        GridNode ne = new GridNode(101, 99);
        GridNode se = new GridNode(101, 101);
        GridNode nw = new GridNode(99, 99);
        GridNode sw = new GridNode(99, 101);
        Assertions.assertEquals(Direction.directionV(c, ne), Direction.DIRECTION.N);
        Assertions.assertEquals(Direction.directionV(c, se), Direction.DIRECTION.S);
        Assertions.assertEquals(Direction.directionV(c, nw), Direction.DIRECTION.N);
        Assertions.assertEquals(Direction.directionV(c, sw), Direction.DIRECTION.S);

    }

    @Test
    void isDiagonal() {
    }
}