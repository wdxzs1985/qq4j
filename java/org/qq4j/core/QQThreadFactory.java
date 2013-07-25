package org.qq4j.core;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class QQThreadFactory implements ThreadFactory {
    final static AtomicInteger poolNumber = new AtomicInteger(1);
    final ThreadGroup group;
    final AtomicInteger threadNumber = new AtomicInteger(1);
    final String namePrefix;

    public QQThreadFactory(final String name) {
        final SecurityManager s = System.getSecurityManager();
        this.group = s != null ? s.getThreadGroup() : Thread.currentThread()
                                                            .getThreadGroup();
        this.namePrefix = "pool-"
                          + this.threadNumber.getAndIncrement()
                          + "-"
                          + name
                          + "-";
    }

    @Override
    public Thread newThread(final Runnable r) {
        final Thread t = new Thread(this.group,
                                    r,
                                    this.namePrefix
                                            + this.threadNumber.getAndIncrement(),
                                    0);
        if (t.isDaemon()) {
            t.setDaemon(false);
        }
        if (t.getPriority() != Thread.NORM_PRIORITY) {
            t.setPriority(Thread.NORM_PRIORITY);
        }
        return t;
    }
}
