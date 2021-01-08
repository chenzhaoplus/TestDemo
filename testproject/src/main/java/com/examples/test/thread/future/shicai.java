package com.examples.test.thread.future;

import java.util.concurrent.Callable;

/**
 * @Author: cz
 * @Date: 2020/8/10
 * @Description:
 */
public class shicai implements Callable<Boolean> {
    @Override
    public Boolean call() throws Exception {
        try {
            System.out.println("买食材");
            Thread.sleep(1000);
            System.out.println("买好食材");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }
}