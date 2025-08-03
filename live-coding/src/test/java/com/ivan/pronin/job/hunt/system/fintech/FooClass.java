package com.ivan.pronin.job.hunt.system.fintech;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Ivan Pronin
 * @since 01.08.2025
 */
public class FooClass {

    private Lock lock = new ReentrantLock();
    private Condition firstCondition = lock.newCondition();
    private Condition secondCondition = lock.newCondition();

    public void first(Runnable printFirst) throws InterruptedException {
        // printFirst.run() outputs "first". Do not change or remove this line.
        printFirst.run();
        firstCondition.signal();
    }

    public void second(Runnable printSecond) throws InterruptedException {
        firstCondition.await();
        // printSecond.run() outputs "second". Do not change or remove this line.
        printSecond.run();
        secondCondition.signal();
    }

    public void third(Runnable printThird) throws InterruptedException {
        secondCondition.await();
        // printThird.run() outputs "third". Do not change or remove this line.
        printThird.run();
    }
}
