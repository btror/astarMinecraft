package com.gubertmc.plugin.main.mazes;

import com.gubertmc.MazeGeneratorPlugin;
import com.gubertmc.plugin.main.Maze;
import com.gubertmc.plugin.main.algorithms.Animation;
import com.gubertmc.plugin.main.algorithms.Simulation;
import com.gubertmc.plugin.main.algorithms.dfs.dfs2d.DepthFirstSearchAnimation2D;
import com.gubertmc.plugin.main.algorithms.dfs.dfs2d.DepthFirstSearchSimulation2D;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import static org.bukkit.Bukkit.getServer;

public class DepthFirstSearchMaze2D extends Maze {

    private long time;
    private boolean isValid;

    public DepthFirstSearchMaze2D(MazeGeneratorPlugin plugin, Block block, int size, double wallPercentage) {
        super(plugin, block, size, wallPercentage);
    }

    /**
     * Generate a simulation maze of integers.
     *
     * @return simulation maze.
     */
    @Override
    public int[][][] generateSimulation() {
        int[][][] maze = new int[getSize()][getSize()][getSize()];

        for (int i = 0; i < getSize(); i++) {
            for (int j = 0; j < getSize(); j++) {
                for (int k = 0; k < getSize(); k++) {
                    maze[i][j][k] = 0;
                }
            }
        }

        int randomStartX = (int) (Math.random() * getSize());
        int randomStartY = (int) (Math.random() * getSize());

        int randomEndX = (int) (Math.random() * getSize());
        int randomEndY = (int) (Math.random() * getSize());

        boolean badPositions = true;
        while (badPositions) {
            randomStartX = (int) (Math.random() * getSize());
            randomStartY = (int) (Math.random() * getSize());
            randomEndX = (int) (Math.random() * getSize());
            randomEndY = (int) (Math.random() * getSize());

            int distance = (int) Math.sqrt(Math.pow(randomEndX - randomStartX, 2)
                    + Math.pow(randomEndY - randomStartY, 2));
            if (getSize() / 1.3 < distance) {
                badPositions = false;
            }
        }

        maze[randomStartX][randomStartY][0] = 4;
        maze[randomEndX][randomEndY][0] = 5;

        getStartCoordinate()[0] = randomStartY;
        getStartCoordinate()[1] = randomStartX;
        getStartCoordinate()[2] = 0;

        getEndCoordinate()[0] = randomEndY;
        getEndCoordinate()[1] = randomEndX;
        getEndCoordinate()[2] = 0;

        int k = 0;
        int randomX = (int) (Math.random() * getSize());
        int randomY = (int) (Math.random() * getSize());
        for (int i = 0; i < (getSize() * getSize()) * getBlockerPercentage(); i++) {
            if (k % 2 == 0) {
                randomX = (int) (Math.random() * getSize());
                randomY = (int) (Math.random() * getSize());
            } else {
                int random = (int) (Math.random() * 2);
                if (random == 0) {
                    if (randomX < getSize() - 1 && randomX + 1 != randomStartX && randomX + 1 != randomStartY) {
                        randomX++;
                    }
                } else {
                    if (randomY < getSize() - 1 && randomY + 1 != randomStartX && randomY + 1 != randomStartY) {
                        randomY++;
                    }
                }
            }
            while ((randomX == randomStartX && randomY == randomStartY)
                    || (randomX == randomEndX && randomY == randomEndY)) {
                randomX = (int) (Math.random() * getSize());
                randomY = (int) (Math.random() * getSize());
            }
            maze[randomX][randomY][0] = 1;
            k++;
        }

        return maze;
    }

