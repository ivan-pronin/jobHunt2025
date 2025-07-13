package com.ivan.pronin.job.hunt.senior;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;

import com.ivan.pronin.job.hunt.middle.model.TreeNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * @author Ivan Pronin
 * @since 12.07.2025
 */
public class SerializeAndDeserializeBinaryTree297 {

    /*
     * 297. Serialize and Deserialize Binary Tree
     * Design an algorithm to serialize and deserialize a binary tree.
     * There is no restriction on how your serialization/deserialization algorithm should work.
     * You just need to ensure that a binary tree can be serialized to a string and this string can be deserialized to the original tree structure.
     * Example:
     * Input: root = [1,2,3,null,null,4,5]
     * Output: [1,2,3,null,null,4,5]
     */
    private static class DfsSolution {

        private static final String NULL_SYMBOL = "null";

        private static final String DELIMITER = ",";

        public String serialize(TreeNode root) {
            StringBuilder sb = new StringBuilder();
            serializeHelper(root, sb);
            return sb.toString();
        }

        private void serializeHelper(TreeNode node, StringBuilder sb) {
            if (node == null) {
                sb.append(NULL_SYMBOL).append(DELIMITER);
            } else {
                sb.append(node.val).append(DELIMITER);
                serializeHelper(node.left, sb);
                serializeHelper(node.right, sb);
            }
        }

        public TreeNode deserialize(String data) {
            Deque<String> nodes = new LinkedList<>(Arrays.asList(data.split(DELIMITER)));
            return deserializeHelper(nodes);
        }

        private TreeNode deserializeHelper(Deque<String> nodes) {
            String val = nodes.poll();
            if (val.equals(NULL_SYMBOL)) return null;
            TreeNode node = new TreeNode(Integer.parseInt(val));
            node.left = deserializeHelper(nodes);
            node.right = deserializeHelper(nodes);
            return node;
        }

    }

    private static class BfsSolution {

        private static final String NULL_SYMBOL = "null";

        private static final String DELIMITER = ",";

        public String serialize(TreeNode root) {
            if (root == null) return "";

            StringBuilder sb = new StringBuilder();
            Queue<TreeNode> queue = new LinkedList<>();
            queue.offer(root);

            while (!queue.isEmpty()) {
                TreeNode node = queue.poll();
                if (node == null) {
                    sb.append(NULL_SYMBOL).append(DELIMITER);
                } else {
                    sb.append(node.val).append(DELIMITER);
                    queue.offer(node.left);
                    queue.offer(node.right);
                }
            }

            return sb.toString();
        }

        public TreeNode deserialize(String data) {
            if (data == null || data.isEmpty()) return null;

            String[] values = data.split(DELIMITER);
            TreeNode root = new TreeNode(Integer.parseInt(values[0]));
            Queue<TreeNode> queue = new LinkedList<>();
            queue.offer(root);

            int i = 1;
            while (i < values.length) {
                TreeNode parent = queue.poll();
                if (!values[i].equals(NULL_SYMBOL)) {
                    parent.left = new TreeNode(Integer.parseInt(values[i]));
                    queue.offer(parent.left);
                }
                i++;

                if (i < values.length && !values[i].equals(NULL_SYMBOL)) {
                    parent.right = new TreeNode(Integer.parseInt(values[i]));
                    queue.offer(parent.right);
                }
                i++;
            }

            return root;
        }

    }

    private TreeNode buildSampleTree() {
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.right.left = new TreeNode(4);
        root.right.right = new TreeNode(5);
        return root;
    }

    private void assertTreesEqual(TreeNode t1, TreeNode t2) {
        if (t1 == null || t2 == null) {
            assertEquals(t1, t2);
            return;
        }
        assertEquals(t1.val, t2.val);
        assertTreesEqual(t1.left, t2.left);
        assertTreesEqual(t1.right, t2.right);
    }

    @Test
    public void testCodecDFS() {
        DfsSolution codec = new DfsSolution();
        TreeNode root = buildSampleTree();
        String serialized = codec.serialize(root);
        TreeNode deserialized = codec.deserialize(serialized);
        assertTreesEqual(root, deserialized);
    }

    @Test
    public void testCodecBFS() {
        BfsSolution codec = new BfsSolution();
        TreeNode root = buildSampleTree();
        String serialized = codec.serialize(root);
        TreeNode deserialized = codec.deserialize(serialized);
        assertTreesEqual(root, deserialized);
    }

    @Test
    public void testEmptyTree() {
        DfsSolution codec = new DfsSolution();
        String serialized = codec.serialize(null);
        TreeNode deserialized = codec.deserialize(serialized);
        assertNull(deserialized);
    }

}
