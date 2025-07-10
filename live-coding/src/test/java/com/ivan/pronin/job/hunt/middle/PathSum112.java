package com.ivan.pronin.job.hunt.middle;

import java.util.Stack;

import com.ivan.pronin.job.hunt.middle.model.TreeNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author Ivan Pronin
 * @since 09.07.2025
 */
public class PathSum112 {

    /*
     * 112. Path Sum
     * Given the root of a binary tree and an integer targetSum, return true if the tree has a root-to-leaf path such that adding up all the values along the path equals targetSum.
     * Example 1:
     * Input: root = [5,4,8,11,null,13,4,7,2,null,null,null,1], targetSum = 22
     * Output: true
     * Example 2:
     * Input: root = [1,2,3], targetSum = 5
     * Output: false
     * 📊 Сложность:
     * Time: O(n) — каждый узел посещается один раз.
     * Space: O(h) — глубина рекурсии, где h — высота дерева (в худшем случае O(n) для несбалансированного дерева).
     */
    public static boolean hasPathSum(TreeNode root, int targetSum) {
        if (root == null) return false;

        if (root.left == null && root.right == null) {
            return targetSum == root.val;
        }

        int newTarget = targetSum - root.val;

        return hasPathSum(root.left, newTarget) || hasPathSum(root.right, newTarget);
    }

    public static boolean hasPathSumWithStack(TreeNode root, int targetSum) {
        if (root == null) return false;

        Stack<Pair<TreeNode, Integer>> stack = new Stack<>();
        stack.push(new Pair<>(root, root.val));

        while (!stack.isEmpty()) {
            Pair<TreeNode, Integer> current = stack.pop();
            TreeNode node = current.key();
            int sum = current.value();

            if (node.left == null && node.right == null && sum == targetSum) {
                return true;
            }

            if (node.right != null) {
                stack.push(new Pair<>(node.right, sum + node.right.val));
            }

            if (node.left != null) {
                stack.push(new Pair<>(node.left, sum + node.left.val));
            }
        }

        return false;
    }

    // Simple Pair class (if you're not using javafx or javafx.util.Pair)
    private record Pair<K, V>(K key, V value) {

    }

    private TreeNode buildExampleTree() {
        return new TreeNode(5,
            new TreeNode(4,
                new TreeNode(11, new TreeNode(7), new TreeNode(2)),
                null
            ),
            new TreeNode(8,
                new TreeNode(13),
                new TreeNode(4)
            )
        );
    }

    @Test
    void testHasPathSumTrue() {
        TreeNode root = buildExampleTree();
        assertTrue(PathSum112.hasPathSum(root, 22)); // 5→4→11→2
    }

    @Test
    void testHasPathSumFalse() {
        TreeNode root = buildExampleTree();
        assertFalse(PathSum112.hasPathSum(root, 100));
    }

    @Test
    void testEmptyTree() {
        assertFalse(PathSum112.hasPathSum(null, 0));
    }

    @Test
    void testSingleNodeMatching() {
        TreeNode root = new TreeNode(7);
        assertTrue(PathSum112.hasPathSum(root, 7));
    }

    @Test
    void testSingleNodeNotMatching() {
        TreeNode root = new TreeNode(1);
        assertFalse(PathSum112.hasPathSum(root, 2));
    }

}
