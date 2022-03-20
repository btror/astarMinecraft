package com.gubertmc.maze.astar.algorithm3d;

import com.gubertmc.maze.astar.Simulation;

public class Simulation3D extends Simulation {

    public Simulation3D(int[][][] maze, int[] startCoordinate, int[] endCoordinate) {
        super(maze, startCoordinate, endCoordinate, true);
    }
}