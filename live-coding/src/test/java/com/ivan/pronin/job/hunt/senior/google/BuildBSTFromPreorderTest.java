package com.ivan.pronin.job.hunt.senior.google;

import com.ivan.pronin.job.hunt.senior.google.model.TreeNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan Pronin
 * @since 03.09.2025
 */
class BuildBSTFromPreorderTest {

    @Test
    public void testConstructBST() {
        TreeNode<Integer> root = BuildBSTFromPreorder.buildBstUsingStack(new int[]{8, 5, 1, 7, 10, 12});
        assertEquals(8, root.getValue());
        assertEquals(5, root.getLeft().getValue());
        assertEquals(10, root.getRight().getValue());
        assertEquals(1, root.getLeft().getLeft().getValue());
        assertEquals(7, root.getLeft().getRight().getValue());
        assertEquals(12, root.getRight().getRight().getValue());
    }

}