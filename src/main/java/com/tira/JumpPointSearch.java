package com.tira;

import lombok.extern.log4j.Log4j2;

import java.util.*;

// Used http://users.cecs.anu.edu.au/~dharabor/data/papers/harabor-grastien-aaai11.pdf
// as specification, and used also https://github.com/kevinsheehan/jps for sample code (should the MIT license be copied here?)
@Log4j2
public class JumpPointSearch {

    private final MapGrid mapGrid;

    private final IUpdateView viewUpdater;

    public JumpPointSearch(MapGrid mapGrid, IUpdateView viewUpdater) {
        this.mapGrid = mapGrid;
        this.viewUpdater = viewUpdater;
    }

    public Queue<GridNode> search(GridNode start, GridNode goal) {
        Map<GridNode, Double> fullPathLength = new HashMap<>(); // cost from start + heuristics to goal
        Map<GridNode, Double> costFomStart = new HashMap<>();
        Map<GridNode, Double> heuristicToGoal = new HashMap<>();

        // put the nodes to queue and select the shortest estimated full path from there
        Queue<GridNode> open = new PriorityQueue<>((a, b) -> {
            // returns the minimum full path length first
            return Double.compare(fullPathLength.getOrDefault(a, 0d), fullPathLength.getOrDefault(b, 0d));
        });
        // Nodes handled are added to this set, so that they are not handled again inside the recursion
        Set<GridNode> closed = new HashSet<>();
        // Store the predecessors of the node for building the full path
        Map<GridNode, GridNode> parentMap = new HashMap<>();
        // the goal (and its neighbours) are stored here
        Set<GridNode> goals = new HashSet<>();

        if (goal.getNodeType().isFree()) {
            goals.add(goal);
        }
        // Add goal neighbours to the list of points where jumping stops
        // debugging showed that this helps to find the goal
        goals.addAll(mapGrid.getGridNodesNonBlockedNeighbours(goal));

        if (goals.isEmpty()) {
            return null;
        }

        // add the start node into the list to be checked
        open.add(start);

        // while the open list is not empty
        while (!open.isEmpty()) {
            // pop the position of node which has the shortest estimated total length
            GridNode node = open.poll();
            // log.debug("Adding {} to closed list , open still has {}", node, open.size());
            closed.add(node);

            if (goals.contains(node)) {
                // We already found the target
                return backtrace(node, parentMap);
            } else {
                // log.debug("Goals did not contain {}: {}", node, goals);
            }
            // add all possible next steps from the current node
            getSuccessors(parentMap, node, start, goal, goals, open, closed, fullPathLength, costFomStart, heuristicToGoal);
        }
        log.debug("No more entries, failed to find");
        // failed to find a path
        return null;
    }

    // Algorithm 1: identify successors
    private void getSuccessors(Map<GridNode, GridNode> parentMap,
                               GridNode node,
                               GridNode s,
                               GridNode g,
                               Set<GridNode> goals,
                               Queue<GridNode> open,
                               Set<GridNode> closed,
                               Map<GridNode, Double> fMap,
                               Map<GridNode, Double> cMap,
                               Map<GridNode, Double> hMap) {

        List<GridNode> neighbours = prune(parentMap, node, g);

        // log.debug("Found {} neighbours for {}: {}", neighbours.size(), node, neighbours);
        for (GridNode neighbour : neighbours) {
            GridNode jumpNode = jump(neighbour, node, g, goals);

            // don't add a node we have already handled and is shorter path
            // or if there was no path found at all
            if (jumpNode == null || closed.contains(jumpNode)) continue;

            // determine the jumpNode's distance from the start along the current path
            Double distanceToJump = mapGrid.getHeuristic().cost(jumpNode, node);    // cost between these
            Double newLength = cMap.getOrDefault(node, 0d) + distanceToJump;   // added to the stored length

            // check if this is a shorter path for a node already in the open queue
            // or if it hasn't been opened, add to open queue open and update it
            Double oldLength = cMap.getOrDefault(jumpNode, 0d);
            if (!open.contains(jumpNode) || newLength < oldLength) {
                // log.debug("Length to {} is {} ({} from {}, start {}, og {}", jumpNode, ng, d, node, s, og);
                cMap.put(jumpNode, newLength);
                hMap.put(jumpNode, mapGrid.getHeuristic().heuristic(jumpNode, g));
                fMap.put(jumpNode, cMap.getOrDefault(jumpNode, 0d) + hMap.getOrDefault(jumpNode, 0d));
                // adding node to parent map
                parentMap.put(jumpNode, node);
                // if this is new short path, add to open nodes
                if (!open.contains(jumpNode)) {
                    open.offer(jumpNode);
                }
            }
        }
    }

    private List<GridNode> addValidGridNode(List<GridNode> nodes, int x, int y) {
        if (mapGrid.getNode(x, y) != null) {
            nodes.add(mapGrid.getNode(x, y));
        }
        return nodes;
    }

