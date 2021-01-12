package com.example.demo.test;

import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;

/**
 * @Author: cz
 * @Date: 2021/1/12
 * @Description:
 */
public class SubmitJobFlinkMode {

    private static final String HDFS_DOMAIN = "master:8020";

    public static void main(String[] args) {
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        DataSource<String> text = env.readTextFile("hdfs://"+HDFS_DOMAIN+"/user/wuhulala/input/core-site.xml");
    }

}
