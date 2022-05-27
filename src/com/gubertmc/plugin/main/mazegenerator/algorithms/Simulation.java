package com.gubertmc.plugin.main.mazegenerator.algorithms;

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

    /**
     * Set up initial logic - start/end nodes, blocker nodes, and current node.
     */
    public abstract void setup();

    /**
     * Start calculations for searching the simulation maze.
     *
     * @return if a solution was found.
     */
    public abstract boolean start();

    /**
     * Get simulation maze size.
     *
     * @return simulation maze size.
     */
    public int getSize() {
        return size;
    }

    /**
     * Set simulation maze size.
     *
     * @param size simulation maze size.
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Get the simulation maze.
     *
     * @return simulation maze.
     */
    public int[][][] getTileGrid() {
        return tileGrid;
    }

    /**
     * Set the simulation maze.
     *
     * @param tileGrid simulation maze.
     */
    public void setTileGrid(int[][][] tileGrid) {
        this.tileGrid = tileGrid;
    }

    /**
     * Get the simulation maze start coordinate.
     *
     * @return simulation maze start coordinate.
     */
    public int[] getStartCoordinate() {
        return startCoordinate;
    }

    /**
     * Set the simulation maze start coordinate.
     *
     * @param startCoordinate simulation maze start coordinate.
     */
    public void setStartCoordinate(int[] startCoordinate) {
        this.startCoordinate = startCoordinate;
    }

    /**
     * Get the simulation maze end coordinate.
     *
     * @return simulation maze end coordinate.
     */
    public int[] getEndCoordinate() {
        return endCoordinate;
    }

    /**
     * Set the simulation maze end coordinate.
     *
     * @param endCoordinate simulation maze end coordinate.
     */
    public void setEndCoordinate(int[] endCoordinate) {
        this.endCoordinate = endCoordinate;
    }

    /**
     * Get node maze.
     *
     * @return node maze.
     */
    public Node[][][] getGrid() {
        return grid;
    }

    /**
     * Set node maze.
     *
     * @param grid node maze.
     */
    public void setGrid(Node[][][] grid) {
        this.grid = grid;
    }

    /**
     * Get list of nodes that need to be examined.
     *
     * @return open list.
     */
    public PriorityQueue<Node> getOpenList() {
        return openList;
    }

    /**
     * Set list of nodes that need to be examined.
     *
     * @param openList open list.
     */
    public void setOpenList(PriorityQueue<Node> openList) {
        this.openList = openList;
    }

    /**
     * Get list of nodes that have been explored.
     *
     * @return closed list.
     */
    public ArrayList<Node> getClosedList() {
        return closedList;
    }

    /**
     * Set list of nodes that have been explored.
     *
     * @param closedList closed list.
     */
    public void setClosedList(ArrayList<Node> closedList) {
        this.closedList = closedList;
    }

    /**
     * Get the simulation maze start node.
     *
     * @return simulation maze start node.
     */
    public Node getStartNode() {
        return startNode;
    }

    /**
     * Set the simulation maze start node.
     *
     * @param startNode simulation maze start node.
     */
    public void setStartNode(Node startNode) {
        this.startNode = startNode;
    }

    /**
     * Get the simulation maze current node.
     *
     * @return simulation maze current node.
     */
    public Node getCurrentNode() {
        return currentNode;
    }

    /**
     * Set the simulation maze current node.
     *
     * @param currentNode simulation maze current node.
     */
    public void setCurrentNode(Node currentNode) {
        this.currentNode = currentNode;
    }

    /**
     * Get the simulation maze end node.
     *
     * @return simulation end node.
     */
    public Node getEndNode() {
        return endNode;
    }

    /**
     * Set the simulation maze end node.
     *
     * @param endNode simulation end node.
     */
    public void setEndNode(Node endNode) {
        this.endNode = endNode;
    }
}
