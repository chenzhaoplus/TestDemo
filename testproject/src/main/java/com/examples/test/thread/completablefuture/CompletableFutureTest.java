package com.examples.test.thread.completablefuture;

import com.alibaba.fastjson.JSON;
import com.examples.test.util.CompletableFutureUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @Author: cz
 * @Date: 2020/9/9
 * @Description:
 */
public class CompletableFutureTest {

    public static void main(String[] args) {
        List<String> result = new ArrayList<>();
        List<CompletableFuture<List<String>>> futureList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            CompletableFuture<List<String>> future = CompletableFuture.supplyAsync(() -> {
                List<String> tempList = run();
                return tempList;
            }).exceptionally(throwable -> {
                return Collections.emptyList();
            });
            futureList.add(future);
            System.out.println("ThreadName = "+Thread.currentThread() + " run at " + new Date());
        }

        //方法一：等待所有线程完成之后，聚合数据
        result = CompletableFutureUtils.allOfFutures(futureList);
        //方法二：等待所有线程完成之后，聚合数据
//        for(CompletableFuture<List<String>> future:futureList){
//            List<String> s = future.join();
//            if(s!=null && s.size()>0){
//                result.addAll(s);
//            }
//        }

        System.out.println("result = "+ JSON.toJSONString(result));
        while(true){
            // 防止主程序跑完了，run()方法里的线程还没有打印完
        }
    }

    private static List<String> run(){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        List<String> list = new ArrayList<>();
        String s = Thread.currentThread() + " run at " + new Date();
        list.add(s);
        list.add(s);
        System.out.println(s);
        return list;
    }

}
