package com.gubertmc.plugin.commands;

import com.gubertmc.MazeGeneratorPlugin;
import com.gubertmc.plugin.main.snakegame.Arena;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public record SnakeGameCommand(MazeGeneratorPlugin plugin) implements CommandExecutor, Listener {

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
            Location location = player.getLocation();
            location = new Location(
                    location.getWorld(),
                    location.getX() + 1,
                    location.getY(),
                    location.getZ() + 1
            );

            Arena arena = new Arena(plugin, location.getBlock(), 20);

            // start game
            arena.startGame();
            // end game

            player.sendMessage("Spawning control platform...");
            return true;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "/snake");
        }
        return false;
    }
}
