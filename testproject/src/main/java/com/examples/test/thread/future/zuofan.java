package com.examples.test.thread.future;

import java.util.concurrent.Callable;

/**
 * @Author: cz
 * @Date: 2020/8/10
 * @Description:
 */
public class zuofan implements Callable<Boolean> {
    @Override
    public Boolean call() throws Exception {
        try{
            System.out.println("做饭");
            Thread.sleep(5000);
            System.out.println("做好饭");
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return true;
    }
}
