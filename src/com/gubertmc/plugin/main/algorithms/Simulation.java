package com.gubertmc.plugin.main.algorithms;

import com.gubertmc.plugin.main.algorithms.astar.Node;
import com.gubertmc.plugin.main.algorithms.astar.NodeComparator;

import java.util.ArrayList;
import java.util.PriorityQueue;

public abstract class Simulation {

    public int SIZE;
    public int[][][] tile_grid;
    public int[] startCoordinate;
    public int[] endCoordinate;
    public boolean is3d;

    public Node[][][] grid;
    public PriorityQueue<Node> open_list = new PriorityQueue<>(10, new NodeComparator());
    public ArrayList<Node> closed_list = new ArrayList<>();
    public Node start_node;
    public Node current_node;
    public Node end_node;

    public Simulation(int[][][] maze, int[] startCoordinate, int[] endCoordinate, boolean is3d) {
        this.tile_grid = maze;
        this.SIZE = maze[0].length;
        this.startCoordinate = startCoordinate;
        this.endCoordinate = endCoordinate;
        this.is3d = is3d;
        this.grid = new Node[this.SIZE][this.SIZE][this.SIZE];
    }

    public abstract boolean start();

}
