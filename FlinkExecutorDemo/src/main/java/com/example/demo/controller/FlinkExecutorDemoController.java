package com.example.demo.controller;

import com.example.demo.entity.FlinkJobParams;
import com.example.demo.utils.CommandUtil;
import com.smcaiot.cloud.common.entity.RestResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.client.deployment.ClusterDeploymentException;
import org.apache.flink.client.deployment.ClusterSpecification;
import org.apache.flink.client.deployment.application.ApplicationConfiguration;
import org.apache.flink.client.program.ClusterClient;
import org.apache.flink.client.program.ClusterClientProvider;
import org.apache.flink.configuration.*;
import org.apache.flink.yarn.YarnClientYarnClusterInformationRetriever;
import org.apache.flink.yarn.YarnClusterDescriptor;
import org.apache.flink.yarn.YarnClusterInformationRetriever;
import org.apache.flink.yarn.configuration.YarnConfigOptions;
import org.apache.flink.yarn.configuration.YarnDeploymentTarget;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
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
//    private static final String HDFS_DOMAIN = "v81:9000";

    @PostMapping(value = "/execFlinkJob")
    @ApiOperation(value = "执行flink任务", notes = "")
    public RestResp execFlinkJob(@RequestBody FlinkJobParams params) {
        log.info("begin");
        try {
            String jarHdfsUrl = params.getJarHdfsUrl();
            if(!validPath(jarHdfsUrl)){
                return RestResp.failureRestResp("文件路径不正确: "+jarHdfsUrl);
            }
            String fileName = jarHdfsUrl.substring(jarHdfsUrl.lastIndexOf("/") + 1, jarHdfsUrl.length());
            log.info("fileName = {}", fileName);

//            List<String> hdfsGet = CommandUtil.runLinuxCmd("hdfs dfs -get " + jarHdfsUrl + " /ops/data/flinkexecutordemo/");
//            List<String> flinkRun = CommandUtil.runLinuxCmd("/ops/app/flink-1.11.1/bin/flink run /ops/data/flinkexecutordemo/" + fileName);
//            CommandUtil.runLinuxCmd("echo 111;echo 222;mkdir /ops/data/flinkexecutordemo/111;");
//            StringBuffer sb = new StringBuffer();
//            sb.append("/ops/app/flink-1.11.1/bin/flink run-application -t yarn-application \\");
//            sb.append("-Djobmanager.memory.process.size=1024m \\");
//            sb.append("-Dtaskmanager.memory.process.size=2048m \\");
//            sb.append("-Dyarn.application.name=\"MyFlinkWordCount\" \\");
//            sb.append("-Dtaskmanager.numberOfTaskSlots=1 \\");
//            sb.append("-Dyarn.provided.lib.dirs=\"hdfs://"+HDFS_DOMAIN+"/flink/libs/lib;hdfs://"+HDFS_DOMAIN+"/flink/libs/plugins\" hdfs://"+HDFS_DOMAIN+jarHdfsUrl);
//            CommandUtil.runLinuxCmd(sb.toString());
            CommandUtil.runLinuxCmd("/ops/app/flinkexecutordemo/flink-application.sh "+jarHdfsUrl);

            log.info("end");
            return RestResp.successRestResp("成功");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return RestResp.failureRestResp("失败, " + e.getLocalizedMessage());
        }
    }

    private boolean validPath(String jarHdfsUrl){
        log.debug("jarHdfsUrl = {}", jarHdfsUrl);
        if(StringUtils.isBlank(jarHdfsUrl)){
            return false;
        }
        int i = jarHdfsUrl.lastIndexOf("/");
        if(i==-1){
            return false;
        }
        return true;
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

    @PostMapping(value = "/submitFlinkApplication")
    @ApiOperation(value = "执行flink任务", notes = "")
    public RestResp submitFlinkApplication(@RequestBody FlinkJobParams params) {
        log.info("begin");
        try {
            String jarHdfsUrl = params.getJarHdfsUrl();
            if(!validPath(jarHdfsUrl)){
                return RestResp.failureRestResp("文件路径不正确: "+jarHdfsUrl);
            }

            String fileName = jarHdfsUrl.substring(jarHdfsUrl.lastIndexOf("/") + 1, jarHdfsUrl.length());
            log.info("fileName = {}", fileName);

            //flink的本地配置目录，为了得到flink的配置
            String configurationDirectory = "/ops/app/flink-1.11.1/conf/";
//		    String configurationDirectory = "D:\\workspace\\myfile\\learn\\TestDemo\\FlinkDemo\\src\\main\\resources\\conf";
//		    String configurationDirectory = "hdfs://"+ HDFS_DOMAIN +"/flink/conf";
//		    String configurationDirectory = getFlinkConfPath();
            //存放flink集群相关的jar包目录
            String flinkLibs = "hdfs://"+ HDFS_DOMAIN +"/flink/libs/lib";
            //用户jar
//		    String userJarPath = "hdfs://"+ HDFS_DOMAIN +"/flink/jars/WordCount.jar";
//            String userJarPath = "hdfs://"+ HDFS_DOMAIN +"/flink/jars/hello-jar-with-dependencies.jar";
            String userJarPath = "hdfs://"+ HDFS_DOMAIN +params.getJarHdfsUrl();

            String flinkDistJar = "hdfs://"+ HDFS_DOMAIN +"/flink/libs/flink-yarn_2.11-1.11.1.jar";

            log.info("--------------------------yarnClient begin--------------------------");
            YarnClient yarnClient = YarnClient.createYarnClient();
            YarnConfiguration yarnConfiguration = new YarnConfiguration();
            yarnClient.init(yarnConfiguration);
            yarnClient.start();
            log.info("--------------------------yarnClient start--------------------------");

            YarnClusterInformationRetriever clusterInformationRetriever = YarnClientYarnClusterInformationRetriever.create(yarnClient);

            log.info("--------------------------flinkConfiguration begin--------------------------");
            //获取flink的配置
            Configuration flinkConfiguration = GlobalConfiguration.loadConfiguration(configurationDirectory);
            flinkConfiguration.set(CheckpointingOptions.INCREMENTAL_CHECKPOINTS, true);
            flinkConfiguration.set(PipelineOptions.JARS, Collections.singletonList(userJarPath));

            Path remoteLib = new Path(flinkLibs);
            flinkConfiguration.set(YarnConfigOptions.PROVIDED_LIB_DIRS,Collections.singletonList(remoteLib.toString()));

            flinkConfiguration.set(YarnConfigOptions.FLINK_DIST_JAR,flinkDistJar);
            //设置为application模式
            flinkConfiguration.set(DeploymentOptions.TARGET, YarnDeploymentTarget.APPLICATION.getName());
            //yarn application name
            flinkConfiguration.set(YarnConfigOptions.APPLICATION_NAME, "jobName");
            log.info("--------------------------flinkConfiguration end--------------------------");

            ClusterSpecification clusterSpecification = new ClusterSpecification.ClusterSpecificationBuilder().createClusterSpecification();

            // 设置用户jar的参数和主类
            List<String> argsList = params.getArgsList();
            String[] args = null;
            if(!CollectionUtils.isEmpty(argsList)){
                args = argsList.toArray(new String[]{});
            }
            ApplicationConfiguration appConfig = new ApplicationConfiguration(args, null);

            YarnClusterDescriptor yarnClusterDescriptor = new YarnClusterDescriptor(
                    flinkConfiguration,
                    yarnConfiguration,
                    yarnClient,
                    clusterInformationRetriever,
                    true);
            log.info("--------------------------yarnClusterDescriptor.deployApplicationCluster begin--------------------------");
            ClusterClientProvider<ApplicationId> clusterClientProvider = yarnClusterDescriptor.deployApplicationCluster(
                    clusterSpecification,
                    appConfig);
            log.info("--------------------------yarnClusterDescriptor.deployApplicationCluster end--------------------------");

            log.info("--------------------------getClusterClient begin--------------------------");
            ClusterClient<ApplicationId> clusterClient = clusterClientProvider.getClusterClient();
            ApplicationId applicationId = clusterClient.getClusterId();
            log.info("--------------------------applicationId = "+applicationId+"--------------------------");
            log.info("--------------------------getClusterClient end--------------------------");

            log.info("--------------------------end--------------------------");

            log.info("end");
            return RestResp.successRestResp("成功");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return RestResp.failureRestResp("失败, " + e.getLocalizedMessage());
        }
    }

}
