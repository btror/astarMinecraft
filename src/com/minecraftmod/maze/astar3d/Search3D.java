package com.minecraftmod.maze.astar3d;

import com.minecraftmod.GenMazePlugin;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class Search3D {

    private final int SIZE;
    private final Material WALL_MATERIAL;
    private final Material PATH_MATERIAL;
    private final Material PATH_SPREAD_MATERIAL;
    private final Node3D[][][] grid;
    private final PriorityQueue<Node3D> open_list = new PriorityQueue<>(10, new NodeComparator3D()); // sorted by f value
    private final ArrayList<Node3D> closed_list = new ArrayList<>();
    private final Location[][][] tile_grid;
    private final int[][][] tile_grid_int;
    private final Node3D start_node3D;
    private Node3D current_node3D;
    private final Node3D end_node3D;
    private final GenMazePlugin plugin;

    // new
    private final ArrayList<Location> thePath = new ArrayList<>();
    private final ArrayList<Location> exploredPlaces = new ArrayList<>();

    /*
     * Default constructor
     */
    public Search3D(GenMazePlugin plugin, Location[][][] tiles, int[] startCoordinate, int[] endCoordinate, int size, Material wallMaterial, Material pathMaterial, Material pathSpreadMaterial) {
        grid = new Node3D[size][size][size];

        int[][][] tempArray = new int[size][size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    tempArray[i][j][k] = 0;
                }
            }
        }
        tile_grid_int = tempArray;

        this.plugin = plugin;
        SIZE = size;
        WALL_MATERIAL = wallMaterial;
        PATH_MATERIAL = pathMaterial;
        PATH_SPREAD_MATERIAL = pathSpreadMaterial;
        tile_grid = tiles;
        current_node3D = new Node3D(startCoordinate[1], startCoordinate[0], startCoordinate[2], 0);
        end_node3D = new Node3D(endCoordinate[1], endCoordinate[0], endCoordinate[2], 0);
        grid[startCoordinate[1]][startCoordinate[0]][startCoordinate[2]] = current_node3D;
        grid[endCoordinate[1]][endCoordinate[0]][endCoordinate[2]] = end_node3D;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                for (int k = 0; k < SIZE; k++) {
                    if (tiles[i][j][k].getBlock().getType() == Material.LIGHT_BLUE_STAINED_GLASS) {
                        Node3D node3D = new Node3D(i, j, k, 0);
                        grid[i][j][k] = node3D;
                    }
                    if (tiles[i][j][k].getBlock().getType() == WALL_MATERIAL) {
                        Node3D node3D = new Node3D(i, j, k, 1);
                        grid[i][j][k] = node3D;
                    }
                }
            }
        }
        // calculate g
        int g = calculateG(current_node3D);
        current_node3D.setG(g);
        // calculate h
        int h = calculateH(current_node3D);
        current_node3D.setH(h);
        // calculate f
        current_node3D.setF();
        start_node3D = current_node3D;
        open_list.add(current_node3D);
    }


    /*
     * Method that starts the A* search
     */
    public boolean start() {
            boolean pathFound = true;
            while (!open_list.isEmpty() && !current_node3D.equals(end_node3D)) { // open list isn't empty or goal node isn't reached
                current_node3D = open_list.peek();
                // remove the node with lowest f score
                open_list.remove(open_list.peek());
                // check if current node is goal node
                if (current_node3D.equals(end_node3D)) {
                    // if yes, generate a path
                    closed_list.add(current_node3D);
                    ArrayList<Node3D> path = generatePath();
                    for (int i = path.size() - 1; i > -1; i--) {
                        int row = path.get(i).getRow();
                        int col = path.get(i).getCol();
                        int zNum = path.get(i).getZ();

                        //if (tile_grid[row][col].getBlock().getType() == PATH_SPREAD_MATERIAL) { // duplicate of tile_grid ---------------------------------
                        if (tile_grid_int[row][col][zNum] == 1) {
                            // tile_grid[row][col].getBlock().setType(PATH_MATERIAL);
                            int x = tile_grid[row][col][zNum].getBlockX();
                            int y = tile_grid[row][col][zNum].getBlockY();
                            int z = tile_grid[row][col][zNum].getBlockZ();
                            Location floor = new Location(tile_grid[row][col][zNum].getWorld(), x, y, z);
                            thePath.add(floor);

                        }
                    }
                    break;
                } else {
                    // generate neighbors
                    try {
                        calculateNeighborValues();
                    } catch (NullPointerException np){}

                    int x = tile_grid[start_node3D.getRow()][start_node3D.getCol()][start_node3D.getZ()].getBlockX();
                    int y = tile_grid[start_node3D.getRow()][start_node3D.getCol()][start_node3D.getZ()].getBlockY();
                    int z = tile_grid[start_node3D.getRow()][start_node3D.getCol()][start_node3D.getZ()].getBlockZ();

//                    Location newLoc = new Location(tile_grid[start_node3D.getRow()][start_node3D.getCol()][start_node3D.getZ()].getWorld(), x, y, z);
//                    newLoc.getBlock().setType(Material.BEACON);

                    try {
                        assert open_list.peek() != null;
                    } catch (NullPointerException e){
                        pathFound = false;
                    }

                    // add current node to closed list
                    closed_list.add(current_node3D);
                }
            }

            if (open_list.size() == 0) {
                pathFound = false;
            }
            return pathFound;
    }

    public void showAnimation(long time) {
        time += 50L;
        int count = 1;
        for (Location loc : exploredPlaces) {
            runnableDelayed(loc, time, PATH_SPREAD_MATERIAL);
            count++;
            if (count % (int)(SIZE * 0.25) == 0) {
                time += 1L;
            }
        }

        time += 10L;
        for (Location loc : thePath) {
            if (thePath.get(thePath.size() - 1) == loc) {
                // do something cool
            } else {
                runnableDelayed(loc, time, PATH_MATERIAL);
                time += 1L;
            }
        }
    }

    public void runnableDelayed(Location loc, long time, Material material) {
        new BukkitRunnable() {
            @Override
            public void run() {
                loc.getBlock().setType(material);
                cancel();
            }
        }.runTaskTimer(this.plugin, time, 20L);
    }


    /*
     * method that calculates distance from start
     */
    public int calculateG(Node3D node3D) {
        int row = node3D.getRow();
        int col = node3D.getCol();
        int zNum = node3D.getZ();
        if (row == current_node3D.getRow() && col == current_node3D.getCol() && zNum == current_node3D.getZ()) {
            return 0;
        }

        Node3D parent = node3D.getParent();
        if (parent == null) {
            int xDistance;
            if (col > current_node3D.getCol()) {
                xDistance = col - current_node3D.getCol();
            } else {
                xDistance = current_node3D.getCol() - col;
            }

            int yDistance;
            if (row > current_node3D.getRow()) {
                yDistance = row - current_node3D.getRow();
            } else {
                yDistance = current_node3D.getRow() - row;
            }

            int zDistance;
            if (zNum > current_node3D.getZ()) {
                zDistance = zNum - current_node3D.getZ();
            } else {
                zDistance = current_node3D.getZ() - zNum;
            }

            return (xDistance * 10) + (yDistance * 10) + (zDistance * 10);
        }
        return 10 + parent.getG();
    }


    /*
     * method that calculates the heuristic (distance of a node from the goal)
     */
    public int calculateH(Node3D node3D) {
        int row = node3D.getRow();
        int col = node3D.getCol();
        int zNum = node3D.getZ();
        int x = 0;
        int y = 0;
        int z = 0;

        while (col < end_node3D.getCol() || col > end_node3D.getCol()) {
            x += 10;
            if (col < end_node3D.getCol()) {
                col++;
            }
            if (col > end_node3D.getCol()) {
                col--;
            }
        }
        while (row < end_node3D.getRow() || row > end_node3D.getRow()) {
            y += 10;
            if (row < end_node3D.getRow()) {
                row++;
            }
            if (row > end_node3D.getRow()) {
                row--;
            }
        }
        while (zNum < end_node3D.getZ() || zNum > end_node3D.getZ()) {
            z += 10;
            if (zNum < end_node3D.getZ()) {
                zNum++;
            }
            if (zNum > end_node3D.getZ()) {
                zNum--;
            }
        }
        return x + y + z;
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
        int row = current_node3D.getRow();
        int col = current_node3D.getCol();
        int zNum = current_node3D.getZ();

        // front node
        if (row - 1 > -1 && grid[row - 1][col][zNum].getType() == 0 && !closed_list.contains(grid[row - 1][col][zNum])) {
            grid[row - 1][col][zNum].setParent(current_node3D);
            int g = calculateG(grid[row - 1][col][zNum]);
            grid[row - 1][col][zNum].setG(g);
            int h = calculateH(grid[row - 1][col][zNum]);
            grid[row - 1][col][zNum].setH(h);
            grid[row - 1][col][zNum].setF();
            open_list.add(grid[row - 1][col][zNum]);
            tile_grid_int[row - 1][col][zNum] = 1;

            Location loc = tile_grid[row - 1][col][zNum];
            if (!exploredPlaces.contains(loc)) {
                loc = new Location(loc.getWorld(), loc.getBlock().getX(), loc.getBlock().getY(), loc.getBlock().getZ());
                exploredPlaces.add(loc);
            }
        }

        // left node
        if (col + 1 < SIZE && grid[row][col + 1][zNum].getType() == 0 && !closed_list.contains(grid[row][col + 1][zNum])) {
            grid[row][col + 1][zNum].setParent(current_node3D);
            int g = calculateG(grid[row][col + 1][zNum]);
            grid[row][col + 1][zNum].setG(g);
            int h = calculateH(grid[row][col + 1][zNum]);
            grid[row][col + 1][zNum].setH(h);
            grid[row][col + 1][zNum].setF();
            open_list.add(grid[row][col + 1][zNum]);
            tile_grid_int[row][col + 1][zNum] = 1;

            Location loc = tile_grid[row][col + 1][zNum];
            if (!exploredPlaces.contains(loc)) {
                loc = new Location(loc.getWorld(), loc.getBlock().getX(), loc.getBlock().getY(), loc.getBlock().getZ());
                exploredPlaces.add(loc);
            }
        }

        // behind node
        if (row + 1 < SIZE && grid[row + 1][col][zNum].getType() == 0 && !closed_list.contains(grid[row + 1][col][zNum])) {
            grid[row + 1][col][zNum].setParent(current_node3D);
            int g = calculateG(grid[row + 1][col][zNum]);
            grid[row + 1][col][zNum].setG(g);
            int h = calculateH(grid[row + 1][col][zNum]);
            grid[row + 1][col][zNum].setH(h);
            grid[row + 1][col][zNum].setF();
            open_list.add(grid[row + 1][col][zNum]);
            tile_grid_int[row + 1][col][zNum] = 1;

            Location loc = tile_grid[row + 1][col][zNum];
            if (!exploredPlaces.contains(loc)) {
                loc = new Location(loc.getWorld(), loc.getBlock().getX(), loc.getBlock().getY(), loc.getBlock().getZ());
                exploredPlaces.add(loc);
            }
        }

        // right node
        if (col - 1 > -1 && grid[row][col - 1][zNum].getType() == 0 && !closed_list.contains(grid[row][col - 1][zNum])) {
            grid[row][col - 1][zNum].setParent(current_node3D);
            int g = calculateG(grid[row][col - 1][zNum]);
            grid[row][col - 1][zNum].setG(g);
            int h = calculateH(grid[row][col - 1][zNum]);
            grid[row][col - 1][zNum].setH(h);
            grid[row][col - 1][zNum].setF();
            open_list.add(grid[row][col - 1][zNum]);
            tile_grid_int[row][col - 1][zNum] = 1;

            Location loc = tile_grid[row][col - 1][zNum];
            if (!exploredPlaces.contains(loc)) {
                loc = new Location(loc.getWorld(), loc.getBlock().getX(), loc.getBlock().getY(), loc.getBlock().getZ());
                exploredPlaces.add(loc);
            }
        }

        // bottom node
        if (zNum - 1 > -1 && grid[row][col][zNum - 1].getType() == 0 && !closed_list.contains(grid[row][col][zNum - 1])) {
            grid[row][col][zNum - 1].setParent(current_node3D);
            int g = calculateG(grid[row][col][zNum - 1]);
            grid[row][col][zNum - 1].setG(g);
            int h = calculateH(grid[row][col][zNum - 1]);
            grid[row][col][zNum - 1].setH(h);
            grid[row][col][zNum - 1].setF();
            open_list.add(grid[row][col][zNum - 1]);
            tile_grid_int[row][col][zNum - 1] = 1;

            Location loc = tile_grid[row][col][zNum - 1];
            if (!exploredPlaces.contains(loc)) {
                loc = new Location(loc.getWorld(), loc.getBlock().getX(), loc.getBlock().getY(), loc.getBlock().getZ());
                exploredPlaces.add(loc);
            }
        }

        // top node
        if (zNum + 1 < SIZE && grid[row][col][zNum + 1].getType() == 0 && !closed_list.contains(grid[row][col][zNum + 1])) {
            grid[row][col][zNum + 1].setParent(current_node3D);
            int g = calculateG(grid[row][col][zNum + 1]);
            grid[row][col][zNum + 1].setG(g);
            int h = calculateH(grid[row][col][zNum + 1]);
            grid[row][col][zNum + 1].setH(h);
            grid[row][col][zNum + 1].setF();
            open_list.add(grid[row][col][zNum + 1]);
            tile_grid_int[row][col][zNum + 1] = 1;

            Location loc = tile_grid[row][col][zNum + 1];
            if (!exploredPlaces.contains(loc)) {
                loc = new Location(loc.getWorld(), loc.getBlock().getX(), loc.getBlock().getY(), loc.getBlock().getZ());
                exploredPlaces.add(loc);
            }
        }
    }


    /*
     * Method that creates an arraylist containing the path
     */
    public ArrayList<Node3D> generatePath() {
        ArrayList<Node3D> path = new ArrayList<>();
        // get the parent nodes
        Node3D temp = current_node3D;
        path.add(temp);
        while(temp.getParent() != null) {
            temp = temp.getParent();
            path.add(temp);
        }
        return path;
    }
}
