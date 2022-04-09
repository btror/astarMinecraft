package com.gubertmc.plugin.main.algorithms.astar;

public class Node {
    private final int row;
    private final int col;
    private final int z;
    private int f;
    private int g;
    private int h;
    private final int type;
    private Node parent;

    public Node(int r, int c, int z, int t) {
        this.row = r;
        this.col = c;
        this.z = z;
        this.type = t;
        this.parent = null;
    }

    public void setF() {
        f = g + h;
    }

    public void setG(int value) {
        g = value;
    }

    public void setH(int value) {
        h = value;
    }

    public void setParent(Node n) {
        parent = n;
    }

    public int getF() {
        return f;
    }

    public int getG() {
        return g;
    }

    public int getType() {
        return type;
    }

    public Node getParent() {
        return parent;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public int getZ() {
        return z;
    }

    public boolean equals(Object in) {
        Node n = (Node) in;
        return row == n.getRow() && col == n.getCol() && z == n.getZ();
    }

    public String toString() {
        return "Node: " + row + "_" + col + "_" + z;
    }
}
