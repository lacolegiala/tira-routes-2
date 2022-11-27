package com.tira;

import java.util.ArrayList;
import java.util.List;

public class MapGrid {
    private final GridNode[][] grid;
    private final int sizeX;
    private final int sizeY;

    public MapGrid(int cx, int cy) {
        this.sizeX = cx;
        this.sizeY = cy;
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

    public List<GridNode> getGridNodes()
    {
        List<GridNode> gridNodeList = new ArrayList<>();

        for(int y = 0; y < this.sizeY; y++)
        {
            for(int x = 0; x < this.sizeX; x++)
            {
                gridNodeList.add(grid[x][y]);
            }
        }
        return gridNodeList;
    }

    public int getSizeX() {
        return sizeX;
    }
    public int getSizeY() {
        return sizeY;
    }

}
