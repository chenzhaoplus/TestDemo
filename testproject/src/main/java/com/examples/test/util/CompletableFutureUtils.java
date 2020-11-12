package com.examples.test.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

/**
 * @Author: cz
 * @Date: 2020/9/11
 * @Description:
 */
public class CompletableFutureUtils {

    /**
     * futureList所有的任务完成以后，返回聚合后的值
     * @param futureList
     * @param <T>
     * @return
     */
    public static <T> List<T> allOfFutures(List<CompletableFuture<List<T>>> futureList){
        if(futureList==null || futureList.isEmpty()){
            return Collections.emptyList();
        }
        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.allOf(futureList.toArray(new CompletableFuture[futureList.size()]));
        CompletableFuture<List<T>> allFutures = voidCompletableFuture.thenApplyAsync(v -> {
            List<T> tempList = new ArrayList<>();
            for(CompletableFuture<List<T>> future:futureList){
                List<T> join = future.join();
                if(join==null || join.isEmpty()){
                    continue;
                }
                tempList.addAll(join);
            }
            return tempList;
        });
        List<T> result = new ArrayList<>();
        try {
            result = allFutures.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return result;
    }

}
