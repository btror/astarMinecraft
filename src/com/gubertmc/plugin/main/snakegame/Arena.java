package com.gubertmc.plugin.main.snakegame;

import com.gubertmc.MazeGeneratorPlugin;
import com.gubertmc.plugin.main.snakegame.logicalcomponents.Node;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class Arena {

    private final MazeGeneratorPlugin plugin;
    private final Block arenaLocationBlock;
    private final int size;

    private final Location[][] arenaBlockLocations;
    private ArrayList<Node> path;

    public Arena(MazeGeneratorPlugin plugin, Block arenaLocationBlock, int size) {
        this.plugin = plugin;
        this.arenaLocationBlock = arenaLocationBlock;
        this.size = size;

        this.arenaBlockLocations = new Location[size][size];
    }

    public void startGame() {
        spawnArena(Material.BLACK_WOOL);
//        long time = 0L;

        // run the algorithm
        int startRow = (int) (Math.random() * arenaBlockLocations[0].length);
        int startCol = (int) (Math.random() * arenaBlockLocations[0].length);
        Ai ai = new Ai(
                plugin,
                arenaBlockLocations,
                startRow,
                startCol
        );
        ai.start();

//        for (Node node : path) {
//            runnableDelayed(node.getLocation(), time, Material.GREEN_WOOL);
//            time += 1L;
//        }
    }

    public void spawnArena(Material arenaMaterial) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Location location = new Location(arenaLocationBlock.getWorld(), arenaLocationBlock.getX() + i, arenaLocationBlock.getY(), arenaLocationBlock.getZ() + j);
                location.getBlock().setType(arenaMaterial);
                arenaBlockLocations[i][j] = location;
            }
        }
    }

    public void runnableDelayed(Location loc, long time, Material material) {
        new BukkitRunnable() {
            @Override
            public void run() {
                loc.getBlock().setType(material);
                cancel();
            }
        }.runTaskTimer(plugin, time, 20L);
    }
}
