package com.minecraftmod.maze.astar3d;

import java.util.Comparator;

public class NodeComparator3D implements Comparator<Node3D> {
    @Override
    public int compare(Node3D o1, Node3D o2) {
        if (o1.getF() > o2.getF()) {
            return 1;
        } else if (o1.getF() < o2.getF()) {
            return -1;
        }
        return 0;
    }
}