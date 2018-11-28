package com.td.ca.compiler.threads;

import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Description : 线程管理类
 * Created by Wang Yue on 2018/10/30.
 * Job number：135033
 * Phone ：18610413765
 * Email：wangyue@syswin.com
 * Person in charge :Wang Yue
 * Leader：Ding Lei
 */
public class AThreadPoolManager extends ThreadPoolExecutor {
    //核心线程池大小
    private static final int CORE_POOL_SIZE = 5;
    //最大线程池队列大小
    private static final int MAXIMUM_POOL_SIZE = 16;
    //保持存活时间，当线程数大于corePoolSize的空闲线程能保持的最大时间。
    private static final int KEEP_ALIVE = 1;
    //主要获取添加任务
    private static final AtomicLong SEQ_SEED = new AtomicLong(0);

    private static volatile AThreadPoolManager poolManager;

    /**
     * 创建线程工厂
     */
    private static final ThreadFactory mThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable runnable) {
            return new Thread(runnable, "aThread#" + mCount.getAndIncrement());
        }
    };

    public static synchronized AThreadPoolManager getThreadPool(){
        if (poolManager == null){
            synchronized (AThreadPoolManager.class){
                if (poolManager == null){
                    poolManager = new AThreadPoolManager(CORE_POOL_SIZE, FIFO);
                }
            }
        }
        return poolManager;
    }

    /**
     * @param poolSize 工作线程数
     * @param type     加入的任务排序方式.
     */
    private AThreadPoolManager(int poolSize, Comparator<Runnable> type) {
        this(poolSize, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, new PriorityBlockingQueue<>(MAXIMUM_POOL_SIZE, type), mThreadFactory);
    }

    private AThreadPoolManager(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }


    /**
     * 判断当前线程池是否繁忙
     * @return 返回当前线程池是否忙碌状态
     */
    public boolean isBusy() {
        return getActiveCount() >= getCorePoolSize();
    }

    /**
     * 执行任务
     * @param runnable 线程
     */
    @Override
    public void execute(Runnable runnable) {
        if (runnable instanceof QueueRunnable) {
            ((QueueRunnable) runnable).SEQ = SEQ_SEED.getAndIncrement();
        }
        super.execute(runnable);
    }

    /**
     * 线程队列方式 先进先出
     */
    private static final Comparator<Runnable> FIFO = new Comparator<Runnable>() {
        @Override
        public int compare(Runnable lhs, Runnable rhs) {
            if (lhs instanceof QueueRunnable && rhs instanceof QueueRunnable) {
                QueueRunnable lpr = ((QueueRunnable) lhs);
                QueueRunnable rpr = ((QueueRunnable) rhs);
                int result = lpr.priority.ordinal() - rpr.priority.ordinal();
                return result == 0 ? (int) (lpr.SEQ - rpr.SEQ) : result;
            } else {
                return 0;
            }
        }
    };

    /**
     * 线程队列方式 后进先出
     */
    private static final Comparator<Runnable> LIFO = new Comparator<Runnable>() {
        @Override
        public int compare(Runnable lhs, Runnable rhs) {
            if (lhs instanceof QueueRunnable && rhs instanceof QueueRunnable) {
                QueueRunnable lpr = ((QueueRunnable) lhs);
                QueueRunnable rpr = ((QueueRunnable) rhs);
                int result = lpr.priority.ordinal() - rpr.priority.ordinal();
                return result == 0 ? (int) (rpr.SEQ - lpr.SEQ) : result;
            } else {
                return 0;
            }
        }
    };

}
