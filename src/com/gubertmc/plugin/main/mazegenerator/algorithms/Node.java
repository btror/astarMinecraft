package com.gubertmc.plugin.main.mazegenerator.algorithms;

public class Node {
    private final int row;
    private final int col;
    private final int z;
    private int f;
    private int g = 0;
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

    /**
     * Set the f-score.
     */
    public void setF() {
        f = g + h;
    }

    public void setBfsF() {
        f = h;
    }

    /**
     * Set distance from end node.
     *
     * @param value distance from end.
     */
    public void setG(int value) {
        g = value;
    }

    /**
     * Set distance from start node.
     *
     * @param value distance from start.
     */
    public void setH(int value) {
        h = value;
    }

    /**
     * Get distance from end node.
     *
     * @return distance from end.
     */
    public int getF() {
        return f;
    }

    /**
     * Get distance from start node.
     *
     * @return distance from start.
     */
    public int getG() {
        return g;
    }

    /**
     * Get type of node (blocker, open space, start, end)
     *
     * @return node type.
     */
    public int getType() {
        return type;
    }

    /**
     * Get parent node.
     *
     * @return parent node.
     */
    public Node getParent() {
        return parent;
    }

    /**
     * Set parent node.
     *
     * @param n parent node.
     */
    public void setParent(Node n) {
        parent = n;
    }

    /**
     * Get node row.
     *
     * @return node row.
     */
    public int getRow() {
        return row;
    }

    /**
     * Get node column.
     *
     * @return node column.
     */
    public int getCol() {
        return col;
    }

    /**
     * Get node z.
     *
     * @return node z.
     */
    public int getZ() {
        return z;
    }

    /**
     * Compare nodes.
     *
     * @param in node to compare.
     * @return if node is same.
     */
    public boolean equals(Object in) {
        Node n = (Node) in;
        return row == n.getRow() && col == n.getCol() && z == n.getZ();
    }
}
