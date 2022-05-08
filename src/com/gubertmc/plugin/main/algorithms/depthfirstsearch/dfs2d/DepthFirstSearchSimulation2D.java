package com.gubertmc.plugin.main.algorithms.depthfirstsearch.dfs2d;

import com.gubertmc.plugin.main.algorithms.Simulation;

import java.util.Stack;

public class DepthFirstSearchSimulation2D extends Simulation {

    private boolean[][][] visited;

    public DepthFirstSearchSimulation2D(int[][][] maze, int[] startCoordinate, int[] endCoordinate) {
        super(maze, startCoordinate, endCoordinate, false);
    }

    @Override
    public void setup() {
        visited = new boolean[getSize()][getSize()][getSize()];
        for (int i = 0; i < getSize(); i++) {
            for (int j = 0; j < getSize(); j++) {
                if (getTileGrid()[i][j][0] == 1) {
                    visited[i][j][0] = true;
                }
            }
        }
    }

    @Override
    public boolean start() {
        Stack<String> stack = new Stack<>();
        stack.push(getStartCoordinate()[1] + "," + getStartCoordinate()[0]);

        boolean pathFound = false;
        while (!stack.empty()) {
            if (getTileGrid()[getEndCoordinate()[1]][getEndCoordinate()[0]][0] == 2) {
                pathFound = true;
                break;
            }
            String x = stack.pop();
            int row = Integer.parseInt(x.split(",")[0]);
            int col = Integer.parseInt(x.split(",")[1]);

            if (row < 0 || col < 0 || row >= getSize() || col >= getSize() || visited[row][col][0]) {
                continue;
            }
            visited[row][col][0] = true;

            int[][][] tileGrid = getTileGrid();
            tileGrid[row][col][0] = 2;
            setTileGrid(tileGrid);

            stack.push(row + "," + (col - 1)); //go left
            stack.push(row + "," + (col + 1)); //go right
            stack.push((row - 1) + "," + col); //go up
            stack.push((row + 1) + "," + col); //go down
        }
        return pathFound;
    }
}
