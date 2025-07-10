package com.ivan.pronin.job.hunt.middle;

import com.ivan.pronin.job.hunt.middle.model.TreeNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * @author Ivan Pronin
 * @since 09.07.2025
 */
public class SymmetricTree101 {

    /*
     * 101. Symmetric Tree
     * Given the root of a binary tree, check whether it is a mirror of itself (i.e., symmetric around its center).
     * Example 1:
     * Input: root = [1,2,2,3,4,4,3]
     * Output: true
     * Example 2:
     * Input: root = [1,2,2,null,3,null,3]
     * Output: false
     * 📊 Сложность:
     * Time: O(n) — каждый узел посещается один раз.
     * Space: O(h) — глубина рекурсии, где h — высота дерева (в худшем случае O(n) для несбалансированного дерева).
     */
    public boolean isSymmetric(TreeNode root) {
        if (root == null) return true;
        return isMirror(root.left, root.right);
    }

    private boolean isMirror(TreeNode t1, TreeNode t2) {
        if (t1 == null && t2 == null) return true;
        if (t1 == null || t2 == null) return false;
        return (t1.val == t2.val)
            && isMirror(t1.left, t2.right)
            && isMirror(t1.right, t2.left);
    }

    @Test
    void testSymmetricTree() {
        TreeNode root = new TreeNode(1,
            new TreeNode(2,
                new TreeNode(3),
                new TreeNode(4)
            ),
            new TreeNode(2,
                new TreeNode(4),
                new TreeNode(3)
            )
        );
        assertTrue(isSymmetric(root));
    }

    @Test
    void testAsymmetricTree() {
        TreeNode root = new TreeNode(1,
            new TreeNode(2,
                null,
                new TreeNode(3)
            ),
            new TreeNode(2,
                null,
                new TreeNode(3)
            )
        );
        assertFalse(isSymmetric(root));
    }

}
