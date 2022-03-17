package com.minecraftmod.maze.astar3d;

public class Node3D {
    private int row, col, z, f, g, h, type;
    private Node3D parent;

    public Node3D(int r, int c, int z, int t){
        row = r;
        col = c;
        this.z = z;
        type = t;
        parent = null;
        //type 0 is traversable, 1 is not
    }
    //mutator methods to set values
    public void setF(){
        f = g + h;
    }

    public void setG(int value){
        g = value;
    }

    public void setH(int value){
        h = value;
    }

    public void setParent(Node3D n){
        parent = n;
    }

    //accessor methods to get values
    public int getF(){
        return f;
    }

    public int getG(){
        return g;
    }

    public int getH(){
        return h;
    }

    public int getType() {
        return type;
    }

    public Node3D getParent(){
        return parent;
    }

    public int getRow(){
        return row;
    }

    public int getCol(){
        return col;
    }

    public int getZ() {
        return z;
    }

    public boolean equals(Object in) {
        // typecast to Node
        Node3D n = (Node3D) in;
        return row == n.getRow() && col == n.getCol() && z == n.getZ();
    }

    public String toString(){
        return "Node: " + row + "_" + col + "_" + z;
    }
}