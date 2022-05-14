package com.gubertmc.plugin.main.algorithms.breadthfirstsearch.bfs2d;

import com.gubertmc.plugin.main.algorithms.Simulation;

import java.util.LinkedList;
import java.util.Queue;

public class BreadthFirstSearchSimulation2D extends Simulation {

    private boolean[][][] visited;

    public BreadthFirstSearchSimulation2D(int[][][] maze, int[] startCoordinate, int[] endCoordinate) {
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
        Queue<String> queue = new LinkedList<>();
        queue.add(getStartCoordinate()[1] + "," + getStartCoordinate()[0]);

        boolean pathFound = false;
        while (!queue.isEmpty()) {
            if (getTileGrid()[getEndCoordinate()[1]][getEndCoordinate()[0]][0] == 2) {
                pathFound = true;
                break;
            }
            String x = queue.remove();
            int row = Integer.parseInt(x.split(",")[0]);
            int col = Integer.parseInt(x.split(",")[1]);

            if (row < 0 || col < 0 || row >= getSize() || col >= getSize() || visited[row][col][0]) {
                continue;
            }
            visited[row][col][0] = true;

            int[][][] tileGrid = getTileGrid();
            tileGrid[row][col][0] = 2;
            setTileGrid(tileGrid);

            queue.add(row + "," + (col - 1)); //go left
            queue.add(row + "," + (col + 1)); //go right
            queue.add((row - 1) + "," + col); //go up
            queue.add((row + 1) + "," + col); //go down
        }
        return pathFound;
    }
}
