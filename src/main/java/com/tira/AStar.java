package com.tira;

import java.util.*;

// implementation based on pseudocode in https://en.wikipedia.org/wiki/A*_search_algorithm
public class AStar {

    private static final org.apache.logging.log4j.Logger log
            = org.apache.logging.log4j.LogManager.getLogger(AStar.class);
    private MapGrid mapGrid;

    private IUpdateView viewUpdater;

    private IHeuristic heuristic;

    private Map<GridNode, GridNode> cameFrom;
    private Map<GridNode, Double> gScore;
    private Map<GridNode, Double> fScore;

    public AStar(MapGrid mapGrid, IUpdateView viewUpdater, Double D, Double D2) {
        this.mapGrid = mapGrid;
        this.viewUpdater = viewUpdater;
        this.heuristic = mapGrid.getHeuristic();
        // Adjust heuristic : based on debugging IDA* works with larger values than JPS?
        IHeuristic iHeuristic = mapGrid.getHeuristic();
        mapGrid.clearVisited();
        if (iHeuristic instanceof DiagonalHeuristic) {
            DiagonalHeuristic diagonalHeuristic = (DiagonalHeuristic)iHeuristic;
            diagonalHeuristic.setD(D);
            diagonalHeuristic.setD2(D2);
        }
    }

    public Stack<GridNode> reconstructPath(Map<GridNode, GridNode> cameFrom, GridNode current) {
        Stack<GridNode> totalPath = new Stack<>();
        totalPath.add(current);
        while (cameFrom.containsKey(current)) {
            current = cameFrom.get(current);
            totalPath.push(current);
        }
        return totalPath;
    }

    public Stack<GridNode> a_star(GridNode start, GridNode goal, Double h) {
        cameFrom = new HashMap<>();
        gScore = new HashMap<>();
        fScore = new HashMap<>();

        // put the nodes to queue and select the shortest estimated full path from there
        Queue<GridNode> openSet = new PriorityQueue<>((a, b) -> {
            // returns the minimum full path length first
            return Double.compare(fScore.getOrDefault(a, Double.MAX_VALUE), fScore.getOrDefault(b, Double.MAX_VALUE));
        });
        Set<GridNode> closedSet = new HashSet<>();

        openSet.add(start);
        gScore.put(start, 0.0);
        fScore.put(start, heuristic.heuristic(start, goal));
        while(!openSet.isEmpty()) {
            GridNode current = openSet.poll();
            if (current.equals(goal)) {
                return reconstructPath(cameFrom, current);
            }
            openSet.remove(current);
            closedSet.add(current);
            // log.debug("Current is {}, openSet has {}", current, openSet.size());
            List<GridNode> neighbors = neighbors(current, goal, fScore);
            for (GridNode neighbor : neighbors) {
                if (closedSet.contains(neighbor)) {
                    // already handled?
                    continue;
                }
                Double tentativeGScore = gScore.get(current) + heuristic.cost(current, neighbor);
                if (tentativeGScore < gScore.getOrDefault(neighbor, Double.MAX_VALUE)) {
                    cameFrom.put(neighbor, current);
                    gScore.put(neighbor, tentativeGScore);
                    fScore.put(neighbor, tentativeGScore + heuristic.heuristic(neighbor, goal));
                    if (!openSet.contains(neighbor)) {
                        openSet.add(neighbor);
                    }
                }
            }
        }
        log.debug("Did not find anything");
        return null;
    }

    private List<GridNode> neighbors(GridNode node, GridNode goal, Map<GridNode, Double> costMap) {
        // List<GridNode> neighbours = mapGrid.getNeighbours(node);
        List<GridNode> neighbours = mapGrid.getGridNodesNonBlockedNeighbours(node);
        GridNodeComparator gridNodeComparator = new GridNodeComparator(goal, heuristic, costMap);
        neighbours.sort(gridNodeComparator);
        return neighbours;
    }
}

