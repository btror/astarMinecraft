package com.gubertmc.plugin.algorithms.astar2d;

import com.gubertmc.plugin.algorithms.Simulation;

public class Simulation2D extends Simulation {

    public Simulation2D(int[][][] maze, int[] startCoordinate, int[] endCoordinate) {
        super(maze, startCoordinate, endCoordinate, false);
    }
}