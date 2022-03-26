package com.gubertmc.plugin.algorithms.bfs;

import java.util.LinkedList;
import java.util.Queue;

public class Simulation {

    public int SIZE;
    public int[][][] tile_grid;
    public int[] startCoordinate;
    public int[] endCoordinate;

    public Simulation(int[][][] maze, int[] startCoordinate, int[] endCoordinate) {
        SIZE = maze[0].length;
        this.startCoordinate = startCoordinate;
        this.endCoordinate = endCoordinate;

        tile_grid = maze;
    }

    // 0 - open space
    // 1 - wall
    // 2 - algorithm/spread
    // 3 -
    // 4 - start coordinate
    // 5 - end coordinate

    public boolean start() {
        boolean[][][] visited = new boolean[SIZE][SIZE][SIZE];

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (tile_grid[i][j][0] == 1) { // if space is a wall
                    visited[i][j][0] = true;
                }
            }
        }

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
