package com.gubertmc.plugin.main.algorithms;

import com.gubertmc.MazeGeneratorPlugin;
import com.gubertmc.plugin.main.algorithms.astar.Node;
import com.gubertmc.plugin.main.algorithms.astar.NodeComparator;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.PriorityQueue;

public abstract class Animation {

    public Location[][][] tile_grid;
    public int[][][] tile_grid_int;
    public int[] startCoordinate;
    public int[] endCoordinate;
    public Material WALL_MATERIAL;
    public Material PATH_MATERIAL;
    public Material PATH_SPREAD_MATERIAL;
    public Material PATH_GROUND_MATERIAL;
    public Material START_POINT_GLASS;
    public Material END_POINT_GLASS;
    public MazeGeneratorPlugin plugin;
    public int size;
    public ArrayList<Location> exploredPlaces = new ArrayList<>();
    public boolean is3d;
    public Node start_node;
    public Node current_node;
    public Node end_node;
    public PriorityQueue<Node> open_list = new PriorityQueue<>(10, new NodeComparator());
    public ArrayList<Node> closed_list = new ArrayList<>();
    public Node[][][] grid;
    public ArrayList<Location> thePath = new ArrayList<>();

    public Animation(
            MazeGeneratorPlugin plugin,
            Location[][][] tiles,
            int[] startCoordinate,
            int[] endCoordinate,
            int size,
            Material wallMaterial,
            Material pathMaterial,
            Material pathSpreadMaterial,
            Material groundMaterial,
            Material startGlassMaterial,
            Material endGlassMaterial,
            boolean is3d
    ) {
        int[][][] tempArray = new int[size][size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    tempArray[i][j][k] = 0;
                }
            }
        }
        this.tile_grid_int = tempArray;
        this.tile_grid = tiles;
        this.plugin = plugin;
        this.size = size;
        this.WALL_MATERIAL = wallMaterial;
        this.PATH_MATERIAL = pathMaterial;
        this.PATH_SPREAD_MATERIAL = pathSpreadMaterial;
        this.PATH_GROUND_MATERIAL = groundMaterial;
        this.START_POINT_GLASS = startGlassMaterial;
        this.END_POINT_GLASS = endGlassMaterial;
        this.startCoordinate = startCoordinate;
        this.endCoordinate = endCoordinate;
        this.is3d = is3d;
        this.grid = new Node[size][size][size];
    }

    public abstract boolean start();

    public abstract void showAnimation(long time);

    public abstract void runnableDelayed(Location loc, long time, Material material);

}
