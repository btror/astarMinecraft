package com.gubertmc.plugin.mazes;

import com.gubertmc.MazeGeneratorPlugin;
import com.gubertmc.plugin.Maze;
import com.gubertmc.plugin.algorithms.Simulation;
import com.gubertmc.plugin.algorithms.astar3d.Search3D;
import com.gubertmc.plugin.algorithms.astar3d.Simulation3D;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import static org.bukkit.Bukkit.getServer;

public class Maze3D extends Maze {

    private long time;
    private boolean isValid;

    public Maze3D(MazeGeneratorPlugin plugin, Block block, int size) {
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
        int randomStartZ = (int) (Math.random() * size);

        int randomEndX = (int) (Math.random() * size);
        int randomEndY = (int) (Math.random() * size);
        int randomEndZ = (int) (Math.random() * size);

        boolean badPositions = true;
        while (badPositions) {
            randomStartX = (int) (Math.random() * size);
            randomStartY = (int) (Math.random() * size);
            randomStartZ = (int) (Math.random() * size);
            randomEndX = (int) (Math.random() * size);
            randomEndY = (int) (Math.random() * size);
            randomEndZ = (int) (Math.random() * size);

            int distance = (int) Math.sqrt(Math.pow(randomEndX - randomStartX, 2) + Math.pow(randomEndY - randomStartY, 2) + Math.pow(randomEndZ - randomStartZ, 2));
            if (size / 1.3 < distance) {
                badPositions = false;
            }
        }

        maze[randomStartX][randomStartY][randomStartZ] = 4;
        maze[randomEndX][randomEndY][randomEndZ] = 5;

        startCoordinate[0] = randomStartY;
        startCoordinate[1] = randomStartX;
        startCoordinate[2] = randomStartZ;

        endCoordinate[0] = randomEndY;
        endCoordinate[1] = randomEndX;
        endCoordinate[2] = randomEndZ;

        int k = 0;
        int randomX = (int) (Math.random() * size);
        int randomY = (int) (Math.random() * size);
        int randomZ = (int) (Math.random() * size);
        for (int i = 0; i < (size * size * size) * wallPercentage; i++) {
            if (k % 2 == 0) {
                randomX = (int) (Math.random() * size);
                randomY = (int) (Math.random() * size);
                randomZ = (int) (Math.random() * size);
            } else {
                int random = (int) (Math.random() * 3);
                if (random == 0) {
                    if (randomX < size - 1 && randomX + 1 != randomStartX && randomX + 1 != randomStartY) {
                        randomX++;
                    }
                } else if (random == 1) {
                    if (randomY < size - 1 && randomY + 1 != randomStartX && randomY + 1 != randomStartY) {
                        randomY++;
                    }
                } else {
                    if (randomZ < size - 1 && randomZ + 1 != randomStartZ && randomZ + 1 != randomStartZ) {
                        randomZ++;
                    }
                }
            }
            while ((randomX == randomStartX && randomY == randomStartY && randomZ == randomStartZ) || (randomX == randomEndX && randomY == randomEndY && randomZ == randomEndZ)) {
                randomX = (int) (Math.random() * size);
                randomY = (int) (Math.random() * size);
                randomZ = (int) (Math.random() * size);
            }
            maze[randomX][randomY][randomZ] = 1;
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
            Simulation simulation = new Simulation3D(simulationMaze, startCoordinate, endCoordinate);
            isValid = simulation.start();

            if (isValid) {
                generateStartAndEndPoints();
                generateBlockedAreas(simulationMaze, blockerMaterial);

                time += 5L;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Search3D search3D = new Search3D(plugin, locations, startCoordinate, endCoordinate, size, blockerMaterial, pathMaterial, spreadMaterial, coreMaterial, startPointGlassMaterial, endPointGlassMaterial);
                        isValid = search3D.start();
                        getServer().broadcastMessage("3D maze generated...");
                        search3D.showAnimation(time);
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
     * Create the mazes volume.
     */
    public void generateCore(Material core) {
        time += 5L;

        int count = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    Location floor = new Location(block.getWorld(), block.getX() + i, block.getY() + k, block.getZ() + j);
                    if (floor.getBlock().getType() == core) {
                        locations[i][j][k] = floor;
                    } else {
                        runnableDelayed(floor, time, core, i, j, k);
                    }
                    if (count % 90 == 0) {
                        time += 1L;
                    }
                    count++;
                }
            }
            if (i % 12 == 0) {
                time += 15L;
            }
        }

        time += 5L;
    }

    /**
     * Create the maze start and end points.
     */
    public void generateStartAndEndPoints() {
        time += 5L;

        int randomStartX = startCoordinate[1];
        int randomStartY = startCoordinate[0];
        int randomStartZ = startCoordinate[2];

        int randomEndX = endCoordinate[1];
        int randomEndY = endCoordinate[0];
        int randomEndZ = endCoordinate[2];

        Location startPoint = new Location(block.getWorld(), block.getX() + randomStartX, block.getY() + randomStartZ, block.getZ() + randomStartY);
        runnableDelayed(startPoint, time, Material.BEACON, -1, -1, -1);
        startCoordinate[0] = randomStartY;
        startCoordinate[1] = randomStartX;
        startCoordinate[2] = randomStartZ;

        Location endPoint = new Location(block.getWorld(), block.getX() + randomEndX, block.getY() + randomEndZ, block.getZ() + randomEndY);
        runnableDelayed(endPoint, time, Material.BEACON, -1, -1, -1);
        endCoordinate[0] = randomEndY;
        endCoordinate[1] = randomEndX;
        endCoordinate[2] = randomEndZ;
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
                for (int k = 0; k < size; k++) {
                    if (maze[i][j][k] == 1) {
                        Location mazeWall = new Location(block.getWorld(), block.getX() + i, block.getY() + k, block.getZ() + j);
                        runnableDelayed(mazeWall, time, blockerMaterial, i, j, k);
                    }
                }
            }
            if (i % (int) (size * .15) == 0) {
                time += 2L;
            }
        }

        time += 5L;
    }
}
