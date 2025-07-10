package com.ivan.pronin.job.hunt.middle;

import com.ivan.pronin.job.hunt.middle.model.TreeNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan Pronin
 * @since 09.07.2025
 */
public class MaximumDepthOfBinaryTree104 {

    /*
     * 104. Maximum Depth of Binary Tree
     * Given the root of a binary tree, return its maximum depth.
     * A binary tree's maximum depth is the number of nodes along the longest path from the root node down to the farthest leaf node.
     * Example 1:
     * Input: root = [3,9,20,null,null,15,7]
     * Output: 3
     * Example 2:
     * Input: root = [1,null,2]
     * Output: 2
     * 📊 Сложность:
     * Time: O(n) — каждый узел посещается один раз.
     * Space: O(h) — глубина рекурсии, где h — высота дерева (в худшем случае O(n) для несбалансированного дерева).
     */

    public static int maxDepth(TreeNode root) {
        if (root == null) return 0;
        return 1 + Math.max(maxDepth(root.left), maxDepth(root.right));
    }

    @Test
    void testNullTree() {
        assertEquals(0, MaximumDepthOfBinaryTree104.maxDepth(null));
    }

    @Test
    void testSingleNode() {
        TreeNode root = new TreeNode(1);
        assertEquals(1, MaximumDepthOfBinaryTree104.maxDepth(root));
    }

    @Test
    void testBalancedTree() {
        TreeNode root = new TreeNode(1,
            new TreeNode(2,
                new TreeNode(4),
                new TreeNode(5)
            ),
            new TreeNode(3)
        );
        assertEquals(3, MaximumDepthOfBinaryTree104.maxDepth(root));
    }

    @Test
    void testLeftSkewedTree() {
        TreeNode root = new TreeNode(1,
            new TreeNode(2,
                new TreeNode(3,
                    new TreeNode(4),
                    null
                ),
                null
            ),
            null
        );
        assertEquals(4, MaximumDepthOfBinaryTree104.maxDepth(root));
    }

    @Test
    void testRightSkewedTree() {
        TreeNode root = new TreeNode(1,
            null,
            new TreeNode(2,
                null,
                new TreeNode(3,
                    null,
                    new TreeNode(4)
                )
            )
        );
        assertEquals(4, MaximumDepthOfBinaryTree104.maxDepth(root));
    }

    @Test
    void testCompleteTree() {
        TreeNode root = new TreeNode(1,
            new TreeNode(2,
                new TreeNode(4),
                new TreeNode(5)
            ),
            new TreeNode(3,
                new TreeNode(6),
                new TreeNode(7)
            )
        );
        assertEquals(3, MaximumDepthOfBinaryTree104.maxDepth(root));
    }

}
