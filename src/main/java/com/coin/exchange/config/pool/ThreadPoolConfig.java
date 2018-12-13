/**
 * @(#) ThreadPoolConfig.java
 *
 * Copyright (c) 2018, Credan(上海)-版权所有
 */
package com.coin.exchange.config.pool;

import com.coin.exchange.common.utils.StringsHelper;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author storys.zhang@gmail.com
 *
 * Created at 2018/9/25 by Storys.Zhang in coin_exchange
 */
@Configuration
public class ThreadPoolConfig {

    /**
     * 火币市场深度数据获取线程池
     *
     * @return
     */
    @Bean("huobiMarketDepthListeningExecutorService")
    public ListeningExecutorService huobiMarketDepthListeningExecutorService() {
        return MoreExecutors.listeningDecorator(huobiMarketDepthThreadPoolExecutor());
    }

    @Bean("huobiMarketDepthThreadPoolExecutor")
    public ThreadPoolExecutor huobiMarketDepthThreadPoolExecutor() {
        int corePoolSize = Runtime.getRuntime().availableProcessors();
        return threadPoolExecutor(corePoolSize, corePoolSize + 1, 10L, 500,
            "Guava-Executor-huobi-market-depth");
    }
    /**
     * IO密集型任务  = 一般为2*CPU核心数（常出现于线程中：数据库数据交互、文件上传下载、网络数据传输等等）
     * CPU密集型任务 = 一般为CPU核心数+1（常出现于线程中：复杂算法）
     * 混合型任务  = 视机器配置和复杂度自测而定
     */
    /**
     * public ThreadPoolExecutor(int corePoolSize,int maximumPoolSize,long keepAliveTime,
     * TimeUnit unit,BlockingQueue<Runnable> workQueue)
     * corePoolSize用于指定核心线程数量
     * maximumPoolSize指定最大线程数
     * keepAliveTime和TimeUnit指定线程空闲后的最大存活时间
     * workQueue则是线程池的缓冲队列,还未执行的线程会在队列中等待
     * 监控队列长度，确保队列有界
     * 不当的线程池大小会使得处理速度变慢，稳定性下降，并且导致内存泄露。如果配置的线程过少，则队列会持续变大，消耗过多内存。
     * 而过多的线程又会 由于频繁的上下文切换导致整个系统的速度变缓——殊途而同归。队列的长度至关重要，它必须得是有界的，这样如果线程池不堪重负了它可以暂时拒绝掉新的请求。
     * ExecutorService 默认的实现是一个无界的 LinkedBlockingQueue。
     */
    private ThreadPoolExecutor threadPoolExecutor(int corePoolSize, int maxPoolSize,
        long keepAliveTime, int capacity, String threadNamePrefix) {
        final LinkedBlockingQueue<Runnable> runnables = new LinkedBlockingQueue<>(capacity);
        final ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(corePoolSize,
            maxPoolSize, keepAliveTime, TimeUnit.SECONDS,
            runnables, new ThreadFactory() {
            private final AtomicInteger poolNumber = new AtomicInteger(1);

            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r,
                    StringsHelper.join(threadNamePrefix, "-", poolNumber.getAndIncrement()));
                return thread;
            }
        });
        threadPoolExecutor.allowCoreThreadTimeOut(true);
        return threadPoolExecutor;
    }
}
