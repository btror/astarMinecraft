package com.gubertmc.plugin.main.algorithms.bfs.bfs2d;

import com.gubertmc.MazeGeneratorPlugin;
import com.gubertmc.plugin.main.algorithms.Animation;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.LinkedList;
import java.util.Queue;

public class BreadthFirstSearchAnimation2D extends Animation {

    public boolean[][][] visited;
    public int[][][] textGrid;

    public BreadthFirstSearchAnimation2D(
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
            Material endGlassMaterial,
            boolean is3d
    ) {
        super(plugin, tiles, startCoordinate, endCoordinate, size, wallMaterial, pathMaterial, pathSpreadMaterial, groundMaterial, startGlassMaterial, endGlassMaterial, false);
        visited = new boolean[size][size][size];
        textGrid = new int[size][size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    if (tile_grid[i][j][0].getBlock().getType() == WALL_MATERIAL) {
                        textGrid[i][j][0] = 1;
                        visited[i][j][0] = true;
                    } else {
                        textGrid[i][j][0] = 0;
                    }
                }
            }
        }
        textGrid[endCoordinate[1]][endCoordinate[0]][endCoordinate[2]] = 5;
    }

    @Override
    public boolean start() {
        int height = textGrid.length;
        int length = textGrid[0].length;

        Queue<String> queue = new LinkedList<>();
        queue.add(startCoordinate[1] + "," + startCoordinate[0]);

        while (!queue.isEmpty()) {
            if (textGrid[endCoordinate[1]][endCoordinate[0]][endCoordinate[2]] != 5) {
                break;
            }

            String xs = queue.remove();
            int row = Integer.parseInt(xs.split(",")[0]);
            int col = Integer.parseInt(xs.split(",")[1]);

            if (row < 0 || col < 0 || row >= height || col >= length || visited[row][col][0]) {
                continue;
            }
            visited[row][col][0] = true;
            textGrid[row][col][0] = 2;

            exploredPlaces.add(tile_grid[row][col][0]);

            queue.add(row + "," + (col - 1)); //go left
            queue.add(row + "," + (col + 1)); //go right
            queue.add((row - 1) + "," + col); //go up
            queue.add((row + 1) + "," + col); //go down
        }
        return true;
    }

    @Override
    public void showAnimation(long time) {
        time += 5L;
        int count = 1;
        exploredPlaces.remove(exploredPlaces.size() - 1);
        exploredPlaces.remove(0);
        for (Location loc : exploredPlaces) {
            Location location = new Location(loc.getWorld(), loc.getBlock().getX(), loc.getBlock().getY() - 1, loc.getBlock().getZ());
            runnableDelayed(location, time, PATH_SPREAD_MATERIAL);
            count++;
            if (count % (int) (size * 0.25) == 0) {
                time += 1L;
            }
        }
    }

    @Override
    public void runnableDelayed(Location loc, long time, Material material) {
        new BukkitRunnable() {
            @Override
            public void run() {
                loc.getBlock().setType(material);
                cancel();
            }
        }.runTaskTimer(this.plugin, time, 20L);
    }
}
