package com.tira;

import lombok.extern.log4j.Log4j2;

import java.util.Comparator;

@Log4j2
public class GridNodeComparator implements Comparator<GridNode> {
    private GridNode target;
    private IHeuristic heuristic;

    public GridNodeComparator(GridNode target, IHeuristic heuristic) {
        this.target = target;
        this.heuristic = heuristic;
    }


    @Override
    // Note: compares only the coordinates, not whether the nodes are equal objects
    public int compare(GridNode o1, GridNode o2) {
        Double h1 = heuristic.cost(o1, target);
        Double h2 = heuristic.cost(o2, target);
        if (h1 == h2) {
            return 0;
        } else if (h1 < h2) {
            return -1;
        } else {
            return 1;
        }
    }
}
