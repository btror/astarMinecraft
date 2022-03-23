package com.gubertmc.plugin.commands;

import com.gubertmc.MazeGeneratorPlugin;
import com.gubertmc.plugin.ControlPlatform;
import com.gubertmc.plugin.Maze;
import com.gubertmc.plugin.mazes.Maze2D;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import static org.bukkit.Bukkit.getServer;

public class Pathfinding2dCommand implements CommandExecutor, Listener {

    private final MazeGeneratorPlugin plugin;

    public Pathfinding2dCommand(MazeGeneratorPlugin plugin) {
        this.plugin = plugin;
    }

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
        player.sendMessage("2D mase spawned...");

        int size = Integer.parseInt(args[0]);
        Maze maze = new Maze2D(plugin, player.getLocation().getBlock(), size);

        getServer().getPluginManager().registerEvents(new ControlPlatform(plugin, maze, player.getLocation().getBlock(), size), plugin);

        ControlPlatform controlPlatform = new ControlPlatform(plugin, maze, player.getLocation().getBlock(), size);
        controlPlatform.spawn();

        return true;
    }
}