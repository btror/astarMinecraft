package com.gubertmc.plugin.algorithms.astar.astar3d;

import com.gubertmc.plugin.algorithms.astar.Simulation;

public class Simulation3D extends Simulation {

    public Simulation3D(int[][][] maze, int[] startCoordinate, int[] endCoordinate) {
        super(maze, startCoordinate, endCoordinate, true);
    }
}