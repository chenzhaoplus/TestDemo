package com.examples.test.thread.threadlocal;

/**
 * @Author: cz
 * @Date: 2020/8/6
 * @Description:
 */
public class ThreadLocalStudy {

    private static ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>();

    public static void main(String[] args) throws InterruptedException {
        for(int i = 0; i<100; i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for(int j =0 ; j <100 ; j++){
                        threadLocal.set(j);
                    }
                    System.out.println("thread: " + Thread.currentThread().getName() + " result: " + threadLocal.get());
                }
            }).start();
        }
    }


}
