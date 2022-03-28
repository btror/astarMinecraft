package com.gubertmc.plugin.main.algorithms.bfs.bfs2d;

import com.gubertmc.plugin.main.algorithms.Simulation;

import java.util.LinkedList;
import java.util.Queue;

public class BreadthFirstSearchSimulation2D extends Simulation {

    public boolean[][][] visited;

    public BreadthFirstSearchSimulation2D(int[][][] maze, int[] startCoordinate, int[] endCoordinate) {
        super(maze, startCoordinate, endCoordinate, false);
        setup();
    }

    public void setup() {
        visited = new boolean[SIZE][SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (tile_grid[i][j][0] == 1) { // if space is a wall
                    visited[i][j][0] = true;
                }
            }
        }
    }

    public boolean start() {
        Queue<String> queue = new LinkedList<>();
        queue.add(startCoordinate[1] + "," + startCoordinate[0]);

        boolean pathFound = false;
        while (!queue.isEmpty()) {
            if (tile_grid[endCoordinate[1]][endCoordinate[0]][0] == 2) {
                pathFound = true;
                break;
            }
            String x = queue.remove();
            int row = Integer.parseInt(x.split(",")[0]);
            int col = Integer.parseInt(x.split(",")[1]);

            if (row < 0 || col < 0 || row >= SIZE || col >= SIZE || visited[row][col][0]) {
                continue;
            }
            visited[row][col][0] = true;
            tile_grid[row][col][0] = 2;

            queue.add(row + "," + (col - 1)); //go left
            queue.add(row + "," + (col + 1)); //go right
            queue.add((row - 1) + "," + col); //go up
            queue.add((row + 1) + "," + col); //go down
        }
        return pathFound;
    }
}
