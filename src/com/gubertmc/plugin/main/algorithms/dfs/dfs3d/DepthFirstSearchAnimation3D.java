package com.gubertmc.plugin.main.algorithms.dfs.dfs3d;

import com.gubertmc.MazeGeneratorPlugin;
import com.gubertmc.plugin.main.algorithms.Animation;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Stack;

public class DepthFirstSearchAnimation3D extends Animation {

    private boolean[][][] visited;
    private int[][][] textGrid;

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
        super(
                plugin,
                tiles,
                startCoordinate,
                endCoordinate,
                size,
                wallMaterial,
                pathMaterial,
                pathSpreadMaterial,
                groundMaterial,
                startGlassMaterial,
                endGlassMaterial,
                true
        );
    }

    @Override
    public void setup() {
        visited = new boolean[getSize()][getSize()][getSize()];
        textGrid = new int[getSize()][getSize()][getSize()];

        for (int i = 0; i < getSize(); i++) {
            for (int j = 0; j < getSize(); j++) {
                for (int k = 0; k < getSize(); k++) {
                    if (getTileGrid()[i][j][k].getBlock().getType() == getWallMaterial()) {
                        textGrid[i][j][k] = 1;
                        visited[i][j][k] = true;
                    } else {
                        textGrid[i][j][k] = 0;
                    }
                }
            }
        }
        textGrid[getEndCoordinate()[1]][getEndCoordinate()[0]][getEndCoordinate()[2]] = 5;
    }

    @Override
    public boolean start() {
        int height = textGrid.length;
        int length = textGrid[0].length;
        int zLength = textGrid[0][0].length;

        Stack<String> stack = new Stack<>();
        stack.push(getStartCoordinate()[1] + "," + getStartCoordinate()[0] + "," + getStartCoordinate()[2]);

        while (!stack.empty()) {
            if (textGrid[getEndCoordinate()[1]][getEndCoordinate()[0]][getEndCoordinate()[2]] != 5) {
                break;
            }

            String x = stack.pop();
            int row = Integer.parseInt(x.split(",")[0]);
            int col = Integer.parseInt(x.split(",")[1]);
            int z = Integer.parseInt(x.split(",")[2]);

            if (row < 0 || col < 0 || row >= height || col >= length
                    || z < 0 || z >= zLength || visited[row][col][z]) {
                continue;
            }
            visited[row][col][z] = true;
            textGrid[row][col][z] = 2;

            ArrayList<Location> exploredPlaces = getExploredPlaces();
            exploredPlaces.add(getTileGrid()[row][col][z]);
            setExploredPlaces(exploredPlaces);

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
        ArrayList<Location> exploredPlaces = getExploredPlaces();
        exploredPlaces.remove(exploredPlaces.size() - 1);
        exploredPlaces.remove(0);
        setExploredPlaces(exploredPlaces);
        for (Location loc : exploredPlaces) {
            Location location = new Location(
                    loc.getWorld(), loc.getBlock().getX(), loc.getBlock().getY(), loc.getBlock().getZ()
            );
            runnableDelayed(location, time, getPathSpreadMaterial());
            count++;
            if (count % (int) (getSize() * 0.25) == 0) {
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
        }.runTaskTimer(getPlugin(), time, 20L);
    }
}
