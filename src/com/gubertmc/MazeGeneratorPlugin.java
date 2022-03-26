package com.gubertmc;

import com.gubertmc.plugin.commands.BreadthFirstSearchCommand;
import com.gubertmc.plugin.commands.PathFindingCommand;
import com.gubertmc.plugin.commands.TestCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class MazeGeneratorPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        System.out.println("Initializing MazeGenerator Plugin...");

        // A* Pathfinding command
        PathFindingCommand pathFindingCommand = new PathFindingCommand(this);
        Objects.requireNonNull(getCommand("astar")).setExecutor(pathFindingCommand);
        getServer().getPluginManager().registerEvents(pathFindingCommand, this);

        // BFS command
        BreadthFirstSearchCommand breadthFirstSearchCommand = new BreadthFirstSearchCommand(this);
        Objects.requireNonNull(getCommand("bfs")).setExecutor(breadthFirstSearchCommand);
        getServer().getPluginManager().registerEvents(breadthFirstSearchCommand, this);

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
