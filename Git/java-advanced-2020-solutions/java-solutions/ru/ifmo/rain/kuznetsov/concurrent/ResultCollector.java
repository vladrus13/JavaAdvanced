package ru.ifmo.rain.kuznetsov.concurrent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class result collector
 *
 * @param <R> type of data
 */
public class ResultCollector<R> {
    private final List<R> res;
    private int cnt;

    /**
     * Constructor with size
     *
     * @param size size
     */
    ResultCollector(final int size) {
        res = new ArrayList<>(Collections.nCopies(size, null));
        cnt = 0;
    }

    /**
     * Set data
     *
     * @param pos  position of data
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
     *
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
