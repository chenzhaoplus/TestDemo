package com.examples.test.thread.rejectpolicy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: cz
 * @Date: 2020/8/15
 * @Description:
 */
public class MyTask implements Runnable{

    private int taskNum;

    public MyTask(int num) {
        this.taskNum = num;
    }

    @Override
    public void run() {
//        System.out.println("正在执行task "+taskNum);
        try {
            List<Map<String,Object>> mapList = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                Map<String,Object> map = new HashMap<>();
                for (int j = 0; j < 100; j++) {
                     map.put(String.valueOf(i),j);
                }
                mapList.add(map);
            }
            Thread.currentThread().sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("task "+taskNum+"执行完毕");

    }

}
