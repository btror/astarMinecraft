package com.gubertmc.plugin.main.mazegenerator.algorithms.greedybestfirstsearch.greedybestfirstsearch3d.diagonalmovement;

import com.gubertmc.plugin.main.mazegenerator.algorithms.Node;
import com.gubertmc.plugin.main.mazegenerator.algorithms.greedybestfirstsearch.greedybestfirstsearch3d.GreedyBestFirstSearchSimulation3D;

import java.util.PriorityQueue;

public class GreedyBestFirstSearchSimulationDiagonal3D extends GreedyBestFirstSearchSimulation3D {

    public GreedyBestFirstSearchSimulationDiagonal3D(int[][][] maze, int[] startCoordinate, int[] endCoordinate) {
        super(maze, startCoordinate, endCoordinate);
    }

    @Override
    public void calculateNeighborValues() {
        int row = getCurrentNode().getRow();
        int col = getCurrentNode().getCol();
        int zNum = getCurrentNode().getZ();

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
            int[][][] tileGridInt = getTileGrid();
            tileGridInt[row - 1][col][zNum] = 2;
            setTileGrid(tileGridInt);
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
            int[][][] tileGridInt = getTileGrid();
            tileGridInt[row - 1][col + 1][zNum] = 2;
            setTileGrid(tileGridInt);
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
            int[][][] tileGridInt = getTileGrid();
            tileGridInt[row][col + 1][zNum] = 2;
            setTileGrid(tileGridInt);
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
            int[][][] tileGridInt = getTileGrid();
            tileGridInt[row + 1][col + 1][zNum] = 2;
            setTileGrid(tileGridInt);
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
            int[][][] tileGridInt = getTileGrid();
            tileGridInt[row + 1][col][zNum] = 2;
            setTileGrid(tileGridInt);
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
            int[][][] tileGridInt = getTileGrid();
            tileGridInt[row + 1][col - 1][zNum] = 2;
            setTileGrid(tileGridInt);
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
            int[][][] tileGridInt = getTileGrid();
            tileGridInt[row][col - 1][zNum] = 2;
            setTileGrid(tileGridInt);
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
            int[][][] tileGridInt = getTileGrid();
            tileGridInt[row - 1][col - 1][zNum] = 2;
            setTileGrid(tileGridInt);
        }

