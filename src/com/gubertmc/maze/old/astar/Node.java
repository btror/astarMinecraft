package com.gubertmc.maze.old.astar;

public class Node {
    private int row, col, z, f, g, h, type;
    private Node parent;

    public Node(int r, int c, int z, int t) {
        row = r;
        col = c;
        this.z = z;
        type = t;
        parent = null;
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