package com.gubertmc.plugin.commands;

import com.gubertmc.MazeGeneratorPlugin;
import com.gubertmc.plugin.ControlPlatform;
import com.gubertmc.plugin.main.Maze;
import com.gubertmc.plugin.main.mazes.BreadthFirstSearchMaze2D;
import com.gubertmc.plugin.main.mazes.DepthFirstSearchMaze2D;
import com.gubertmc.plugin.main.mazes.PathfindingMaze2D;
import com.gubertmc.plugin.main.mazes.PathfindingMaze3D;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Objects;

public record MazeGeneratorCommand(MazeGeneratorPlugin plugin) implements CommandExecutor, Listener {

    private static Maze maze;
    private static ControlPlatform controlPlatform;

    /**
     * Handle the command call.
     * <p>
     * /astar Dimensions Size Percentage
     *
     * @param commandSender command sender.
     * @param command       command.
     * @param alias         alias.
     * @param args          args.
     * @return true.
     */
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String alias, String[] args) {
        Player player = (Player) commandSender;
        // /maze <algo> <size> <percentage>
        try {
            if (args.length > 0 && args.length < 4) {
                double percentage;
                Location location = player.getLocation();
                location = new Location(location.getWorld(), location.getX() + 1, location.getY(), location.getZ() + 1);
                if (args.length < 3) {
                    percentage = .2;
                } else {
                    percentage = Double.parseDouble(args[2]);
                    if (percentage < 0 || percentage > 1) {
                        percentage /= 100;
                    }
                    if (percentage > .80) {
                        player.sendMessage(ChatColor.AQUA + "The percentage of maze blockers must be less than 81%.");
                        player.sendMessage(ChatColor.AQUA + "Resetting percentage to 80%...");
                    }
                }
                if (args[0].equalsIgnoreCase("astar2d")) {
                    maze = new PathfindingMaze2D(
                            plugin,
                            location.getBlock(),
                            Integer.parseInt(args[1]),
                            percentage
                    );
                } else if (args[0].equalsIgnoreCase("astar3d")) {
                    maze = new PathfindingMaze3D(
                            plugin,
                            location.getBlock(),
                            Integer.parseInt(args[1]),
                            percentage
                    );
                } else if (args[0].equalsIgnoreCase("bfs2d")) {
                    maze = new BreadthFirstSearchMaze2D(
                            plugin,
                            location.getBlock(),
                            Integer.parseInt(args[1]),
                            percentage
                    );
                } else if (args[0].equalsIgnoreCase("dfs2d")) {
                    maze = new DepthFirstSearchMaze2D(
                            plugin,
                            location.getBlock(),
                            Integer.parseInt(args[1]),
                            percentage
                    );
                } else {
                    player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "/astar <Dimensions> <Size> <BlockedPercentage>");
                    player.sendMessage(ChatColor.YELLOW + "" + "Dimensions -> 2 or 3");
                    player.sendMessage(ChatColor.YELLOW + "" + "Size -> a positive integer");
                    player.sendMessage(ChatColor.YELLOW + "" + "BlockedPercentage -> 0 - 1");
                    return false;
                }

                player.sendMessage("Spawning new maze...");
                controlPlatform = new ControlPlatform(
                        plugin,
                        maze,
                        player.getLocation().getBlock(),
                        Integer.parseInt(args[1])
                );
                controlPlatform.spawn();
                return true;
            } else {
                player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "/astar <Dimensions> <Size> <BlockedPercentage>");
                player.sendMessage(ChatColor.YELLOW + "" + "Dimensions -> 2 or 3");
                player.sendMessage(ChatColor.YELLOW + "" + "Size -> a positive integer");
                player.sendMessage(ChatColor.YELLOW + "" + "BlockedPercentage -> 0 - 1");
            }
        } catch (Exception exception) {
            player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "/astar <Dimensions> <Size> <BlockedPercentage>");
            player.sendMessage(ChatColor.YELLOW + "" + "Dimensions -> 2 or 3");
            player.sendMessage(ChatColor.YELLOW + "" + "Size -> a positive integer");
            player.sendMessage(ChatColor.YELLOW + "" + "BlockedPercentage -> 0 - 1");
        }

        return false;
    }

    /**
     * Clear the old maze out and create a new one.
     *
     * @param e event
     */
    @EventHandler
    public void onButtonPressed(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (Objects.requireNonNull(e.getClickedBlock()).getType() == Material.WARPED_BUTTON) {
                maze.generateNewMaze(
                        controlPlatform.getCoreMaterial(),
                        controlPlatform.getBlockerMaterial(),
                        controlPlatform.getSpreadMaterial(),
                        controlPlatform.getPathMaterial(),
                        controlPlatform.getStartPointGlassMaterial(),
                        controlPlatform.getEndPointGlassMaterial()
                );
            }
        }
    }
}