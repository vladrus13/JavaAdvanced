package ru.ifmo.rain.kuznetsov.concurrent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class result collector
 *
 * @param <R> type of data
 */
class ResultCollector<R> {
    private final List<R> results;
    private int count;

    /**
     * Constructor with size
     *
     * @param size size
     */
    ResultCollector(final int size) {
        results = new ArrayList<>(Collections.nCopies(size, null));
        count = 0;
    }

    /**
     * Set data
     *
     * @param pos  position of data
     * @param data data
     */
    void setData(final int pos, R data) {
        results.set(pos, data);
        synchronized (this) {
            if (++count == results.size()) {
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
    synchronized List<R> getResults() throws InterruptedException {
        while (count < results.size()) {
            wait();
        }
        return results;
    }
}
