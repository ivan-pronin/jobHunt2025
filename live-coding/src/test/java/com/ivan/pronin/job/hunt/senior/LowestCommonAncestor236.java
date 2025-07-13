package com.ivan.pronin.job.hunt.senior;

import java.util.ArrayList;
import java.util.List;

import com.ivan.pronin.job.hunt.middle.model.TreeNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan Pronin
 * @since 10.07.2025
 */
public class LowestCommonAncestor236 {

    /*
     * 236. Lowest Common Ancestor of a Binary Tree
     * Given a binary tree, find the lowest common ancestor (LCA) of two given nodes in the tree.
     * Example:
     * Input: root = [3,5,1,6,2,0,8,null,null,7,4], p = 5, q = 1
     * Output: 3
     * Explanation: The LCA of nodes 5 and 1 is 3.
     * 📊 Сложность:
     * Time: O(n) — где n — количество узлов в дереве, каждый узел посещается один раз.
     * Space: O(h) — где h — высота дерева, используется стек вызовов для рекурсии.
     */
    public TreeNode lowestCommonAncestor(TreeNode root, TreeNode p, TreeNode q) {
        if (root == null || root == p || root == q) return root;

        TreeNode left = lowestCommonAncestor(root.left, p, q);
        TreeNode right = lowestCommonAncestor(root.right, p, q);

        if (left != null && right != null) return root;
        return (left != null) ? left : right;
    }

    private TreeNode buildTree() {
        //      3
        //     / \
        //    5   1
        //   / \ / \
        //  6  2 0  8
        //    / \
        //   7   4
        TreeNode root = new TreeNode(3);
        root.left = new TreeNode(5);
        root.right = new TreeNode(1);
        root.left.left = new TreeNode(6);
        root.left.right = new TreeNode(2);
        root.left.right.left = new TreeNode(7);
        root.left.right.right = new TreeNode(4);
        root.right.left = new TreeNode(0);
        root.right.right = new TreeNode(8);
        return root;
    }

    public TreeNode lowestCommonAncestorSearch(TreeNode root, TreeNode p, TreeNode q) {
        List<TreeNode> pathP = new ArrayList<>();
        List<TreeNode> pathQ = new ArrayList<>();
        findPath(root, p, pathP);
        findPath(root, q, pathQ);

        int i = 0;
        while (i < pathP.size() && i < pathQ.size() && pathP.get(i) == pathQ.get(i)) {
            i++;
        }

        return pathP.get(i - 1);
    }

    private boolean findPath(TreeNode root, TreeNode target, List<TreeNode> path) {
        if (root == null) return false;

        path.add(root);
        if (root == target) return true;

        if (findPath(root.left, target, path) || findPath(root.right, target, path)) return true;

        path.remove(path.size() - 1);
        return false;
    }

    @Test
    public void testRecursive() {
        TreeNode root = buildTree();
        TreeNode p = root.left; // 5
        TreeNode q = root.left.right.right; // 4
        TreeNode expected = root.left; // 5

        TreeNode result = lowestCommonAncestor(root, p, q);
        assertEquals(expected, result);
    }

    @Test
    public void testPathCompare() {
        TreeNode root = buildTree();
        TreeNode p = root.left; // 5
        TreeNode q = root.right.right; // 8
        TreeNode expected = root; // 3

        TreeNode result = lowestCommonAncestorSearch(root, p, q);
        assertEquals(expected, result);
    }

    @Test
    public void testOneNodeInSubtree() {
        // Дерево:
        //     3
        //    /
        //   5
        //  /
        // 6

        TreeNode root = new TreeNode(3);
        TreeNode node5 = new TreeNode(5);
        TreeNode node6 = new TreeNode(6);
        root.left = node5;
        node5.left = node6;

        TreeNode result = lowestCommonAncestor(root, node5, node6);

        // Ожидаем LCA = 5
        assertEquals(node5, result);
    }
}
