package com.gubertmc.plugin.main.mazegenerator.algorithms.astar.astar3d.diagonalmovement;

import com.gubertmc.plugin.main.mazegenerator.algorithms.Node;
import com.gubertmc.plugin.main.mazegenerator.algorithms.astar.astar3d.AstarSimulation3D;

import java.util.PriorityQueue;

public class AstarSimulationDiagonal3D extends AstarSimulation3D {

    public AstarSimulationDiagonal3D(int[][][] maze, int[] startCoordinate, int[] endCoordinate) {
        super(maze, startCoordinate, endCoordinate);
    }

    @Override
    public int calculateG(Node node) {
        int row = node.getRow();
        int col = node.getCol();
        int zNum = node.getZ();
        if (row == getCurrentNode().getRow() && col == getCurrentNode().getCol() && zNum == getCurrentNode().getZ()) {
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

            int zDistance;
            if (zNum > getCurrentNode().getZ()) {
                zDistance = zNum - getCurrentNode().getZ();
            } else {
                zDistance = getCurrentNode().getZ() - zNum;
            }

            if (zNum == -1) {
                zDistance = 0;
            }

            return (xDistance * 10) + (yDistance * 10) + (zDistance * 10);
        }
        return 10 + parent.getG();
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
            int g = calculateG(grid[row - 1][col][zNum]);
            grid[row - 1][col][zNum].setG(g);
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
            int g = calculateG(grid[row - 1][col + 1][zNum]);
            grid[row - 1][col + 1][zNum].setG(g);
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
            int g = calculateG(grid[row][col + 1][zNum]);
            grid[row][col + 1][zNum].setG(g);
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
            int g = calculateG(grid[row + 1][col + 1][zNum]);
            grid[row + 1][col + 1][zNum].setG(g);
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
            int g = calculateG(grid[row + 1][col][zNum]);
            grid[row + 1][col][zNum].setG(g);
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
            int g = calculateG(grid[row + 1][col - 1][zNum]);
            grid[row + 1][col - 1][zNum].setG(g);
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
            int g = calculateG(grid[row][col - 1][zNum]);
            grid[row][col - 1][zNum].setG(g);
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
            int g = calculateG(grid[row - 1][col - 1][zNum]);
            grid[row - 1][col - 1][zNum].setG(g);
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
        if (zNum - 1 > -1) {
            // down
            if (getGrid()[row][col][zNum - 1].getType() == 0
                    && !getClosedList().contains(getGrid()[row][col][zNum - 1])) {
                Node[][][] grid = getGrid();
                grid[row][col][zNum - 1].setParent(getCurrentNode());
                int g = calculateG(grid[row][col][zNum - 1]);
                grid[row][col][zNum - 1].setG(g);
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
                int g = calculateG(grid[row - 1][col][zNum - 1]);
                grid[row - 1][col][zNum - 1].setG(g);
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
            if ( row - 1 > -1 && col + 1 < getSize() && getGrid()[row - 1][col + 1][zNum - 1].getType() == 0
                    && !getClosedList().contains(getGrid()[row - 1][col + 1][zNum - 1])) {
                Node[][][] grid = getGrid();
                grid[row - 1][col + 1][zNum - 1].setParent(getCurrentNode());
                int g = calculateG(grid[row - 1][col + 1][zNum - 1]);
                grid[row - 1][col + 1][zNum - 1].setG(g);
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
            if ( col + 1 < getSize() && getGrid()[row][col + 1][zNum - 1].getType() == 0
                    && !getClosedList().contains(getGrid()[row][col + 1][zNum - 1])) {
                Node[][][] grid = getGrid();
                grid[row][col + 1][zNum - 1].setParent(getCurrentNode());
                int g = calculateG(grid[row][col + 1][zNum - 1]);
                grid[row][col + 1][zNum - 1].setG(g);
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
            if ( row + 1 < getSize() && col + 1 < getSize() && getGrid()[row + 1][col + 1][zNum - 1].getType() == 0
                    && !getClosedList().contains(getGrid()[row + 1][col + 1][zNum - 1])) {
                Node[][][] grid = getGrid();
                grid[row + 1][col + 1][zNum - 1].setParent(getCurrentNode());
                int g = calculateG(grid[row + 1][col + 1][zNum - 1]);
                grid[row + 1][col + 1][zNum - 1].setG(g);
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
            if ( row + 1 < getSize() && getGrid()[row + 1][col][zNum - 1].getType() == 0
                    && !getClosedList().contains(getGrid()[row + 1][col][zNum - 1])) {
                Node[][][] grid = getGrid();
                grid[row + 1][col][zNum - 1].setParent(getCurrentNode());
                int g = calculateG(grid[row + 1][col][zNum - 1]);
                grid[row + 1][col][zNum - 1].setG(g);
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
            if ( row + 1 < getSize() && col - 1 > -1 && getGrid()[row + 1][col - 1][zNum - 1].getType() == 0
                    && !getClosedList().contains(getGrid()[row + 1][col - 1][zNum - 1])) {
                Node[][][] grid = getGrid();
                grid[row + 1][col - 1][zNum - 1].setParent(getCurrentNode());
                int g = calculateG(grid[row + 1][col - 1][zNum - 1]);
                grid[row + 1][col - 1][zNum - 1].setG(g);
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
            if ( col - 1 > -1 && getGrid()[row][col - 1][zNum - 1].getType() == 0
                    && !getClosedList().contains(getGrid()[row][col - 1][zNum - 1])) {
                Node[][][] grid = getGrid();
                grid[row][col - 1][zNum - 1].setParent(getCurrentNode());
                int g = calculateG(grid[row][col - 1][zNum - 1]);
                grid[row][col - 1][zNum - 1].setG(g);
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
            if ( row - 1 > -1 && col - 1 > -1 && getGrid()[row - 1][col - 1][zNum - 1].getType() == 0
                    && !getClosedList().contains(getGrid()[row - 1][col - 1][zNum - 1])) {
                Node[][][] grid = getGrid();
                grid[row - 1][col - 1][zNum - 1].setParent(getCurrentNode());
                int g = calculateG(grid[row - 1][col - 1][zNum - 1]);
                grid[row - 1][col - 1][zNum - 1].setG(g);
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
            // up
            if (getGrid()[row][col][zNum + 1].getType() == 0
                    && !getClosedList().contains(getGrid()[row][col][zNum + 1])) {
                Node[][][] grid = getGrid();
                grid[row][col][zNum + 1].setParent(getCurrentNode());
                int g = calculateG(grid[row][col][zNum + 1]);
                grid[row][col][zNum + 1].setG(g);
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
                int g = calculateG(grid[row - 1][col + 1][zNum + 1]);
                grid[row - 1][col + 1][zNum + 1].setG(g);
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
                int g = calculateG(grid[row][col + 1][zNum + 1]);
                grid[row][col + 1][zNum + 1].setG(g);
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
                int g = calculateG(grid[row + 1][col + 1][zNum + 1]);
                grid[row + 1][col + 1][zNum + 1].setG(g);
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
                int g = calculateG(grid[row + 1][col][zNum + 1]);
                grid[row + 1][col][zNum + 1].setG(g);
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
                int g = calculateG(grid[row + 1][col - 1][zNum + 1]);
                grid[row + 1][col - 1][zNum + 1].setG(g);
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
                int g = calculateG(grid[row][col - 1][zNum + 1]);
                grid[row][col - 1][zNum + 1].setG(g);
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
                int g = calculateG(grid[row - 1][col - 1][zNum + 1]);
                grid[row - 1][col - 1][zNum + 1].setG(g);
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
