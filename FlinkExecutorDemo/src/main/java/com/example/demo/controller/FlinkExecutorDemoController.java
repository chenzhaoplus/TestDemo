package com.example.demo.controller;

import com.example.demo.entity.FlinkJobParams;
import com.example.demo.utils.CommandUtil;
import com.smcaiot.cloud.common.entity.RestResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: cz
 * @Date: 2021/1/12
 * @Description:
 */
@RestController
@RequestMapping("/flinkexecutordemo")
@Api(tags = {"FlinkExecutorDemo"})
@Slf4j
public class FlinkExecutorDemoController {

    private static final String HDFS_DOMAIN = "master:8020";

    public static void main(String[] args) {
        String jarHdfsUrl = "/flink/jars/WordCount.jar";
        String substring = jarHdfsUrl.substring(jarHdfsUrl.lastIndexOf("/")+1, jarHdfsUrl.length());
        System.out.println(substring);
    }

    @PostMapping(value = "/execFlinkJob")
    @ApiOperation(value = "执行flink任务", notes = "")
    public RestResp execFlinkJob(@RequestBody FlinkJobParams params) {
        log.info("begin");
        try {
//            ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
            String jarHdfsUrl = params.getJarHdfsUrl();
            log.debug("jarHdfsUrl = {}", jarHdfsUrl);
            if(StringUtils.isBlank(jarHdfsUrl)){
                return RestResp.failureRestResp("文件路径不正确: "+jarHdfsUrl);
            }
            int i = jarHdfsUrl.lastIndexOf("/");
            if(i==-1){
                return RestResp.failureRestResp("文件路径不正确: "+jarHdfsUrl);
            }
            String fileName = jarHdfsUrl.substring(i + 1, jarHdfsUrl.length());
            log.debug("fileName = {}", fileName);

//            String[] command = new String[]{
//                    "hdfs dfs -get "+jarHdfsUrl+" /ops/data/flinkexecutordemo/",
//                    "/ops/app/flink-1.11.1/bin/flink run /ops/data/flinkexecutordemo/" + fileName
//            };

            log.info("begin ------------ hdfs dfs -get " + jarHdfsUrl + " /ops/data/flinkexecutordemo/");
            List<String> hdfsGet = CommandUtil.runLinuxCmd("hdfs dfs -get " + jarHdfsUrl + " /ops/data/flinkexecutordemo/");
            log.info("end ------------ hdfsGet = {}", hdfsGet);

            log.info("begin ------------ /ops/app/flink-1.11.1/bin/flink run /ops/data/flinkexecutordemo/" + fileName);
            List<String> flinkRun = CommandUtil.runLinuxCmd("/ops/app/flink-1.11.1/bin/flink run /ops/data/flinkexecutordemo/" + fileName);
            log.info("end ------------ flinkRun = {}", flinkRun);

            log.info("end");
            return RestResp.successRestResp("成功");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return RestResp.failureRestResp("失败, " + e.getLocalizedMessage());
        }
    }

    private static String getFlinkConfPath(){
        String os = System.getProperty("os.name").toLowerCase();
        String path = FlinkExecutorDemoController.class.getResource("/").getPath().replaceAll("%20", " ").substring(1).replace("/", "\\");
        if (!os.contains("windows")) {
            path = "/ops/data/flinkexecutordemo";
        }else{
            path += "..\\..\\flinkconf\\";
        }
        return path;
    }

}
