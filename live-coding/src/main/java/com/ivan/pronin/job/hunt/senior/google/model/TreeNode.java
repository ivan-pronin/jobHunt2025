package com.ivan.pronin.job.hunt.senior.google.model;

import lombok.Data;

/**
 * @author Ivan Pronin
 * @since 26.08.2025
 */
@Data
public class TreeNode<T> {

    T value;

    TreeNode<T> left;

    TreeNode<T> right;

    public TreeNode(T value, TreeNode<T> left, TreeNode<T> right) {
        this.value = value;
        this.left = left;
        this.right = right;
    }

    public TreeNode(T value) {
        this.value = value;
    }

}