    /**
     * Generate a new maze.
     *
     * @param coreMaterial            floor material for 2D | volume material for 3D.
     * @param blockerMaterial         wall/blocker material.
     * @param spreadMaterial          spread animation material.
     * @param pathMaterial            final path animation material (astar only).
     * @param startPointGlassMaterial material over the start coordinate (preferably glass).
     * @param endPointGlassMaterial   material over the end coordinate (preferably glass).
     */
    @Override
    public void generateNewMaze(
            Material coreMaterial,
            Material blockerMaterial,
            Material spreadMaterial,
            Material pathMaterial,
            Material startPointGlassMaterial,
            Material endPointGlassMaterial
    ) {
        isValid = false;
        int count = 0;
        while (!isValid) {
            int[][][] simulationMaze = generateSimulation();
            Simulation simulation = new DepthFirstSearchSimulation2D(
                    simulationMaze, getStartCoordinate(), getEndCoordinate()
            );
            simulation.setup();
            isValid = simulation.start();

            if (isValid) {
                time = 0L;
                generateCore(coreMaterial);
                generateBorder(coreMaterial);
                clearOldBeacons();
                generateStartAndEndPoints(startPointGlassMaterial, endPointGlassMaterial);
                generateBlockedAreas(simulationMaze, blockerMaterial);

                time += 5L;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Animation animation = new DepthFirstSearchAnimation2D(
                                getPlugin(), getMazeBlockLocations(),
                                getStartCoordinate(),
                                getEndCoordinate(),
                                getSize(),
                                blockerMaterial,
                                pathMaterial,
                                spreadMaterial,
                                coreMaterial,
                                startPointGlassMaterial,
                                endPointGlassMaterial
                        );
                        animation.setup();
                        isValid = animation.start();
                        getServer().broadcastMessage(ChatColor.GREEN + "" + getSize() + "x" + getSize() +
                                " maze generated..."
                        );
                        animation.showAnimation(time);
                        cancel();
                    }
                }.runTaskTimer(getPlugin(), time, 20L);

                time = 0;
            } else {
                count++;
                System.out.println("Invalid maze - starting new breadthFirstSearchSimulation2D...");
            }
            if (count == 50) {
                getServer().broadcastMessage(ChatColor.RED + "A maze could not be successfully generated " +
                        "within 50 simulations. You may experience server lag. Creating a larger maze with a " +
                        "lower percentage of walls/blockers will greatly help and put less stress on the server."
                );
            }
        }
    }

    /**
     * Generate the core maze.
     * 2D is the floor and border walls.
     * 3D is the volume and border shell.
     *
     * @param coreMaterial floor material for 2D | volume material for 3D.
     */
    @Override
    public void generateCore(Material coreMaterial) {
        time = 1L;

        int count = 0;
        for (int i = 0; i < getSize(); i++) {
            for (int j = 0; j < getSize(); j++) {
                Location floor = new Location(
                        getMazeLocationBlock().getWorld(),
                        getMazeLocationBlock().getX() + i,
                        getMazeLocationBlock().getY(),
                        getMazeLocationBlock().getZ() + j
                );
                runnableDelayed(floor, time, coreMaterial, -1, -1, -1);

                Location locationAboveFloor = new Location(
                        getMazeLocationBlock().getWorld(),
                        getMazeLocationBlock().getX() + i,
                        getMazeLocationBlock().getY() + 1,
                        getMazeLocationBlock().getZ() + j
                );
                runnableDelayed(locationAboveFloor, time, Material.AIR, i, j, 0);

                for (int k = 2; k < 4; k++) {
                    Location debris = new Location(
                            getMazeLocationBlock().getWorld(),
                            getMazeLocationBlock().getX() + i,
                            getMazeLocationBlock().getY() + k,
                            getMazeLocationBlock().getZ() + j
                    );
                    runnableDelayed(debris, time, Material.AIR, -1, -1, -1);
                }

                if (count % 110 == 0) {
                    time += 5L;
                }
                count++;
            }
            if (i % 12 == 0) {
                time += 5L;
            }
        }

        time += 1L;
    }

    /**
     * Generate the maze start and end points.
     *
     * @param startPointGlassMaterial material over the start coordinate (preferably glass).
     * @param endPointGlassMaterial   material over the end coordinate (preferably glass).
     */
    @Override
    public void generateStartAndEndPoints(Material startPointGlassMaterial, Material endPointGlassMaterial) {
        time += 5L;

        int randomStartX = getStartCoordinate()[1];
        int randomStartY = getStartCoordinate()[0];

        int randomEndX = getEndCoordinate()[1];
        int randomEndY = getEndCoordinate()[0];

        Location startPoint = new Location(
                getMazeLocationBlock().getWorld(),
                getMazeLocationBlock().getX() + randomStartX,
                getMazeLocationBlock().getY() - 1,
                getMazeLocationBlock().getZ() + randomStartY
        );
        runnableDelayed(startPoint, time, Material.BEACON, -1, -1, -1);
        getStartCoordinate()[0] = randomStartY;
        getStartCoordinate()[1] = randomStartX;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Location ironBlock = new Location(
                        getMazeLocationBlock().getWorld(),
                        getMazeLocationBlock().getX() + randomStartX + (i - 1),
                        getMazeLocationBlock().getY() - 2,
                        getMazeLocationBlock().getZ() + randomStartY + (j - 1)
                );
                runnableDelayed(ironBlock, time, Material.IRON_BLOCK, -1, -1, -1);
            }
        }

        Location startPointGlass = new Location(
                getMazeLocationBlock().getWorld(),
                getMazeLocationBlock().getX() + randomStartX,
                getMazeLocationBlock().getY(),
                getMazeLocationBlock().getZ() + randomStartY
        );
        runnableDelayed(startPointGlass, time, startPointGlassMaterial, -1, -1, -1);

        Location endPoint = new Location(
                getMazeLocationBlock().getWorld(),
                getMazeLocationBlock().getX() + randomEndX,
                getMazeLocationBlock().getY() - 1,
                getMazeLocationBlock().getZ() + randomEndY
        );
        runnableDelayed(endPoint, time, Material.BEACON, -1, -1, -1);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Location ironBlock = new Location(
                        getMazeLocationBlock().getWorld(),
                        getMazeLocationBlock().getX() + randomEndX + (i - 1),
                        getMazeLocationBlock().getY() - 2,
                        getMazeLocationBlock().getZ() + randomEndY + (j - 1)
                );
                runnableDelayed(ironBlock, time, Material.IRON_BLOCK, -1, -1, -1);
            }
        }

        Location endPointGlass = new Location(
                getMazeLocationBlock().getWorld(),
                getMazeLocationBlock().getX() + randomEndX,
                getMazeLocationBlock().getY(),
                getMazeLocationBlock().getZ() + randomEndY
        );
        runnableDelayed(endPointGlass, time, endPointGlassMaterial, -1, -1, -1);

        time += 5L;
    }

    /**
     * Generate blocked parts of the maze.
     *
     * @param maze            maze.
     * @param blockerMaterial maze walls.
     */
    @Override
    public void generateBlockedAreas(int[][][] maze, Material blockerMaterial) {
        time += 1L;

        for (int i = 0; i < getSize(); i++) {
            for (int j = 0; j < getSize(); j++) {
                if (maze[i][j][0] == 1) {
                    Location mazeWall = new Location(
                            getMazeLocationBlock().getWorld(),
                            getMazeLocationBlock().getX() + i,
                            getMazeLocationBlock().getY() + 1,
                            getMazeLocationBlock().getZ() + j
                    );
                    runnableDelayed(mazeWall, time, blockerMaterial, i, j, 0);

                    for (int k = 0; k < 2; k++) {
                        Location mazeWallAbove = new Location(
                                getMazeLocationBlock().getWorld(),
                                getMazeLocationBlock().getX() + i,
                                getMazeLocationBlock().getY() + 1 + k,
                                getMazeLocationBlock().getZ() + j
                        );
                        runnableDelayed(mazeWallAbove, time, blockerMaterial, -1, -1, -1);
                    }
                }
            }
            if (i % (int) (getSize() * .15) == 0) {
                time += 2L;
            }
        }

        time += 1L;
    }

    /**
     * Generate border around the maze.
     *
     * @param coreMaterial ground material.
     */
    @Override
    public void generateBorder(Material coreMaterial) {
        time += 1L;

        for (int i = -1; i < getSize() + 1; i++) {
            for (int j = -1; j < getSize() + 1; j++) {
                if (i == -1) {
                    for (int k = 0; k < 3; k++) {
                        Location border = new Location(
                                getMazeLocationBlock().getWorld(),
                                getMazeLocationBlock().getX() + i,
                                getMazeLocationBlock().getY() + k,
                                getMazeLocationBlock().getZ() + j
                        );
                        if (border.getBlock().getType() != coreMaterial) {
                            runnableDelayed(border, time, coreMaterial, -1, -1, -1);
                        }
                    }
                }
                if (i == getSize()) {
                    for (int k = 0; k < 3; k++) {
                        Location border = new Location(
                                getMazeLocationBlock().getWorld(),
                                getMazeLocationBlock().getX() + i,
                                getMazeLocationBlock().getY() + k,
                                getMazeLocationBlock().getZ() + j
                        );
                        if (border.getBlock().getType() != coreMaterial) {
                            runnableDelayed(border, time, coreMaterial, -1, -1, -1);
                        }
                    }
                }
                if (j == -1) {
                    for (int k = 0; k < 3; k++) {
                        Location border = new Location(
                                getMazeLocationBlock().getWorld(),
                                getMazeLocationBlock().getX() + i,
                                getMazeLocationBlock().getY() + k,
                                getMazeLocationBlock().getZ() + j
                        );
                        if (border.getBlock().getType() != coreMaterial) {
                            runnableDelayed(border, time, coreMaterial, -1, -1, -1);
                        }
                    }
                }
                if (j == getSize()) {
                    for (int k = 0; k < 3; k++) {
                        Location border = new Location(
                                getMazeLocationBlock().getWorld(),
                                getMazeLocationBlock().getX() + i,
                                getMazeLocationBlock().getY() + k,
                                getMazeLocationBlock().getZ() + j
                        );
                        if (border.getBlock().getType() != coreMaterial) {
                            runnableDelayed(border, time, coreMaterial, -1, -1, -1);
                        }
                    }
                }
            }
            if (i > 0) {
                if (i % (int) (getSize() * .15) == 0) {
                    time += 2L;
                }
            }
        }

        time += 1L;
    }

    /**
     * Remove old start and end points.
     */
    public void clearOldBeacons() {
        time += 2L;

        for (int i = -1; i < getSize() + 1; i++) {
            for (int j = -1; j < getSize() + 1; j++) {
                for (int k = 1; k < 3; k++) {
                    Location location = new Location(
                            getMazeLocationBlock().getWorld(),
                            getMazeLocationBlock().getX() + i,
                            getMazeLocationBlock().getY() - k,
                            getMazeLocationBlock().getZ() + j
                    );
                    if (location.getBlock().getType() != Material.AIR) {
                        runnableDelayed(location, time, Material.AIR, -1, -1, -1);
                    }
                }
            }
        }

        time += 2L;
    }
}
