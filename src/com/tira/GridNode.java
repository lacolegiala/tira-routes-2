package com.tira;

public class GridNode {
    private final int x;
    private final int y;
    private NodeType t;
    public GridNode(int x, int y, NodeType t) {
        this.x = x;
        this.y = y;
        this.t = t;
    }

    public void setNodeType(NodeType t) {
        this.t = t;
    }
    public  NodeType getNodeType() {
        return this.t;
    }
}
