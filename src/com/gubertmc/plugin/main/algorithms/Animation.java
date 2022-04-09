package com.gubertmc.plugin.main.algorithms;

import com.gubertmc.MazeGeneratorPlugin;
import com.gubertmc.plugin.main.algorithms.astar.Node;
import com.gubertmc.plugin.main.algorithms.astar.NodeComparator;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.PriorityQueue;

public abstract class Animation {

    private Location[][][] tileGrid;
    private int[][][] tileGridInt;
    private int[] startCoordinate;
    private int[] endCoordinate;
    private Material wallMaterial;
    private Material pathMaterial;
    private Material pathSpreadMaterial;
    private Material pathGroundMaterial;
    private Material startPointGlass;
    private Material endPointGlass;
    private MazeGeneratorPlugin plugin;
    private int size;
    private ArrayList<Location> exploredPlaces = new ArrayList<>();
    private boolean is3d;
    private Node startNode;
    private Node currentNode;
    private Node endNode;
    private PriorityQueue<Node> openList = new PriorityQueue<>(10, new NodeComparator());
    private ArrayList<Node> closedList = new ArrayList<>();
    private Node[][][] grid;
    private ArrayList<Location> thePath = new ArrayList<>();

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
        this.tileGridInt = tempArray;
        this.tileGrid = tiles;
        this.plugin = plugin;
        this.size = size;
        this.wallMaterial = wallMaterial;
        this.pathMaterial = pathMaterial;
        this.pathSpreadMaterial = pathSpreadMaterial;
        this.pathGroundMaterial = groundMaterial;
        this.startPointGlass = startGlassMaterial;
        this.endPointGlass = endGlassMaterial;
        this.startCoordinate = startCoordinate;
        this.endCoordinate = endCoordinate;
        this.is3d = is3d;
        this.grid = new Node[size][size][size];
    }

    public abstract void setup();

    public abstract boolean start();

    public abstract void showAnimation(long time);

    public abstract void runnableDelayed(Location loc, long time, Material material);

    public Location[][][] getTileGrid() {
        return tileGrid;
    }

    public void setTileGrid(Location[][][] tileGrid) {
        this.tileGrid = tileGrid;
    }

    public int[][][] getTileGridInt() {
        return tileGridInt;
    }

    public void setTileGridInt(int[][][] tileGridInt) {
        this.tileGridInt = tileGridInt;
    }

    public int[] getStartCoordinate() {
        return startCoordinate;
    }

    public void setStartCoordinate(int[] startCoordinate) {
        this.startCoordinate = startCoordinate;
    }

    public int[] getEndCoordinate() {
        return endCoordinate;
    }

    public void setEndCoordinate(int[] endCoordinate) {
        this.endCoordinate = endCoordinate;
    }

    public Material getWallMaterial() {
        return wallMaterial;
    }

    public void setWallMaterial(Material wallMaterial) {
        this.wallMaterial = wallMaterial;
    }

    public Material getPathMaterial() {
        return pathMaterial;
    }

    public void setPathMaterial(Material pathMaterial) {
        this.pathMaterial = pathMaterial;
    }

    public Material getPathSpreadMaterial() {
        return pathSpreadMaterial;
    }

    public void setPathSpreadMaterial(Material pathSpreadMaterial) {
        this.pathSpreadMaterial = pathSpreadMaterial;
    }

    public Material getPathGroundMaterial() {
        return pathGroundMaterial;
    }

    public void setPathGroundMaterial(Material pathGroundMaterial) {
        this.pathGroundMaterial = pathGroundMaterial;
    }

    public Material getStartPointGlass() {
        return startPointGlass;
    }

    public void setStartPointGlass(Material startPointGlass) {
        this.startPointGlass = startPointGlass;
    }

    public Material getEndPointGlass() {
        return endPointGlass;
    }

    public void setEndPointGlass(Material endPointGlass) {
        this.endPointGlass = endPointGlass;
    }

    public MazeGeneratorPlugin getPlugin() {
        return plugin;
    }

    public void setPlugin(MazeGeneratorPlugin plugin) {
        this.plugin = plugin;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public ArrayList<Location> getExploredPlaces() {
        return exploredPlaces;
    }

    public void setExploredPlaces(ArrayList<Location> exploredPlaces) {
        this.exploredPlaces = exploredPlaces;
    }

    public boolean isIs3d() {
        return is3d;
    }

    public void setIs3d(boolean is3d) {
        this.is3d = is3d;
    }

    public Node getStartNode() {
        return startNode;
    }

    public void setStartNode(Node startNode) {
        this.startNode = startNode;
    }

    public Node getCurrentNode() {
        return currentNode;
    }

    public void setCurrentNode(Node currentNode) {
        this.currentNode = currentNode;
    }

    public Node getEndNode() {
        return endNode;
    }

    public void setEndNode(Node endNode) {
        this.endNode = endNode;
    }

    public PriorityQueue<Node> getOpenList() {
        return openList;
    }

    public void setOpenList(PriorityQueue<Node> openList) {
        this.openList = openList;
    }

    public ArrayList<Node> getClosedList() {
        return closedList;
    }

    public void setClosedList(ArrayList<Node> closedList) {
        this.closedList = closedList;
    }

    public Node[][][] getGrid() {
        return grid;
    }

    public void setGrid(Node[][][] grid) {
        this.grid = grid;
    }

    public ArrayList<Location> getThePath() {
        return thePath;
    }

    public void setThePath(ArrayList<Location> thePath) {
        this.thePath = thePath;
    }
}
