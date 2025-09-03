package com.ivan.pronin.job.hunt.senior.google;

import java.util.Stack;

import com.ivan.pronin.job.hunt.senior.google.model.TreeNode;

/**
 * @author Ivan Pronin
 * @since 03.09.2025
 */
public class BuildBSTFromPreorder {

    public static TreeNode<Integer> buildBstUsingStack(int[] preorder) {
        TreeNode<Integer> root = new TreeNode<>(preorder[0]);
        Stack<TreeNode<Integer>> stack = new Stack<>();
        stack.push(root);
        for (int i = 1; i < preorder.length; i++) {
            TreeNode<Integer> parent = stack.peek();
            TreeNode<Integer> node = new TreeNode<>(preorder[i]);

            if (node.getValue() < parent.getValue()) {
                parent.setLeft(node);
            } else {
                while (!stack.isEmpty() && stack.peek().getValue() < node.getValue()) {
                    parent = stack.pop();
                }
                parent.setRight(node);
            }
            stack.push(node);
        }
        return root;
    }

}