        // bottom layer
        if (zNum -1 > -1) {
            // down
            if (getGrid()[row][col][zNum - 1].getType() == 0
                    && !getClosedList().contains(getGrid()[row][col][zNum - 1])) {
                Node[][][] grid = getGrid();
                grid[row][col][zNum - 1].setParent(getCurrentNode());
                int h = calculateH(grid[row][col][zNum - 1]);
                grid[row][col][zNum - 1].setH(h);
                grid[row][col][zNum - 1].setF();
                setGrid(grid);
                PriorityQueue<Node> openList = getOpenList();
                openList.add(grid[row][col][zNum - 1]);
                setOpenList(openList);
                int[][][] tileGridInt = getTileGrid();
                tileGridInt[row][col][zNum - 1] = 2;
                setTileGrid(tileGridInt);
            }

            // north
            if (row - 1 > -1 && getGrid()[row - 1][col][zNum - 1].getType() == 0
                    && !getClosedList().contains(getGrid()[row - 1][col][zNum - 1])) {
                Node[][][] grid = getGrid();
                grid[row - 1][col][zNum - 1].setParent(getCurrentNode());
                int h = calculateH(grid[row - 1][col][zNum - 1]);
                grid[row - 1][col][zNum - 1].setH(h);
                grid[row - 1][col][zNum - 1].setF();
                setGrid(grid);
                PriorityQueue<Node> openList = getOpenList();
                openList.add(grid[row - 1][col][zNum - 1]);
                setOpenList(openList);
                int[][][] tileGridInt = getTileGrid();
                tileGridInt[row - 1][col][zNum - 1] = 2;
                setTileGrid(tileGridInt);
            }

            // northeast
            if (row - 1 > -1 && col + 1 < getSize() && getGrid()[row - 1][col + 1][zNum - 1].getType() == 0
                    && !getClosedList().contains(getGrid()[row - 1][col + 1][zNum - 1])) {
                Node[][][] grid = getGrid();
                grid[row - 1][col + 1][zNum - 1].setParent(getCurrentNode());
                int h = calculateH(grid[row - 1][col + 1][zNum - 1]);
                grid[row - 1][col + 1][zNum - 1].setH(h);
                grid[row - 1][col + 1][zNum - 1].setF();
                setGrid(grid);
                PriorityQueue<Node> openList = getOpenList();
                openList.add(grid[row - 1][col + 1][zNum - 1]);
                setOpenList(openList);
                int[][][] tileGridInt = getTileGrid();
                tileGridInt[row - 1][col + 1][zNum - 1] = 2;
                setTileGrid(tileGridInt);
            }

            // east
            if (col + 1 < getSize() && getGrid()[row][col + 1][zNum - 1].getType() == 0
                    && !getClosedList().contains(getGrid()[row][col + 1][zNum - 1])) {
                Node[][][] grid = getGrid();
                grid[row][col + 1][zNum - 1].setParent(getCurrentNode());
                int h = calculateH(grid[row][col + 1][zNum - 1]);
                grid[row][col + 1][zNum - 1].setH(h);
                grid[row][col + 1][zNum - 1].setF();
                setGrid(grid);
                PriorityQueue<Node> openList = getOpenList();
                openList.add(grid[row][col + 1][zNum - 1]);
                setOpenList(openList);
                int[][][] tileGridInt = getTileGrid();
                tileGridInt[row][col + 1][zNum - 1] = 2;
                setTileGrid(tileGridInt);
            }

            // southeast
            if (row + 1 < getSize() && col + 1 < getSize() && getGrid()[row + 1][col + 1][zNum - 1].getType() == 0
                    && !getClosedList().contains(getGrid()[row + 1][col + 1][zNum - 1])) {
                Node[][][] grid = getGrid();
                grid[row + 1][col + 1][zNum - 1].setParent(getCurrentNode());
                int h = calculateH(grid[row + 1][col + 1][zNum - 1]);
                grid[row + 1][col + 1][zNum - 1].setH(h);
                grid[row + 1][col + 1][zNum - 1].setF();
                setGrid(grid);
                PriorityQueue<Node> openList = getOpenList();
                openList.add(grid[row + 1][col + 1][zNum - 1]);
                setOpenList(openList);
                int[][][] tileGridInt = getTileGrid();
                tileGridInt[row + 1][col + 1][zNum - 1] = 2;
                setTileGrid(tileGridInt);
            }

            // south
            if (row + 1 < getSize() && getGrid()[row + 1][col][zNum - 1].getType() == 0
                    && !getClosedList().contains(getGrid()[row + 1][col][zNum - 1])) {
                Node[][][] grid = getGrid();
                grid[row + 1][col][zNum - 1].setParent(getCurrentNode());
                int h = calculateH(grid[row + 1][col][zNum - 1]);
                grid[row + 1][col][zNum - 1].setH(h);
                grid[row + 1][col][zNum - 1].setF();
                setGrid(grid);
                PriorityQueue<Node> openList = getOpenList();
                openList.add(grid[row + 1][col][zNum - 1]);
                setOpenList(openList);
                int[][][] tileGridInt = getTileGrid();
                tileGridInt[row + 1][col][zNum - 1] = 2;
                setTileGrid(tileGridInt);
            }

            // southwest
            if (row + 1 < getSize() && col - 1 > -1 && getGrid()[row + 1][col - 1][zNum - 1].getType() == 0
                    && !getClosedList().contains(getGrid()[row + 1][col - 1][zNum - 1])) {
                Node[][][] grid = getGrid();
                grid[row + 1][col - 1][zNum - 1].setParent(getCurrentNode());
                int h = calculateH(grid[row + 1][col - 1][zNum - 1]);
                grid[row + 1][col - 1][zNum - 1].setH(h);
                grid[row + 1][col - 1][zNum - 1].setF();
                setGrid(grid);
                PriorityQueue<Node> openList = getOpenList();
                openList.add(grid[row + 1][col - 1][zNum - 1]);
                setOpenList(openList);
                int[][][] tileGridInt = getTileGrid();
                tileGridInt[row + 1][col - 1][zNum - 1] = 2;
                setTileGrid(tileGridInt);
            }

            // west
            if (col - 1 > -1 && getGrid()[row][col - 1][zNum - 1].getType() == 0
                    && !getClosedList().contains(getGrid()[row][col - 1][zNum - 1])) {
                Node[][][] grid = getGrid();
                grid[row][col - 1][zNum - 1].setParent(getCurrentNode());
                int h = calculateH(grid[row][col - 1][zNum - 1]);
                grid[row][col - 1][zNum - 1].setH(h);
                grid[row][col - 1][zNum - 1].setF();
                setGrid(grid);
                PriorityQueue<Node> openList = getOpenList();
                openList.add(grid[row][col - 1][zNum - 1]);
                setOpenList(openList);
                int[][][] tileGridInt = getTileGrid();
                tileGridInt[row][col - 1][zNum - 1] = 2;
                setTileGrid(tileGridInt);
            }

            // northwest
            if (row - 1 > -1 && col - 1 > -1 && getGrid()[row - 1][col - 1][zNum - 1].getType() == 0
                    && !getClosedList().contains(getGrid()[row - 1][col - 1][zNum - 1])) {
                Node[][][] grid = getGrid();
                grid[row - 1][col - 1][zNum - 1].setParent(getCurrentNode());
                int h = calculateH(grid[row - 1][col - 1][zNum - 1]);
                grid[row - 1][col - 1][zNum - 1].setH(h);
                grid[row - 1][col - 1][zNum - 1].setF();
                setGrid(grid);
                PriorityQueue<Node> openList = getOpenList();
                openList.add(grid[row - 1][col - 1][zNum - 1]);
                setOpenList(openList);
                int[][][] tileGridInt = getTileGrid();
                tileGridInt[row - 1][col - 1][zNum - 1] = 2;
                setTileGrid(tileGridInt);
            }
        }

