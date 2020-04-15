package ru.ifmo.rain.kuznetsov.crawler;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.ExecutorService;

/**
 * Class for one host to control the number of simultaneous downloads from one host
 */
public class Host {
    /**
     * Queue for control waiting and runnable tasks
     */
    private final Queue<Runnable> runnableQueue;
    /**
     * Size of runnable tasks
     */
    private int size;
    /**
     * Maximum size of runnable tasks
     */
    private final int maximumSize;
    /**
     * {@link ExecutorService} for run tasks
     */
    private final ExecutorService downloadersPool;

    /**
     * Constructor
     * @param maximumSize maximum count of runnable task
     * @param downloadersPool {@link ExecutorService} for runnable tasks
     */
    Host(int maximumSize, ExecutorService downloadersPool) {
        runnableQueue = new ArrayDeque<>();
        size = 0;
        this.downloadersPool = downloadersPool;
        this.maximumSize = maximumSize;
    }

    /**
     * Add task to runnables (or to waitings, if queue is full)
     * @param runnable task
     */
    public synchronized void add(Runnable runnable) {
        if (size < maximumSize) {
            size++;
            downloadersPool.submit(runnable);
        } else {
            runnableQueue.add(runnable);
        }
    }

    /**
     * Run task
     */
    public synchronized void next() {
        Runnable next = runnableQueue.poll();
        if (next != null) {
            downloadersPool.submit(next);
        } else {
            size--;
        }
    }
}
