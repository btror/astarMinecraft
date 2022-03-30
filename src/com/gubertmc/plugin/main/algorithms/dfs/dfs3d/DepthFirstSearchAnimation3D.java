package com.gubertmc.plugin.main.algorithms.dfs.dfs3d;

import com.gubertmc.MazeGeneratorPlugin;
import com.gubertmc.plugin.main.algorithms.Animation;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.Stack;

public class DepthFirstSearchAnimation3D extends Animation {

    public boolean[][][] visited;
    public int[][][] textGrid;

    public DepthFirstSearchAnimation3D(
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
        super(plugin, tiles, startCoordinate, endCoordinate, size, wallMaterial, pathMaterial, pathSpreadMaterial, groundMaterial, startGlassMaterial, endGlassMaterial, true);
        visited = new boolean[size][size][size];
        textGrid = new int[size][size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    if (tile_grid[i][j][k].getBlock().getType() == WALL_MATERIAL) {
                        textGrid[i][j][k] = 1;
                        visited[i][j][k] = true;
                    } else {
                        textGrid[i][j][k] = 0;
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
        int zLength = textGrid[0][0].length;

        Stack<String> stack = new Stack<>();
        stack.push(startCoordinate[1] + "," + startCoordinate[0] + "," + startCoordinate[2]);

        while (!stack.empty()) {
            if (textGrid[endCoordinate[1]][endCoordinate[0]][endCoordinate[2]] != 5) {
                break;
            }

            String x = stack.pop();
            int row = Integer.parseInt(x.split(",")[0]);
            int col = Integer.parseInt(x.split(",")[1]);
            int z = Integer.parseInt(x.split(",")[2]);

            // row < 0 || row >= height
            // col < 0 || col >= length
            // z < 0 || z >= zLength
            if (row < 0 || col < 0 || row >= height || col >= length || z < 0 || z >= zLength || visited[row][col][z]) {
                continue;
            }
            visited[row][col][z] = true;
            textGrid[row][col][z] = 2;

            exploredPlaces.add(tile_grid[row][col][z]);

            stack.push(row + "," + (col - 1) + "," + z);
            stack.push(row + "," + (col + 1) + "," + z);
            stack.push((row - 1) + "," + col + "," + z);
            stack.push((row + 1) + "," + col + "," + z);
            stack.push(row + "," + col + "," + (z - 1));
            stack.push(row + "," + col + "," + (z + 1));
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
