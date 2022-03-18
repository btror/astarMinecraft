package com.gubertmc.maze.astar.algorithm2d;

import com.gubertmc.MazeGeneratorPlugin;
import com.gubertmc.maze.astar.Node;
import com.gubertmc.maze.astar.Search;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.*;

public class Search2D extends Search {

    public Search2D(MazeGeneratorPlugin plugin, Location[][][] tiles, int[] startCoordinate, int[] endCoordinate, int size, Material wallMaterial, Material pathMaterial, Material pathSpreadMaterial, Material groundMaterial) {
        super(plugin, tiles, startCoordinate, endCoordinate, size, wallMaterial, pathMaterial, pathSpreadMaterial, groundMaterial, false);
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

                    if (tile_grid_int[row][col][0] == 1) {
                        int x = tile_grid[row][col][0].getBlockX();
                        int y = tile_grid[row][col][0].getBlockY() - 1;
                        int z = tile_grid[row][col][0].getBlockZ();
                        Location floor = new Location(tile_grid[row][col][0].getWorld(), x, y, z);
                        thePath.add(floor);

                        if (row == end_node.getRow() && col == end_node.getCol()) {
                            floor.getBlock().setType(Material.RED_STAINED_GLASS);
                        }
                    }
                }
                break;
            } else {
                try {
                    calculateNeighborValues();
                } catch (NullPointerException np) {
                }

                int x = tile_grid[start_node.getRow()][start_node.getCol()][0].getBlockX();
                int y = tile_grid[start_node.getRow()][start_node.getCol()][0].getBlockY() - 2;
                int z = tile_grid[start_node.getRow()][start_node.getCol()][0].getBlockZ();

                Location newLoc = new Location(tile_grid[start_node.getRow()][start_node.getCol()][0].getWorld(), x, y, z); // ----------------------
                newLoc.getBlock().setType(Material.BEACON);

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

        // north node
        if (row - 1 > -1 && grid[row - 1][col][0].getType() == 0 && !closed_list.contains(grid[row - 1][col][0])) {
            grid[row - 1][col][0].setParent(current_node);
            int g = calculateG(grid[row - 1][col][0]);
            grid[row - 1][col][0].setG(g);
            int h = calculateH(grid[row - 1][col][0]);
            grid[row - 1][col][0].setH(h);
            grid[row - 1][col][0].setF();
            open_list.add(grid[row - 1][col][0]);
            tile_grid_int[row - 1][col][0] = 1;

            Location loc = tile_grid[row - 1][col][0];
            if (!exploredPlaces.contains(loc)) {
                loc = new Location(loc.getWorld(), loc.getBlock().getX(), loc.getBlock().getY() - 1, loc.getBlock().getZ());
                exploredPlaces.add(loc);
            }
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
            tile_grid_int[row][col + 1][0] = 1;

            Location loc = tile_grid[row][col + 1][0];
            if (!exploredPlaces.contains(loc)) {
                loc = new Location(loc.getWorld(), loc.getBlock().getX(), loc.getBlock().getY() - 1, loc.getBlock().getZ());
                exploredPlaces.add(loc);
            }
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
            tile_grid_int[row + 1][col][0] = 1;

            Location loc = tile_grid[row + 1][col][0];
            if (!exploredPlaces.contains(loc)) {
                loc = new Location(loc.getWorld(), loc.getBlock().getX(), loc.getBlock().getY() - 1, loc.getBlock().getZ());
                exploredPlaces.add(loc);
            }
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
            tile_grid_int[row][col - 1][0] = 1;

            Location loc = tile_grid[row][col - 1][0];
            if (!exploredPlaces.contains(loc)) {
                loc = new Location(loc.getWorld(), loc.getBlock().getX(), loc.getBlock().getY() - 1, loc.getBlock().getZ());
                exploredPlaces.add(loc);
            }
        }
    }
}
