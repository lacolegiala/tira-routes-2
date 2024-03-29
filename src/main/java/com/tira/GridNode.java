package com.tira;

public class GridNode {

    private final int x;
    private final int y;
    private NodeType t;

    private boolean searching = false;
    private boolean checked = false;
    public GridNode(int x, int y, NodeType t) {
        this.x = x;
        this.y = y;
        this.t = t;
        this.searching = false;
        this.checked = false;
    }

    public GridNode(int x, int y) {
        this.x = x;
        this.y = y;
        this.t = NodeType.FREE;
        this.searching = false;
        this.checked = false;
    }

    public void setNodeType(NodeType t) {
        this.t = t;
    }
    public  NodeType getNodeType() {
        return this.t;
    }

    public int getX() {
        return x ;
    }
    public int getY() {
        return y ;
    }

    public String toString() {
        return "("+x+","+y+")";
    }
    public boolean isSearching() {
        return searching;
    }

    public void setSearching(boolean searching) {
        this.searching = searching;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isTraversable() {
        return t == NodeType.FREE;
    }

    // Return true in comparison if another GridNode has same coordinates
    public boolean equals(Object n) {
        if (n instanceof GridNode) {
            if (x==((GridNode)n).getX() && y == ((GridNode)n).getY()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        // Note that even if x+y can be the same for different objects, it is ok to return x+y as hash code
        // matching how equals is handled.
        return x+y;
    }
}

