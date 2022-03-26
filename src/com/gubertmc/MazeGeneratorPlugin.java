package com.gubertmc;

import com.gubertmc.plugin.commands.MazeGeneratorCommand;
import com.gubertmc.plugin.commands.TestCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class MazeGeneratorPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        System.out.println("Initializing MazeGenerator Plugin...");

        // A* Pathfinding command
        MazeGeneratorCommand mazeGeneratorCommand = new MazeGeneratorCommand(this);
        Objects.requireNonNull(getCommand("maze")).setExecutor(mazeGeneratorCommand);
        getServer().getPluginManager().registerEvents(mazeGeneratorCommand, this);

        // Test command
        TestCommand testCommand = new TestCommand(this);
        Objects.requireNonNull(getCommand("test")).setExecutor(testCommand);
        getServer().getPluginManager().registerEvents(testCommand, this);

    }

    @Override
    public void onDisable() {
        System.out.println("Shutting down MazeGenerator Plugin...");
    }
}
