package com.ivan.pronin.job.hunt.debug;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * @author Ivan Pronin
 * @since 23.07.2025
 */
public class DEBUGGING {

    private static class SingletonLazy {

        private static SingletonLazy instance;

        private SingletonLazy() {
            Object o = new Object();
            BitSet bs = new BitSet();
            List<Object> ll = new ArrayList<>();
            ll.add("ss");
            ll.add(2);
            List<String> ss = new ArrayList<>();
            ss.add("aaa");
            ll.addAll(ss);
            List<? extends Number> nn = new ArrayList<>();
            final Number number = nn.get(1);
        }

        public static SingletonLazy get() {
            if (null == instance) {
                instance = new SingletonLazy();
            }
            return instance;
        }

    }

    private static class SingletonThreadSafe {

        private static volatile SingletonThreadSafe instance;

        private SingletonThreadSafe() {
        }

        public static SingletonThreadSafe get() {
            if (null == instance) {
                synchronized (SingletonThreadSafe.class) {
                    if (instance == null) {
                        instance = new SingletonThreadSafe();
                    }
                }
            }
            return instance;
        }

    }

    @Test
    void testSema() {
        Semaphore s1 = new Semaphore(2);
        Semaphore s2 = new Semaphore(1);
        CyclicBarrier b = new CyclicBarrier(3);
        Object o = "ss";
        String s = (String) o;
        System.out.println(s);
    }

    @Test
    void testStringChars() {
        Map<String, Integer> mmm = new HashMap<>();

        String s = "asd";
        s.hashCode();
        s.contains("s");

        String t = "d";
        t.contains(String.valueOf(s.charAt(1)));
    }

    @Test
    void testDeque() {
        Deque<Integer> deque = new ArrayDeque<>();
        List<Integer> lll = new ArrayList<>();
        Collections.sort(lll, (a, b) -> b - a);
        lll.reversed();
        String s = "sss";
        s.length();
        int[] arr = new int[]{1, 2, 3};
        // how to convert arr to List?
        List<Integer> list = new ArrayList<>();
    }

    @Test
    void listToArray() {
        List<Integer> result = new ArrayList<>();
        int[] res = result.stream().mapToInt(i -> i).toArray(); // list to int[]
        result.add(1);
        result.add(2);
        result.add(3);
        result.remove((Integer) 2); // removing by value, not index
        System.out.println(result); // [1, 3];
    }

    @Test
    void treeSetTest() {
        SortedSet<Integer> set = new TreeSet<>();
        set.add(10);
        set.add(20);
        set.add(30);

        System.out.println(set.first());
        System.out.println(set.last());
    }

    @Test
    void testArraysEquality() {
        String s1 = "racecar";
        String s2 = "carrace";
        char[] c1 = s1.toCharArray();
        char[] c2 = s2.toCharArray();
        Assertions.assertFalse(Arrays.equals(c1, c2));

        Arrays.sort(c1);
        Arrays.sort(c2);
        assertArrayEquals(c1, c2);

        int[] a1 = new int[]{0, 1, 2};
        int[] a2 = new int[]{1, 2, 0};
        Assertions.assertFalse(Arrays.equals(a1, a2));
        Arrays.sort(a1);
        Arrays.sort(a2);
        assertArrayEquals(a1, a2);
    }

    @Test
    void t() {
        List<Integer> list = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        list.stream().mapToInt(i -> i).toArray();
        Map<String, String> m = new HashMap<>();
        Queue<int[]> numbers = new PriorityQueue<>((a, b) -> b[1] - a[1]);

        var str = List.of("Hello", "world", "123");
        StringBuilder sb = new StringBuilder();
        for (String s : str) {
            sb.append(s).append("#");
        }
        System.out.println(sb.toString());
        final String[] split = sb.toString().split("#");
        System.out.println(Arrays.toString(split));
        System.out.println(List.of("\"\""));

        Map<Integer, Set<Character>> cars = new HashMap<>();
        cars.putIfAbsent(1, new HashSet<>());
        cars.get(1).add('a');

        System.out.println(fib(6));
        System.out.println(fib(7));
        System.out.println(fib(8));
        System.out.println(fib(50));
    }

    private long fib(int n) {
        if (n <= 2) return 1L;
        return fib(n - 1) + fib(n - 2);
    }

}
