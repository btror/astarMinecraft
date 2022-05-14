package com.gubertmc.plugin.main.algorithms.astar.astar2d;

import com.gubertmc.plugin.main.algorithms.Simulation;
import com.gubertmc.plugin.main.algorithms.Node;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class AstarSimulation2D extends Simulation {

    public AstarSimulation2D(int[][][] maze, int[] startCoordinate, int[] endCoordinate) {
        super(maze, startCoordinate, endCoordinate, false);
    }

    @Override
    public void setup() {
        setCurrentNode(new Node(getStartCoordinate()[1], getStartCoordinate()[0], -1, 0));
        setEndNode(new Node(getEndCoordinate()[1], getEndCoordinate()[0], -1, 0));
        Node[][][] grid = getGrid();
        grid[getStartCoordinate()[1]][getStartCoordinate()[0]][0] = getCurrentNode();
        grid[getEndCoordinate()[1]][getEndCoordinate()[0]][0] = getEndNode();
        setGrid(grid);

        for (int i = 0; i < getSize(); i++) {
            for (int j = 0; j < getSize(); j++) {
                if (getTileGrid()[i][j][0] == 0) {
                    Node node = new Node(i, j, -1, 0);
                    grid[i][j][0] = node;
                    setGrid(grid);
                }
                if (getTileGrid()[i][j][0] == 1) {
                    Node node = new Node(i, j, -1, 1);
                    grid[i][j][0] = node;
                    setGrid(grid);
                }
            }
        }

        Node currentNode = getCurrentNode();
        int g = calculateG(currentNode);
        currentNode.setG(g);
        int h = calculateH(currentNode);
        currentNode.setH(h);
        currentNode.setF();
        setCurrentNode(currentNode);
        setStartNode(currentNode);
        PriorityQueue<Node> openList = getOpenList();
        openList.add(currentNode);
        setOpenList(openList);
    }

    @Override
    public boolean start() {
        boolean pathFound = true;
        while (!getOpenList().isEmpty() && !getCurrentNode().equals(getEndNode())) {
            setCurrentNode(getOpenList().peek());
            PriorityQueue<Node> openList = getOpenList();
            openList.remove(getOpenList().peek());
            setOpenList(openList);

            if (getCurrentNode().equals(getEndNode())) {
                ArrayList<Node> closedList = getClosedList();
                closedList.add(getCurrentNode());

                ArrayList<Node> path = generatePath();

                for (int i = path.size() - 1; i > -1; i--) {
                    int row = path.get(i).getRow();
                    int col = path.get(i).getCol();
                    int zNum = 0;

                    if (getTileGrid()[row][col][zNum] == 2) {
                        int[][][] tileGrid = getTileGrid();
                        tileGrid[row][col][zNum] = 3;
                        setTileGrid(tileGrid);
                    }
                }
                break;
            } else {
                try {
                    calculateNeighborValues();
                } catch (NullPointerException e) {
                    System.out.println(e.getMessage());
                }
                int[][][] tileGrid = getTileGrid();
                tileGrid[getStartNode().getRow()][getStartNode().getCol()][0] = 4;
                tileGrid[getEndNode().getRow()][getEndNode().getCol()][0] = 5;
                setTileGrid(tileGrid);
                try {
                    assert getOpenList().peek() != null;
                } catch (NullPointerException e) {
                    pathFound = false;
                }
                ArrayList<Node> closedList = getClosedList();
                closedList.add(getCurrentNode());
                setClosedList(closedList);
            }
        }

        if (getOpenList().size() == 0) {
            pathFound = false;
        }
        return pathFound;
    }

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

            return (xDistance * 10) + (yDistance * 10);
        }
        return 10 + parent.getG();
    }

    public int calculateH(Node node) {
        int row = node.getRow();
        int col = node.getCol();
        int x = 0;
        int y = 0;

        while (col < getEndNode().getCol() || col > getEndNode().getCol()) {
            x += 10;
            if (col < getEndNode().getCol()) {
                col++;
            }
            if (col > getEndNode().getCol()) {
                col--;
            }
        }
        while (row < getEndNode().getRow() || row > getEndNode().getRow()) {
            y += 10;
            if (row < getEndNode().getRow()) {
                row++;
            }
            if (row > getEndNode().getRow()) {
                row--;
            }
        }

        return x + y;
    }

    public void calculateNeighborValues() {
        int row = getCurrentNode().getRow();
        int col = getCurrentNode().getCol();
        int zNum = 0;

        // front node
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
            int[][][] tileGrid = getTileGrid();
            tileGrid[row - 1][col][zNum] = 2;
            setTileGrid(tileGrid);
        }

        // left node
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
            int[][][] tileGrid = getTileGrid();
            tileGrid[row][col + 1][zNum] = 2;
            setTileGrid(tileGrid);
        }

        // behind node
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
            int[][][] tileGrid = getTileGrid();
            tileGrid[row + 1][col][zNum] = 2;
            setTileGrid(tileGrid);
        }

        // right node
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
            int[][][] tileGrid = getTileGrid();
            tileGrid[row][col - 1][zNum] = 2;
            setTileGrid(tileGrid);
        }
    }

    public ArrayList<Node> generatePath() {
        ArrayList<Node> path = new ArrayList<>();
        Node temp = getCurrentNode();
        path.add(temp);
        while (temp.getParent() != null) {
            temp = temp.getParent();
            path.add(temp);
        }
        return path;
    }
}