        // upper layer
        if (zNum + 1 < getSize()) {
            if (getGrid()[row][col][zNum + 1].getType() == 0
                    && !getClosedList().contains(getGrid()[row][col][zNum + 1])) {
                Node[][][] grid = getGrid();
                grid[row][col][zNum + 1].setParent(getCurrentNode());
                int h = calculateH(grid[row][col][zNum + 1]);
                grid[row][col][zNum + 1].setH(h);
                grid[row][col][zNum + 1].setF();
                setGrid(grid);
                PriorityQueue<Node> openList = getOpenList();
                openList.add(grid[row][col][zNum + 1]);
                setOpenList(openList);
                int[][][] tileGridInt = getTileGrid();
                tileGridInt[row][col][zNum + 1] = 2;
                setTileGrid(tileGridInt);
            }

            // northeast
            if (row - 1 > -1 && col + 1 < getSize() && getGrid()[row - 1][col + 1][zNum + 1].getType() == 0
                    && !getClosedList().contains(getGrid()[row - 1][col + 1][zNum + 1])) {
                Node[][][] grid = getGrid();
                grid[row - 1][col + 1][zNum + 1].setParent(getCurrentNode());
                int h = calculateH(grid[row - 1][col + 1][zNum + 1]);
                grid[row - 1][col + 1][zNum + 1].setH(h);
                grid[row - 1][col + 1][zNum + 1].setF();
                setGrid(grid);
                PriorityQueue<Node> openList = getOpenList();
                openList.add(grid[row - 1][col + 1][zNum + 1]);
                setOpenList(openList);
                int[][][] tileGridInt = getTileGrid();
                tileGridInt[row - 1][col + 1][zNum + 1] = 2;
                setTileGrid(tileGridInt);
            }

            // east
            if (col + 1 < getSize() && getGrid()[row][col + 1][zNum + 1].getType() == 0
                    && !getClosedList().contains(getGrid()[row][col + 1][zNum + 1])) {
                Node[][][] grid = getGrid();
                grid[row][col + 1][zNum + 1].setParent(getCurrentNode());
                int h = calculateH(grid[row][col + 1][zNum + 1]);
                grid[row][col + 1][zNum + 1].setH(h);
                grid[row][col + 1][zNum + 1].setF();
                setGrid(grid);
                PriorityQueue<Node> openList = getOpenList();
                openList.add(grid[row][col + 1][zNum + 1]);
                setOpenList(openList);
                int[][][] tileGridInt = getTileGrid();
                tileGridInt[row][col + 1][zNum + 1] = 2;
                setTileGrid(tileGridInt);
            }

            // southeast
            if (row + 1 < getSize() && col + 1 < getSize() && getGrid()[row + 1][col + 1][zNum + 1].getType() == 0
                    && !getClosedList().contains(getGrid()[row + 1][col + 1][zNum + 1])) {
                Node[][][] grid = getGrid();
                grid[row + 1][col + 1][zNum + 1].setParent(getCurrentNode());
                int h = calculateH(grid[row + 1][col + 1][zNum + 1]);
                grid[row + 1][col + 1][zNum + 1].setH(h);
                grid[row + 1][col + 1][zNum + 1].setF();
                setGrid(grid);
                PriorityQueue<Node> openList = getOpenList();
                openList.add(grid[row + 1][col + 1][zNum + 1]);
                setOpenList(openList);
                int[][][] tileGridInt = getTileGrid();
                tileGridInt[row + 1][col + 1][zNum + 1] = 2;
                setTileGrid(tileGridInt);
            }

            // south
            if (row + 1 < getSize() && getGrid()[row + 1][col][zNum + 1].getType() == 0
                    && !getClosedList().contains(getGrid()[row + 1][col][zNum + 1])) {
                Node[][][] grid = getGrid();
                grid[row + 1][col][zNum + 1].setParent(getCurrentNode());
                int h = calculateH(grid[row + 1][col][zNum + 1]);
                grid[row + 1][col][zNum + 1].setH(h);
                grid[row + 1][col][zNum + 1].setF();
                setGrid(grid);
                PriorityQueue<Node> openList = getOpenList();
                openList.add(grid[row + 1][col][zNum + 1]);
                setOpenList(openList);
                int[][][] tileGridInt = getTileGrid();
                tileGridInt[row + 1][col][zNum + 1] = 2;
                setTileGrid(tileGridInt);
            }

            // southwest
            if (row + 1 < getSize() && col - 1 > -1 && getGrid()[row + 1][col - 1][zNum + 1].getType() == 0
                    && !getClosedList().contains(getGrid()[row + 1][col - 1][zNum + 1])) {
                Node[][][] grid = getGrid();
                grid[row + 1][col - 1][zNum + 1].setParent(getCurrentNode());
                int h = calculateH(grid[row + 1][col - 1][zNum + 1]);
                grid[row + 1][col - 1][zNum + 1].setH(h);
                grid[row + 1][col - 1][zNum + 1].setF();
                setGrid(grid);
                PriorityQueue<Node> openList = getOpenList();
                openList.add(grid[row + 1][col - 1][zNum + 1]);
                setOpenList(openList);
                int[][][] tileGridInt = getTileGrid();
                tileGridInt[row + 1][col - 1][zNum + 1] = 2;
                setTileGrid(tileGridInt);
            }

            // west
            if (col - 1 > -1 && getGrid()[row][col - 1][zNum + 1].getType() == 0
                    && !getClosedList().contains(getGrid()[row][col - 1][zNum + 1])) {
                Node[][][] grid = getGrid();
                grid[row][col - 1][zNum + 1].setParent(getCurrentNode());
                int h = calculateH(grid[row][col - 1][zNum + 1]);
                grid[row][col - 1][zNum + 1].setH(h);
                grid[row][col - 1][zNum + 1].setF();
                setGrid(grid);
                PriorityQueue<Node> openList = getOpenList();
                openList.add(grid[row][col - 1][zNum + 1]);
                setOpenList(openList);
                int[][][] tileGridInt = getTileGrid();
                tileGridInt[row][col - 1][zNum + 1] = 2;
                setTileGrid(tileGridInt);
            }

            // northwest
            if (row - 1 > -1 && col - 1 > -1 && getGrid()[row - 1][col - 1][zNum + 1].getType() == 0
                    && !getClosedList().contains(getGrid()[row - 1][col - 1][zNum + 1])) {
                Node[][][] grid = getGrid();
                grid[row - 1][col - 1][zNum + 1].setParent(getCurrentNode());
                int h = calculateH(grid[row - 1][col - 1][zNum + 1]);
                grid[row - 1][col - 1][zNum + 1].setH(h);
                grid[row - 1][col - 1][zNum + 1].setF();
                setGrid(grid);
                PriorityQueue<Node> openList = getOpenList();
                openList.add(grid[row - 1][col - 1][zNum + 1]);
                setOpenList(openList);
                int[][][] tileGridInt = getTileGrid();
                tileGridInt[row - 1][col - 1][zNum + 1] = 2;
                setTileGrid(tileGridInt);
            }
        }
    }
}
