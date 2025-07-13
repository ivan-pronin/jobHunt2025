package com.ivan.pronin.job.hunt.senior.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivan Pronin
 * @since 12.07.2025
 */
public class Node {

    public int val;

    public List<Node> neighbors;

    public Node(int i) {
        this.val = i;
        this.neighbors = new ArrayList<>();
    }

}
