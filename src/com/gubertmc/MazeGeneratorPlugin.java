package com.gubertmc;

import com.gubertmc.plugin.commands.MazeGeneratorCommand;
import com.gubertmc.plugin.commands.SnakeGameCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class MazeGeneratorPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        System.out.println("Initializing plugin...");

        // Maze generator
        MazeGeneratorCommand mazeGeneratorCommand = new MazeGeneratorCommand(this);
        Objects.requireNonNull(getCommand("maze")).setExecutor(mazeGeneratorCommand);
        getServer().getPluginManager().registerEvents(mazeGeneratorCommand, this);

        // Snake
        SnakeGameCommand snakeGameCommand = new SnakeGameCommand(this);
        Objects.requireNonNull(getCommand("snake")).setExecutor(snakeGameCommand);
        getServer().getPluginManager().registerEvents(snakeGameCommand, this);

    }

    @Override
    public void onDisable() {
        System.out.println("Shutting down plugin...");
    }
}
