package com.gubertmc.maze.commands;

import com.gubertmc.MazeGeneratorPlugin;
import com.gubertmc.maze.astar.algorithm2d.Search2D;
import com.gubertmc.maze.astar.algorithm2d.SearchSimulation2D;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

import static org.bukkit.Bukkit.getServer;

public class Pathfinding2dCommand implements CommandExecutor, Listener {

    private boolean aStarEnabled = false;
    private int SIZE = 15;
    private double MAZE_WALL_PERCENTAGE = .40;
    private Location[][][] locations;
    private int[][][] maze;
    private final String[] difficulties = new String[6];
    private boolean validMaze;

    private final int[] startCoordinate = new int[3];
    private final int[] endCoordinate = new int[3];

    private final Material GROUND_MATERIAL = Material.GRASS_BLOCK;
    private final Material WALL_MATERIAL = Material.STONE_BRICKS;
    private final Material SIDE_WALLS = Material.IRON_BARS;
    private final Material PATH_MATERIAL = Material.BLUE_WOOL;
    private final Material PATH_SPREAD_MATERIAL = Material.SANDSTONE;
    private final Material START_POINT_MATERIAL = Material.BEACON;
    private final Material END_POINT_MATERIAL = Material.BEACON;

    private long time = 0;
    private final MazeGeneratorPlugin plugin;

    public Pathfinding2dCommand(MazeGeneratorPlugin plugin) {
        this.plugin = plugin;
        difficulties[0] = "EASY";
        difficulties[1] = "SIMPLE";
        difficulties[2] = "MEDIUM";
        difficulties[3] = "MODERATE";
        difficulties[4] = "HARD";
        difficulties[5] = "DIFFICULT";
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String alias, String[] args) {
        String difficulty = "MEDIUM";
        if (args.length > 0 && args.length < 3) {
            try {
                SIZE = Integer.parseInt(args[0]);
                if (args.length == 2) {
                    difficulty = args[1].toUpperCase();
                    if (Arrays.asList(difficulties).contains(difficulty)) {
                        getServer().broadcastMessage("Maze command initiated.");
                        getServer().broadcastMessage("Break a block to spawn a " + args[0] + "x" + args[0] + " (" + difficulty.toUpperCase() + ") maze.");
                    } else {
                        getServer().broadcastMessage("Invalid syntax. Proper syntax is /genmaze <width> <difficulty>");
                        getServer().broadcastMessage("<width> is required and must be an integer.");
                        getServer().broadcastMessage("<difficulty> is optional and must be \"easy\", \"medium\", or \"hard\".");
                    }
                } else {
                    getServer().broadcastMessage("Maze command initiated.");
                    getServer().broadcastMessage("Break a block to spawn a " + args[0] + "x" + args[0] + " (" + difficulty.toUpperCase() + ") maze.");
                }
            } catch (NumberFormatException e) {
                getServer().broadcastMessage("Invalid syntax. Proper syntax is /genmaze <width> <difficulty>");
                getServer().broadcastMessage("<width> is required and must be an integer.");
                getServer().broadcastMessage("<difficulty> is optional and must be \"easy\", \"medium\", or \"hard\".");
            }
        } else {
            getServer().broadcastMessage("Invalid syntax. Proper syntax is /genmaze <width> <difficulty>");
            getServer().broadcastMessage("<width> is required and must be an integer.");
            getServer().broadcastMessage("<difficulty> is optional and must be \"easy\", \"medium\", or \"hard\".");
        }

        switch (difficulty) {
            case "EASY", "SIMPLE" -> MAZE_WALL_PERCENTAGE = .20;
            case "MEDIUM", "MODERATE" -> MAZE_WALL_PERCENTAGE = .35;
            case "HARD", "DIFFICULT" -> MAZE_WALL_PERCENTAGE = .50;
        }

        locations = new Location[SIZE][SIZE][SIZE];
        aStarEnabled = true;
        time = 0;
        return true;
    }

