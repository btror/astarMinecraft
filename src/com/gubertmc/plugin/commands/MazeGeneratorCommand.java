package com.gubertmc.plugin.commands;

import com.gubertmc.MazeGeneratorPlugin;
import com.gubertmc.plugin.ControlPlatform;
import com.gubertmc.plugin.main.Maze;
import com.gubertmc.plugin.main.mazes.custom.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ItemFrame;
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
        try {
            if (args.length > 0 && args.length < 4) {
                double percentage;

                Location location = player.getLocation();
                location = new Location(
                        location.getWorld(),
                        location.getX() + 1,
                        location.getY(),
                        location.getZ() + 1
                );

                if (args.length < 3) {
                    percentage = .2;
                } else {
                    percentage = Double.parseDouble(args[2]);

                    if (percentage < 0 || percentage > 1) {
                        percentage /= 100;
                    }
                    if (percentage > .80) {
                        player.sendMessage(
                                ChatColor.AQUA + "The percentage of maze blockers must be less than 81%."
                        );
                        player.sendMessage(ChatColor.AQUA + "Resetting percentage to 80%...");
                    }
                }

                switch (args[0].toUpperCase()) {
                    case "ASTAR2D" -> maze = new PathfindingMaze2D(
                            plugin,
                            location.getBlock(),
                            Integer.parseInt(args[1]),
                            percentage
                    );
                    case "ASTAR3D" -> maze = new PathfindingMaze3D(
                            plugin,
                            location.getBlock(),
                            Integer.parseInt(args[1]),
                            percentage
                    );
                    case "BFS2D" -> maze = new BreadthFirstSearchMaze2D(
                            plugin,
                            location.getBlock(),
                            Integer.parseInt(args[1]),
                            percentage
                    );
                    case "BFS3D" -> maze = new BreadthFirstSearchMaze3D(
                            plugin,
                            location.getBlock(),
                            Integer.parseInt(args[1]),
                            percentage
                    );
                    case "DFS2D" -> maze = new DepthFirstSearchMaze2D(
                            plugin,
                            location.getBlock(),
                            Integer.parseInt(args[1]),
                            percentage
                    );
                    case "DFS3D" -> maze = new DepthFirstSearchMaze3D(
                            plugin,
                            location.getBlock(),
                            Integer.parseInt(args[1]),
                            percentage
                    );
                    default -> {
                        player.sendMessage(
                                ChatColor.RED + "" + ChatColor.BOLD + "/maze <algorithm> <size> <percentage>"
                        );
                        player.sendMessage(
                                ChatColor.YELLOW + "" + "algorithm -> astar2d, astar3d, bfs2d, bfs3d, dfs2d, dfs3d"
                        );
                        player.sendMessage(ChatColor.YELLOW + "" + "size -> a positive integer");
                        player.sendMessage(ChatColor.YELLOW + "" + "percentage -> 0-1 or 1-100");

                        return false;
                    }
                }
                player.sendMessage("Spawning control platform...");

                controlPlatform = new ControlPlatform(
                        player.getLocation().getBlock()
                );
                controlPlatform.spawn();

                return true;
            } else {
                player.sendMessage(
                        ChatColor.RED + "" + ChatColor.BOLD + "/maze <algorithm> <size> <percentage>"
                );
                player.sendMessage(
                        ChatColor.YELLOW + "" + "algorithm -> astar2d, astar3d, bfs2d, bfs3d, dfs2d, dfs3d"
                );
                player.sendMessage(ChatColor.YELLOW + "" + "size -> a positive integer");
                player.sendMessage(ChatColor.YELLOW + "" + "percentage -> 0-1 or 1-100");
            }
        } catch (Exception exception) {
            player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "/maze <algorithm> <size> <percentage>");
            player.sendMessage(ChatColor.YELLOW + "" + "algorithm -> astar2d, astar3d, bfs2d, bfs3d, dfs2d, dfs3d");
            player.sendMessage(ChatColor.YELLOW + "" + "size -> a positive integer");
            player.sendMessage(ChatColor.YELLOW + "" + "percentage -> 0-1 or 1-100");
        }

        return false;
    }

    /**
     * Clear the old maze out and create a new one.
     *
     * @param e PlayerInteractEvent
     */
    @EventHandler
    public void onButtonPressed(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (Objects.equals(e.getClickedBlock(), controlPlatform.getStartButton())) {
                boolean acceptableFrames = true;
                ItemFrame[] frames = controlPlatform.getFrames();

                for (ItemFrame item : frames) {
                    if (!item.getItem().getType().isBlock()) {
                        acceptableFrames = false;
                    }
                }
                if (acceptableFrames) {
                    controlPlatform.setFrames(frames);
                    maze.generateNewMaze(
                            controlPlatform.getCoreMaterial(),
                            controlPlatform.getBlockerMaterial(),
                            controlPlatform.getSpreadMaterial(),
                            controlPlatform.getPathMaterial(),
                            controlPlatform.getStartPointGlassMaterial(),
                            controlPlatform.getEndPointGlassMaterial()
                    );
                } else {
                    e.getPlayer().sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "Maze items must be blocks...");
                }
            }
        }
    }
}
