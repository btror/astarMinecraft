package com.gubertmc.maze.astar.algorithm3d;

import com.gubertmc.MazeGeneratorPlugin;
import com.gubertmc.maze.astar.Node;
import com.gubertmc.maze.astar.Search;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;

public class Search3D extends Search {

    public Search3D(MazeGeneratorPlugin plugin, Location[][][] tiles, int[] startCoordinate, int[] endCoordinate, int size, Material wallMaterial, Material pathMaterial, Material pathSpreadMaterial, Material groundMaterial) {
        super(plugin, tiles, startCoordinate, endCoordinate, size, wallMaterial, pathMaterial, pathSpreadMaterial, groundMaterial, true);
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

                    if (tile_grid_int[row][col][zNum] == 1) {
                        int x = tile_grid[row][col][zNum].getBlockX();
                        int y = tile_grid[row][col][zNum].getBlockY();
                        int z = tile_grid[row][col][zNum].getBlockZ();

                        Location floor = new Location(tile_grid[row][col][zNum].getWorld(), x, y, z);
                        thePath.add(floor);
                    }
                }
                break;
            } else {
                try {
                    calculateNeighborValues();
                } catch (NullPointerException np) {
                }

//                    int x = tile_grid[start_node.getRow()][start_node.getCol()][start_node.getZ()].getBlockX();
//                    int y = tile_grid[start_node.getRow()][start_node.getCol()][start_node.getZ()].getBlockY();
//                    int z = tile_grid[start_node.getRow()][start_node.getCol()][start_node.getZ()].getBlockZ();

//                    Location newLoc = new Location(tile_grid[start_node3D.getRow()][start_node3D.getCol()][start_node3D.getZ()].getWorld(), x, y, z);
//                    newLoc.getBlock().setType(Material.BEACON);

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

    public void calculateNeighborValues() {
        int row = current_node.getRow();
        int col = current_node.getCol();
        int zNum = current_node.getZ();

        // front node
        if (row - 1 > -1 && grid[row - 1][col][zNum].getType() == 0 && !closed_list.contains(grid[row - 1][col][zNum])) {
            grid[row - 1][col][zNum].setParent(current_node);
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
            grid[row][col + 1][zNum].setParent(current_node);
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
            grid[row + 1][col][zNum].setParent(current_node);
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
            grid[row][col - 1][zNum].setParent(current_node);
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
            grid[row][col][zNum - 1].setParent(current_node);
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
            grid[row][col][zNum + 1].setParent(current_node);
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
}
