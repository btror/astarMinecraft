package com.minecraftmod;


import com.minecraftmod.maze.GenMazeCommand;
import com.minecraftmod.maze.TestCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class GenMazePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        System.out.println("Initializing plugin...");

        // Maze command
        GenMazeCommand genMazeCommand = new GenMazeCommand(this);
        getCommand("genmaze").setExecutor(genMazeCommand);
        getServer().getPluginManager().registerEvents(genMazeCommand, this);

        // Test command
        TestCommand testCommand = new TestCommand(this);
        getCommand("test").setExecutor(testCommand);
        getServer().getPluginManager().registerEvents(testCommand, this);
    }

    @Override
    public void onDisable() {
        System.out.println("Shutting down");
    }
}
