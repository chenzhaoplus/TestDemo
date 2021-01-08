package com.examples.test.thread.threadlocal;

import com.examples.test.entity.FaceDTO;
import com.examples.test.entity.ThreadlocalPojo;
import com.examples.test.util.GsonUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.util.Date;

/**
 * @Author: cz
 * @Date: 2020/8/6
 * @Description:
 */
public class ThreadLocalStudy {

    /**
     * 使用threadlocal要注意防止内存泄露，最好在线程完成之前执行一下threadLocal.remove()方法
     */
    private static ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>() {
        @Override
        public Integer initialValue() {
            return 0;
        }
    };

    private static ThreadlocalPojo threadlocalPojo = new ThreadlocalPojo();
    private static ThreadLocal<ThreadlocalPojo> tlStatic = new ThreadLocal<ThreadlocalPojo>() {
        @Override
        public ThreadlocalPojo initialValue() {
            return threadlocalPojo;
        }
    };

    public static void main(String[] args) throws InterruptedException {
        ThreadLocalStudy study = new ThreadLocalStudy();
//        study.testThreadLocal();
        study.testStatic();
        while (true) {
            System.out.println(threadlocalPojo.getAge());
            Thread.sleep(3000);
        }
    }

    /**
     * 测试threadLocal里的对象，在多线程里是否相互影响
     * 结论： 不影响
     */
    private void testThreadLocal() {
        for (int i = 0; i < 100; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 100; j++) {
                        threadLocal.set(j);
                    }
                    System.out.println("thread: " + Thread.currentThread().getName() + " result: " + threadLocal.get());
                }
            }).start();
        }
    }

    /**
     * 测试静态变量放入threadlocal，多线程里面对象是否是同一个
     * 结论：  静态对象在threadlocal里，threadlocalPojo会有线程安全问题，但是tlStatic没有线程安全问题，即tlStatic在每个线程是一个新的副本，但是tlStatic里面承载的value不一定
     */
    private void testStatic() {
        for (int i = 0; i < 100; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ThreadlocalPojo threadlocalPojo = tlStatic.get();
                    Long current = System.currentTimeMillis();
                    threadlocalPojo.setAge(current.intValue());
                    tlStatic.set(threadlocalPojo);
                    System.out.println("thread: " + Thread.currentThread().getName() + " result: " + tlStatic.get() + " age: " + threadlocalPojo.getAge());
                }
            }).start();
        }
    }

}
