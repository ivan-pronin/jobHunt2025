package com.ivan.pronin.job.hunt.senior;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan Pronin
 * @since 10.07.2025
 */
public class FindMedianFromDataStream295ArraySort {

    /*
     * 295. Find Median from Data Stream
     * Design a data structure that supports the following two operations:
     * 1. void addNum(int num) - Add a integer number from the data stream to the data structure.
     * 2. double findMedian() - Return the median of all elements so far.
     *
     */
    private List<Integer> data = new ArrayList<>();

    /*
     * Adds a number to the data structure, maintaining sorted order.
     * Time complexity: O(n) for insertion due to binary search and shifting elements.
     * Space complexity: O(n) for storing the elements.
     *
     * Note: This solution is not optimal for large datasets due to O(n) insertion time.
     */
    public void addNum(int num) {
        int idx = Collections.binarySearch(data, num);
        if (idx < 0) idx = -idx - 1;
        data.add(idx, num);
    }

    /*
     * Finds the median of the current data.
     * Time complexity: O(1) for finding the median after sorting.
     * Space complexity: O(1) additional space, not counting the storage of elements.
     *
     * Note: This method assumes that data is already sorted.
     */
    public double findMedian() {
        int n = data.size();
        if (n % 2 == 1) {
            return data.get(n / 2);
        } else {
            return (data.get(n / 2 - 1) + data.get(n / 2)) / 2.0;
        }
    }

    @Test
    public void testSortedList() {
        addNum(5);
        addNum(3);
        addNum(4);
        assertEquals(4.0, findMedian());
        addNum(2);
        assertEquals(3.5, findMedian());
        addNum(1);
        assertEquals(3.0, findMedian());
    }

}
