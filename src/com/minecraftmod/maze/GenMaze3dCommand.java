package com.minecraftmod.maze;

import com.minecraftmod.GenMazePlugin;
import com.minecraftmod.maze.astar3d.Search3D;
import com.minecraftmod.maze.astar3d.SearchSimulation3D;
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

public class GenMaze3dCommand implements CommandExecutor, Listener {

    private boolean aStarEnabled = false;
    private int SIZE = 15;
    private double MAZE_WALL_PERCENTAGE = .40;
    private Location[][][] locations;
    private int[][][] maze;
    private final String[] difficulties = new String[6];
    private boolean validMaze;

    private final int[] startCoordinate = new int[3];
    private final int[] endCoordinate = new int[3];

    private final Material GROUND_MATERIAL = Material.LIGHT_BLUE_STAINED_GLASS;
    private final Material WALL_MATERIAL = Material.BLACK_STAINED_GLASS;
    private final Material SIDE_WALLS = Material.IRON_BARS;
    private final Material PATH_MATERIAL = Material.BLUE_WOOL;
    private final Material PATH_SPREAD_MATERIAL = Material.RED_STAINED_GLASS;
    private final Material START_POINT_MATERIAL = Material.BEACON;
    private final Material END_POINT_MATERIAL = Material.BEACON;

    private long time = 0;
    private final GenMazePlugin plugin;

    public GenMaze3dCommand(GenMazePlugin plugin) {
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
                GenMaze3dCommand.this.maze = generateSimulationMaze();
                SearchSimulation3D searchSimulation = new SearchSimulation3D(maze, startCoordinate, endCoordinate);
                validMaze = searchSimulation.start();

                if (validMaze) {
                    generateRandomArenaMaze(e);

                    time += 10L;
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Search3D search = new Search3D(plugin, locations, startCoordinate, endCoordinate, SIZE, WALL_MATERIAL, PATH_MATERIAL, PATH_SPREAD_MATERIAL);
                            validMaze = search.start();
                            getServer().broadcastMessage("Maze generated.");
                            search.showAnimation(time);
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
     *
     * 0 - empty space
     * 1 - wall
     * 2 -
     * 3 -
     * 4 - start
     * 5 - end
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
        int randomStartZ = (int) (Math.random() * SIZE);

        int randomEndX = (int) (Math.random() * SIZE);
        int randomEndY = (int) (Math.random() * SIZE);
        int randomEndZ = (int) (Math.random() * SIZE);

        boolean badPositions = true;
        while (badPositions) {
            randomStartX = (int) (Math.random() * SIZE);
            randomStartY = (int) (Math.random() * SIZE);
            randomStartZ = (int) (Math.random() * SIZE);
            randomEndX = (int) (Math.random() * SIZE);
            randomEndY = (int) (Math.random() * SIZE);
            randomEndZ = (int) (Math.random() * SIZE);

            int distance = (int) Math.sqrt(Math.pow(randomEndX - randomStartX, 2) + Math.pow(randomEndY - randomStartY, 2) + Math.pow(randomEndZ - randomStartZ, 2));
            if (SIZE / 1.3 < distance) {
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
        int randomX = (int) (Math.random() * SIZE);
        int randomY = (int) (Math.random() * SIZE);
        int randomZ = (int) (Math.random() * SIZE);
        for (int i = 0; i < (SIZE * SIZE * SIZE) * MAZE_WALL_PERCENTAGE; i++) {
            if (k % 2 == 0) {
                randomX = (int) (Math.random() * SIZE);
                randomY = (int) (Math.random() * SIZE);
                randomZ = (int) (Math.random() * SIZE);
            } else {
                int random = (int) (Math.random() * 3);
                if (random == 0) {
                    if (randomX < SIZE - 1 && randomX + 1 != randomStartX && randomX + 1 != randomStartY) {
                        randomX++;
                    }
                } else if (random == 1) {
                    if (randomY < SIZE - 1 && randomY + 1 != randomStartX && randomY + 1 != randomStartY) {
                        randomY++;
                    }
                } else {
                    if (randomZ < SIZE - 1 && randomZ + 1 != randomStartZ && randomZ + 1 != randomStartZ) {
                        randomZ++;
                    }
                }
            }
            while ((randomX == randomStartX && randomY == randomStartY && randomZ == randomStartZ) || (randomX == randomEndX && randomY == randomEndY && randomZ == randomEndZ)) {
                randomX = (int) (Math.random() * SIZE);
                randomY = (int) (Math.random() * SIZE);
                randomZ = (int) (Math.random() * SIZE);
            }
            maze[randomX][randomY][randomZ] = 1;
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
                for (int k = 0; k < SIZE; k++) {
                    Location floor = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + i, e.getBlock().getY() + k, e.getBlock().getZ() + j);
                    runnableDelayed(floor, time, GROUND_MATERIAL, i, j, k);
                    if (count % 80 == 0) { // was 50
                        time += 5L;
                    }
                    count++;
                }
            }
            if (i % 12 == 0) { // was 25
                time += 20L;
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
        int randomStartZ = startCoordinate[2];

        int randomEndX = endCoordinate[1];
        int randomEndY = endCoordinate[0];
        int randomEndZ = endCoordinate[2];

        Location startPoint = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + randomStartX, e.getBlock().getY() + randomStartZ, e.getBlock().getZ() + randomStartY);
        runnableDelayed(startPoint, time, START_POINT_MATERIAL, -1, -1, -1);
        startCoordinate[0] = randomStartY;
        startCoordinate[1] = randomStartX;
        startCoordinate[2] = randomStartZ;

        Location endPoint = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + randomEndX, e.getBlock().getY() + randomEndZ, e.getBlock().getZ() + randomEndY);
        runnableDelayed(endPoint, time, END_POINT_MATERIAL, -1, -1, -1);
        endCoordinate[0] = randomEndY;
        endCoordinate[1] = randomEndX;
        endCoordinate[2] = randomEndZ;

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
                for (int k = 0; k < SIZE; k++) {
                    if (maze[i][j][k] == 1) {
                        Location mazeWall = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + i, e.getBlock().getY() + k, e.getBlock().getZ() + j);
                        runnableDelayed(mazeWall, time, WALL_MATERIAL, i, j, k);
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
        // clearDebrisAboveArena(e);
        // generateArenaWalls(e);
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
    public void runnableDelayed(Location loc, long time, Material material, int i, int j, int k) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (i < 0 || j < 0 || i > SIZE || j > SIZE || k < 0 || k > SIZE) {
                    loc.getBlock().setType(material);
                    cancel();
                } else {
                    loc.getBlock().setType(material);
                    locations[i][j][k] = loc;
                    cancel();
                }
            }
        }.runTaskTimer(this.plugin, time, 20L);
    }
}