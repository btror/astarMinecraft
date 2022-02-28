package com.minecraftmod.astar;

import com.minecraftmod.astar.algorithm.Search;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import static org.bukkit.Bukkit.getServer;

public class AstarCommand implements CommandExecutor, Listener {

    private boolean astarEnabled = false;
    private int SIZE = 15;
    private Location[][] locations;
    private final int[] startCoordinate = new int[2];
    private final int[] endCoordinate = new int[2];
    private final Material GROUND_MATERIAL = Material.GRASS_BLOCK;
    private final Material WALL_MATERIAL = Material.STONE_BRICKS;
    private final Material PATH_MATERIAL = Material.DIRT_PATH;
    private final Material PATH_SPREAD_MATERIAL = Material.REDSTONE_TORCH;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String alias, String[] args) {
        if (args.length > 0) {
            getServer().broadcastMessage("astar command initiated");
            getServer().broadcastMessage("arena size is " + args[0] + "x" + args[0]);
            SIZE = Integer.parseInt(args[0]);
        } else {
            getServer().broadcastMessage("astar command initiated");
            getServer().broadcastMessage("arena size is 15x15");
            SIZE = 15;
        }

        locations = new Location[SIZE][SIZE];
        astarEnabled = true;
        return true;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (astarEnabled) {
            getServer().broadcastMessage("block break");
            e.isCancelled();
            astarEnabled = false;

            // create an arena below the player
            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j <  SIZE; j++) {
                    // spawn floor
                    Location floor = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + i, e.getBlock().getY() - 2, e.getBlock().getZ() + j);
                    floor.getBlock().setType(GROUND_MATERIAL);

                    // spots above the floor
                    Location loc = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + i, e.getBlock().getY() - 1, e.getBlock().getZ() + j);
                    loc.getBlock().setType(Material.AIR);
                    locations[i][j] = loc;

                    // clear debris above the arena
                    for (int k = 1; k < 12; k++) {
                        Location locationAbove = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + i, e.getBlock().getY() - 2 + k, e.getBlock().getZ() + j);
                        locationAbove.getBlock().setType(Material.AIR);
                    }

                    // create a glass ceiling to stand on
                    Location top = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + i, e.getBlock().getY() + 10, e.getBlock().getZ() + j);
                    top.getBlock().setType(Material.GLASS);

                    // teleport player on top of glass
                    Location playerLocation = new Location(e.getPlayer().getWorld(), e.getBlock().getX(), e.getBlock().getY() + 11, e.getBlock().getZ());
                    e.getPlayer().teleport(playerLocation);

                }
            }

            // walls around the maze
            for (int i = -1; i < SIZE + 1; i++) {
                // side 1
                for (int j = 0; j < 4; j++) {
                    Location loc = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + i, e.getBlock().getY() - 2 + j, e.getBlock().getZ() - 1);
                    loc.getBlock().setType(WALL_MATERIAL);
                }
                for (int j = 4; j < 9; j++) {
                    Location loc = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + i, e.getBlock().getY() - 2 + j, e.getBlock().getZ() - 1);
                    loc.getBlock().setType(Material.IRON_BARS);
                }
                for (int j = 9; j < 13; j++) {
                    Location loc = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + i, e.getBlock().getY() - 2 + j, e.getBlock().getZ() - 1);
                    loc.getBlock().setType(WALL_MATERIAL);
                }

                // side 2
                for (int j = 0; j < 4; j++) {
                    Location loc = new Location(e.getPlayer().getWorld(), e.getBlock().getX() - 1, e.getBlock().getY() - 2 + j, e.getBlock().getZ() + i);
                    loc.getBlock().setType(WALL_MATERIAL);
                }
                for (int j = 4; j < 9; j++) {
                    Location loc = new Location(e.getPlayer().getWorld(), e.getBlock().getX() - 1, e.getBlock().getY() - 2 + j, e.getBlock().getZ() + i);
                    loc.getBlock().setType(Material.IRON_BARS);
                }
                for (int j = 9; j < 13; j++) {
                    Location loc = new Location(e.getPlayer().getWorld(), e.getBlock().getX() - 1, e.getBlock().getY() - 2 + j, e.getBlock().getZ() + i);
                    loc.getBlock().setType(WALL_MATERIAL);
                }

                // side 3
                for (int j = 0; j < 4; j++) {
                    Location loc = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + SIZE, e.getBlock().getY() - 2 + j, e.getBlock().getZ() + i);
                    loc.getBlock().setType(WALL_MATERIAL);
                }
                for (int j = 4; j < 9; j++) {
                    Location loc = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + SIZE, e.getBlock().getY() - 2 + j, e.getBlock().getZ() + i);
                    loc.getBlock().setType(Material.IRON_BARS);
                }
                for (int j = 9; j < 13; j++) {
                    Location loc = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + SIZE, e.getBlock().getY() - 2 + j, e.getBlock().getZ() + i);
                    loc.getBlock().setType(WALL_MATERIAL);
                }

                // side 4
                for (int j = 0; j < 4; j++) {
                    Location loc = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + i, e.getBlock().getY() - 2 + j, e.getBlock().getZ() + SIZE);
                    loc.getBlock().setType(WALL_MATERIAL);
                }
                for (int j = 4; j < 9; j++) {
                    Location loc = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + i, e.getBlock().getY() - 2 + j, e.getBlock().getZ() + SIZE);
                    loc.getBlock().setType(Material.IRON_BARS);
                }
                for (int j = 9; j < 13; j++) {
                    Location loc = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + i, e.getBlock().getY() - 2 + j, e.getBlock().getZ() + SIZE);
                    loc.getBlock().setType(WALL_MATERIAL);
                }
            }

            // create start point
            int randomStartX = (int)(Math.random() * SIZE);
            int randomStartY = (int)(Math.random() * SIZE);

            // create end point
            int randomEndX = (int)(Math.random() * SIZE);
            int randomEndY = (int)(Math.random() * SIZE);
            boolean badPositions = true;
            while (badPositions) {
                randomStartX = (int)(Math.random() * SIZE);
                randomStartY = (int)(Math.random() * SIZE);
                randomEndX = (int)(Math.random() * SIZE);
                randomEndY = (int)(Math.random() * SIZE);
                // make sure the distance is greater than half of SIZE
                int distance = (int) Math.sqrt(Math.pow(randomEndX - randomStartX, 2) + Math.pow(randomEndY - randomStartY, 2));
                if (SIZE / 1.3 < distance) {
                    badPositions = false;
                }
            }

            Location startPoint = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + randomStartX, e.getBlock().getY() - 3, e.getBlock().getZ() + randomStartY);
            startPoint.getBlock().setType(Material.BEACON);
            startCoordinate[0] = randomStartY;
            startCoordinate[1] = randomStartX;
            // put iron under the beacon
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    Location ironBlock = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + randomStartX + (i - 1), e.getBlock().getY() - 4, e.getBlock().getZ() + randomStartY + (j - 1));
                    ironBlock.getBlock().setType(Material.IRON_BLOCK);
                }
            }
            // make the beam blue
            Location glassBlock1 = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + randomStartX, e.getBlock().getY() - 2, e.getBlock().getZ() + randomStartY);
            glassBlock1.getBlock().setType(Material.BLUE_STAINED_GLASS);

            Location endPoint = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + randomEndX, e.getBlock().getY() - 3, e.getBlock().getZ() + randomEndY);
            endPoint.getBlock().setType(Material.BEACON);
            endCoordinate[0] = randomEndY;
            endCoordinate[1] = randomEndX;
            // put iron under the beacon
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    Location ironBlock = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + randomEndX + (i - 1), e.getBlock().getY() - 4, e.getBlock().getZ() + randomEndY + (j - 1));
                    ironBlock.getBlock().setType(Material.IRON_BLOCK);
                }
            }

            // make the beam red
            Location glassBlock2 = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + randomEndX, e.getBlock().getY() - 2, e.getBlock().getZ() + randomEndY);
            glassBlock2.getBlock().setType(Material.RED_STAINED_GLASS);


            // create random maze
            for (int i = 0 ; i < (SIZE * SIZE) * .40; i++) { // 40%
                int randomX = (int)(Math.random() * SIZE);
                int randomY = (int)(Math.random() * SIZE);
                while ((randomX == randomStartX && randomY == randomStartY) || (randomX == randomEndX && randomY == randomEndY)) {
                    randomX = (int)(Math.random() * SIZE);
                    randomY = (int)(Math.random() * SIZE);
                }
                Location newWall1 = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + randomX, e.getBlock().getY() - 1, e.getBlock().getZ() + randomY);
                newWall1.getBlock().setType(WALL_MATERIAL);
                locations[randomX][randomY] = newWall1;

                // add height to the maze walls
                for (int j = 0; j < 2; j++) {
                    Location newWallAbove = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + randomX, e.getBlock().getY() + j, e.getBlock().getZ() + randomY);
                    newWallAbove.getBlock().setType(WALL_MATERIAL);
                }
            }

            // start search
            Search search = new Search(locations, startCoordinate, endCoordinate, SIZE, WALL_MATERIAL, PATH_MATERIAL, PATH_SPREAD_MATERIAL);
            search.start();
        }
    }

}
