package com.minecraftmod.maze;

import com.minecraftmod.GenMazePlugin;
import com.minecraftmod.maze.algorithm.Search;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import static org.bukkit.Bukkit.getServer;

public class GenMazeCommand implements CommandExecutor, Listener {

    private boolean aStarEnabled = false;
    private int SIZE = 15;
    private double MAZE_WALL_PERCENTAGE = .40;
    private Location[][] locations;

    private final int[] startCoordinate = new int[2];
    private final int[] endCoordinate = new int[2];

    private final Material GROUND_MATERIAL = Material.GRASS_BLOCK;
    private final Material WALL_MATERIAL = Material.STONE_BRICKS;
    private final Material SIDE_WALLS = Material.IRON_BARS;
    private final Material PATH_MATERIAL = Material.SANDSTONE;
    private final Material PATH_SPREAD_MATERIAL = Material.REDSTONE_TORCH;
    private final Material START_POINT_MATERIAL = Material.BEACON;
    private final Material END_POINT_MATERIAL = Material.BEACON;
    private final GenMazePlugin plugin;

    public GenMazeCommand(GenMazePlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String alias, String[] args) {
        // Process the maze command.
        String difficulty = "MEDIUM";
        if (args.length > 0 && args.length < 3) {
            try {
                SIZE = Integer.parseInt(args[0]);
                if (args.length == 2) {
                    difficulty = args[1].toUpperCase();
                }
                getServer().broadcastMessage("Maze command initiated.");
                getServer().broadcastMessage("Break a block to spawn a " + args[0] + "x" + args[0] + " (" + difficulty.toUpperCase() + ") maze.");
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
            case "MEDIUM", "MODERATE" -> MAZE_WALL_PERCENTAGE = .40;
            case "HARD", "DIFFICULT" -> MAZE_WALL_PERCENTAGE = .60;
        }

        locations = new Location[SIZE][SIZE];
        aStarEnabled = true;
        return true;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (aStarEnabled) {
            getServer().broadcastMessage("Spawned " + SIZE + "x" + SIZE + " maze...");
            e.isCancelled();
            aStarEnabled = false;

            // Create the maze floor.
            generateArenaFloor(e);

            // Create walls around the entire maze.
            generateArenaWalls(e);

            // Create start and end points for the maze.
            int[] coordinates = generateArenaStartAndEndPoints(e);

            // Generate a random maze.
            generateRandomArenaMaze(e, coordinates[0], coordinates[1], coordinates[2], coordinates[3]);

            // Check for viable paths using A* pathfinding.
            Search search = new Search(this.plugin, locations, startCoordinate, endCoordinate, SIZE, WALL_MATERIAL, PATH_MATERIAL, PATH_SPREAD_MATERIAL);
            search.start();
        }
    }

    public void generateArenaFloor(BlockBreakEvent e) {
        // Create the arena floor.
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j <  SIZE; j++) {
                // Create the floor.
                Location floor = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + i, e.getBlock().getY() - 2, e.getBlock().getZ() + j);
                floor.getBlock().setType(GROUND_MATERIAL);

                // Store the location above the floor to the locations array.
                Location loc = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + i, e.getBlock().getY() - 1, e.getBlock().getZ() + j);
                loc.getBlock().setType(Material.AIR);
                locations[i][j] = loc;

                // Clear all debris from the floor up to the ceiling.
                for (int k = 1; k < 12; k++) {
                    Location locationAbove = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + i, e.getBlock().getY() - 2 + k, e.getBlock().getZ() + j);
                    locationAbove.getBlock().setType(Material.AIR);
                }

                // Create a glass ceiling to stand on.
