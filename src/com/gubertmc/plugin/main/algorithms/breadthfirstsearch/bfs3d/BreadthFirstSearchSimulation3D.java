package com.gubertmc.plugin.main.algorithms.breadthfirstsearch.bfs3d;

import com.gubertmc.plugin.main.algorithms.Simulation;

import java.util.LinkedList;
import java.util.Queue;

public class BreadthFirstSearchSimulation3D extends Simulation {

    private boolean[][][] visited;

    public BreadthFirstSearchSimulation3D(int[][][] maze, int[] startCoordinate, int[] endCoordinate) {
        super(maze, startCoordinate, endCoordinate, false);
    }

    @Override
    public void setup() {
        visited = new boolean[getSize()][getSize()][getSize()];
        for (int i = 0; i < getSize(); i++) {
            for (int j = 0; j < getSize(); j++) {
                for (int k = 0; k < getSize(); k++) {
                    if (getTileGrid()[i][j][k] == 1) {
                        visited[i][j][k] = true;
                    }
                }
            }
        }
    }

    @Override
    public boolean start() {
        Queue<String> queue = new LinkedList<>();
        queue.add(getStartCoordinate()[1] + "," + getStartCoordinate()[0] + "," + getStartCoordinate()[2]);

        boolean pathFound = false;
        while (!queue.isEmpty()) {
            if (getTileGrid()[getEndCoordinate()[1]][getEndCoordinate()[0]][2] == 2) {
                pathFound = true;
                break;
            }
            String x = queue.remove();
            int row = Integer.parseInt(x.split(",")[0]);
            int col = Integer.parseInt(x.split(",")[1]);
            int z = Integer.parseInt(x.split(",")[2]);

            if (row < 0 || col < 0 || row >= getSize() || col >= getSize()
                    || z < 0 || z >= getSize() || visited[row][col][z]) {
                continue;
            }
            visited[row][col][z] = true;

            int[][][] tileGrid = getTileGrid();
            tileGrid[row][col][z] = 2;
            setTileGrid(tileGrid);

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
