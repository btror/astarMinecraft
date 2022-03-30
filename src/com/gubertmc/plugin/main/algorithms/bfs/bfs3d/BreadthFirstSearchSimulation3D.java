package com.gubertmc.plugin.main.algorithms.bfs.bfs3d;

import com.gubertmc.plugin.main.algorithms.Simulation;

import java.util.LinkedList;
import java.util.Queue;

public class BreadthFirstSearchSimulation3D extends Simulation {

    public boolean[][][] visited;

    public BreadthFirstSearchSimulation3D(int[][][] maze, int[] startCoordinate, int[] endCoordinate) {
        super(maze, startCoordinate, endCoordinate, false);
        setup();
    }

    public void setup() {
        visited = new boolean[SIZE][SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                for (int k = 0; k < SIZE; k++) {
                    if (tile_grid[i][j][k] == 1) { // if space is a wall
                        visited[i][j][k] = true;
                    }
                }
            }
        }
    }

    public boolean start() {
        Queue<String> queue = new LinkedList<>();
        queue.add(startCoordinate[1] + "," + startCoordinate[0] + "," + startCoordinate[2]);

        boolean pathFound = false;
        while (!queue.isEmpty()) {
            if (tile_grid[endCoordinate[1]][endCoordinate[0]][2] == 2) {
                pathFound = true;
                break;
            }
            String x = queue.remove();
            int row = Integer.parseInt(x.split(",")[0]);
            int col = Integer.parseInt(x.split(",")[1]);
            int z = Integer.parseInt(x.split(",")[2]);

            if (row < 0 || col < 0 || row >= SIZE || col >= SIZE || z < 0 || z >= SIZE || visited[row][col][z]) {
                continue;
            }
            visited[row][col][z] = true;
            tile_grid[row][col][z] = 2;

            queue.add(row + "," + (col - 1) + "," + z);
            queue.add(row + "," + (col + 1) + "," + z);
            queue.add((row - 1) + "," + col + "," + z);
            queue.add((row + 1) + "," + col + "," + z);
            queue.add(row + "," + col + "," + (z - 1));
            queue.add(row + "," + col + "," + (z + 1));
        }
        return pathFound;
    }
}
