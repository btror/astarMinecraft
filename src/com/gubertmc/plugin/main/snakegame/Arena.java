package com.gubertmc.plugin.main.snakegame;

import com.gubertmc.MazeGeneratorPlugin;
import com.gubertmc.plugin.main.mazegenerator.algorithms.Animation;
import com.gubertmc.plugin.main.mazegenerator.algorithms.astar.astar2d.AstarAnimation2D;
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
    private long time;
    private Food food;
    private boolean snakeIsStillAlive;
    private int numberOfFoodEaten;

    public Arena(MazeGeneratorPlugin plugin, Block arenaLocationBlock, int size) {
        this.plugin = plugin;
        this.arenaLocationBlock = arenaLocationBlock;
        this.size = size;

        this.arenaBlockLocations = new Location[size][size];
        this.time = 0L;
    }

    public void startGame() {
        spawnArena(Material.BLACK_WOOL);

        // spawn new food object
        food = spawnFood(Material.YELLOW_WOOL);
        food.spawn();

        // run the algorithm
        Snake snake = new Snake(plugin, arenaBlockLocations);
        int row = (int) (Math.random() * arenaBlockLocations[0].length);
        int col = (int) (Math.random() * arenaBlockLocations[0].length);
        snakeIsStillAlive = snake.pursueFood(row, col, food, numberOfFoodEaten);
        time += snake.getTime() + 2L;
        numberOfFoodEaten = 1;
        System.out.println("TIME - " + time);
        ArrayList<Long> times = new ArrayList<>();
        times.add(time);
        for (int i = 0; i < 10; i++) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    System.out.println("IN HERE WOOOH");
                    Food oldFood = food;
                    food = spawnFood(Material.YELLOW_WOOL);
                    food.spawn();
                    snakeIsStillAlive = snake.pursueFood(oldFood.getX(), oldFood.getY(), food, numberOfFoodEaten);
                    System.out.println("NEW TIME - " + time);
                    cancel();
                }
            }.runTaskTimer(plugin, time, 20L);

            time += snake.getTime() + 2L;
            times.add(time);
            numberOfFoodEaten++;
        }
        System.out.println("times - " + times);

//        while (snakeIsStillAlive) {
//            new BukkitRunnable() {
//                @Override
//                public void run() {
//                    // spawn new food
//                    Food oldFood = food;
//                    food = spawnFood(Material.YELLOW_WOOL);
//                    food.spawn();
//                    snakeIsStillAlive = snake.pursueFood(oldFood.getX(), oldFood.getY(), food, time);
//                    cancel();
//                }
//            }.runTaskTimer(plugin, time, 20L);
//
//            time += snake.getTime() + 1L;
//        }

        // if the algorithm runs into the food change the food status to false (aka eaten)


    }

    public void spawnArena(Material arenaMaterial) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Location location = new Location(arenaLocationBlock.getWorld(), arenaLocationBlock.getX() + i, arenaLocationBlock.getY(), arenaLocationBlock.getZ() + j);
                location.getBlock().setType(arenaMaterial);
                arenaBlockLocations[i][j] = location;
            }
            time += 1L;
        }
    }

    public Food spawnFood(Material foodMaterial) {
        int xCoordinate = (int) (Math.random() * size);
        int yCoordinate = (int) (Math.random() * size);

        return new Food(arenaLocationBlock, xCoordinate, yCoordinate, false);
    }
}
