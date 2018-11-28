package com.td.ca.annotation_compiler.threads;

/**
 * Description : 队列线程
 * Created by YW on 2018/11/28.
 * Email：1809267944@qq.com
 */
public class QueueRunnable implements Runnable {

    public final Priority priority;//任务优先级
    private final Runnable runnable;//任务真正执行者
    long SEQ;//任务唯一标示

    public QueueRunnable(Runnable runnable) {
        this(Priority.NORMAL, runnable);
    }

    public QueueRunnable(Priority priority, Runnable runnable) {
        this.priority = priority == null ? Priority.NORMAL : priority;
        this.runnable = runnable;
    }

    @Override
    public final void run() {
        this.runnable.run();
    }
}
