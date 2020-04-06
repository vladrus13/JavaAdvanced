package ru.ifmo.rain.kuznetsov.concurrent;

import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

import java.util.*;
import java.util.function.Function;

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
     * Class result collector
     * @param <R> type of data
     */
    private static class ResultCollector<R> {
        private final List<R> res;
        private int cnt;

        /**
         * Constructor with size
         * @param size size
         */
        ResultCollector(final int size) {
            res = new ArrayList<>(Collections.nCopies(size, null));
            cnt = 0;
        }

        /**
         * Set data
         * @param pos position of data
         * @param data data
         */
        void setData(final int pos, R data) {
            res.set(pos, data);
            synchronized (this) {
                if (++cnt == res.size()) {
                    notify();
                }
            }
        }

        /**
         * Wait until results
         * @return result
         * @throws InterruptedException if we got something error
         */
        synchronized List<R> getRes() throws InterruptedException {
            while (cnt < res.size()) {
                wait();
            }
            return res;
        }
    }

    /**
     * Constructor
     * @param threads number of threads
     */
    public ParallelMapperImpl(final int threads) {
        tasks = new ArrayDeque<>();
        workers = new ArrayList<>();
        for (int i = 0; i < threads; i++) {
            Thread temp = new Thread(() -> {
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
                // ignored
            } finally {
                Thread.currentThread().interrupt();
            } } );
            workers.add(temp);
            temp.start();
        }
    }

    @Override
    public <T, R> List<R> map(Function<? super T, ? extends R> f, List<? extends T> args) throws InterruptedException {
        ResultCollector<R> collector = new ResultCollector<>(args.size());
        for (int i = 0; i < args.size(); i++) {
            final int ind = i;
            synchronized (tasks) {
                tasks.add(() -> collector.setData(ind, f.apply(args.get(ind))));
                tasks.notifyAll();
            }
        }
        return collector.getRes();
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
                // ignored
            }
        }
    }
}
