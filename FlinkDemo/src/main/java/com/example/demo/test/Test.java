package com.example.demo.test;

/**
 * @Author: cz
 * @Date: 2021/1/8
 * @Description:
 */
public class Test {

    private static String os = System.getProperty("os.name").toLowerCase();

    public static void main(String[] args) {
        String path = Test.class.getResource("/").getPath().replaceAll("%20", " ").substring(1).replace("/", "\\");
        if (!os.contains("windows")) {
            path = "/ops/app/flink-1.11.1/conf/";
        }
    }

}
