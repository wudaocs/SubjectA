package com.td.ca.compiler.threads;

/**
 * Description :
 * Created by Wang Yue on 2018/10/30.
 * Job number：135033
 * Phone ：18610413765
 * Email：wangyue@syswin.com
 * Person in charge :Wang Yue
 * Leader：Ding Lei
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
