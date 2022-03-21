package com.gubertmc.plugin.algorithms.astar2d;

import com.gubertmc.MazeGeneratorPlugin;
import com.gubertmc.plugin.algorithms.Search;
import org.bukkit.Location;
import org.bukkit.Material;

public class Search2D extends Search {

    public Search2D(MazeGeneratorPlugin plugin, Location[][][] tiles, int[] startCoordinate, int[] endCoordinate, int size, Material wallMaterial, Material pathMaterial, Material pathSpreadMaterial, Material groundMaterial, Material startGlassMaterial, Material endGlassMaterial) {
        super(plugin, tiles, startCoordinate, endCoordinate, size, wallMaterial, pathMaterial, pathSpreadMaterial, groundMaterial, startGlassMaterial, endGlassMaterial, false);
    }
}
