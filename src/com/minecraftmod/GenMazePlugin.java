package com.minecraftmod;


import com.minecraftmod.maze.GenMazeCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class GenMazePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        System.out.println("Initializing plugin...");
        GenMazeCommand genMazeCommand = new GenMazeCommand(this);
        getCommand("genmaze").setExecutor(genMazeCommand);
        getServer().getPluginManager().registerEvents(genMazeCommand, this);
    }

    @Override
    public void onDisable() {
        System.out.println("Shutting down");
    }
}
