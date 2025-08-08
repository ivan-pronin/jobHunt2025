package com.ivan.pronin.job.hunt.senior.collections;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Ivan Pronin
 * @since 07.08.2025
 */
public class JavaCollections {

    public void listOps() {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add(1, "s");
        list.set(1, "2");
        list.remove(1);
        list.remove("a");
        list.contains("2");
        list.iterator();
    }

    public void setOps() {
        Set<String> set = new HashSet<>();
        set.add("s");
        set.remove("2");
    }

    public void dequeOps(){
        Deque<String> deq = new ArrayDeque<>();
        deq.push("1st");
        deq.push("2nd");
        deq.push("3rd");
        System.out.println(deq);

        Deque<String> deq2 = new ArrayDeque<>();
        deq2.offer("1st");
        deq2.offer("2nd");
        deq2.offer("3rd");
        System.out.println(deq2);
    }

}
