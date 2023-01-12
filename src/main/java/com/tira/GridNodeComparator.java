package com.tira;

import lombok.extern.log4j.Log4j2;

import java.util.Comparator;
import java.util.Map;

@Log4j2
public class GridNodeComparator implements Comparator<GridNode> {
    private GridNode target;
    private IHeuristic heuristic;

    private Map<GridNode, Double> mapG;

    public GridNodeComparator(GridNode target, IHeuristic heuristic) {
        this.target = target;
        this.heuristic = heuristic;
        this.mapG = null;
    }

    public GridNodeComparator(GridNode target, IHeuristic heuristic, Map<GridNode, Double> mapG) {
        this.target = target;
        this.heuristic = heuristic;
        this.mapG = mapG;
    }

    @Override
    // Note: compares only the coordinates related to target, not whether the nodes are equal objects
    public int compare(GridNode o1, GridNode o2) {
        Double h1 = heuristic.heuristic(o1, target);
        Double h2 = heuristic.heuristic(o2, target);
        if (mapG != null) {
            if (mapG.containsKey(o1) && mapG.containsKey(o2)) {
                Double g1 = mapG.get(o1);
                Double g2 = mapG.get(o2);
                Double f1 = g1+h1;
                Double f2 = g2+h2;
                return f1.compareTo(f2);
            }
        }
        return h1.compareTo(h2);
    }
}
