package com.ivan.pronin.job.hunt.senior;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Ivan Pronin
 * @since 11.07.2025
 */
public class PrintInOrder1114 {

    /*
     * 1114. Print in Order
     * Suppose we have a class:
     * class Foo {
     *   public void first() {
     *      // printFirst
     *   }
     *  public void second() {
     *     // printSecond
     *  }
     * public void third() {
     *     // printThird
     *  }
     *  The same instance of Foo will be passed to three different threads:
     *     * Thread A will call first(), thread B will call second(), and thread C will call third().
     *     * Design a mechanism and modify the program to ensure that first() is called before second(),
     *     * and second() is called before third().
     */
    private final Semaphore semSecond = new Semaphore(0);

    private final Semaphore semThird = new Semaphore(0);

    public void first(Runnable printFirst) {
        printFirst.run();
        semSecond.release(); // разрешает second()
    }

    public void second(Runnable printSecond) {
        try {
            semSecond.acquire(); // ждет first()
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        printSecond.run();
        semThird.release();  // разрешает third()
    }

    public void third(Runnable printThird) {
        try {
            semThird.acquire(); // ждет second()
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        printThird.run();
    }

    private StringBuilder output = new StringBuilder();

    private final Runnable printFirst = () -> output.append("first");

    private final Runnable printSecond = () -> output.append("second");

    private final Runnable printThird = () -> output.append("third");

    private void runInRandomOrder(PrintInOrder1114 foo) throws InterruptedException {
        List<Runnable> tasks = Arrays.asList(
            () -> safe(foo, () -> foo.first(printFirst)),
            () -> safe(foo, () -> foo.second(printSecond)),
            () -> safe(foo, () -> foo.third(printThird))
        );
        Collections.shuffle(tasks);
        ExecutorService exec = Executors.newFixedThreadPool(3);
        for (Runnable task : tasks) exec.submit(task);
        exec.shutdown();
        exec.awaitTermination(2, TimeUnit.SECONDS);
    }

    private void safe(Object obj, Runnable r) {
        try {
            r.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSemaphoreOrder() throws InterruptedException {
        output.setLength(0);
        runInRandomOrder(new PrintInOrder1114());
        assertEquals("firstsecondthird", output.toString());
    }

}