//                Location top = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + i, e.getBlock().getY() + 10, e.getBlock().getZ() + j);
//                top.getBlock().setType(Material.GLASS);

                // Teleport command user on top of the glass.
                Location playerLocation = new Location(e.getPlayer().getWorld(), e.getBlock().getX(), e.getBlock().getY() + 11, e.getBlock().getZ());
                e.getPlayer().teleport(playerLocation);

            }
        }
    }

    public void generateArenaWalls(BlockBreakEvent e) {
        // Create the walls around the arena.
        for (int i = -1; i < SIZE + 1; i++) {
            for (int j = 0; j < 4; j++) {
                Location loc1 = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + i, e.getBlock().getY() - 2 + j, e.getBlock().getZ() - 1);
                loc1.getBlock().setType(WALL_MATERIAL);

                Location loc2 = new Location(e.getPlayer().getWorld(), e.getBlock().getX() - 1, e.getBlock().getY() - 2 + j, e.getBlock().getZ() + i);
                loc2.getBlock().setType(WALL_MATERIAL);

                Location loc3 = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + SIZE, e.getBlock().getY() - 2 + j, e.getBlock().getZ() + i);
                loc3.getBlock().setType(WALL_MATERIAL);

                Location loc4 = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + i, e.getBlock().getY() - 2 + j, e.getBlock().getZ() + SIZE);
                loc4.getBlock().setType(WALL_MATERIAL);
            }
            for (int j = 4; j < 9; j++) {
                Location loc1 = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + i, e.getBlock().getY() - 2 + j, e.getBlock().getZ() - 1);
                loc1.getBlock().setType(SIDE_WALLS);

                Location loc2 = new Location(e.getPlayer().getWorld(), e.getBlock().getX() - 1, e.getBlock().getY() - 2 + j, e.getBlock().getZ() + i);
                loc2.getBlock().setType(SIDE_WALLS);

                Location loc3 = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + SIZE, e.getBlock().getY() - 2 + j, e.getBlock().getZ() + i);
                loc3.getBlock().setType(SIDE_WALLS);

                Location loc4 = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + i, e.getBlock().getY() - 2 + j, e.getBlock().getZ() + SIZE);
                loc4.getBlock().setType(SIDE_WALLS);
            }
            for (int j = 9; j < 13; j++) {
                Location loc1 = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + i, e.getBlock().getY() - 2 + j, e.getBlock().getZ() - 1);
                loc1.getBlock().setType(WALL_MATERIAL);

                Location loc2 = new Location(e.getPlayer().getWorld(), e.getBlock().getX() - 1, e.getBlock().getY() - 2 + j, e.getBlock().getZ() + i);
                loc2.getBlock().setType(WALL_MATERIAL);

                Location loc3 = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + SIZE, e.getBlock().getY() - 2 + j, e.getBlock().getZ() + i);
                loc3.getBlock().setType(WALL_MATERIAL);

                Location loc4 = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + i, e.getBlock().getY() - 2 + j, e.getBlock().getZ() + SIZE);
                loc4.getBlock().setType(WALL_MATERIAL);
            }
        }
    }

    public int[] generateArenaStartAndEndPoints(BlockBreakEvent e) {
            // Create a random start point (x, y).
            int randomStartX = (int)(Math.random() * SIZE);
            int randomStartY = (int)(Math.random() * SIZE);

            // Create a random end point (x, y).
            int randomEndX = (int)(Math.random() * SIZE);
            int randomEndY = (int)(Math.random() * SIZE);

            // Reinitialize the start and end points if they have bad values.
            boolean badPositions = true;
            while (badPositions) {
                randomStartX = (int)(Math.random() * SIZE);
                randomStartY = (int)(Math.random() * SIZE);
                randomEndX = (int)(Math.random() * SIZE);
                randomEndY = (int)(Math.random() * SIZE);

                // Make sure the distance between the start and end points is large enough.
                int distance = (int) Math.sqrt(Math.pow(randomEndX - randomStartX, 2) + Math.pow(randomEndY - randomStartY, 2));
                if (SIZE / 1.3 < distance) {
                    badPositions = false;
                }
            }

            // Create the start point block.
            Location startPoint = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + randomStartX, e.getBlock().getY() - 3, e.getBlock().getZ() + randomStartY);
            startPoint.getBlock().setType(START_POINT_MATERIAL);
            startCoordinate[0] = randomStartY;
            startCoordinate[1] = randomStartX;

            // Put iron blocks under the start point to power the beacon.
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    Location ironBlock = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + randomStartX + (i - 1), e.getBlock().getY() - 4, e.getBlock().getZ() + randomStartY + (j - 1));
                    ironBlock.getBlock().setType(Material.IRON_BLOCK);
                }
            }

            // Make the start point beacon blue.
            Location glassBlock1 = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + randomStartX, e.getBlock().getY() - 2, e.getBlock().getZ() + randomStartY);
            glassBlock1.getBlock().setType(Material.BLUE_STAINED_GLASS);

            // Create the end point block.
            Location endPoint = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + randomEndX, e.getBlock().getY() - 3, e.getBlock().getZ() + randomEndY);
            endPoint.getBlock().setType(END_POINT_MATERIAL);
            endCoordinate[0] = randomEndY;
            endCoordinate[1] = randomEndX;

            // Put iron blocks under the end point to power the beacon.
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    Location ironBlock = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + randomEndX + (i - 1), e.getBlock().getY() - 4, e.getBlock().getZ() + randomEndY + (j - 1));
                    ironBlock.getBlock().setType(Material.IRON_BLOCK);
                }
            }

            // Make the end point beacon red.
            Location glassBlock2 = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + randomEndX, e.getBlock().getY() - 2, e.getBlock().getZ() + randomEndY);
            glassBlock2.getBlock().setType(Material.RED_STAINED_GLASS);

            // Store the start and end points to an array.
            int[] coordinates = new int[4];
            coordinates[0] = randomStartX;
            coordinates[1] = randomStartY;
            coordinates[2] = randomEndX;
            coordinates[3] = randomEndY;

            return coordinates;
    }

    public void generateRandomArenaMaze(BlockBreakEvent e, int startX, int startY, int endX, int endY) {
        // Generate the maze.
        for (int i = 0 ; i < (SIZE * SIZE) * MAZE_WALL_PERCENTAGE; i++) {
            int randomX = (int)(Math.random() * SIZE);
            int randomY = (int)(Math.random() * SIZE);
            while ((randomX == startX && randomY == startY) || (randomX == endX && randomY == endY)) {
                randomX = (int)(Math.random() * SIZE);
                randomY = (int)(Math.random() * SIZE);
            }

            // Create a new maze wall and store its location to an array.
            Location newWall1 = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + randomX, e.getBlock().getY() - 1, e.getBlock().getZ() + randomY);
            newWall1.getBlock().setType(WALL_MATERIAL);
            locations[randomX][randomY] = newWall1;

            // Make the maze walls taller.
            for (int j = 0; j < 2; j++) {
                Location newWallAbove = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + randomX, e.getBlock().getY() + j, e.getBlock().getZ() + randomY);
                newWallAbove.getBlock().setType(WALL_MATERIAL);
            }
        }
    }
}
