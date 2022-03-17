package com.gubertmc.maze.astar.algorithm2d;

import com.gubertmc.maze.astar.Node;
import com.gubertmc.maze.astar.NodeComparator;

import java.util.*;

public class SearchSimulation2D {

    private final int SIZE;
    private final Node[][] grid;
    private final PriorityQueue<Node> open_list = new PriorityQueue<>(10, new NodeComparator()); // sorted by f value
    private final ArrayList<Node> closed_list = new ArrayList<>();
    private final int[][] tile_grid;
    private final Node start_node;
    private Node current_node;
    private final Node end_node;
    private final ArrayList<Integer> thePath = new ArrayList<>();

    /*
     * Default constructor
     */
    public SearchSimulation2D(int[][] maze, int[] startCoordinate, int[] endCoordinate) {
        int size = maze[0].length;
        SIZE = size;
        grid = new Node[size][size];
        tile_grid = maze;

        current_node = new Node(startCoordinate[1], startCoordinate[0], -1, 0);
        end_node = new Node(endCoordinate[1], endCoordinate[0], -1, 0);
        grid[startCoordinate[1]][startCoordinate[0]] = current_node;
        grid[endCoordinate[1]][endCoordinate[0]] = end_node;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (tile_grid[i][j] == 0) {
                    Node node = new Node(i, j, -1, 0);
                    grid[i][j] = node;
                }
                if (tile_grid[i][j] == 1) {
                    Node node = new Node(i, j, -1, 1);
                    grid[i][j] = node;
                }
            }
        }
        // calculate g
        int g = calculateG(current_node);
        current_node.setG(g);
        // calculate h
        int h = calculateH(current_node);
        current_node.setH(h);
        // calculate f
        current_node.setF();
        start_node = current_node;
        open_list.add(current_node);
    }


    /*
     * Method that starts the A* search
     */
    public boolean start() {
        boolean pathFound = true;
        while (!open_list.isEmpty() && !current_node.equals(end_node)) { // open list isn't empty or goal node isn't reached
            current_node = open_list.peek();
            // remove the node with lowest f score
            open_list.remove(open_list.peek());
            // check if current node is goal node
            if (current_node.equals(end_node)) {
                // if yes, generate a path
                closed_list.add(current_node);
                ArrayList<Node> path = generatePath();
                for (int i = path.size() - 1; i > -1; i--) {
                    int row = path.get(i).getRow();
                    int col = path.get(i).getCol();

                    //if (tile_grid[row][col].getBlock().getType() == PATH_SPREAD_MATERIAL) { // duplicate of tile_grid (0 is empty, 1 is WALL, 2 is visited nodes, 3 is path, 4 is start, 5 is end)
                    if (tile_grid[row][col] == 2) {
                        tile_grid[row][col] = 3;
                    }
                }
                // path found at this point
                break;
            } else {
                // generate neighbors
                try {
                    calculateNeighborValues();
                } catch (NullPointerException np){
                    System.out.println(np.getMessage());
                }

                tile_grid[start_node.getRow()][start_node.getCol()] = 4;
                tile_grid[end_node.getRow()][end_node.getCol()] = 5;

                try {
                    assert open_list.peek() != null;
                } catch (NullPointerException e){
                    pathFound = false;
                }

                // add current node to closed list
                closed_list.add(current_node);

            }
        }

        if (open_list.size() == 0) {
            pathFound = false;
        }
        return pathFound;
    }

    /*
     * method that calculates distance from start
     */
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


    /*
     * method that calculates the heuristic (distance of a node from the goal)
     */
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


    /*
     * method that calculates neighbor data
     *
     * neighbors must be within the bounds of the world
     * neighbors must be pathable (type 0)
     * neighbors must not exist in the closed list
     * (0 is empty, 1 is WALL, 2 is visited nodes, 3 is path, 4 is start, 5 is end)
     */
    public void calculateNeighborValues() {
        int row = current_node.getRow();
        int col = current_node.getCol();

        // north node
        if (row - 1 > -1 && grid[row - 1][col].getType() == 0 && !closed_list.contains(grid[row - 1][col])) {
            grid[row - 1][col].setParent(current_node);
            int g = calculateG(grid[row - 1][col]);
            grid[row - 1][col].setG(g);
            int h = calculateH(grid[row - 1][col]);
            grid[row - 1][col].setH(h);
            grid[row - 1][col].setF();
            open_list.add(grid[row - 1][col]);
            // tile_grid[row - 1][col].getBlock().setType(PATH_SPREAD_MATERIAL); uncomment to show places explored
            tile_grid[row - 1][col] = 2;
        }

        // east node
        if (col + 1 < SIZE && grid[row][col + 1].getType() == 0 && !closed_list.contains(grid[row][col + 1])) {
            grid[row][col + 1].setParent(current_node);
            int g = calculateG(grid[row][col + 1]);
            grid[row][col + 1].setG(g);
            int h = calculateH(grid[row][col + 1]);
            grid[row][col + 1].setH(h);
            grid[row][col + 1].setF();
            open_list.add(grid[row][col + 1]);
            // tile_grid[row][col + 1].getBlock().setType(PATH_SPREAD_MATERIAL); uncomment to show places explored
            tile_grid[row][col + 1] = 2;
        }

        // south node
        if (row + 1 < SIZE && grid[row + 1][col].getType() == 0 && !closed_list.contains(grid[row + 1][col])) {
            grid[row + 1][col].setParent(current_node);
            int g = calculateG(grid[row + 1][col]);
            grid[row + 1][col].setG(g);
            int h = calculateH(grid[row + 1][col]);
            grid[row + 1][col].setH(h);
            grid[row + 1][col].setF();
            open_list.add(grid[row + 1][col]);
            // tile_grid[row + 1][col].getBlock().setType(PATH_SPREAD_MATERIAL); uncomment to show places explored
            tile_grid[row + 1][col] = 2;
        }

        // west node
        if (col - 1 > -1 && grid[row][col - 1].getType() == 0 && !closed_list.contains(grid[row][col - 1])) {
            grid[row][col - 1].setParent(current_node);
            int g = calculateG(grid[row][col - 1]);
            grid[row][col - 1].setG(g);
            int h = calculateH(grid[row][col - 1]);
            grid[row][col - 1].setH(h);
            grid[row][col - 1].setF();
            open_list.add(grid[row][col - 1]);
            // tile_grid[row][col - 1].getBlock().setType(PATH_SPREAD_MATERIAL); uncomment to show places explored
            tile_grid[row][col - 1] = 2;
        }
    }


    /*
     * Method that creates an arraylist containing the path
     */
    public ArrayList<Node> generatePath() {
        ArrayList<Node> path = new ArrayList<>();
        // get the parent nodes
        Node temp = current_node;
        path.add(temp);
        while(temp.getParent() != null) {
            temp = temp.getParent();
            path.add(temp);
        }
        return path;
    }
}