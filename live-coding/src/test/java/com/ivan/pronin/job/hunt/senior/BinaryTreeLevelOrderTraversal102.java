package com.ivan.pronin.job.hunt.senior;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import com.ivan.pronin.job.hunt.middle.model.TreeNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan Pronin
 * @since 10.07.2025
 */
public class BinaryTreeLevelOrderTraversal102 {

    /*
     * 102. Binary Tree Level Order Traversal
     * Given the root of a binary tree, return the level order traversal of its nodes' values. (i.e., from left to right, level by level).
     * Example:
     * Input: root = [3,9,20,null,null,15,7]
     * Output: [[3],[9,20],[15,7]]
     *
     *
     * 📊 Сложность:
     * Time: O(n) — где n — количество узлов в дереве, каждый узел посещается один раз.
     * Space: O(n) — в худшем случае, когда дерево полностью сбалансировано, на каждом уровне может быть до n/2 узлов, что приводит к O(n) памяти для хранения всех узлов.
     */
    public List<List<Integer>> levelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) return result;

        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            List<Integer> level = new ArrayList<>();

            for (int i = 0; i < levelSize; i++) {
                TreeNode node = queue.poll();
                level.add(node.val);

                if (node.left != null) queue.offer(node.left);
                if (node.right != null) queue.offer(node.right);
            }

            result.add(level);
        }

        return result;
    }

    /*
     * Рекурсивный подход с использованием DFS
     * 📊 Сложность:
     * Time: O(n) — где n — количество узлов в дереве, каждый узел посещается один раз.
     * Space: O(h) — где h — высота дерева, используется стек вызовов для хранения текущего уровня, в худшем случае O(n) для сбалансированного дерева.
     */
    public List<List<Integer>> levelOrderDfsRecursion(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        dfs(root, 0, result);
        return result;
    }

    private void dfs(TreeNode node, int level, List<List<Integer>> result) {
        if (node == null) return;

        if (result.size() <= level) {
            result.add(new ArrayList<>());
        }

        result.get(level).add(node.val);

        dfs(node.left, level + 1, result);
        dfs(node.right, level + 1, result);
    }

    private TreeNode buildTree() {
        //      3
        //     / \
        //    9  20
        //       / \
        //      15  7
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(9);
        root.right = new TreeNode(20);
        root.right.left = new TreeNode(15);
        root.right.right = new TreeNode(7);
        return root;
    }

    @Test
    public void testBFS() {
        List<List<Integer>> expected = Arrays.asList(
            Arrays.asList(3),
            Arrays.asList(9, 20),
            Arrays.asList(15, 7)
        );
        assertEquals(expected, levelOrder(buildTree()));
    }

    @Test
    public void testDFS() {
        List<List<Integer>> expected = Arrays.asList(
            Arrays.asList(3),
            Arrays.asList(9, 20),
            Arrays.asList(15, 7)
        );
        assertEquals(expected, levelOrderDfsRecursion(buildTree()));
    }

}
