package com.tira;

import lombok.extern.log4j.Log4j2;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Log4j2
public class MapGrid {
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
        return this.grid[x][y];
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


    public List<GridNode> getGridNodesSortedByDistance(GridNode node, GridNode target) {
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

    void setSearching(int x, int y, boolean working) {
        grid[x][y].setSearching(working);
    }

    public IHeuristic getHeuristic() {
        return heuristic;
    }

    public void clearVisited() {
        for (int y = 0; y < this.sizeY; y++) {
            for (int x = 0; x < this.sizeX; x++) {
                grid[x][y].setChecked(false);
            }
        }
    }

}
