package com.gubertmc.plugin.algorithms;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class Simulation {

    public int SIZE;
    public Node[][][] grid;
    public PriorityQueue<Node> open_list = new PriorityQueue<>(10, new NodeComparator());
    public ArrayList<Node> closed_list = new ArrayList<>();
    public int[][][] tile_grid;
    public Node start_node;
    public Node current_node;
    public final Node end_node;
    public boolean is3d;

    public Simulation(int[][][] maze, int[] startCoordinate, int[] endCoordinate, boolean is3d) {
        int size = maze[0].length;
        SIZE = size;
        grid = new Node[size][size][size];
        tile_grid = maze;
        this.is3d = is3d;

        if (!is3d) {
            current_node = new Node(startCoordinate[1], startCoordinate[0], -1, 0);
            end_node = new Node(endCoordinate[1], endCoordinate[0], -1, 0);
            grid[startCoordinate[1]][startCoordinate[0]][0] = current_node;
            grid[endCoordinate[1]][endCoordinate[0]][0] = end_node;

            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    if (tile_grid[i][j][0] == 0) {
                        Node node = new Node(i, j, -1, 0);
                        grid[i][j][0] = node;
                    }
                    if (tile_grid[i][j][0] == 1) {
                        Node node = new Node(i, j, -1, 1);
                        grid[i][j][0] = node;
                    }
                }
            }
        } else {
            current_node = new Node(startCoordinate[1], startCoordinate[0], startCoordinate[2], 0);
            end_node = new Node(endCoordinate[1], endCoordinate[0], endCoordinate[2], 0);
            grid[startCoordinate[1]][startCoordinate[0]][startCoordinate[2]] = current_node;
            grid[endCoordinate[1]][endCoordinate[0]][endCoordinate[2]] = end_node;

            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < SIZE; j++) {
                    for (int k = 0; k < SIZE; k++) {
                        if (tile_grid[i][j][k] == 0) {
                            Node node = new Node(i, j, k, 0);
                            grid[i][j][k] = node;
                        }
                        if (tile_grid[i][j][k] == 1) {
                            Node node = new Node(i, j, k, 1);
                            grid[i][j][k] = node;
                        }
                    }
                }
            }
        }

        int g = calculateG(current_node);
        current_node.setG(g);
        int h = calculateH(current_node);
        current_node.setH(h);
        current_node.setF();
        start_node = current_node;
        open_list.add(current_node);
    }

    public boolean start() {
        boolean pathFound = true;
        while (!open_list.isEmpty() && !current_node.equals(end_node)) {
            current_node = open_list.peek();
            open_list.remove(open_list.peek());
            if (current_node.equals(end_node)) {
                closed_list.add(current_node);
                ArrayList<Node> path = generatePath();

                for (int i = path.size() - 1; i > -1; i--) {
                    int row = path.get(i).getRow();
                    int col = path.get(i).getCol();
                    int zNum = path.get(i).getZ();
                    if (!is3d) {
                        zNum = 0;
                    }

                    if (tile_grid[row][col][zNum] == 2) {
                        tile_grid[row][col][zNum] = 3;
                    }
                }
                break;
            } else {
                try {
                    calculateNeighborValues();
                } catch (NullPointerException np) {
                    System.out.println(np.getMessage());
                }

                if (!is3d) {
                    tile_grid[start_node.getRow()][start_node.getCol()][0] = 4;
                    tile_grid[end_node.getRow()][end_node.getCol()][0] = 5;
                } else {
                    tile_grid[start_node.getRow()][start_node.getCol()][start_node.getZ()] = 4;
                    tile_grid[end_node.getRow()][end_node.getCol()][end_node.getZ()] = 5;
                }

                try {
                    assert open_list.peek() != null;
                } catch (NullPointerException e) {
                    pathFound = false;
                }
                closed_list.add(current_node);
            }
        }

        if (open_list.size() == 0) {
            pathFound = false;
        }
        return pathFound;
    }

    public int calculateG(Node node) {
        int row = node.getRow();
        int col = node.getCol();
        int zNum = node.getZ();
        if (row == current_node.getRow() && col == current_node.getCol() && zNum == current_node.getZ()) {
            return 0;
        }

        Node parent = node.getParent();
        if (parent == null) {
            int xDistance;
            if (col > current_node.getCol()) {
                xDistance = col - current_node.getCol();
            } else {
                xDistance = current_node.getCol() - col;
            }

            int yDistance;
            if (row > current_node.getRow()) {
                yDistance = row - current_node.getRow();
            } else {
                yDistance = current_node.getRow() - row;
            }

            int zDistance;
            if (zNum > current_node.getZ()) {
                zDistance = zNum - current_node.getZ();
            } else {
                zDistance = current_node.getZ() - zNum;
            }

            if (zNum == -1) {
                zDistance = 0;
            }

            return (xDistance * 10) + (yDistance * 10) + (zDistance * 10);
        }
        return 10 + parent.getG();
    }

    public int calculateH(Node node) {
        int row = node.getRow();
        int col = node.getCol();
        int zNum = node.getZ();
        int x = 0;
        int y = 0;
        int z = 0;

        while (col < end_node.getCol() || col > end_node.getCol()) {
            x += 10;
            if (col < end_node.getCol()) {
                col++;
            }
            if (col > end_node.getCol()) {
                col--;
            }
        }
        while (row < end_node.getRow() || row > end_node.getRow()) {
            y += 10;
            if (row < end_node.getRow()) {
                row++;
            }
            if (row > end_node.getRow()) {
                row--;
            }
        }
        if (zNum != -1) {
            while (zNum < end_node.getZ() || zNum > end_node.getZ()) {
                z += 10;
                if (zNum < end_node.getZ()) {
                    zNum++;
                }
                if (zNum > end_node.getZ()) {
                    zNum--;
                }
            }
        }

        return x + y + z;
    }

    public void calculateNeighborValues() {
        int row = current_node.getRow();
        int col = current_node.getCol();
        int zNum = current_node.getZ();

        if (!is3d) {
            zNum = 0;
        }

        // front node
        if (row - 1 > -1 && grid[row - 1][col][zNum].getType() == 0 && !closed_list.contains(grid[row - 1][col][zNum])) {
            grid[row - 1][col][zNum].setParent(current_node);
            int g = calculateG(grid[row - 1][col][zNum]);
            grid[row - 1][col][zNum].setG(g);
            int h = calculateH(grid[row - 1][col][zNum]);
            grid[row - 1][col][zNum].setH(h);
            grid[row - 1][col][zNum].setF();
            open_list.add(grid[row - 1][col][zNum]);
            tile_grid[row - 1][col][zNum] = 2;
        }

        // left node
        if (col + 1 < SIZE && grid[row][col + 1][zNum].getType() == 0 && !closed_list.contains(grid[row][col + 1][zNum])) {
            grid[row][col + 1][zNum].setParent(current_node);
            int g = calculateG(grid[row][col + 1][zNum]);
            grid[row][col + 1][zNum].setG(g);
            int h = calculateH(grid[row][col + 1][zNum]);
            grid[row][col + 1][zNum].setH(h);
            grid[row][col + 1][zNum].setF();
            open_list.add(grid[row][col + 1][zNum]);
            tile_grid[row][col + 1][zNum] = 2;
        }

        // behind node
        if (row + 1 < SIZE && grid[row + 1][col][zNum].getType() == 0 && !closed_list.contains(grid[row + 1][col][zNum])) {
            grid[row + 1][col][zNum].setParent(current_node);
            int g = calculateG(grid[row + 1][col][zNum]);
            grid[row + 1][col][zNum].setG(g);
            int h = calculateH(grid[row + 1][col][zNum]);
            grid[row + 1][col][zNum].setH(h);
            grid[row + 1][col][zNum].setF();
            open_list.add(grid[row + 1][col][zNum]);
            tile_grid[row + 1][col][zNum] = 2;
        }

        // right node
        if (col - 1 > -1 && grid[row][col - 1][zNum].getType() == 0 && !closed_list.contains(grid[row][col - 1][zNum])) {
            grid[row][col - 1][zNum].setParent(current_node);
            int g = calculateG(grid[row][col - 1][zNum]);
            grid[row][col - 1][zNum].setG(g);
            int h = calculateH(grid[row][col - 1][zNum]);
            grid[row][col - 1][zNum].setH(h);
            grid[row][col - 1][zNum].setF();
            open_list.add(grid[row][col - 1][zNum]);
            tile_grid[row][col - 1][zNum] = 2;
        }

        if (is3d) {
            // bottom node
            if (zNum - 1 > -1 && grid[row][col][zNum - 1].getType() == 0 && !closed_list.contains(grid[row][col][zNum - 1])) {
                grid[row][col][zNum - 1].setParent(current_node);
                int g = calculateG(grid[row][col][zNum - 1]);
                grid[row][col][zNum - 1].setG(g);
                int h = calculateH(grid[row][col][zNum - 1]);
                grid[row][col][zNum - 1].setH(h);
                grid[row][col][zNum - 1].setF();
                open_list.add(grid[row][col][zNum - 1]);
                tile_grid[row][col][zNum - 1] = 2;
            }

            // top node
            if (zNum + 1 < SIZE && grid[row][col][zNum + 1].getType() == 0 && !closed_list.contains(grid[row][col][zNum + 1])) {
                grid[row][col][zNum + 1].setParent(current_node);
                int g = calculateG(grid[row][col][zNum + 1]);
                grid[row][col][zNum + 1].setG(g);
                int h = calculateH(grid[row][col][zNum + 1]);
                grid[row][col][zNum + 1].setH(h);
                grid[row][col][zNum + 1].setF();
                open_list.add(grid[row][col][zNum + 1]);
                tile_grid[row][col][zNum + 1] = 2;
            }
        }
    }

    public ArrayList<Node> generatePath() {
        ArrayList<Node> path = new ArrayList<>();
        Node temp = current_node;
        path.add(temp);
        while (temp.getParent() != null) {
            temp = temp.getParent();
            path.add(temp);
        }
        return path;
    }
}