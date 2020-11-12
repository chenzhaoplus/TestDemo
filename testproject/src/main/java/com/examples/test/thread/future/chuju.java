package com.examples.test.thread.future;

import java.util.concurrent.Callable;

/**
 * @Author: cz
 * @Date: 2020/8/10
 * @Description:
 */
public class chuju implements Callable<Boolean> {
    @Override
    public Boolean call() throws Exception {
        try{
            System.out.println("买厨具");
            Thread.sleep(3000);
            System.out.println("买好厨具");
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        return true;
    }
}
