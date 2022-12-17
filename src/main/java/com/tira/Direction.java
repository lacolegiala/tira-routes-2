package com.tira;

public class Direction {
    public enum DIRECTION {
        N,
        NE,
        E,
        SE,
        S,
        SW,
        W,
        NW,
        C
    }
    public static DIRECTION direction(GridNode source, GridNode goal) {
        final int sx = source.getX();
        final int sy = source.getY();
        final int tx = goal.getX();
        final int ty = goal.getY();

        if (sx < tx && sy < ty) {
            return DIRECTION.SE;
        }
        if (sx == tx && sy < ty) {
            return DIRECTION.S;
        }
        if (sx > tx && sy < ty) {
            return DIRECTION.SW;
        }
        if (sx > tx && sy == ty) {
            return DIRECTION.W;
        }
        if (sx > tx && sy > ty) {
            return DIRECTION.NW;
        }
        if (sx == tx && sy > ty) {
            return DIRECTION.N;
        }
        if (sx < tx && sy > ty) {
            return DIRECTION.NE;
        }
        if (sx < tx && sy == ty) {
            return DIRECTION.E;
        }

        return DIRECTION.C; //direction C = central?  when there is no direction
    }

    public static DIRECTION directionH(GridNode source, GridNode goal) {
        final int sx = source.getX();
        final int tx = goal.getX();

        if (sx < tx) {
            return DIRECTION.E;
        }
        if (sx > tx) {
            return DIRECTION.W;
        }
        return DIRECTION.E;
    }

    public static DIRECTION directionV(GridNode source, GridNode goal) {
        final int sy = source.getY();
        final int ty = goal.getY();

        if (sy < ty) {
            return DIRECTION.S;
        }
        if (sy > ty) {
            return DIRECTION.N;
        }
        return DIRECTION.N;
    }
    
}
