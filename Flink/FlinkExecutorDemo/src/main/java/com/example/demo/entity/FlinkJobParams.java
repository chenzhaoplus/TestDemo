package com.example.demo.entity;

import lombok.Data;

import java.util.List;

/**
 * @Author: cz
 * @Date: 2021/1/12
 * @Description:
 */
@Data
public class FlinkJobParams {

    /** flink任务执行脚本的本地路径 */
    private String scriptPath;
    /** flink任务的jar包在hdfs上的地址 */
    private String jarHdfsUrl;
    /** 任务参数列表 */
    private List<String> argsList;

}
