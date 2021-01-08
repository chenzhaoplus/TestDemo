package com.examples.test.thread.synchronize;

/**
 * @Author: cz
 * @Date: 2020/8/17
 * @Description:
 */
public class SynchronizedDemo implements Runnable {
    private static Integer count = 0;

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(new SynchronizedDemo());
            thread.start();
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("result: " + count);
    }

    @Override
    public void run() {
//        synchronized (count) {
        synchronized (SynchronizedDemo.class) {
            for (int i = 0; i < 1000000; i++) {
                count++;
            }
        }
    }
}