    // pruning, here trying to add the neighbours in the correct direction
    private List<GridNode> prune(Map<GridNode, GridNode> parentMap, GridNode node, GridNode goal) {
        List<GridNode> neighbours = new ArrayList<>();
        GridNode parent = parentMap.get(node);
        if (parent == null) {
            // add all non blocked neighbours
            neighbours.addAll(mapGrid.getGridNodesSortedByDistance(node, goal));
            // log.debug("No parent, adding neighbours of {}: {}", node, neighbours);
        } else {
            // add suitable neighbours based on parent
            final int x = node.getX();
            final int y = node.getY();
            // determine direction from parent
            final int dx = (x - parent.getX()) / Math.max(Math.abs(x - parent.getX()), 1);
            final int dy = (y - parent.getY()) / Math.max(Math.abs(y - parent.getY()), 1);

            // both x and y differ: diagonal search
            if (dx != 0 && dy != 0) {
                // above or below
                if (mapGrid.isTraversableNode(x, y + dy))
                    neighbours = addValidGridNode(neighbours, x, y + dy);
                // left or right
                if (mapGrid.isTraversableNode(x + dx, y))
                    neighbours = addValidGridNode(neighbours, x + dx, y);
                // diagonal in NW,NE,SE,SW
                if (mapGrid.isTraversableNode(x + dx, y + dy))
                    neighbours = addValidGridNode(neighbours, x + dx, y + dy);
                // forced?  block around the corner
                // if node left or right is not traversable, add corner node
                if (!mapGrid.isTraversableNode(x - dx, y))
                    neighbours = addValidGridNode(neighbours, x - dx, y + dy);
                // If node up or down is not traversable, add corner node
                if (!mapGrid.isTraversableNode(x, y - dy))
                    neighbours = addValidGridNode(neighbours, x + dx, y - dy);
            } else { // horizontal or vertical : either x or y is the same
                if (dx == 0) {
                    if (mapGrid.isTraversableNode(x, y + dy))
                        neighbours = addValidGridNode(neighbours, x, y + dy);
                    // if next is blocked add possible node after that
                    if (!mapGrid.isTraversableNode(x + 1, y))
                        neighbours = addValidGridNode(neighbours, x + 1, y + dy);
                    // if previous is blocked add possible node after that
                    if (!mapGrid.isTraversableNode(x - 1, y))
                        neighbours = addValidGridNode(neighbours, x - 1, y + dy);
                } else {
                    if (mapGrid.isTraversableNode(x + dx, y))
                        neighbours = addValidGridNode(neighbours, x + dx, y);
                    // if above is blocked add possible node after that
                    if (!mapGrid.isTraversableNode(x, y + 1))
                        neighbours = addValidGridNode(neighbours, x + dx, y + 1);
                    // if below is blocked add possible node after that
                    if (!mapGrid.isTraversableNode(x, y - 1))
                        neighbours = addValidGridNode(neighbours, x + dx, y - 1);
                }
            }
            // log.debug("Pruning: Node {}, parent {}, dx: {}, dy {}: neighbours {}", node, parent, dx, dy, neighbours);
        }
        return neighbours;
    }

    private GridNode jump(GridNode node, GridNode current, GridNode goal, Set<GridNode> goals) {
        if (node == null) {
            return null;
        }
        if (!node.isTraversable()) {
            // log.debug("Node {} not traversable", node);
            return null;
        }

        if (goals.contains(node)) {
            // found it (or a neighbour)
            return node;
        }

        // Check if ∃ n0 ∈ neighbours(n) s.t. n0  is forced then return n
        // How "forced" is checked; adjacent to a blocked GridNode "round the corner"?
        final int dx = node.getX() - current.getX();
        final int dy = node.getY() - current.getY();

        // check if diagonal movement
        if (dx != 0 && dy != 0) {
            //
            if ((mapGrid.isTraversableNode(node.getX() - dx, node.getY() + dy) && !mapGrid.isTraversableNode(node.getX() - dx, node.getY())) ||
                    (mapGrid.isTraversableNode(node.getX() + dx, node.getY() - dy) && !mapGrid.isTraversableNode(node.getX(), node.getY() - dy))) {
                return node;
            }
            // when moving diagonally, check for vertical or horizontal jump points
            /* This is the part:
                 9: for all i ∈ {1, 2} do
                10:   if jump(n, ~di, s, g) is not null then
                11: return n
             */
            if (jump(mapGrid.getNode(node.getX() + dx, node.getY()), node, goal, goals) != null ||
                    jump(mapGrid.getNode(node.getX(), node.getY() + dy), node, goal, goals) != null) {
                return node;
            }
        } else { // check along horizontal/vertical
            if (dx != 0) {
                if ((mapGrid.isTraversableNode(node.getX() + dx, node.getY() + 1) && !mapGrid.isTraversableNode(node.getX(), node.getY() + 1)) ||
                        (mapGrid.isTraversableNode(node.getX() + dx, node.getY() - 1) && !mapGrid.isTraversableNode(node.getX(), node.getY() - 1))) {
                    return node;
                }
            } else {
                if ((mapGrid.isTraversableNode(node.getX() + 1, node.getY() + dy) && !mapGrid.isTraversableNode(node.getX() + 1, node.getY())) ||
                        (mapGrid.isTraversableNode(node.getX() - 1, node.getY()) && !mapGrid.isTraversableNode(node.getX() - 1, node.getY()))) {
                    return node;
                }
            }
        }

        return jump(mapGrid.getNode(node.getX() + dx, node.getY() + dy), node, goal, goals);
    }


    // Generate the path from node backwards based on the parent nodes stored.
    private Queue<GridNode> backtrace(GridNode node, Map<GridNode, GridNode> parentMap) {
        LinkedList<GridNode> path = new LinkedList<>();
        path.add(node);

        int previousX, previousY, currentX, currentY;
        int dx, dy;
        int steps;
        GridNode temp;
        while (parentMap.containsKey(node)) {
            previousX = parentMap.get(node).getX();
            previousY = parentMap.get(node).getY();
            currentX = node.getX();
            currentY = node.getY();
            steps = Integer.max(Math.abs(previousX - currentX), Math.abs(previousY - currentY));
            dx = Integer.compare(previousX, currentX);
            dy = Integer.compare(previousY, currentY);

            temp = node;
            for (int i = 0; i < steps; i++) {
                temp = mapGrid.getNode(temp.getX() + dx, temp.getY() + dy);
                path.addFirst(temp);
            }
            node = parentMap.get(node);
        }
        return path;
    }
}
