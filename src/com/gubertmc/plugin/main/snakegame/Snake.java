package com.gubertmc.plugin.main.snakegame;

import com.gubertmc.MazeGeneratorPlugin;
import com.gubertmc.plugin.main.snakegame.logicalcomponents.Node;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;

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

    public void start(int startRow, int startCol) {
        ai = new Ai(
                plugin,
                arenaBlockLocations,
                startRow,
                startCol
        );
        ai.start();
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
