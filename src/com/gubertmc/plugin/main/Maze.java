package com.gubertmc.plugin.main;

import com.gubertmc.MazeGeneratorPlugin;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class Maze {

    private final int size;
    private final double wallPercentage;
    private final int[] startCoordinate;
    private final int[] endCoordinate;
    private final MazeGeneratorPlugin plugin;
    private final Block mazeLocationBlock;
    private final Location[][][] mazeBlockLocations;


    public Maze(MazeGeneratorPlugin plugin, Block mazeLocationBlock, int size, double wallPercentage) {
        this.plugin = plugin;
        this.mazeLocationBlock = mazeLocationBlock;
        this.size = size;
        this.wallPercentage = wallPercentage;
        this.mazeBlockLocations = new Location[size][size][size];
        this.startCoordinate = new int[3];
        this.endCoordinate = new int[3];
    }

    /**
     * Generate simulation maze.
     */
    public abstract int[][][] generateSimulation();

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
    public abstract void generateCore(Material coreMaterial, long time);

    /**
     * Generate the maze start and end points.
     *
     * @param startPointGlassMaterial material over the start coordinate (preferably glass).
     * @param endPointGlassMaterial   material over the end coordinate (preferably glass).
     */
    public abstract void generateStartAndEndPoints(Material startPointGlassMaterial, Material endPointGlassMaterial);

    /**
     * Generate the maze walls/blockers.
     *
     * @param simulationMaze  integer maze.
     * @param blockerMaterial wall/blocker material.
     */
    public abstract void generateBlockedAreas(int[][][] simulationMaze, Material blockerMaterial);

//    /**
//     * Generate the maze border.
//     * 2D is the wall around the maze.
//     * 3D is the shell around the volume.
//     *
//     * @param coreMaterial floor material for 2D | volume material for 3D.
//     */
//    public abstract void generateBorder(Material coreMaterial);

    /**
     * Handle the animations.
     *
     * @param location location of a block.
     * @param time     when to start the block placement animation.
     * @param material type of block to use.
     * @param row      location row.
     * @param col      location column.
     * @param zNum     location z.
     */
    public void runnableDelayed(Location location, long time, Material material, int row, int col, int zNum) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (row < 0 || row > size || col < 0 || col > size || zNum < 0 || zNum > size) {
                    location.getBlock().setType(material);
                    cancel();
                } else {
                    location.getBlock().setType(material);
                    mazeBlockLocations[row][col][zNum] = location;
                    cancel();
                }
            }
        }.runTaskTimer(plugin, time, 20L);
    }

    /**
     * Getter: size.
     *
     * @return size of the maze.
     */
    public int getSize() {
        return size;
    }

    /**
     * Getter: plugin.
     *
     * @return plugin object.
     */
    public MazeGeneratorPlugin getPlugin() {
        return plugin;
    }

    /**
     * Getter: maze location block.
     *
     * @return location of where the maze originally spawns.
     */
    public Block getMazeLocationBlock() {
        return mazeLocationBlock;
    }

    /**
     * Getter: maze block locations.
     *
     * @return locations of blocks in the maze.
     */
    public Location[][][] getMazeBlockLocations() {
        return mazeBlockLocations;
    }

    /**
     * Getter: get maze start coordinate.
     *
     * @return start coordinate in which the animation starts.
     */
    public int[] getStartCoordinate() {
        return startCoordinate;
    }

    /**
     * Getter: get maze end coordinate.
     *
     * @return end coordinate in which the animation ends.
     */
    public int[] getEndCoordinate() {
        return endCoordinate;
    }

    /**
     * Getter: get maze blocker percentage.
     *
     * @return percentage of the maze containing blockers (walls).
     */
    public double getBlockerPercentage() {
        return wallPercentage;
    }
}
