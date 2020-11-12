package com.examples.test.thread.rejectpolicy;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author: cz
 * @Date: 2020/8/15
 * @Description:
 */
public class BlockWhenQueueFull implements RejectedExecutionHandler {

    private ThreadLocal<Integer> threadLocalSleepTimeout = new ThreadLocal<>();

    public BlockWhenQueueFull(){
        threadLocalSleepTimeout.set(500);
    }

    public BlockWhenQueueFull(Integer sleepTimeout){
        threadLocalSleepTimeout.set(sleepTimeout);
    }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        try {
            while(executor.getPoolSize()>=executor.getMaximumPoolSize()){
//                System.out.println("拒绝线程 "+r.toString()+" ，等待队列数量： "+queueSize);
                Thread.sleep(threadLocalSleepTimeout.get());
//                Thread.sleep(3000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("拒绝线程结束，重新开始执行线程");
        executor.execute(r);
    }

}
