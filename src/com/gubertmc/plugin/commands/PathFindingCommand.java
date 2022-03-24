package com.gubertmc.plugin.commands;

import com.gubertmc.MazeGeneratorPlugin;
import com.gubertmc.plugin.ControlPlatform;
import com.gubertmc.plugin.Maze;
import com.gubertmc.plugin.mazes.Maze2D;
import com.gubertmc.plugin.mazes.Maze3D;
import org.bukkit.ChatColor;
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

public record PathFindingCommand(MazeGeneratorPlugin plugin) implements CommandExecutor, Listener {

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

        player.sendMessage(String.valueOf(args.length));
        if (args.length > 0 && args.length < 4) {
            if (args[0].equalsIgnoreCase("2")) {
                maze = new Maze2D(
                        plugin,
                        player.getLocation().getBlock(),
                        Integer.parseInt(args[1]),
                        Double.parseDouble(args[2])
                );
            } else if (args[0].equalsIgnoreCase("3")) {
                maze = new Maze3D(
                        plugin,
                        player.getLocation().getBlock(),
                        Integer.parseInt(args[1]),
                        Double.parseDouble(args[2])
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
                    Integer.parseInt(args[0])
            );
            controlPlatform.spawn();

            return true;
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
                maze.generateCore(controlPlatform.getCoreMaterial());
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