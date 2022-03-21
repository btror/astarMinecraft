package com.gubertmc.plugin.mazes;

import com.gubertmc.MazeGeneratorPlugin;
import com.gubertmc.plugin.Maze;
import com.gubertmc.plugin.algorithms.Simulation;
import com.gubertmc.plugin.algorithms.astar2d.Search2D;
import com.gubertmc.plugin.algorithms.astar2d.Simulation2D;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import static org.bukkit.Bukkit.getServer;

public class Maze2D extends Maze {

    private long time;
    private boolean isValid;

    public Maze2D(MazeGeneratorPlugin plugin, Block block, int size) {
        super(plugin, block, size);
    }

    /**
     * Generates a simulation maze of integers.
     *
     * @return integer maze.
     */
    public int[][][] generateSimulation() {
        int[][][] maze = new int[size][size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    maze[i][j][k] = 0;
                }
            }
        }

        int randomStartX = (int) (Math.random() * size);
        int randomStartY = (int) (Math.random() * size);

        int randomEndX = (int) (Math.random() * size);
        int randomEndY = (int) (Math.random() * size);

        boolean badPositions = true;
        while (badPositions) {
            randomStartX = (int) (Math.random() * size);
            randomStartY = (int) (Math.random() * size);
            randomEndX = (int) (Math.random() * size);
            randomEndY = (int) (Math.random() * size);

            int distance = (int) Math.sqrt(Math.pow(randomEndX - randomStartX, 2) + Math.pow(randomEndY - randomStartY, 2));
            if (size / 1.3 < distance) {
                badPositions = false;
            }
        }

        maze[randomStartX][randomStartY][0] = 4;
        maze[randomEndX][randomEndY][0] = 5;

        startCoordinate[0] = randomStartY;
        startCoordinate[1] = randomStartX;
        startCoordinate[2] = 0;

        endCoordinate[0] = randomEndY;
        endCoordinate[1] = randomEndX;
        endCoordinate[2] = 0;

        int k = 0;
        int randomX = (int) (Math.random() * size);
        int randomY = (int) (Math.random() * size);
        for (int i = 0; i < (size * size) * wallPercentage; i++) {
            if (k % 2 == 0) {
                randomX = (int) (Math.random() * size);
                randomY = (int) (Math.random() * size);
            } else {
                int random = (int) (Math.random() * 2);
                if (random == 0) {
                    if (randomX < size - 1 && randomX + 1 != randomStartX && randomX + 1 != randomStartY) {
                        randomX++;
                    }
                } else {
                    if (randomY < size - 1 && randomY + 1 != randomStartX && randomY + 1 != randomStartY) {
                        randomY++;
                    }
                }
            }
            while ((randomX == randomStartX && randomY == randomStartY) || (randomX == randomEndX && randomY == randomEndY)) {
                randomX = (int) (Math.random() * size);
                randomY = (int) (Math.random() * size);
            }
            maze[randomX][randomY][0] = 1;
            k++;
        }

        return maze;
    }

    /**
     * Generates a new maze.
     *
     * @param coreMaterial    floor material.
     * @param blockerMaterial wall material.
     * @param spreadMaterial  algorithm spread material.
     * @param pathMaterial    algorithm path material.
     */
    public void generateNewMaze(Material coreMaterial, Material blockerMaterial, Material spreadMaterial, Material pathMaterial, Material startPointGlassMaterial, Material endPointGlassMaterial) {
        isValid = false;
        while (!isValid) {
            int[][][] simulationMaze = generateSimulation();
            Simulation simulation = new Simulation2D(simulationMaze, startCoordinate, endCoordinate);
            isValid = simulation.start();

            if (isValid) {
                clearOldBeacons();
                generateStartAndEndPoints();
                generateBlockedAreas(simulationMaze, blockerMaterial);

                time += 5L;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Search2D search2D = new Search2D(plugin, locations, startCoordinate, endCoordinate, size, blockerMaterial, pathMaterial, spreadMaterial, coreMaterial, startPointGlassMaterial, endPointGlassMaterial);
                        isValid = search2D.start();
                        getServer().broadcastMessage("2D maze generated...");
                        search2D.showAnimation(time);
                        cancel();
                    }
                }.runTaskTimer(plugin, time, 20L);

                time = 0;
            } else {
                getServer().broadcastMessage("Invalid maze - creating new simulation...");
            }
        }
    }

    /**
     * Create the mazes floor.
     */
    public void generateCore(Material core) {
        time = 5L;

        int count = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Location floor = new Location(block.getWorld(), block.getX() + i, block.getY(), block.getZ() + j);
                runnableDelayed(floor, time, core, -1, -1, -1);

                Location locationAboveFloor = new Location(block.getWorld(), block.getX() + i, block.getY() + 1, block.getZ() + j);
                runnableDelayed(locationAboveFloor, time, Material.AIR, i, j, 0);

                for (int k = 2; k < 4; k++) {
                    Location debris = new Location(block.getWorld(), block.getX() + i, block.getY() + k, block.getZ() + j);
                    runnableDelayed(debris, time, Material.AIR, -1, -1, -1);
                }

                if (count % 80 == 0) {
                    time += 5L;
                }
                count++;
            }
            if (i % 12 == 0) {
                time += 5L;
            }
        }

        time += 5L;
    }

    /**
     * Create the mazes start and end points.
     */
    public void generateStartAndEndPoints() {
        time += 5L;

        int randomStartX = startCoordinate[1];
        int randomStartY = startCoordinate[0];

        int randomEndX = endCoordinate[1];
        int randomEndY = endCoordinate[0];

        Location startPoint = new Location(block.getWorld(), block.getX() + randomStartX, block.getY() - 1, block.getZ() + randomStartY);
        runnableDelayed(startPoint, time, Material.BEACON, -1, -1, -1);
        startCoordinate[0] = randomStartY;
        startCoordinate[1] = randomStartX;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Location ironBlock = new Location(block.getWorld(), block.getX() + randomStartX + (i - 1), block.getY() - 2, block.getZ() + randomStartY + (j - 1));
                runnableDelayed(ironBlock, time, Material.IRON_BLOCK, -1, -1, -1);
            }
        }

        Location startPointGlass = new Location(block.getWorld(), block.getX() + randomStartX, block.getY(), block.getZ() + randomStartY);
        runnableDelayed(startPointGlass, time, Material.BLUE_STAINED_GLASS, -1, -1, -1);

        Location endPoint = new Location(block.getWorld(), block.getX() + randomEndX, block.getY() - 1, block.getZ() + randomEndY);
        runnableDelayed(endPoint, time, Material.BEACON, -1, -1, -1);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Location ironBlock = new Location(block.getWorld(), block.getX() + randomEndX + (i - 1), block.getY() - 2, block.getZ() + randomEndY + (j - 1));
                runnableDelayed(ironBlock, time, Material.IRON_BLOCK, -1, -1, -1);
            }
        }

        Location endPointGlass = new Location(block.getWorld(), block.getX() + randomEndX, block.getY(), block.getZ() + randomEndY);
        runnableDelayed(endPointGlass, time, Material.RED_STAINED_GLASS, -1, -1, -1);

        time += 5L;
    }

    /**
     * Generates blocked parts of the maze.
     *
     * @param maze            maze.
     * @param blockerMaterial maze walls.
     */
    public void generateBlockedAreas(int[][][] maze, Material blockerMaterial) {
        time += 5L;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (maze[i][j][0] == 1) {
                    Location mazeWall = new Location(block.getWorld(), block.getX() + i, block.getY() + 1, block.getZ() + j);
                    runnableDelayed(mazeWall, time, blockerMaterial, i, j, 0);

                    for (int k = 0; k < 2; k++) {
                        Location mazeWallAbove = new Location(block.getWorld(), block.getX() + i, block.getY() + 1 + k, block.getZ() + j);
                        runnableDelayed(mazeWallAbove, time, blockerMaterial, -1, -1, -1);
                    }
                }
            }
            if (i % (int) (size * .15) == 0) {
                time += 2L;
            }
        }

        time += 5L;
    }

    /**
     * Remove old start and end points.
     */
    public void clearOldBeacons() {
        time += 5L;

        for (int i = -1; i < size + 1; i++) {
            for (int j = -1; j < size + 1; j++) {
                for (int k = 1; k < 3; k++) {
                    Location location = new Location(block.getWorld(), block.getX() + i, block.getY() - k, block.getZ() + j);
                    if (location.getBlock().getType() != Material.AIR) {
                        runnableDelayed(location, time, Material.AIR, -1, -1, -1);
                    }
                }
            }
        }

        time += 5L;
    }
}