    /**
     * Generate a maze where a block was broken after initiating the genmaze command.
     *
     * @param e event
     */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (aStarEnabled) {
            getServer().broadcastMessage("Spawned " + SIZE + "x" + SIZE + " maze...");
            e.isCancelled();
            aStarEnabled = false;
            validMaze = false;

            int i = 0;
            while (!validMaze) {
                Pathfinding2dCommand.this.maze = generateSimulationMaze();
                SearchSimulation2D searchSimulation2D = new SearchSimulation2D(maze, startCoordinate, endCoordinate);
                validMaze = searchSimulation2D.start();

                if (validMaze) {
                    generateRandomArenaMaze(e);

                    time += 10L;
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Search2D search2D = new Search2D(plugin, locations, startCoordinate, endCoordinate, SIZE, WALL_MATERIAL, PATH_MATERIAL, PATH_SPREAD_MATERIAL, GROUND_MATERIAL);
                            validMaze = search2D.start();
                            getServer().broadcastMessage("Maze generated.");
                            search2D.showAnimation(time);
                            cancel();
                        }
                    }.runTaskTimer(this.plugin, time, 20L);

                    time = 0;
                } else {
                    getServer().broadcastMessage("Invalid maze - retrying..." + i);
                    i++;
                }
            }
        }
    }

    /**
     * Generate a simulation maze of integers.
     *
     * @return a maze of integers where a path from start to finish exists.
     */
    public int[][][] generateSimulationMaze() {

        int[][][] maze = new int[SIZE][SIZE][SIZE];

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                for (int k = 0; k < SIZE; k++) {
                    maze[i][j][k] = 0;
                }
            }
        }

        int randomStartX = (int) (Math.random() * SIZE);
        int randomStartY = (int) (Math.random() * SIZE);

        int randomEndX = (int) (Math.random() * SIZE);
        int randomEndY = (int) (Math.random() * SIZE);

        boolean badPositions = true;
        while (badPositions) {
            randomStartX = (int) (Math.random() * SIZE);
            randomStartY = (int) (Math.random() * SIZE);
            randomEndX = (int) (Math.random() * SIZE);
            randomEndY = (int) (Math.random() * SIZE);

            int distance = (int) Math.sqrt(Math.pow(randomEndX - randomStartX, 2) + Math.pow(randomEndY - randomStartY, 2));
            if (SIZE / 1.3 < distance) {
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
        int randomX = (int) (Math.random() * SIZE);
        int randomY = (int) (Math.random() * SIZE);
        for (int i = 0; i < (SIZE * SIZE) * MAZE_WALL_PERCENTAGE; i++) {
            if (k % 2 == 0) {
                randomX = (int) (Math.random() * SIZE);
                randomY = (int) (Math.random() * SIZE);
            } else {
                int random = (int) (Math.random() * 2);
                if (random == 0) {
                    if (randomX < SIZE - 1 && randomX + 1 != randomStartX && randomX + 1 != randomStartY) {
                        randomX++;
                    }
                } else {
                    if (randomY < SIZE - 1 && randomY + 1 != randomStartX && randomY + 1 != randomStartY) {
                        randomY++;
                    }
                }
            }
            while ((randomX == randomStartX && randomY == randomStartY) || (randomX == randomEndX && randomY == randomEndY)) {
                randomX = (int) (Math.random() * SIZE);
                randomY = (int) (Math.random() * SIZE);
            }
            maze[randomX][randomY][0] = 1;
            k++;
        }

        return maze;
    }

    /**
     * Generate the floor of the maze.
     *
     * @param e event
     */
    public void generateArenaFloor(BlockBreakEvent e) {
        time += 10L;
        int count = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                Location floor = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + i, e.getBlock().getY(), e.getBlock().getZ() + j);
                runnableDelayed(floor, time, GROUND_MATERIAL, -1, -1);

                Location locationAboveFloor = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + i, e.getBlock().getY() + 1, e.getBlock().getZ() + j);
                runnableDelayed(locationAboveFloor, time, Material.AIR, i, j);

                if (count % 80 == 0) { // was 50
                    time += 5L;
                }
                count++;
            }
            if (i % 12 == 0) { // was 25
                time += 20L;
            }
        }
        time += 10L;
    }

    /**
     * Clear the debris above the floor.
     *
     * @param e event
     */
    public void clearDebrisAboveArena(BlockBreakEvent e) {
        time += 10L;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                for (int k = 1; k < 8; k++) {
                    Location debris = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + i, e.getBlock().getY() + k, e.getBlock().getZ() + j);
                    if (debris.getBlock().getType() != Material.AIR) {
                        runnableDelayed(debris, time, Material.AIR, -1, -1);
                        if (j % 50 == 0) {
                            time += 5L;
                        }
                    }
                }
            }
            if (i % 25 == 0) {
                time += 10L;
            }
        }
        time += 10L;
    }

    /**
     * Generate the walls that go around the entire maze.
     *
     * @param e event
     */
    public void generateArenaWalls(BlockBreakEvent e) {
        time += 10L;
        for (int i = -1; i < SIZE + 1; i++) {
            for (int j = 0; j < 3; j++) {
                Location side1 = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + i, e.getBlock().getY() + j, e.getBlock().getZ() - 1);
                runnableDelayed(side1, time, WALL_MATERIAL, -1, -1);

                Location side2 = new Location(e.getPlayer().getWorld(), e.getBlock().getX() - 1, e.getBlock().getY() + j, e.getBlock().getZ() + i);
                runnableDelayed(side2, time, WALL_MATERIAL, -1, -1);

                Location side3 = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + SIZE, e.getBlock().getY() + j, e.getBlock().getZ() + i);
                runnableDelayed(side3, time, WALL_MATERIAL, -1, -1);

                Location side4 = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + i, e.getBlock().getY() + j, e.getBlock().getZ() + SIZE);
                runnableDelayed(side4, time, WALL_MATERIAL, -1, -1);
            }
            if (i > -1 && i % (int)(SIZE * .15) == 0) {
                time += 2L;
            }
        }
        time += 10L;
    }

    /**
     * Generate maze start and end points.
     *
     * @param e event
     */
    public void generateArenaStartAndEndPoints(BlockBreakEvent e) {
        time += 10L;
        int randomStartX = startCoordinate[1];
        int randomStartY = startCoordinate[0];

        int randomEndX = endCoordinate[1];
        int randomEndY = endCoordinate[0];

        Location startPoint = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + randomStartX, e.getBlock().getY() - 1, e.getBlock().getZ() + randomStartY);
        startPoint.getBlock().setType(START_POINT_MATERIAL);
        startCoordinate[0] = randomStartY;
        startCoordinate[1] = randomStartX;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Location ironBlock = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + randomStartX + (i - 1), e.getBlock().getY() - 2, e.getBlock().getZ() + randomStartY + (j - 1));
                runnableDelayed(ironBlock, time, Material.IRON_BLOCK, -1, -1);
            }
        }

        Location startPointGlass = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + randomStartX, e.getBlock().getY(), e.getBlock().getZ() + randomStartY);
        runnableDelayed(startPointGlass, time, Material.BLUE_STAINED_GLASS, -1, -1);

        Location endPoint = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + randomEndX, e.getBlock().getY() - 1, e.getBlock().getZ() + randomEndY);
        runnableDelayed(endPoint, time, END_POINT_MATERIAL, -1, -1);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                Location ironBlock = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + randomEndX + (i - 1), e.getBlock().getY() - 2, e.getBlock().getZ() + randomEndY + (j - 1));
                runnableDelayed(ironBlock, time, Material.IRON_BLOCK, -1, -1);
            }
        }

        Location endPointGlass = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + randomEndX, e.getBlock().getY(), e.getBlock().getZ() + randomEndY);
        runnableDelayed(endPointGlass, time, Material.RED_STAINED_GLASS, -1, -1);

        time += 10L;
    }

    /**
     * Generate the maze within the arena.
     *
     * @param e event
     */
    public void generateMazeWalls(BlockBreakEvent e) {
        time += 10L;
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (maze[i][j][0] == 1) {
                    Location mazeWall = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + i, e.getBlock().getY() + 1, e.getBlock().getZ() + j);
                    runnableDelayed(mazeWall, time, WALL_MATERIAL, i, j);

                    for (int k = 0; k < 2; k++) {
                        Location mazeWallAbove = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + i, e.getBlock().getY() + 1 + k, e.getBlock().getZ() + j);
                        runnableDelayed(mazeWallAbove, time, WALL_MATERIAL, -1, -1);
                    }
                }
            }
            if (i % (int)(SIZE * .15) == 0) {
                time += 2L;
            }
        }
        time += 10L;
    }

    /**
     * Generate the entire maze. Put all the parts together.
     *
     * @param e event
     */
    public void generateRandomArenaMaze(BlockBreakEvent e) {
        generateArenaFloor(e);
        clearDebrisAboveArena(e);
        generateArenaWalls(e);
        generateArenaStartAndEndPoints(e);
        generateMazeWalls(e);
    }

    /**
     * Animate the maze.
     *
     * @param loc location of a block.
     * @param time when to place a block.
     * @param material the type of block.
     * @param i row of the block.
     * @param j column of the block.
     */
    public void runnableDelayed(Location loc, long time, Material material, int i, int j) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (i < 0 || j < 0 || i > SIZE || j > SIZE) {
                    loc.getBlock().setType(material);
                    cancel();
                } else {
                    loc.getBlock().setType(material);
                    locations[i][j][0] = loc;
                    cancel();
                }
            }
        }.runTaskTimer(this.plugin, time, 20L);
    }
}
