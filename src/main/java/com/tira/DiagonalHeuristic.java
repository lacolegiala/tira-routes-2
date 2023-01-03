package com.tira;

// Used http://theory.stanford.edu/~amitp/GameProgramming/Heuristics.html#diagonal-distance
// for inspiration
public class DiagonalHeuristic implements IHeuristic {
    Double D = 1.5;
    Double D2 = Math.sqrt(2);

    @Override
    public Double heuristic(GridNode n1, GridNode n2) {
        int dx = Math.abs(n1.getX() - n2.getX());
        int dy = Math.abs(n1.getY() - n2.getY());
        if (dx > dy) {
            return ((dx-dy) + D2 * dy);
        } else {
            return ((dy-dx) + D2 * dx);
        }
    }

    @Override
    public Double cost(GridNode n1, GridNode n2) {
        int dx = Math.abs(n1.getX() - n2.getX());
        int dy = Math.abs(n1.getY() - n2.getY());
        return Double.valueOf(dx+dy);
    }

    public void setD(Double d) {
        D = d;
    }

    public void setD2(Double d) {
        D2 = d;
    }
}
