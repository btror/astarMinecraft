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
                    floor.getBlock().setType(Material.COBBLESTONE);

                    // spots above the floor
                    Location loc = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + i, e.getBlock().getY() - 1, e.getBlock().getZ() + j);
                    loc.getBlock().setType(Material.AIR);
                    locations[i][j] = loc;

                    // clear debris above the arena
                    for (int k = 1; k < 10; k++) {
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

            // create start point
            int randomStartX = (int)(Math.random() * SIZE);
            int randomStartY = (int)(Math.random() * SIZE);
            Location startPoint = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + randomStartX, e.getBlock().getY() - 1, e.getBlock().getZ() + randomStartY);
            startPoint.getBlock().setType(Material.DIAMOND_BLOCK);
            startCoordinate[0] = randomStartY;
            startCoordinate[1] = randomStartX;

            // create end point
            int randomEndX = (int)(Math.random() * SIZE);
            int randomEndY = (int)(Math.random() * SIZE);
            while (randomStartX == randomEndX && randomStartY == randomEndY) {
                randomEndX = (int)(Math.random() * SIZE);
                randomEndY = (int)(Math.random() * SIZE);
            }
            Location endPoint = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + randomEndX, e.getBlock().getY() - 1, e.getBlock().getZ() + randomEndY);
            endPoint.getBlock().setType(Material.REDSTONE_BLOCK);
            endCoordinate[0] = randomEndY;
            endCoordinate[1] = randomEndX;

            // create random maze
            for (int i = 0 ; i < (SIZE * SIZE) * .30; i++) { // 30%
                int randomX = (int)(Math.random() * SIZE);
                int randomY = (int)(Math.random() * SIZE);
                while ((randomX == randomStartX && randomY == randomStartY) || (randomX == randomEndX && randomY == randomEndY)) {
                    randomX = (int)(Math.random() * SIZE);
                    randomY = (int)(Math.random() * SIZE);
                }
                Location newWall = new Location(e.getPlayer().getWorld(), e.getBlock().getX() + randomX, e.getBlock().getY() - 1, e.getBlock().getZ() + randomY);
                newWall.getBlock().setType(Material.DIRT);
                locations[randomX][randomY] = newWall;
            }

            // start search
            Search search = new Search(locations, startCoordinate, endCoordinate, SIZE);
            search.start();
        }
    }

}
