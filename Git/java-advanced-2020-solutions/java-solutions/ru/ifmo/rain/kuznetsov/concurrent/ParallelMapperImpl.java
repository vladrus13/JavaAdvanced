package ru.ifmo.rain.kuznetsov.concurrent;

import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

import java.util.*;
import java.util.function.Function;

/**
 * Impl for class {@link ParallelMapper}
 */
public class ParallelMapperImpl implements ParallelMapper {
    /**
     * Tasks
     */
    private final Queue<Runnable> tasks;

    /**
     * Workers
     */
    private final List<Thread> workers;

    /**
     * Constructor
     *
     * @param threads number of threads
     */
    public ParallelMapperImpl(final int threads) {
        if (threads <= 0) {
            throw new IllegalArgumentException("Threads must be positive");
        }
        tasks = new LinkedList<>();
        workers = new ArrayList<>();
        for (int i = 0; i < threads; i++) {
            Thread thread = new Thread(() -> {
                try {
                    while (!Thread.interrupted()) {
                        Runnable task;
                        synchronized (tasks) {
                            while (tasks.isEmpty()) {
                                tasks.wait();
                            }
                            task = tasks.poll();
                            tasks.notifyAll();
                        }
                        task.run();
                    }
                } catch (InterruptedException ignored) {
                }
            });
            workers.add(thread);
            thread.start();
        }
    }

    @Override
    public <T, R> List<R> map(Function<? super T, ? extends R> f, List<? extends T> args) throws InterruptedException {
        ResultCollector<R> collector = new ResultCollector<>(args.size());
        List<RuntimeException> runtimeExceptions = new ArrayList<>();
        for (int i = 0; i < args.size(); i++) {
            final int index = i;
            synchronized (tasks) {
                tasks.add(() -> {
                    R value = null;
                    try {
                        value = f.apply(args.get(index));
                    } catch (RuntimeException e) {
                        synchronized (runtimeExceptions) {
                            runtimeExceptions.add(e);
                        }
                    }
                    collector.setData(index, value);
                });
                tasks.notifyAll();
            }
        }
        if (!runtimeExceptions.isEmpty()) {
            RuntimeException exception = runtimeExceptions.get(0);
            for (int i = 1; i < runtimeExceptions.size(); i++) {
                exception.addSuppressed(runtimeExceptions.get(i));
            }
            throw exception;
        }
        return collector.getResults();
    }

    @Override
    public void close() {
        for (Thread worker : workers) {
            worker.interrupt();
        }
        for (Thread thread : workers) {
            try {
                thread.join();
            } catch (InterruptedException ignored) {
            }
        }
    }
}
