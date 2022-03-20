package com.gubertmc.maze.old.astar.algorithm2d;

import com.gubertmc.MazeGeneratorPlugin;
import com.gubertmc.maze.old.astar.Search;
import org.bukkit.Location;
import org.bukkit.Material;

public class Search2D extends Search {

    public Search2D(MazeGeneratorPlugin plugin, Location[][][] tiles, int[] startCoordinate, int[] endCoordinate, int size, Material wallMaterial, Material pathMaterial, Material pathSpreadMaterial, Material groundMaterial) {
        super(plugin, tiles, startCoordinate, endCoordinate, size, wallMaterial, pathMaterial, pathSpreadMaterial, groundMaterial, false);
    }
}
