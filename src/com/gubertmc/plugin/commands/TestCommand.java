package com.gubertmc.plugin.commands;

import com.gubertmc.MazeGeneratorPlugin;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

import static org.bukkit.Bukkit.getServer;

public class TestCommand implements CommandExecutor, Listener  {

    private boolean commandEnabled = false;
    private final MazeGeneratorPlugin plugin;

    public TestCommand(MazeGeneratorPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        getServer().broadcastMessage("Test command called.");
        commandEnabled = true;
        return true;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        if (commandEnabled) {
            long time = 0;
            for (int i = 0; i < 20; i++) {
                Location loc = new Location(e.getPlayer().getWorld(), e.getBlock().getX(), e.getBlock().getY() + i, e.getBlock().getZ());
                runnableDelayed(loc, time);
                time += 1L;
            }
            commandEnabled = false;
        }
    }

    public void runnableDelayed(Location loc, long time) {
        new BukkitRunnable() {
            @Override
            public void run() {
                loc.getBlock().setType(Material.COBBLESTONE);
                cancel();
            }
        }.runTaskTimer(this.plugin, time, 20L);
    }
}
