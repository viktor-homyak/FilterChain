package com.tutorials;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by vhomyak on 23.05.2017.
 */
public class DefaultThreadFactory implements ThreadFactory {

    private AtomicInteger counter = new AtomicInteger();
    private String prefix = "";

    public DefaultThreadFactory(String prefix) {
        this.prefix = prefix;
    }

    public Thread newThread(Runnable r) {
        return new Thread(r, prefix + "-" + counter.incrementAndGet());
    }
}
