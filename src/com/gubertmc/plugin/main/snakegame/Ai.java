package com.gubertmc.plugin.main.snakegame;

import com.gubertmc.MazeGeneratorPlugin;
import com.gubertmc.plugin.main.snakegame.logicalcomponents.Node;
import com.gubertmc.plugin.main.snakegame.logicalcomponents.NodeComparator;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.scheduler.BukkitRunnable;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;

public class Ai {

    private final MazeGeneratorPlugin plugin;
    private final int size;
    private Node startNode;
    private Node targetNode;
    private Node currentNode;
    private final Material snakeBodyMaterial;
    private final Material foodMaterial;
    private Location[][] arenaBlockLocations;
    private ArrayList<Node> snakeBody = new ArrayList<>();

    private ArrayList<Node> closedList = new ArrayList<>();
    private final PriorityQueue<Node> openList = new PriorityQueue<>(10, new NodeComparator());
    ArrayList<Location> exploredPlaces = new ArrayList<>();

    private Node[][] arenaNodes;
    private long time;
    private ArrayList<Node> oldSnakeBodyNodes = new ArrayList<>();

    public Ai(
            MazeGeneratorPlugin plugin,
            Location[][] arenaBlockLocations,
            int startRow,
            int startCol
    ) {
        this.plugin = plugin;
        this.arenaBlockLocations = arenaBlockLocations;
        this.size = arenaBlockLocations[0].length;
        this.snakeBodyMaterial = Material.GREEN_WOOL;
        this.foodMaterial = Material.YELLOW_WOOL;

        int targetRow = (int) (Math.random() * arenaBlockLocations[0].length);
        int targetCol = (int) (Math.random() * arenaBlockLocations[0].length);

        currentNode = new Node(arenaBlockLocations[startRow][startCol], startRow, startCol, 0);
        targetNode = new Node(arenaBlockLocations[targetRow][targetCol], targetRow, targetCol, 0);
        arenaNodes = new Node[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (arenaBlockLocations[i][j].getBlock().getType() == snakeBodyMaterial) {
                    Node node = new Node(arenaBlockLocations[i][j], i, j, 4);
                    arenaNodes[i][j] = node;
                } else {
                    Node node = new Node(arenaBlockLocations[i][j], i, j, 0);
                    arenaNodes[i][j] = node;
                }
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
        snakeBody.add(currentNode);
        arenaBlockLocations[targetNode.getRow()][targetNode.getCol()].getBlock().setType(foodMaterial);
    }

    // 0 is open space, 1 is explored, 2 is start, 3 is target, 4 is finalPath
    public void start() {
        time = 0L;

        // open list must have something and the current node cannot be the target node
        while (!openList.isEmpty() && !currentNode.equals(targetNode)) {
            currentNode = openList.peek();
            openList.remove(openList.peek());

            if (currentNode.equals(targetNode)) {
                System.out.println("current node = target node");
                closedList.add(currentNode);
                ArrayList<Node> path = generatePath();
                Collections.reverse(path);
                for (int i = 1; i < path.size(); i++) {
                    int finalI = i;

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            int snakeHeadRow = path.get(finalI).getRow();
                            int snakeHeadCol = path.get(finalI).getCol();

                            // make the food visible
                            arenaBlockLocations[path.get(path.size() - 1).getRow()][path.get(path.size() - 1).getCol()]
                                    .getBlock().setType(foodMaterial);

                            // clear old snake body locations
                            if (oldSnakeBodyNodes.size() > 0) {
                                oldSnakeBodyNodes.get(0).getLocation().getBlock().setType(Material.BLACK_WOOL);
                                oldSnakeBodyNodes.remove(0);
                            }

                            // update the head of the snake
                            arenaNodes[snakeHeadRow][snakeHeadCol].setType(4);
                            arenaNodes[snakeHeadRow][snakeHeadCol].getLocation().getBlock().setType(snakeBodyMaterial);
                            arenaBlockLocations[snakeHeadRow][snakeHeadCol] = arenaNodes[snakeHeadRow][snakeHeadCol]
                                    .getLocation();

                            // update the snake body - turn the old snake body nodes to normal nodes
                            if (finalI - snakeBody.size() > 0) {
                                arenaNodes[path.get(finalI - snakeBody.size()).getRow()][path.get(finalI - snakeBody
                                        .size()).getCol()].setType(0);
                                arenaNodes[path.get(finalI - snakeBody.size()).getRow()][path.get(finalI - snakeBody
                                        .size()).getCol()].getLocation().getBlock().setType(Material.BLACK_WOOL);
                                arenaBlockLocations[path.get(finalI - snakeBody.size()).getRow()][path.get(finalI
                                        - snakeBody.size()).getCol()] = arenaNodes[path.get(finalI - snakeBody.size())
                                        .getRow()][path.get(finalI - snakeBody.size()).getCol()].getLocation();
                            }

                            // grow the snake body (since it ate the food)
                            if (finalI == path.size() - 1) {
                                for (Node node: path) {
                                    if (node.getLocation().getBlock().getType() == snakeBodyMaterial) {
                                        oldSnakeBodyNodes.add(node);
                                    }
                                }
                                snakeBody.add(arenaNodes[snakeHeadRow][snakeHeadCol]);
                            }

                            cancel();
                        }
                    }.runTaskTimer(plugin, time, 20L);
                    System.out.println("time - " + time);
                    time += 5L;
                }
                System.out.println("END OF PATH");
                openList.clear();
                exploredPlaces.clear();
                closedList.clear();

                // create new food
                int targetRow = (int) (Math.random() * arenaBlockLocations[0].length);
                int targetCol = (int) (Math.random() * arenaBlockLocations[0].length);

                // reset current and target node
                currentNode = new Node(arenaBlockLocations[targetNode.getRow()][targetNode.getCol()],
                        targetNode.getRow(), targetNode.getCol(), 0);
                targetNode = new Node(arenaBlockLocations[targetRow][targetCol], targetRow, targetCol, 0);

                arenaNodes = new Node[size][size];

                // reset maze nodes
                for (int i = 0; i < size; i++) {
                    for (int j = 0; j < size; j++) {
                        if (arenaBlockLocations[i][j].getBlock().getType() == snakeBodyMaterial) {
                            Node node = new Node(arenaBlockLocations[i][j], i, j, 4);
                            arenaNodes[i][j] = node;
                        } else {
                            Node node = new Node(arenaBlockLocations[i][j], i, j, 0);
                            arenaNodes[i][j] = node;
                        }
                    }
                }

                arenaNodes[currentNode.getRow()][currentNode.getCol()] = currentNode;
                arenaNodes[targetRow][targetCol] = targetNode;

                // calculate heuristics for current node to new target node
                int g = calculateG(currentNode);
                currentNode.setG(g);

                int h = calculateH(currentNode);
                currentNode.setH(h);

                currentNode.setF();

                startNode = currentNode;

                openList.add(currentNode);
            } else {
                calculateNeighborValues();
                try {
                    assert openList.peek() != null;
                } catch (NullPointerException ignored) {}
                closedList.add(currentNode);
            }
        }
    }

    public long getTime() {
        return time;
    }


    public void calculateNeighborValues() {
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
        while (temp.getParent() != null) {
            temp = temp.getParent();
            path.add(temp);
        }
        return path;
    }

    public void runnableDelayed(Node node, long time, Material material, int row, int col) {
        new BukkitRunnable() {
            @Override
            public void run() {
                Location location = new Location(
                        arenaBlockLocations[row][col].getWorld(),
                        arenaBlockLocations[row][col].getX(),
                        arenaBlockLocations[row][col].getY(),
                        arenaBlockLocations[row][col].getZ()
                );
                location.getBlock().setType(material);
                if (material == snakeBodyMaterial) {
                    arenaNodes[row][col].setType(4);
                } else {
                    arenaNodes[row][col].setType(0);
                }
                arenaBlockLocations[row][col] = location;
                cancel();
            }
        }.runTaskTimer(plugin, time, 20L);
    }
}
