package com.gubertmc.plugin.main.algorithms.dfs.dfs3d;

import com.gubertmc.plugin.main.algorithms.Simulation;
import java.util.Stack;

public class DepthFirstSearchSimulation3D extends Simulation {

    public boolean[][][] visited;

    public DepthFirstSearchSimulation3D(int[][][] maze, int[] startCoordinate, int[] endCoordinate) {
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
        Stack<String> stack = new Stack<>();
        stack.push(startCoordinate[1] + "," + startCoordinate[0] + "," + startCoordinate[2]);

        boolean pathFound = false;
        while (!stack.empty()) {
            if (tile_grid[endCoordinate[1]][endCoordinate[0]][2] == 2) {
                pathFound = true;
                break;
            }
            String x = stack.pop();
            int row = Integer.parseInt(x.split(",")[0]);
            int col = Integer.parseInt(x.split(",")[1]);
            int z = Integer.parseInt(x.split(",")[2]);

            if (row < 0 || col < 0 || row >= SIZE || col >= SIZE || z < 0 || z >= SIZE || visited[row][col][z]) {
                continue;
            }
            visited[row][col][z] = true;
            tile_grid[row][col][z] = 2;

            stack.push(row + "," + (col - 1) + "," + z);
            stack.push(row + "," + (col + 1) + "," + z);
            stack.push((row - 1) + "," + col + "," + z);
            stack.push((row + 1) + "," + col + "," + z);
            stack.push(row + "," + col + "," + (z - 1));
            stack.push(row + "," + col + "," + (z + 1));
        }
        return pathFound;
    }

}
