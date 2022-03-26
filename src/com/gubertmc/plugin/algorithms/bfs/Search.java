package com.gubertmc.plugin.algorithms.bfs;

import com.gubertmc.MazeGeneratorPlugin;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.LinkedList;
import java.util.Queue;

public class Search {

    public Location[][][] tile_grid;
    public int[][][] tile_grid_int;
    public int[] startCoordinate;
    public int[] endCoordinate;
    public Material WALL_MATERIAL;
    public Material PATH_MATERIAL;
    public Material PATH_SPREAD_MATERIAL;
    public Material PATH_GROUND_MATERIAL;
    public Material START_POINT_GLASS;
    public Material END_POINT_GLASS;
    public MazeGeneratorPlugin plugin;
    public int SIZE;

    public Search(
            MazeGeneratorPlugin plugin,
            Location[][][] tiles,
            int[] startCoordinate,
            int[] endCoordinate,
            int size,
            Material wallMaterial,
            Material pathMaterial,
            Material pathSpreadMaterial,
            Material groundMaterial,
            Material startGlassMaterial,
            Material endGlassMaterial
    ) {
        int[][][] tempArray = new int[size][size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    tempArray[i][j][k] = 0;
                }
            }
        }
        tile_grid_int = tempArray;
        tile_grid = tiles;
        this.plugin = plugin;
        SIZE = size;
        WALL_MATERIAL = wallMaterial;
        PATH_MATERIAL = pathMaterial;
        PATH_SPREAD_MATERIAL = pathSpreadMaterial;
        PATH_GROUND_MATERIAL = groundMaterial;
        START_POINT_GLASS = startGlassMaterial;
        END_POINT_GLASS = endGlassMaterial;
        this.startCoordinate = startCoordinate;
        this.endCoordinate = endCoordinate;

    }

    public boolean start(long time) {
        boolean[][][] visited = new boolean[SIZE][SIZE][SIZE];
        int[][][] textGrid = new int[SIZE][SIZE][SIZE];

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                for (int k = 0; k < SIZE; k++) {
                    if (tile_grid[i][j][0].getBlock().getType() == WALL_MATERIAL) {
                        textGrid[i][j][0] = 1;
                        visited[i][j][0] = true;
                    } else {
                        textGrid[i][j][0] = 0;
                    }
                }
            }
        }

        int height = textGrid.length;
        int length = textGrid[0].length;

        Queue<String> queue = new LinkedList<>();
        queue.add(startCoordinate[1] + "," + startCoordinate[0]);

        int count = 0;
        while (!queue.isEmpty()) {
            double x = tile_grid[endCoordinate[1]][endCoordinate[0]][endCoordinate[2]].getBlock().getX();
            double y = tile_grid[endCoordinate[1]][endCoordinate[0]][endCoordinate[2]].getBlock().getY() - 1;
            double z = tile_grid[endCoordinate[1]][endCoordinate[0]][endCoordinate[2]].getBlock().getZ();
            Location location = new Location(tile_grid[0][0][0].getWorld(), x, y, z);
            if (location.getBlock().getType() == PATH_SPREAD_MATERIAL) { // was 1 0 2
                break;
            }

            String xs = queue.remove();
            int row = Integer.parseInt(xs.split(",")[0]);
            int col = Integer.parseInt(xs.split(",")[1]);

            if (row < 0 || col < 0 || row >= height || col >= length || visited[row][col][0]) {
                continue;
            }
            visited[row][col][0] = true;

            runnableDelayed(tile_grid[row][col][0], time, PATH_SPREAD_MATERIAL);

            queue.add(row + "," + (col - 1)); //go left
            queue.add(row + "," + (col + 1)); //go right
            queue.add((row - 1) + "," + col); //go up
            queue.add((row + 1) + "," + col); //go down

            if (count % 10 == 0) {
                time += 1L;
            }
            count++;
        }
        return true;
    }

    public void runnableDelayed(Location loc, long time, Material material) {
        new BukkitRunnable() {
            @Override
            public void run() {
                double x = loc.getBlock().getX();
                double y = loc.getBlock().getY() - 1;
                double z = loc.getBlock().getZ();
                Location location = new Location(tile_grid[0][0][0].getWorld(), x, y, z);

                if (location.getBlock().getType() != END_POINT_GLASS) {
                    location.getBlock().setType(material);
                } else if (location.getBlock().getType() != Material.BEACON) {
                    location.getBlock().setType(material);
                }
                cancel();
            }
        }.runTaskTimer(this.plugin, time, 20L);
    }
}
