package com.gubertmc.plugin.main.snakegame;

import com.gubertmc.MazeGeneratorPlugin;
import com.gubertmc.plugin.main.snakegame.logicalcomponents.Node;
import com.gubertmc.plugin.main.snakegame.logicalcomponents.NodeComparator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class Ai {

    private final MazeGeneratorPlugin plugin;
    private final int size;
    private Node startNode;
    private Node targetNode;
    private Node currentNode;
    private Material snakeBodyMaterial;
    private Material foodMaterial;
    private Location[][] arenaBlockLocations;

    private ArrayList<Node> closedList = new ArrayList<>();
    private final PriorityQueue<Node> openList = new PriorityQueue<>(10, new NodeComparator());
    ArrayList<Location> exploredPlaces = new ArrayList<>();

    private Node[][] arenaNodes;

    public Ai(
            MazeGeneratorPlugin plugin,
            Location[][] arenaBlockLocations,
            int startRow,
            int startCol,
            int targetRow,
            int targetCol,
            Material snakeBodyMaterial,
            Material foodMaterial
    ) {
        this.plugin = plugin;
        this.arenaBlockLocations = arenaBlockLocations;
        this.size = arenaBlockLocations[0].length;
        this.snakeBodyMaterial = snakeBodyMaterial;
        this.foodMaterial = foodMaterial;

        currentNode = new Node(arenaBlockLocations[startRow][startCol], startRow, startCol, 0);
        targetNode = new Node(arenaBlockLocations[targetRow][targetCol], targetRow, targetCol, 0);
        arenaNodes = new Node[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Node node = new Node(arenaBlockLocations[i][j], i, j, 0);
                arenaNodes[i][j] = node;
            }
        }
        arenaNodes[startRow][startCol] = currentNode;
        arenaNodes[targetRow][targetCol] = targetNode;

        int g = calculateG(currentNode);
        currentNode.setG(g);

        int h = calculateH(currentNode);
        currentNode.setH(h);

        currentNode.setF();

        startNode = currentNode;

        openList.add(currentNode);
    }

    // 0 is open space, 1 is explored, 2 is start, 3 is target, 4 is finalPath
    public void start() {
        long time = 0L;
        while (!openList.isEmpty() && !currentNode.equals(targetNode)) {
            currentNode = openList.peek();
            openList.remove(openList.peek());

            if (currentNode.equals(targetNode)) {
                System.out.println("current node = target node");
                closedList.add(currentNode);
                ArrayList<Node> path = generatePath();
                for (int i = path.size() - 1; i > -1; i--) {
                    int row = path.get(i).getRow();
                    int col = path.get(i).getCol();
                    arenaBlockLocations[row][col].getBlock().setType(Material.GREEN_WOOL);
                    arenaNodes[row][col].setType(4);
                    runnableDelayed(arenaNodes[row][col], time, snakeBodyMaterial, row, col);
                    time += 5L;
                }
                break;
            } else {
                calculateNeighborValues();
                arenaBlockLocations[targetNode.getRow()][targetNode.getCol()].getBlock().setType(foodMaterial);
                try {
                    assert openList.peek() != null;
                    System.out.println("GAME OVER!");
                } catch (NullPointerException e){
                    System.out.println("No path could be found");
                }
                closedList.add(currentNode);
            }
        }
    }



    public void calculateNeighborValues() {
        System.out.println("CALCULATING NEIGHBORS");
        int row = currentNode.getRow();
        int col = currentNode.getCol();

        // front node
        if (row - 1 > -1 && arenaNodes[row - 1][col].getType() == 0
                && !closedList.contains(arenaNodes[row - 1][col])) {
            arenaNodes[row - 1][col].setParent(currentNode);
            int g = calculateG(arenaNodes[row - 1][col]);
            arenaNodes[row - 1][col].setG(g);
            int h = calculateH(arenaNodes[row - 1][col]);
            arenaNodes[row - 1][col].setH(h);
            arenaNodes[row - 1][col].setF();
            openList.add(arenaNodes[row - 1][col]);
            arenaNodes[row - 1][col].setType(1);
            if (!exploredPlaces.contains(arenaNodes[row - 1][col].getLocation())) {
                exploredPlaces.add(arenaNodes[row - 1][col].getLocation());
            }
        }

        // left node
        if (col + 1 < size && arenaNodes[row][col + 1].getType() == 0
                && !closedList.contains(arenaNodes[row][col + 1])) {
            arenaNodes[row][col + 1].setParent(currentNode);
            int g = calculateG(arenaNodes[row][col + 1]);
            arenaNodes[row][col + 1].setG(g);
            int h = calculateH(arenaNodes[row][col + 1]);
            arenaNodes[row][col + 1].setH(h);
            arenaNodes[row][col + 1].setF();
            openList.add(arenaNodes[row][col + 1]);
            arenaNodes[row][col + 1].setType(1);
            if (!exploredPlaces.contains(arenaNodes[row][col + 1].getLocation())) {
                exploredPlaces.add(arenaNodes[row][col + 1].getLocation());
            }
        }

        // behind node
        if (row + 1 < size && arenaNodes[row + 1][col].getType() == 0
                && !closedList.contains(arenaNodes[row + 1][col])) {
            arenaNodes[row + 1][col].setParent(currentNode);
            int g = calculateG(arenaNodes[row + 1][col]);
            arenaNodes[row + 1][col].setG(g);
            int h = calculateH(arenaNodes[row + 1][col]);
            arenaNodes[row + 1][col].setH(h);
            arenaNodes[row + 1][col].setF();
            openList.add(arenaNodes[row + 1][col]);
            arenaNodes[row + 1][col].setType(1);
            if (!exploredPlaces.contains(arenaNodes[row + 1][col].getLocation())) {
                exploredPlaces.add(arenaNodes[row + 1][col].getLocation());
            }
        }

        // right node
        if (col - 1 > -1 && arenaNodes[row][col - 1].getType() == 0
                && !closedList.contains(arenaNodes[row][col - 1])) {
            arenaNodes[row][col - 1].setParent(currentNode);
            int g = calculateG(arenaNodes[row][col - 1]);
            arenaNodes[row][col - 1].setG(g);
            int h = calculateH(arenaNodes[row][col - 1]);
            arenaNodes[row][col - 1].setH(h);
            arenaNodes[row][col - 1].setF();
            openList.add(arenaNodes[row][col - 1]);
            arenaNodes[row][col - 1].setType(1);
            if (!exploredPlaces.contains(arenaNodes[row][col - 1].getLocation())) {
                exploredPlaces.add(arenaNodes[row][col - 1].getLocation());
            }
        }

    }

    /*
     * method that calculates distance from start
     */
    public int calculateG(Node node) {
        int row = node.getRow();
        int col = node.getCol();
        if (row == currentNode.getRow() && col == currentNode.getCol()) {
            return 0;
        }

        Node parent = node.getParent();
        if (parent == null) {
            int xDistance;
            if (col > currentNode.getCol()) {
                xDistance = col - currentNode.getCol();
            } else {
                xDistance = currentNode.getCol() - col;
            }
            int yDistance;
            if (row > currentNode.getRow()) {
                yDistance = row - currentNode.getRow();
            } else {
                yDistance = currentNode.getRow() - row;
            }
            return (xDistance * 10) + (yDistance * 10);
        }
        return 10 + parent.getG();
    }


    /*
     * method that calculates the heuristic (distance of a node from the goal)
     */
    public int calculateH(Node node) {
        int row = node.getRow();
        int col = node.getCol();
        int x = 0;
        int y = 0;
        while (col < targetNode.getCol() || col > targetNode.getCol()) {
            x += 10;
            if (col < targetNode.getCol()) {
                col++;
            }
            if (col > targetNode.getCol()) {
                col--;
            }
        }
        while (row < targetNode.getRow() || row > targetNode.getRow()) {
            y += 10;
            if (row < targetNode.getRow()) {
                row++;
            }
            if (row > targetNode.getRow()) {
                row--;
            }
        }
        return x + y;
    }

    public ArrayList<Node> generatePath() {
        ArrayList<Node> path = new ArrayList<>();
        // get the parent nodes
        Node temp = currentNode;
        path.add(temp);
        while(temp.getParent() != null) {
            temp = temp.getParent();
            path.add(temp);
        }
        return path;
    }

    public void runnableDelayed(Node node, long time, Material material, int row, int col) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (row < 0 || row > size || col < 0 || col > size) {
                    node.getLocation().getBlock().setType(material);
                    cancel();
                } else {
                    node.getLocation().getBlock().setType(material);

                    arenaNodes[row][col] = node;
                    cancel();
                }
            }
        }.runTaskTimer(plugin, time, 20L);
    }
}
