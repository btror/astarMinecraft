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
    private int row, col;

    public Snake(MazeGeneratorPlugin plugin, Location[][] arenaBlockLocations) {
        this.plugin = plugin;
        this.arenaBlockLocations = arenaBlockLocations;

        this.snakeIsAlive = true;
        this.foodIsEaten = false;
        this.snakeBodyMaterial = Material.GREEN_WOOL;
        row = (int) (Math.random() * arenaBlockLocations[0].length);
        col = (int) (Math.random() * arenaBlockLocations[0].length);
    }

    public void pursueFood(Food targetFood) {
        Ai ai = new Ai(
                plugin,
                arenaBlockLocations,
                row,
                col,
                targetFood.getX(),
                targetFood.getY(),
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
