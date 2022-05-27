package com.gubertmc.plugin.main.mazegenerator.algorithms.greedybestfirstsearch.greedybestfirstsearch2d.diagonalmovement;

import com.gubertmc.MazeGeneratorPlugin;
import com.gubertmc.plugin.main.mazegenerator.algorithms.Node;
import com.gubertmc.plugin.main.mazegenerator.algorithms.greedybestfirstsearch.greedybestfirstsearch2d.GreedyBestFirstSearchAnimation2D;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class GreedyBestFirstSearchAnimationDiagonal2D extends GreedyBestFirstSearchAnimation2D {

    public GreedyBestFirstSearchAnimationDiagonal2D(
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
    public void calculateNeighborValues() {
        int row = getCurrentNode().getRow();
        int col = getCurrentNode().getCol();
        int zNum = 0;

        // north
        if (row - 1 > -1 && getGrid()[row - 1][col][zNum].getType() == 0
                && !getClosedList().contains(getGrid()[row - 1][col][zNum])) {
            Node[][][] grid = getGrid();
            grid[row - 1][col][zNum].setParent(getCurrentNode());
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
