package ru.ifmo.rain.kuznetsov.crawler;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.ExecutorService;

public class Host {
    private Queue<Runnable> runnableQueue;
    private int size;
    private final int maximumSize;
    private final ExecutorService downloadersPool;

    Host(int maximumSize, ExecutorService downloadersPool) {
        runnableQueue = new ArrayDeque<>();
        size = 0;
        this.downloadersPool = downloadersPool;
        this.maximumSize = maximumSize;
    }

    public synchronized void add(Runnable runnable) {
        if (size < maximumSize) {
            size++;
            downloadersPool.submit(runnable);
        } else {
            runnableQueue.add(runnable);
        }
    }

    public synchronized void next() {
        Runnable next = runnableQueue.poll();
        if (next != null) {
            downloadersPool.submit(next);
        } else {
            size--;
        }
    }
}
