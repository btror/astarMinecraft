package com.gubertmc.plugin.main.algorithms;

import com.gubertmc.plugin.main.algorithms.astar.Node;
import com.gubertmc.plugin.main.algorithms.astar.NodeComparator;

import java.util.ArrayList;
import java.util.PriorityQueue;

public abstract class Simulation {

    private int size;
    private int[][][] tileGrid;
    private int[] startCoordinate;
    private int[] endCoordinate;
    private boolean is3d;
    private Node[][][] grid;
    private PriorityQueue<Node> openList = new PriorityQueue<>(10, new NodeComparator());
    private ArrayList<Node> closedList = new ArrayList<>();
    private Node startNode;
    private Node currentNode;
    private Node endNode;

    public Simulation(int[][][] maze, int[] startCoordinate, int[] endCoordinate, boolean is3d) {
        this.tileGrid = maze;
        this.size = maze[0].length;
        this.startCoordinate = startCoordinate;
        this.endCoordinate = endCoordinate;
        this.is3d = is3d;
        this.grid = new Node[this.size][this.size][this.size];
    }

    public abstract void setup();

    public abstract boolean start();

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int[][][] getTileGrid() {
        return tileGrid;
    }

    public void setTileGrid(int[][][] tileGrid) {
        this.tileGrid = tileGrid;
    }

    public int[] getStartCoordinate() {
        return startCoordinate;
    }

    public void setStartCoordinate(int[] startCoordinate) {
        this.startCoordinate = startCoordinate;
    }

    public int[] getEndCoordinate() {
        return endCoordinate;
    }

    public void setEndCoordinate(int[] endCoordinate) {
        this.endCoordinate = endCoordinate;
    }

    public boolean isIs3d() {
        return is3d;
    }

    public void setIs3d(boolean is3d) {
        this.is3d = is3d;
    }

    public Node[][][] getGrid() {
        return grid;
    }

    public void setGrid(Node[][][] grid) {
        this.grid = grid;
    }

    public PriorityQueue<Node> getOpenList() {
        return openList;
    }

    public void setOpenList(PriorityQueue<Node> openList) {
        this.openList = openList;
    }

    public ArrayList<Node> getClosedList() {
        return closedList;
    }

    public void setClosedList(ArrayList<Node> closeList) {
        this.closedList = closeList;
    }

    public Node getStartNode() {
        return startNode;
    }

    public void setStartNode(Node startNode) {
        this.startNode = startNode;
    }

    public Node getCurrentNode() {
        return currentNode;
    }

    public void setCurrentNode(Node currentNode) {
        this.currentNode = currentNode;
    }

    public Node getEndNode() {
        return endNode;
    }

    public void setEndNode(Node endNode) {
        this.endNode = endNode;
    }

}
