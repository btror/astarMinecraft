package com.gubertmc.plugin.main.snakegame;

import com.gubertmc.MazeGeneratorPlugin;
import com.gubertmc.plugin.main.mazegenerator.algorithms.Animation;
import com.gubertmc.plugin.main.mazegenerator.algorithms.astar.astar2d.AstarAnimation2D;
import com.gubertmc.plugin.main.snakegame.logicalcomponents.Node;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

import static org.bukkit.Bukkit.getServer;

public class Arena {

    private final MazeGeneratorPlugin plugin;
    private final Block arenaLocationBlock;
    private final int size;

    private final Location[][] arenaBlockLocations;
    private Food food;
    private boolean snakeIsStillAlive;
    private int numberOfFoodEaten;
    private ArrayList<Node> path;

    public Arena(MazeGeneratorPlugin plugin, Block arenaLocationBlock, int size) {
        this.plugin = plugin;
        this.arenaLocationBlock = arenaLocationBlock;
        this.size = size;

        this.arenaBlockLocations = new Location[size][size];
    }

    public void startGame() {
        spawnArena(Material.BLACK_WOOL);

        // run the algorithm
        Snake snake = new Snake(plugin, arenaBlockLocations);
        int startRow = (int) (Math.random() * arenaBlockLocations[0].length);
        int startCol = (int) (Math.random() * arenaBlockLocations[0].length);
        snake.start(startRow, startCol);
    }

    public void spawnArena(Material arenaMaterial) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Location location = new Location(arenaLocationBlock.getWorld(), arenaLocationBlock.getX() + i, arenaLocationBlock.getY(), arenaLocationBlock.getZ() + j);
                location.getBlock().setType(arenaMaterial);
                arenaBlockLocations[i][j] = location;
            }
            // time += 1L;
        }
    }

    public Food spawnFood(Material foodMaterial) {
        int xCoordinate = (int) (Math.random() * size);
        int yCoordinate = (int) (Math.random() * size);

        return new Food(arenaLocationBlock, xCoordinate, yCoordinate, false);
    }
}
