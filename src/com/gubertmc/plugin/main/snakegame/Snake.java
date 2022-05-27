package com.gubertmc.plugin.main.snakegame;

import com.gubertmc.MazeGeneratorPlugin;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class Snake {

    private final MazeGeneratorPlugin plugin;
    private final Block arenaLocationBlock;
    private final Location startLocation;
    private final int size;

    private boolean snakeIsAlive;
    private boolean foodIsEaten;
    private Material snakeBodyMaterial;

    public Snake(MazeGeneratorPlugin plugin, Block arenaLocationBlock, Location spawnLocation, int size) {
        this.plugin = plugin;
        this.arenaLocationBlock = arenaLocationBlock;
        this.startLocation = spawnLocation;
        this.size = size;

        this.snakeIsAlive = true;
        this.foodIsEaten = false;
        this.snakeBodyMaterial = Material.GREEN_WOOL;
    }

    public void pursueFood(Food targetFood) {
        Ai ai = new Ai(
                plugin,
                arenaLocationBlock,
                size,
                startLocation,
                targetFood.getLocation(),
                snakeBodyMaterial,
                targetFood.getFoodMaterial()
        );
        ai.start();
        // if the ai doesn't reach the food set foodIsEaten to false
        System.out.println("AFTER STARTING AI");
        snakeIsAlive = false;
    }

    public boolean getSnakeIsAlive() {
        return snakeIsAlive;
    }

    public boolean getFoodStatus() {
        return foodIsEaten;
    }

}
