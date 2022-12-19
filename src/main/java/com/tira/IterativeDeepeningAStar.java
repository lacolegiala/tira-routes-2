package com.tira;

import lombok.extern.log4j.Log4j2;

import java.util.*;

/* specs
From wikipedia:
path              current search path (acts like a stack)
node              current node (last node in current path)
g                 the cost to reach current node
f                 estimated cost of the cheapest path (root..node..goal)
h(node)           estimated cost of the cheapest path (node..goal)
cost(node, succ)  step cost function
is_goal(node)     goal test
successors(node)  node expanding function, expand nodes ordered by g + h(node)
ida_star(root)    return either NOT_FOUND or a pair with the best path and its cost

procedure ida_star(root)
    bound := h(root)
    path := [root]
    loop
        t := search(path, 0, bound)
        if t = FOUND then return (path, bound)
        if t = ∞ then return NOT_FOUND
        bound := t
    end loop
end procedure

function search(path, g, bound)
    node := path.last
    f := g + h(node)
    if f > bound then return f
    if is_goal(node) then return FOUND
    min := ∞
    for succ in successors(node) do
        if succ not in path then
            path.push(succ)
            t := search(path, g + cost(node, succ), bound)
            if t = FOUND then return FOUND
            if t < min then min := t
            path.pop()
        end if
    end for
    return min
end function
 */
@Log4j2
public class IterativeDeepeningAStar {
    private GridNode root;
    private GridNode target;

    private MapGrid mapGrid;

    IUpdateView viewUpdater;

    IHeuristic heuristic;

    // Need also a map to collect the real distances from root calculated during recursion
    Map<GridNode, Double> costMap;

    public IterativeDeepeningAStar(GridNode endNode, MapGrid mapGrid, IUpdateView viewUpdater, Double D, Double D2) {
        this.target = endNode;
        this.mapGrid = mapGrid;
        this.viewUpdater = viewUpdater;
        this.heuristic = mapGrid.getHeuristic();
        this.costMap = new HashMap<>();
        // Adjust heuristic : based on debugging IDA* works with larger values than JPS?
        IHeuristic iHeuristic = mapGrid.getHeuristic();
        if (iHeuristic instanceof DiagonalHeuristic) {
            DiagonalHeuristic diagonalHeuristic = (DiagonalHeuristic)iHeuristic;
            diagonalHeuristic.setD(D);
            diagonalHeuristic.setD2(D2);
        }
        if (iHeuristic instanceof ManhattanHeuristic) {
            ManhattanHeuristic manhattanHeuristic = (ManhattanHeuristic)iHeuristic;
            manhattanHeuristic.setD(D);
        }

    }

    public Stack<GridNode> idaStar(GridNode root) {
        Stack<GridNode> path = new Stack<>();

        // Double bound = cost(root, target);
        // This seems to be critical?  something wrong with the heuristic?
        // Double bound = heuristicDouble(root);
        Double bound = heuristic.heuristic(root, target);

        log.debug("h from root = {}", bound);
        path.add(root);
        int round = 1;
        while (true) {
            Double t = search(path, 0.0, bound);
            if (t == Double.MIN_VALUE) {
                log.debug("Found: path len {}", path.size());
                viewUpdater.updateView();
                return path;
            }
            if (t == Double.MAX_VALUE) {
                log.debug("Not found at all!");
                return null;
            }
            // log.debug("loop {} failed, old bound {}, new bound {} path length now {} ", round, bound, t, path.size());
            bound = t;
            clearVisited();
            ++round;
        }
    }

    private Double search(Stack<GridNode> path, Double g, Double bound) {
        GridNode node = path.peek();
        Double hNode = heuristic.heuristic(node, target);
        Double f = g + hNode;

        if (f > bound) {
            // log.debug("search ({}, {}): f = {}, bound = {} (target ({},{}), path {})", node.getX(), node.getY(), f, bound, target.getX(), target.getY(), path);
            return f;
        }
        if (isGoal(node)) {
            log.debug("Goal reached : {}", node);
            return Double.MIN_VALUE;
        }
        Double min = Double.MAX_VALUE;
        List<GridNode> successors = successors(node);
        for (GridNode succ : successors) {
            if (!path.contains(succ) && !succ.isChecked()) {
                path.push(succ);
                mapGrid.getGrid()[succ.getX()][succ.getY()].setChecked(true);
                // Store the g value to the cost map if it is not there or if this is shorter path
                Double newG = g + heuristic.cost(node, succ);
                if (!this.costMap.containsKey(succ) || this.costMap.get(succ) > newG) {
                    this.costMap.put(succ, newG);
                }
                Double t = search(path, newG, bound);
                if (t == Double.MIN_VALUE) {
                    return Double.MIN_VALUE;    // == FOUND!
                }
                if (t < min) {
                    min = t;
                }
                path.pop();
            } else {
                // log.debug("succ {} already in path, len is {}", succ, path.size());
            }
        }
        return min;
    }

    boolean isGoal(GridNode node) {
        if (node.getX() == target.getX() && node.getY() == target.getY()) {
            return true;
        }
        return false;
    }

    private List<GridNode> successors(GridNode node) {
        // This should be able to calculate
        // successors(node)  node expanding function, expand nodes ordered by g + h(node)
        // List<GridNode> gridNodes = mapGrid.getGridNodesSortedByDistanceToTarget(node, target);
        List<GridNode> neighbours = mapGrid.getGridNodesNonBlockedNeighbours(node);
        GridNodeComparator gridNodeComparator = new GridNodeComparator(target, heuristic, costMap);
        neighbours.sort(gridNodeComparator);
        return neighbours;
    }

    private void clearVisited() {
        mapGrid.clearVisited();
    }

}
