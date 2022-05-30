package com.gubertmc.plugin.main.snakegame;

import com.gubertmc.MazeGeneratorPlugin;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class Snake {

    private final MazeGeneratorPlugin plugin;
    private Location[][] arenaBlockLocations;

    private boolean snakeIsAlive;
    private boolean foodIsEaten;
    private Material snakeBodyMaterial;
    private Ai ai;

    public Snake(MazeGeneratorPlugin plugin, Location[][] arenaBlockLocations) {
        this.plugin = plugin;
        this.arenaBlockLocations = arenaBlockLocations;

        this.snakeIsAlive = true;
        this.foodIsEaten = false;
        this.snakeBodyMaterial = Material.GREEN_WOOL;
    }

    public boolean pursueFood(int startRow, int startCol, Food targetFood, int snakeLength) {
        ai = new Ai(
                plugin,
                arenaBlockLocations,
                startRow,
                startCol,
                targetFood.getX(),
                targetFood.getY(),
                snakeBodyMaterial,
                targetFood.getFoodMaterial(),
                snakeLength
        );
        // if the ai doesn't reach the food set foodIsEaten to false
        System.out.println("AFTER STARTING AI");
        // snakeIsAlive = false;
        return ai.start();
    }

    public boolean getSnakeIsAlive() {
        return snakeIsAlive;
    }

    public boolean getFoodStatus() {
        return foodIsEaten;
    }

    public long getTime() {
        return ai.getTime();
    }

}
