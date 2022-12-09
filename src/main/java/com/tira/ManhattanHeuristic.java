package com.tira;

// Used http://theory.stanford.edu/~amitp/GameProgramming/Heuristics.html#manhattan-distance
// for inspiration
public class ManhattanHeuristic implements IHeuristic{
    private Double D = 0.90;
    @Override
    public Double heuristic(GridNode n1, GridNode n2) {
        /*
        function heuristic(node) =
            dx = abs(node.x - goal.x)
            dy = abs(node.y - goal.y)
            return D * (dx + dy)
         */
        Double dx = Double.valueOf(Math.abs(n1.getX() - n2.getX()));
        Double dy = Double.valueOf(Math.abs(n1.getY() - n2.getY()));
        return D*(dx+dy);
    }

    @Override
    public Double cost(GridNode n1, GridNode n2) {
        Double dx = Double.valueOf(Math.abs(n1.getX() - n2.getX()));
        Double dy = Double.valueOf(Math.abs(n1.getY() - n2.getY()));
        return (dx+dy);
    }

    public void setD(Double d) {
        D = d;
    }
}
