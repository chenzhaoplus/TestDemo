package com.examples.test.thread.future;

import java.util.concurrent.*;

/**
 * @Author: cz
 * @Date: 2020/8/10
 * @Description:
 */
public class CookFutureTest {
    public static void main(String[] args) {
        ThreadPoolExecutor es = new ThreadPoolExecutor(5, 10, 200, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(5));
        chuju cj = new chuju();
        shicai sc = new shicai();
        Future<Boolean> f1 = es.submit(cj);
        Future<Boolean> f2 = es.submit(sc);
        try {
            Boolean b1 = f1.get();//会阻塞当前线程
            Boolean b2 = f2.get();
            System.out.println(b1);
            System.out.println(b2);
            if (b1 && b2) {
                zuofan zf = new zuofan();
                es.submit(zf);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        es.shutdown();
    }
}
