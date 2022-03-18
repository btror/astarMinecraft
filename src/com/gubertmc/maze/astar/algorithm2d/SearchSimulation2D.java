package com.gubertmc.maze.astar.algorithm2d;

import com.gubertmc.maze.astar.Node;
import com.gubertmc.maze.astar.NodeComparator;

import java.util.*;

public class SearchSimulation2D {

    private final int SIZE;
    private final Node[][][] grid;
    private final PriorityQueue<Node> open_list = new PriorityQueue<>(10, new NodeComparator());
    private final ArrayList<Node> closed_list = new ArrayList<>();
    private final int[][][] tile_grid;
    private final Node start_node;
    private Node current_node;
    private final Node end_node;

    public SearchSimulation2D(int[][][] maze, int[] startCoordinate, int[] endCoordinate) {
        int size = maze[0].length;
        SIZE = size;
        grid = new Node[size][size][size];
        tile_grid = maze;

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

                    if (tile_grid[row][col][0] == 2) {
                        tile_grid[row][col][0] = 3;
                    }
                }
                break;
            } else {
                try {
                    calculateNeighborValues();
                } catch (NullPointerException np) {
                    System.out.println(np.getMessage());
                }

                tile_grid[start_node.getRow()][start_node.getCol()][0] = 4;
                tile_grid[end_node.getRow()][end_node.getCol()][0] = 5;

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
        if (row == current_node.getRow() && col == current_node.getCol()) {
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
            return (xDistance * 10) + (yDistance * 10);
        }
        return 10 + parent.getG();
    }

    public int calculateH(Node node) {
        int row = node.getRow();
        int col = node.getCol();
        int x = 0;
        int y = 0;
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
        return x + y;
    }

    public void calculateNeighborValues() {
        int row = current_node.getRow();
        int col = current_node.getCol();

        // north node
        if (row - 1 > -1 && grid[row - 1][col][0].getType() == 0 && !closed_list.contains(grid[row - 1][col][0])) {
            grid[row - 1][col][0].setParent(current_node);
            int g = calculateG(grid[row - 1][col][0]);
            grid[row - 1][col][0].setG(g);
            int h = calculateH(grid[row - 1][col][0]);
            grid[row - 1][col][0].setH(h);
            grid[row - 1][col][0].setF();
            open_list.add(grid[row - 1][col][0]);
            tile_grid[row - 1][col][0] = 2;
        }

        // east node
        if (col + 1 < SIZE && grid[row][col + 1][0].getType() == 0 && !closed_list.contains(grid[row][col + 1][0])) {
            grid[row][col + 1][0].setParent(current_node);
            int g = calculateG(grid[row][col + 1][0]);
            grid[row][col + 1][0].setG(g);
            int h = calculateH(grid[row][col + 1][0]);
            grid[row][col + 1][0].setH(h);
            grid[row][col + 1][0].setF();
            open_list.add(grid[row][col + 1][0]);
            tile_grid[row][col + 1][0] = 2;
        }

        // south node
        if (row + 1 < SIZE && grid[row + 1][col][0].getType() == 0 && !closed_list.contains(grid[row + 1][col][0])) {
            grid[row + 1][col][0].setParent(current_node);
            int g = calculateG(grid[row + 1][col][0]);
            grid[row + 1][col][0].setG(g);
            int h = calculateH(grid[row + 1][col][0]);
            grid[row + 1][col][0].setH(h);
            grid[row + 1][col][0].setF();
            open_list.add(grid[row + 1][col][0]);
            tile_grid[row + 1][col][0] = 2;
        }

        // west node
        if (col - 1 > -1 && grid[row][col - 1][0].getType() == 0 && !closed_list.contains(grid[row][col - 1][0])) {
            grid[row][col - 1][0].setParent(current_node);
            int g = calculateG(grid[row][col - 1][0]);
            grid[row][col - 1][0].setG(g);
            int h = calculateH(grid[row][col - 1][0]);
            grid[row][col - 1][0].setH(h);
            grid[row][col - 1][0].setF();
            open_list.add(grid[row][col - 1][0]);
            tile_grid[row][col - 1][0] = 2;
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