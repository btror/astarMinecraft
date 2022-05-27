package com.gubertmc.plugin.main.snakegame.logicalcomponents;

import org.bukkit.Location;
import org.bukkit.Material;

public class Node {
    private int row, col, f, g, h, type;
    private Node parent;
    private Location location;

    public Node(Location location, int r, int c, int t){
        this.location = location;
        row = r;
        col = c;
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

    public void setParent(Node n){
        parent = n;
    }

    public void setType(int type) { this.type = type; }

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

    public Node getParent(){
        return parent;
    }

    public int getRow(){
        return row;
    }

    public int getCol(){
        return col;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public boolean equals(Object in) {
        // typecast to Node
        Node n = (Node) in;
        return row == n.getRow() && col == n.getCol();
    }

    public String toString(){
        return "Node: " + row + "_" + col;
    }
}