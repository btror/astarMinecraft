package com.gubertmc.plugin.main.algorithms.astar.astar2d.diagonalmovement;

import com.gubertmc.MazeGeneratorPlugin;
import com.gubertmc.plugin.main.algorithms.Animation;
import com.gubertmc.plugin.main.algorithms.Node;
import com.gubertmc.plugin.main.algorithms.astar.astar2d.AstarAnimation2D;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class AstarAnimationDiagonal2D extends AstarAnimation2D {

    public AstarAnimationDiagonal2D(
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
            Material endGlassMaterial
    ) {
        super(
                plugin,
                tiles,
                startCoordinate,
                endCoordinate,
                size,
                wallMaterial,
                pathMaterial,
                pathSpreadMaterial,
                groundMaterial,
                startGlassMaterial,
                endGlassMaterial
        );
    }

    @Override
    public int calculateG(Node node) {
        int row = node.getRow();
        int col = node.getCol();
        if (row == getCurrentNode().getRow() && col == getCurrentNode().getCol()) {
            return 0;
        }

        Node parent = node.getParent();
        if (parent == null) {
            int xDistance;
            if (col > getCurrentNode().getCol()) {
                xDistance = col - getCurrentNode().getCol();
            } else {
                xDistance = getCurrentNode().getCol() - col;
            }

            int yDistance;
            if (row > getCurrentNode().getRow()) {
                yDistance = row - getCurrentNode().getRow();
            } else {
                yDistance = getCurrentNode().getRow() - row;
            }

            if (xDistance != 0 && yDistance != 0) {
                return 14;
            } else {
                return (xDistance * 10) + (yDistance * 10);
            }
        }

        if (col != parent.getCol() && row != parent.getRow()) {
            return 14 + parent.getG();
        }

        return 10 + parent.getG();
    }

    @Override
    public void calculateNeighborValues() {
        int row = getCurrentNode().getRow();
        int col = getCurrentNode().getCol();
        int zNum = 0;

        // north
        if (row - 1 > -1 && getGrid()[row - 1][col][zNum].getType() == 0
                && !getClosedList().contains(getGrid()[row - 1][col][zNum])) {
            Node[][][] grid = getGrid();
            grid[row - 1][col][zNum].setParent(getCurrentNode());
            int g = calculateG(grid[row - 1][col][zNum]);
            grid[row - 1][col][zNum].setG(g);
            int h = calculateH(grid[row - 1][col][zNum]);
            grid[row - 1][col][zNum].setH(h);
            grid[row - 1][col][zNum].setF();
            setGrid(grid);
            PriorityQueue<Node> openList = getOpenList();
            openList.add(grid[row - 1][col][zNum]);
            setOpenList(openList);
            int[][][] tileGridInt = getTileGridInt();
            tileGridInt[row - 1][col][zNum] = 1;
            setTileGridInt(tileGridInt);

            Location loc = getTileGrid()[row - 1][col][zNum];
            if (!getExploredPlaces().contains(loc)) {
                loc = new Location(
                        loc.getWorld(), loc.getBlock().getX(), loc.getBlock().getY() - 1, loc.getBlock().getZ()
                );
                ArrayList<Location> exploredPlaces = getExploredPlaces();
                exploredPlaces.add(loc);
                setExploredPlaces(exploredPlaces);
            }
        }

        // northeast
        if (row - 1 > -1 && col + 1 < getSize() && getGrid()[row - 1][col + 1][zNum].getType() == 0
                && !getClosedList().contains(getGrid()[row - 1][col + 1][zNum])) {
            Node[][][] grid = getGrid();
            grid[row - 1][col + 1][zNum].setParent(getCurrentNode());
            int g = calculateG(grid[row - 1][col + 1][zNum]);
            grid[row - 1][col + 1][zNum].setG(g);
            int h = calculateH(grid[row - 1][col + 1][zNum]);
            grid[row - 1][col + 1][zNum].setH(h);
            grid[row - 1][col + 1][zNum].setF();
            setGrid(grid);
            PriorityQueue<Node> openList = getOpenList();
            openList.add(grid[row - 1][col + 1][zNum]);
            setOpenList(openList);
            int[][][] tileGridInt = getTileGridInt();
            tileGridInt[row - 1][col + 1][zNum] = 1;
            setTileGridInt(tileGridInt);

            Location loc = getTileGrid()[row - 1][col + 1][zNum];
            if (!getExploredPlaces().contains(loc)) {
                loc = new Location(
                        loc.getWorld(), loc.getBlock().getX(), loc.getBlock().getY() - 1, loc.getBlock().getZ()
                );
                ArrayList<Location> exploredPlaces = getExploredPlaces();
                exploredPlaces.add(loc);
                setExploredPlaces(exploredPlaces);
            }
        }

        // east
        if (col + 1 < getSize() && getGrid()[row][col + 1][zNum].getType() == 0
                && !getClosedList().contains(getGrid()[row][col + 1][zNum])) {
            Node[][][] grid = getGrid();
            grid[row][col + 1][zNum].setParent(getCurrentNode());
            int g = calculateG(grid[row][col + 1][zNum]);
            grid[row][col + 1][zNum].setG(g);
            int h = calculateH(grid[row][col + 1][zNum]);
            grid[row][col + 1][zNum].setH(h);
            grid[row][col + 1][zNum].setF();
            setGrid(grid);
            PriorityQueue<Node> openList = getOpenList();
            openList.add(grid[row][col + 1][zNum]);
            setOpenList(openList);
            int[][][] tileGridInt = getTileGridInt();
            tileGridInt[row][col + 1][zNum] = 1;
            setTileGridInt(tileGridInt);

            Location loc = getTileGrid()[row][col + 1][zNum];
            if (!getExploredPlaces().contains(loc)) {
                loc = new Location(
                        loc.getWorld(), loc.getBlock().getX(), loc.getBlock().getY() - 1, loc.getBlock().getZ()
                );
                ArrayList<Location> exploredPlaces = getExploredPlaces();
                exploredPlaces.add(loc);
                setExploredPlaces(exploredPlaces);
            }
        }

        // southeast
        if (row + 1 < getSize() && col + 1 < getSize() && getGrid()[row + 1][col + 1][zNum].getType() == 0
                && !getClosedList().contains(getGrid()[row + 1][col + 1][zNum])) {
            Node[][][] grid = getGrid();
            grid[row + 1][col + 1][zNum].setParent(getCurrentNode());
            int g = calculateG(grid[row + 1][col + 1][zNum]);
            grid[row + 1][col + 1][zNum].setG(g);
            int h = calculateH(grid[row + 1][col + 1][zNum]);
            grid[row + 1][col + 1][zNum].setH(h);
            grid[row + 1][col + 1][zNum].setF();
            setGrid(grid);
            PriorityQueue<Node> openList = getOpenList();
            openList.add(grid[row + 1][col + 1][zNum]);
            setOpenList(openList);
            int[][][] tileGridInt = getTileGridInt();
            tileGridInt[row + 1][col + 1][zNum] = 1;
            setTileGridInt(tileGridInt);

            Location loc = getTileGrid()[row + 1][col + 1][zNum];
            if (!getExploredPlaces().contains(loc)) {
                loc = new Location(
                        loc.getWorld(), loc.getBlock().getX(), loc.getBlock().getY() - 1, loc.getBlock().getZ()
                );
                ArrayList<Location> exploredPlaces = getExploredPlaces();
                exploredPlaces.add(loc);
                setExploredPlaces(exploredPlaces);
            }
        }

        // south
        if (row + 1 < getSize() && getGrid()[row + 1][col][zNum].getType() == 0
                && !getClosedList().contains(getGrid()[row + 1][col][zNum])) {
            Node[][][] grid = getGrid();
            grid[row + 1][col][zNum].setParent(getCurrentNode());
            int g = calculateG(grid[row + 1][col][zNum]);
            grid[row + 1][col][zNum].setG(g);
            int h = calculateH(grid[row + 1][col][zNum]);
            grid[row + 1][col][zNum].setH(h);
            grid[row + 1][col][zNum].setF();
            setGrid(grid);
            PriorityQueue<Node> openList = getOpenList();
            openList.add(grid[row + 1][col][zNum]);
            setOpenList(openList);
            int[][][] tileGridInt = getTileGridInt();
            tileGridInt[row + 1][col][zNum] = 1;
            setTileGridInt(tileGridInt);

            Location loc = getTileGrid()[row + 1][col][zNum];
            if (!getExploredPlaces().contains(loc)) {
                loc = new Location(
                        loc.getWorld(), loc.getBlock().getX(), loc.getBlock().getY() - 1, loc.getBlock().getZ()
                );
                ArrayList<Location> exploredPlaces = getExploredPlaces();
                exploredPlaces.add(loc);
                setExploredPlaces(exploredPlaces);
            }
        }

        // southwest
        if (row + 1 < getSize() && col - 1 > -1 && getGrid()[row + 1][col - 1][zNum].getType() == 0
                && !getClosedList().contains(getGrid()[row + 1][col - 1][zNum])) {
            Node[][][] grid = getGrid();
            grid[row + 1][col - 1][zNum].setParent(getCurrentNode());
            int g = calculateG(grid[row + 1][col - 1][zNum]);
            grid[row + 1][col - 1][zNum].setG(g);
            int h = calculateH(grid[row + 1][col - 1][zNum]);
            grid[row + 1][col - 1][zNum].setH(h);
            grid[row + 1][col - 1][zNum].setF();
            setGrid(grid);
            PriorityQueue<Node> openList = getOpenList();
            openList.add(grid[row + 1][col - 1][zNum]);
            setOpenList(openList);
            int[][][] tileGridInt = getTileGridInt();
            tileGridInt[row + 1][col - 1][zNum] = 1;
            setTileGridInt(tileGridInt);

            Location loc = getTileGrid()[row + 1][col - 1][zNum];
            if (!getExploredPlaces().contains(loc)) {
                loc = new Location(
                        loc.getWorld(), loc.getBlock().getX(), loc.getBlock().getY() - 1, loc.getBlock().getZ()
                );
                ArrayList<Location> exploredPlaces = getExploredPlaces();
                exploredPlaces.add(loc);
                setExploredPlaces(exploredPlaces);
            }
        }

        // west
        if (col - 1 > -1 && getGrid()[row][col - 1][zNum].getType() == 0
                && !getClosedList().contains(getGrid()[row][col - 1][zNum])) {
            Node[][][] grid = getGrid();
            grid[row][col - 1][zNum].setParent(getCurrentNode());
            int g = calculateG(grid[row][col - 1][zNum]);
            grid[row][col - 1][zNum].setG(g);
            int h = calculateH(grid[row][col - 1][zNum]);
            grid[row][col - 1][zNum].setH(h);
            grid[row][col - 1][zNum].setF();
            setGrid(grid);
            PriorityQueue<Node> openList = getOpenList();
            openList.add(grid[row][col - 1][zNum]);
            setOpenList(openList);
            int[][][] tileGridInt = getTileGridInt();
            tileGridInt[row][col - 1][zNum] = 1;
            setTileGridInt(tileGridInt);

            Location loc = getTileGrid()[row][col - 1][zNum];
            if (!getExploredPlaces().contains(loc)) {
                loc = new Location(
                        loc.getWorld(), loc.getBlock().getX(), loc.getBlock().getY() - 1, loc.getBlock().getZ()
                );
                ArrayList<Location> exploredPlaces = getExploredPlaces();
                exploredPlaces.add(loc);
                setExploredPlaces(exploredPlaces);
            }
        }

        // northwest
        if (row - 1 > -1 && col - 1 > -1 && getGrid()[row - 1][col - 1][zNum].getType() == 0
                && !getClosedList().contains(getGrid()[row - 1][col - 1][zNum])) {
            Node[][][] grid = getGrid();
            grid[row - 1][col - 1][zNum].setParent(getCurrentNode());
            int g = calculateG(grid[row - 1][col - 1][zNum]);
            grid[row - 1][col - 1][zNum].setG(g);
            int h = calculateH(grid[row - 1][col - 1][zNum]);
            grid[row - 1][col - 1][zNum].setH(h);
            grid[row - 1][col - 1][zNum].setF();
            setGrid(grid);
            PriorityQueue<Node> openList = getOpenList();
            openList.add(grid[row - 1][col - 1][zNum]);
            setOpenList(openList);
            int[][][] tileGridInt = getTileGridInt();
            tileGridInt[row - 1][col - 1][zNum] = 1;
            setTileGridInt(tileGridInt);

            Location loc = getTileGrid()[row - 1][col - 1][zNum];
            if (!getExploredPlaces().contains(loc)) {
                loc = new Location(
                        loc.getWorld(), loc.getBlock().getX(), loc.getBlock().getY() - 1, loc.getBlock().getZ()
                );
                ArrayList<Location> exploredPlaces = getExploredPlaces();
                exploredPlaces.add(loc);
                setExploredPlaces(exploredPlaces);
            }
        }
    }
}
