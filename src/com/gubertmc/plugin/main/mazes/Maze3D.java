package com.gubertmc.plugin.main.mazes;

import com.gubertmc.MazeGeneratorPlugin;
import com.gubertmc.plugin.main.Maze;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public abstract class Maze3D extends Maze {

    private long time;
    private boolean isValid;

    public Maze3D(MazeGeneratorPlugin plugin, Block block, int size, double wallPercentage) {
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
        int randomStartZ = (int) (Math.random() * getSize());

        int randomEndX = (int) (Math.random() * getSize());
        int randomEndY = (int) (Math.random() * getSize());
        int randomEndZ = (int) (Math.random() * getSize());

        boolean badPositions = true;
        while (badPositions) {
            randomStartX = (int) (Math.random() * getSize());
            randomStartY = (int) (Math.random() * getSize());
            randomStartZ = (int) (Math.random() * getSize());
            randomEndX = (int) (Math.random() * getSize());
            randomEndY = (int) (Math.random() * getSize());
            randomEndZ = (int) (Math.random() * getSize());

            int distance = (int) Math.sqrt(Math.pow(randomEndX - randomStartX, 2)
                    + Math.pow(randomEndY - randomStartY, 2) + Math.pow(randomEndZ - randomStartZ, 2));
            if (getSize() / 1.3 < distance) {
                badPositions = false;
            }
        }

        maze[randomStartX][randomStartY][randomStartZ] = 4;
        maze[randomEndX][randomEndY][randomEndZ] = 5;

        getStartCoordinate()[0] = randomStartY;
        getStartCoordinate()[1] = randomStartX;
        getStartCoordinate()[2] = randomStartZ;

        getEndCoordinate()[0] = randomEndY;
        getEndCoordinate()[1] = randomEndX;
        getEndCoordinate()[2] = randomEndZ;

        int k = 0;
        int randomX = (int) (Math.random() * getSize());
        int randomY = (int) (Math.random() * getSize());
        int randomZ = (int) (Math.random() * getSize());
        for (int i = 0; i < (getSize() * getSize() * getSize()) * getBlockerPercentage(); i++) {
            if (k % 2 == 0) {
                randomX = (int) (Math.random() * getSize());
                randomY = (int) (Math.random() * getSize());
                randomZ = (int) (Math.random() * getSize());
            } else {
                int random = (int) (Math.random() * 3);
                if (random == 0) {
                    if (randomX < getSize() - 1 && randomX + 1 != randomStartX && randomX + 1 != randomStartY) {
                        randomX++;
                    }
                } else if (random == 1) {
                    if (randomY < getSize() - 1 && randomY + 1 != randomStartX && randomY + 1 != randomStartY) {
                        randomY++;
                    }
                } else {
                    if (randomZ < getSize() - 1 && randomZ + 1 != randomStartZ && randomZ + 1 != randomStartZ) {
                        randomZ++;
                    }
                }
            }
            while ((randomX == randomStartX && randomY == randomStartY && randomZ == randomStartZ)
                    || (randomX == randomEndX && randomY == randomEndY && randomZ == randomEndZ)) {
                randomX = (int) (Math.random() * getSize());
                randomY = (int) (Math.random() * getSize());
                randomZ = (int) (Math.random() * getSize());
            }
            maze[randomX][randomY][randomZ] = 1;
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
    public abstract void generateNewMaze(
            Material coreMaterial,
            Material blockerMaterial,
            Material spreadMaterial,
            Material pathMaterial,
            Material startPointGlassMaterial,
            Material endPointGlassMaterial,
            long time
    );

    /**
     * Generate the core maze.
     * 2D is the floor and border walls.
     * 3D is the volume and border shell.
     *
     * @param coreMaterial floor material for 2D | volume material for 3D.
     */
    @Override
    public void generateCore(Material coreMaterial, long adjustedTime) {
        time = adjustedTime + 1L;

        int count = 0;
        for (int i = 0; i < getSize(); i++) {
            for (int j = 0; j < getSize(); j++) {
                for (int k = 0; k < getSize(); k++) {
                    Location floor = new Location(
                            getMazeLocationBlock().getWorld(),
                            getMazeLocationBlock().getX() + i,
                            getMazeLocationBlock().getY() + k,
                            getMazeLocationBlock().getZ() + j
                    );
                    if (floor.getBlock().getType() == coreMaterial) {
                        getMazeBlockLocations()[i][j][k] = floor;
                    } else {
                        runnableDelayed(floor, time, coreMaterial, i, j, k);
                    }
                    if (count % 110 == 0) {
                        time += 1L;
                    }
                    count++;
                }
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
        time += 15L;

        int randomStartX = getStartCoordinate()[1];
        int randomStartY = getStartCoordinate()[0];
        int randomStartZ = getStartCoordinate()[2];

        int randomEndX = getEndCoordinate()[1];
        int randomEndY = getEndCoordinate()[0];
        int randomEndZ = getEndCoordinate()[2];

        Location startPoint = new Location(
                getMazeLocationBlock().getWorld(),
                getMazeLocationBlock().getX() + randomStartX,
                getMazeLocationBlock().getY() + randomStartZ,
                getMazeLocationBlock().getZ() + randomStartY
        );
        runnableDelayed(startPoint, time, Material.BEACON, -1, -1, -1);
        getStartCoordinate()[0] = randomStartY;
        getStartCoordinate()[1] = randomStartX;
        getStartCoordinate()[2] = randomStartZ;

        Location endPoint = new Location(
                getMazeLocationBlock().getWorld(),
                getMazeLocationBlock().getX() + randomEndX,
                getMazeLocationBlock().getY() + randomEndZ,
                getMazeLocationBlock().getZ() + randomEndY
        );
        runnableDelayed(endPoint, time, Material.BEACON, -1, -1, -1);
        getEndCoordinate()[0] = randomEndY;
        getEndCoordinate()[1] = randomEndX;
        getEndCoordinate()[2] = randomEndZ;
    }

    /**
     * Generate blocked parts of the maze.
     *
     * @param simulationMaze  maze.
     * @param blockerMaterial maze walls.
     */
    @Override
    public void generateBlockedAreas(int[][][] simulationMaze, Material blockerMaterial) {
        time += 1L;

        for (int i = 0; i < getSize(); i++) {
            for (int j = 0; j < getSize(); j++) {
                for (int k = 0; k < getSize(); k++) {
                    if (simulationMaze[i][j][k] == 1) {
                        Location mazeWall = new Location(
                                getMazeLocationBlock().getWorld(),
                                getMazeLocationBlock().getX() + i,
                                getMazeLocationBlock().getY() + k,
                                getMazeLocationBlock().getZ() + j
                        );
                        runnableDelayed(mazeWall, time, blockerMaterial, i, j, k);
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
                for (int k = -1; k < getSize() + 1; k++) {
                    if (i == -1) {
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
                    if (i == getSize()) {
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
                    if (j == -1) {
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
                    if (j == getSize()) {
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
                    if (k == -1) {
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
                    if (k == getSize()) {
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
        }

        time += 1L;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }
}
