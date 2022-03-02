package com.minecraftmod.maze.algorithm;

import com.minecraftmod.GenMazePlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import static org.bukkit.Bukkit.getServer;

public class Search {

    private final int SIZE;
    private final Material WALL_MATERIAL;
    private final Material PATH_MATERIAL;
    private final Material PATH_SPREAD_MATERIAL;
    private final Node[][] grid;
    private final PriorityQueue<Node> open_list = new PriorityQueue<>(10, new NodeComparator()); // sorted by f value
    private final ArrayList<Node> closed_list = new ArrayList<>();
    private final Location[][] tile_grid;
    private final int[][] tile_grid_int;
    private final Node start_node;
    private Node current_node;
    private final Node end_node;
    private final GenMazePlugin plugin;

    // new
    private ArrayList<Location> thePath = new ArrayList<>();

    /*
     * Default constructor
     */
    public Search(GenMazePlugin plugin, Location[][] tiles, int[] startCoordinate, int[] endCoordinate, int size, Material wallMaterial, Material pathMaterial, Material pathSpreadMaterial) {
        grid = new Node[size][size];

        int[][] tempArray = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                tempArray[i][j] = 0;
            }
        }
        tile_grid_int = tempArray;

        this.plugin = plugin;
        SIZE = size;
        WALL_MATERIAL = wallMaterial;
        PATH_MATERIAL = pathMaterial;
        PATH_SPREAD_MATERIAL = pathSpreadMaterial;
        tile_grid = tiles;
        current_node = new Node(startCoordinate[1], startCoordinate[0], 0);
        end_node = new Node(endCoordinate[1], endCoordinate[0], 0);
        grid[startCoordinate[1]][startCoordinate[0]] = current_node;
        grid[endCoordinate[1]][endCoordinate[0]] = end_node;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (tiles[i][j].getBlock().getType() == Material.AIR) {
                    Node node = new Node(i, j, 0);
                    grid[i][j] = node;
                }
                if (tiles[i][j].getBlock().getType() == WALL_MATERIAL) {
                    Node node = new Node(i, j, 1);
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
    public void start() {
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

                        //if (tile_grid[row][col].getBlock().getType() == PATH_SPREAD_MATERIAL) { // duplicate of tile_grid
                        if (tile_grid_int[row][col] == 1) {
                            // tile_grid[row][col].getBlock().setType(PATH_MATERIAL);
                            int x = tile_grid[row][col].getBlockX();
                            int y = tile_grid[row][col].getBlockY() - 1;
                            int z = tile_grid[row][col].getBlockZ();
                            Location floor = new Location(tile_grid[row][col].getWorld(), x, y, z);
                            // floor.getBlock().setType(PATH_MATERIAL); uncomment to change blocks to path
                            thePath.add(floor);

                            if (row == end_node.getRow() && col == end_node.getCol()) {
                                floor.getBlock().setType(Material.RED_STAINED_GLASS);
                            }
                        }
                    }
                    break;
                } else {
                    // generate neighbors
                    try {
                        calculateNeighborValues();
                    } catch (NullPointerException np){
                    }

                    // tile_grid[start_node.getRow()][start_node.getCol()].getBlock().setType(Material.DIAMOND_BLOCK);
                    int x = tile_grid[start_node.getRow()][start_node.getCol()].getBlockX();
                    int y = tile_grid[start_node.getRow()][start_node.getCol()].getBlockY() - 2;
                    int z = tile_grid[start_node.getRow()][start_node.getCol()].getBlockZ();
                    Location newLoc = new Location(tile_grid[start_node.getRow()][start_node.getCol()].getWorld(), x, y, z);
                    newLoc.getBlock().setType(Material.BEACON);
                    // tile_grid[end_node.getRow()][end_node.getCol()].getBlock().setType(Material.REDSTONE_BLOCK);

                    try {
                        assert open_list.peek() != null;
                    } catch (NullPointerException e){
                    }

                    // add current node to closed list
                    closed_list.add(current_node);

                }
            }

            // loop through "thePath" array to change blocks with a runnable
            long time = 0;
            for (Location loc : thePath) {
                if (thePath.get(thePath.size() - 1) == loc) {
                    // do something cool
                } else {
                    runnableDelayed(loc, time);
                    time += 5L;
                }
            }
    }

    public void runnableDelayed(Location loc, long time) {
        new BukkitRunnable() {
            @Override
            public void run() {
                loc.getBlock().setType(PATH_MATERIAL);
                cancel();
            }
        }.runTaskTimer(this.plugin, time, 20L);
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
     *
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
            tile_grid_int[row - 1][col] = 1;
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
            tile_grid_int[row][col + 1] = 1;
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
            tile_grid_int[row + 1][col] = 1;
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
            tile_grid_int[row][col - 1] = 1;
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
