package com.gubertmc.plugin.main.mazegenerator.algorithms.astar.astar3d;

import com.gubertmc.MazeGeneratorPlugin;
import com.gubertmc.plugin.main.mazegenerator.algorithms.Animation;
import com.gubertmc.plugin.main.mazegenerator.algorithms.Node;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class AstarAnimation3D extends Animation {

    public AstarAnimation3D(
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
                endGlassMaterial,
                true
        );
    }

    @Override
    public void setup() {
        setGrid(new Node[getSize()][getSize()][getSize()]);

        int[][][] tempArray = new int[getSize()][getSize()][getSize()];
        for (int i = 0; i < getSize(); i++) {
            for (int j = 0; j < getSize(); j++) {
                for (int k = 0; k < getSize(); k++) {
                    tempArray[i][j][k] = 0;
                }
            }
        }
        setTileGridInt(tempArray);

        setCurrentNode(new Node(getStartCoordinate()[1], getStartCoordinate()[0], getStartCoordinate()[2], 0));
        setEndNode(new Node(getEndCoordinate()[1], getEndCoordinate()[0], getEndCoordinate()[2], 0));
        Node[][][] grid = getGrid();
        grid[getStartCoordinate()[1]][getStartCoordinate()[0]][getStartCoordinate()[2]] = getCurrentNode();
        setGrid(grid);
        grid[getEndCoordinate()[1]][getEndCoordinate()[0]][getEndCoordinate()[2]] = getEndNode();
        setGrid(grid);

        for (int i = 0; i < getSize(); i++) {
            for (int j = 0; j < getSize(); j++) {
                for (int k = 0; k < getSize(); k++) {
                    if (getTileGrid()[i][j][k].getBlock().getType() == getPathGroundMaterial()) {
                        Node node = new Node(i, j, k, 0);
                        grid[i][j][k] = node;
                        setGrid(grid);
                    }
                    if (getTileGrid()[i][j][k].getBlock().getType() == getWallMaterial()) {
                        Node node = new Node(i, j, k, 1);
                        grid[i][j][k] = node;
                        setGrid(grid);
                    }
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
                setClosedList(closedList);

                ArrayList<Node> path = generatePath();

                for (int i = path.size() - 1; i > -1; i--) {
                    int row = path.get(i).getRow();
                    int col = path.get(i).getCol();
                    int zNum = path.get(i).getZ();

                    if (getTileGridInt()[row][col][zNum] == 1) {
                        int x = getTileGrid()[row][col][zNum].getBlockX();
                        int y = getTileGrid()[row][col][zNum].getBlockY();
                        int z = getTileGrid()[row][col][zNum].getBlockZ();

                        Location floor = new Location(getTileGrid()[row][col][zNum].getWorld(), x, y, z);
                        ArrayList<Location> thePath = getThePath();
                        thePath.add(floor);
                        setThePath(thePath);
                    }
                }
                break;
            } else {
                try {
                    calculateNeighborValues();
                } catch (NullPointerException e) {
                    System.out.println(e);
                }

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

    @Override
    public void showAnimation(long time) {
        time += 50L;
        int count = 1;
        for (Location loc : getExploredPlaces()) {
            runnableDelayed(loc, time, getPathSpreadMaterial());
            count++;
            if (count % (int) (getSize() * 0.25) == 0) {
                time += 1L;
            }
        }

        time += 10L;
        for (Location loc : getThePath()) {
            if (getThePath().get(getThePath().size() - 1) == loc) {
                // do something cool
            } else {
                runnableDelayed(loc, time, getPathMaterial());
                time += 1L;
            }
        }
    }

    @Override
    public void runnableDelayed(Location loc, long time, Material material) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (loc.getBlock().getType() != Material.BEACON) {
                    loc.getBlock().setType(material);
                }
                cancel();
            }
        }.runTaskTimer(getPlugin(), time, 20L);
    }

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

    public int calculateH(Node node) {
        int row = node.getRow();
        int col = node.getCol();
        int zNum = node.getZ();
        int x = 0;
        int y = 0;
        int z = 0;

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
        while (zNum < getEndNode().getZ() || zNum > getEndNode().getZ()) {
            z += 10;
            if (zNum < getEndNode().getZ()) {
                zNum++;
            }
            if (zNum > getEndNode().getZ()) {
                zNum--;
            }
        }

        return x + y + z;
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

    public void calculateNeighborValues() {
        int row = getCurrentNode().getRow();
        int col = getCurrentNode().getCol();
        int zNum = getCurrentNode().getZ();

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
            int[][][] tileGridInt = getTileGridInt();
            tileGridInt[row - 1][col][zNum] = 1;
            setTileGridInt(tileGridInt);

            Location loc = getTileGrid()[row - 1][col][zNum];
            if (!getExploredPlaces().contains(loc)) {
                loc = new Location(
                        loc.getWorld(), loc.getBlock().getX(), loc.getBlock().getY(), loc.getBlock().getZ()
                );
                ArrayList<Location> exploredPlaces = getExploredPlaces();
                exploredPlaces.add(loc);
                setExploredPlaces(exploredPlaces);
            }
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
            int[][][] tileGridInt = getTileGridInt();
            tileGridInt[row][col + 1][zNum] = 1;
            setTileGridInt(tileGridInt);

            Location loc = getTileGrid()[row][col + 1][zNum];
            if (!getExploredPlaces().contains(loc)) {
                loc = new Location(
                        loc.getWorld(), loc.getBlock().getX(), loc.getBlock().getY(), loc.getBlock().getZ()
                );
                ArrayList<Location> exploredPlaces = getExploredPlaces();
                exploredPlaces.add(loc);
                setExploredPlaces(exploredPlaces);
            }
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
            int[][][] tileGridInt = getTileGridInt();
            tileGridInt[row + 1][col][zNum] = 1;
            setTileGridInt(tileGridInt);

            Location loc = getTileGrid()[row + 1][col][zNum];
            if (!getExploredPlaces().contains(loc)) {
                loc = new Location(
                        loc.getWorld(), loc.getBlock().getX(), loc.getBlock().getY(), loc.getBlock().getZ()
                );
                ArrayList<Location> exploredPlaces = getExploredPlaces();
                exploredPlaces.add(loc);
                setExploredPlaces(exploredPlaces);
            }
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
            int[][][] tileGridInt = getTileGridInt();
            tileGridInt[row][col - 1][zNum] = 1;
            setTileGridInt(tileGridInt);

            Location loc = getTileGrid()[row][col - 1][zNum];
            if (!getExploredPlaces().contains(loc)) {
                loc = new Location(
                        loc.getWorld(), loc.getBlock().getX(), loc.getBlock().getY(), loc.getBlock().getZ()
                );
                ArrayList<Location> exploredPlaces = getExploredPlaces();
                exploredPlaces.add(loc);
                setExploredPlaces(exploredPlaces);
            }
        }

        // bottom node
        if (zNum - 1 > -1 && getGrid()[row][col][zNum - 1].getType() == 0
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
            int[][][] tileGridInt = getTileGridInt();
            tileGridInt[row][col][zNum - 1] = 1;
            setTileGridInt(tileGridInt);

            Location loc = getTileGrid()[row][col][zNum - 1];
            if (!getExploredPlaces().contains(loc)) {
                loc = new Location(
                        loc.getWorld(), loc.getBlock().getX(), loc.getBlock().getY(), loc.getBlock().getZ()
                );
                ArrayList<Location> exploredPlaces = getExploredPlaces();
                exploredPlaces.add(loc);
                setExploredPlaces(exploredPlaces);
            }
        }

        // top node
        if (zNum + 1 < getSize() && getGrid()[row][col][zNum + 1].getType() == 0
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
            int[][][] tileGridInt = getTileGridInt();
            tileGridInt[row][col][zNum + 1] = 1;
            setTileGridInt(tileGridInt);

            Location loc = getTileGrid()[row][col][zNum + 1];
            if (!getExploredPlaces().contains(loc)) {
                loc = new Location(
                        loc.getWorld(), loc.getBlock().getX(), loc.getBlock().getY(), loc.getBlock().getZ()
                );
                ArrayList<Location> exploredPlaces = getExploredPlaces();
                exploredPlaces.add(loc);
                setExploredPlaces(exploredPlaces);
            }
        }
    }
}
