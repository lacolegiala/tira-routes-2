package com.tira;

import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapGrid {

    private static final org.apache.logging.log4j.Logger log
            = org.apache.logging.log4j.LogManager.getLogger(MapGrid.class);
    private final GridNode[][] grid;
    private final int sizeX;
    private final int sizeY;

    private IHeuristic heuristic;

    public MapGrid(int cx, int cy, IHeuristic iHeuristic) {
        this.sizeX = cx;
        this.sizeY = cy;
        this.heuristic = iHeuristic;
        this.grid = new GridNode[cx][cy];

        // Initialize all grid nodes
        for (int y = 0; y < cy; y++) {
            for (int x = 0; x < cx; x++) {
                this.grid[x][y] = new GridNode(x, y, NodeType.FREE);
            }
        }
    }

    public GridNode[][] getGrid() {
        return this.grid;
    }

    public GridNode getNode(int x, int y) {
        if (x >= 0 && x < sizeX &&
                y >= 0 && y < sizeY) {
            return this.grid[x][y];
        }
        return null;
    }

    public GridNode getTraversableNode(int x, int y) {
        if (x >= 0 && x < sizeX &&
                y >= 0 && y < sizeY) {
            if (this.grid[x][y].getNodeType() != NodeType.BLOCKED) {
                return this.grid[x][y];
            }
            if (this.grid[x][y].getNodeType() == NodeType.BLOCKED) {
                log.debug("Node x:{} y:{} blocked", x, y);
            }

        }
        return null;
    }

    public Boolean isTraversableNode(int x, int y) {
        if (x >= 0 && x < sizeX &&
                y >= 0 && y < sizeY) {
            if (this.grid[x][y].getNodeType() != NodeType.BLOCKED) {
                return true;
            }
        }
        return false;
    }

    public void setNodeType(int x, int y, NodeType t) {
        this.grid[x][y].setNodeType(t);
    }

    public List<GridNode> getGridNodes() {
        List<GridNode> gridNodeList = new ArrayList<>();

        for (int y = 0; y < this.sizeY; y++) {
            for (int x = 0; x < this.sizeX; x++) {
                gridNodeList.add(grid[x][y]);
            }
        }
        return gridNodeList;
    }

    List<GridNode> addSearchableNonBlockedNode(int x, int y, List<GridNode> neighbours) {
        if (this.grid[x][y].getNodeType().isFree() &&
                !this.grid[x][y].isChecked()) {
            neighbours.add(this.grid[x][y]);
        }
        return neighbours;
    }

    public List<GridNode> getGridNodesNonBlockedNeighbours(GridNode node) {
        // Get all nodes that are near specific node: at most 8 nodes surrounding the
        // node given
        List<GridNode> neighbours = new ArrayList<>();
        try {
            int x = node.getX();
            int y = node.getY();
            if (x > 0) {    // neighbours left
                if (y > 0) {
                    neighbours = addSearchableNonBlockedNode(x - 1, y - 1, neighbours);
                }
                neighbours = addSearchableNonBlockedNode(x - 1, y, neighbours);
                if (y < this.sizeY - 1) {
                    neighbours = addSearchableNonBlockedNode(x - 1, y + 1, neighbours);
                }
            }
            // above and below
            if (y > 0) {
                neighbours = addSearchableNonBlockedNode(x, y - 1, neighbours);
            }
            if (y < this.sizeY - 1) {
                neighbours = addSearchableNonBlockedNode(x, y + 1, neighbours);
            }
            if (x < this.sizeX - 1) {    // neighbours right
                if (y > 0) {
                    neighbours = addSearchableNonBlockedNode(x + 1, y - 1, neighbours);
                }
                neighbours = addSearchableNonBlockedNode(x + 1, y, neighbours);
                if (y < this.sizeY - 1) {
                    neighbours = addSearchableNonBlockedNode(x + 1, y + 1, neighbours);
                }
            }
        } catch (ArrayIndexOutOfBoundsException ex) {
            log.debug("array ex for {}", node, ex);
        }

        return neighbours;
    }

    private List<GridNode> addNeighbourIfExists(List<GridNode> neighbours, GridNode node, Direction.DIRECTION d) {
        GridNode n = step(node, d);
        if (n != null) {
            neighbours.add(n);
        }
        return neighbours;
    }

    List<GridNode> addNode(int x, int y, List<GridNode> neighbours) {
        if (x >= 0 && x < sizeX && y >= 0 && y < sizeY) {
            neighbours.add(this.grid[x][y]);
        }
        return neighbours;
    }

    public List<GridNode> getGridNodesSortedByDistanceToTarget(GridNode node, GridNode target) {
        List<GridNode> neighbours = getGridNodesNonBlockedNeighbours(node);
        GridNodeComparator gridNodeComparator = new GridNodeComparator(target, getHeuristic());
        neighbours.sort(gridNodeComparator);
        return neighbours;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    public void setSearching(int x, int y, boolean working) {
        grid[x][y].setSearching(working);
    }

    public IHeuristic getHeuristic() {
        // return iHeuristicManhattan;
        return heuristic;
    }

    public void clearVisited() {
        for (int y = 0; y < this.sizeY; y++) {
            for (int x = 0; x < this.sizeX; x++) {
                grid[x][y].setChecked(false);
            }
        }
    }

    public GridNode step(GridNode n, Direction.DIRECTION d) {
        switch (d) {
            case N:
                return getTraversableNode(n.getX(), n.getY() - 1);
            case NE:
                return getTraversableNode(n.getX() + 1, n.getY() - 1);
            case E:
                return getTraversableNode(n.getX() + 1, n.getY());
            case SE:
                return getTraversableNode(n.getX() + 1, n.getY() + 1);
            case S:
                return getTraversableNode(n.getX(), n.getY() + 1);
            case SW:
                return getTraversableNode(n.getX() - 1, n.getY() + 1);
            case W:
                return getTraversableNode(n.getX() - 1, n.getY());
            case NW:
                return getTraversableNode(n.getX() - 1, n.getY() - 1);
        }
        return null;
    }

}
