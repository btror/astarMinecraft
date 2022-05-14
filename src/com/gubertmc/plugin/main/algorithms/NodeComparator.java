package com.gubertmc.plugin.main.algorithms;

import java.util.Comparator;

public class NodeComparator implements Comparator<Node> {

    /**
     * Compare node f-scores.
     *
     * @param o1 node 1.
     * @param o2 node 2.
     * @return node with highest f-score.
     */
    @Override
    public int compare(Node o1, Node o2) {
        if (o1.getF() > o2.getF()) {
            return 1;
        } else if (o1.getF() < o2.getF()) {
            return -1;
        }
        return 0;
    }
}
