package com.gubertmc.plugin.main.algorithms;

import com.gubertmc.MazeGeneratorPlugin;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.PriorityQueue;

public abstract class Animation {

    private Location[][][] tileGrid;
    private int[][][] tileGridInt;
    private int[] startCoordinate;
    private int[] endCoordinate;
    private final Material wallMaterial;
    private Material pathMaterial;
    private final Material pathSpreadMaterial;
    private final Material pathGroundMaterial;
    private final Material endPointGlass;
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
        this.endPointGlass = endGlassMaterial;
        this.startCoordinate = startCoordinate;
        this.endCoordinate = endCoordinate;
        this.is3d = is3d;
        this.grid = new Node[size][size][size];
    }

    /**
     * Set up initial logic - start/end nodes, blocker nodes, and current node.
     */
    public abstract void setup();

    /**
     * Start calculations for searching the maze.
     *
     * @return if a solution was found.
     */
    public abstract boolean start();

    /**
     * Show animation of block types changing.
     *
     * @param time when to change a block's type.
     */
    public abstract void showAnimation(long time);

    /**
     * Logic for changing a block's type at a specified time.
     *
     * @param loc      location of a block.
     * @param time     when to change a block's type.
     * @param material block type.
     */
    public abstract void runnableDelayed(Location loc, long time, Material material);

    /**
     * Get the locations of each block in the maze.
     *
     * @return maze block locations.
     */
    public Location[][][] getTileGrid() {
        return tileGrid;
    }

    /**
     * Set the locations of the blocks in the maze.
     *
     * @param tileGrid locations of blocks in the maze.
     */
    public void setTileGrid(Location[][][] tileGrid) {
        this.tileGrid = tileGrid;
    }

    /**
     * Get the simulation maze.
     *
     * @return simulation maze.
     */
    public int[][][] getTileGridInt() {
        return tileGridInt;
    }

    /**
     * Set the simulation maze.
     *
     * @param tileGridInt simulation maze.
     */
    public void setTileGridInt(int[][][] tileGridInt) {
        this.tileGridInt = tileGridInt;
    }

    /**
     * Get the maze start coordinate.
     *
     * @return start coordinate.
     */
    public int[] getStartCoordinate() {
        return startCoordinate;
    }

    /**
     * Set the maze start coordinate.
     *
     * @param startCoordinate start coordinate.
     */
    public void setStartCoordinate(int[] startCoordinate) {
        this.startCoordinate = startCoordinate;
    }

    /**
     * Get the maze end coordinate.
     *
     * @return end coordinate.
     */
    public int[] getEndCoordinate() {
        return endCoordinate;
    }

    /**
     * Set the maze end coordinate.
     *
     * @param endCoordinate end coordinate.
     */
    public void setEndCoordinate(int[] endCoordinate) {
        this.endCoordinate = endCoordinate;
    }

    /**
     * Get the maze blocker block type.
     *
     * @return blocker block type.
     */
    public Material getWallMaterial() {
        return wallMaterial;
    }

    /**
     * Get the maze path block type.
     *
     * @return path block type.
     */
    public Material getPathMaterial() {
        return pathMaterial;
    }

    /**
     * Set the maze path block type.
     *
     * @param pathMaterial path block type.
     */
    public void setPathMaterial(Material pathMaterial) {
        this.pathMaterial = pathMaterial;
    }

    /**
     * Get the maze spread block type.
     *
     * @return spread block type.
     */
    public Material getPathSpreadMaterial() {
        return pathSpreadMaterial;
    }

    /**
     * Get the maze path ground block type.
     *
     * @return path ground block type.
     */
    public Material getPathGroundMaterial() {
        return pathGroundMaterial;
    }

    /**
     * Get block type of block above end coordinate.
     *
     * @return block type of block above end coordinate.
     */
    public Material getEndPointGlass() {
        return endPointGlass;
    }

    /**
     * Get plugin.
     *
     * @return plugin.
     */
    public MazeGeneratorPlugin getPlugin() {
        return plugin;
    }

    /**
     * Set plugin.
     *
     * @param plugin plugin.
     */
    public void setPlugin(MazeGeneratorPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Get maze size.
     *
     * @return maze size.
     */
    public int getSize() {
        return size;
    }

    /**
     * Set maze size.
     *
     * @param size maze size.
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * Get the explored locations of the maze.
     *
     * @return explored locations.
     */
    public ArrayList<Location> getExploredPlaces() {
        return exploredPlaces;
    }

    /**
     * Set the explored locations of the maze.
     *
     * @param exploredPlaces explored locations.
     */
    public void setExploredPlaces(ArrayList<Location> exploredPlaces) {
        this.exploredPlaces = exploredPlaces;
    }

    /**
     * Set start node.
     *
     * @param startNode start node.
     */
    public void setStartNode(Node startNode) {
        this.startNode = startNode;
    }

    /**
     * Get current node.
     *
     * @return current node.
     */
    public Node getCurrentNode() {
        return currentNode;
    }

    /**
     * Set current node.
     *
     * @param currentNode current node.
     */
    public void setCurrentNode(Node currentNode) {
        this.currentNode = currentNode;
    }

    /**
     * Get end node.
     *
     * @return end node.
     */
    public Node getEndNode() {
        return endNode;
    }

    /**
     * Set end node.
     *
     * @param endNode end node.
     */
    public void setEndNode(Node endNode) {
        this.endNode = endNode;
    }

    /**
     * Get list of nodes that need to be examined.
     *
     * @return open list.
     */
    public PriorityQueue<Node> getOpenList() {
        return openList;
    }

    /**
     * Set list of nodes that need to be examined.
     *
     * @param openList open list.
     */
    public void setOpenList(PriorityQueue<Node> openList) {
        this.openList = openList;
    }

    /**
     * Get list of nodes that have been explored.
     *
     * @return closed list.
     */
    public ArrayList<Node> getClosedList() {
        return closedList;
    }

    /**
     * Set list of nodes that have been explored.
     *
     * @param closedList closed list.
     */
    public void setClosedList(ArrayList<Node> closedList) {
        this.closedList = closedList;
    }

    /**
     * Get node maze.
     *
     * @return node maze.
     */
    public Node[][][] getGrid() {
        return grid;
    }

    /**
     * Set node maze.
     *
     * @param grid node maze.
     */
    public void setGrid(Node[][][] grid) {
        this.grid = grid;
    }

    /**
     * Get locations of blocks making up the maze path.
     *
     * @return maze path block locations.
     */
    public ArrayList<Location> getThePath() {
        return thePath;
    }

    /**
     * Set locations of blocks making up the maze path.
     *
     * @param thePath maze path block locations.
     */
    public void setThePath(ArrayList<Location> thePath) {
        this.thePath = thePath;
    }
}
