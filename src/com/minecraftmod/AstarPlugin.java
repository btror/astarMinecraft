package com.minecraftmod;


import com.minecraftmod.astar.AstarCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class AstarPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        System.out.println("Initializing plugin...");
        AstarCommand astarCommand = new AstarCommand();
        getCommand("genmaze").setExecutor(astarCommand);
        getServer().getPluginManager().registerEvents(astarCommand, this);
    }

    @Override
    public void onDisable() {
        System.out.println("Shutting down");
    }
}
