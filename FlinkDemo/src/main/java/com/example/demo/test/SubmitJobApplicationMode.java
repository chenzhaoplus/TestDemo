package com.example.demo.test;

import org.apache.flink.client.deployment.ClusterDeploymentException;
import org.apache.flink.client.deployment.ClusterSpecification;
import org.apache.flink.client.deployment.application.ApplicationConfiguration;
import org.apache.flink.client.program.ClusterClient;
import org.apache.flink.client.program.ClusterClientProvider;
import org.apache.flink.configuration.CheckpointingOptions;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.DeploymentOptions;
import org.apache.flink.configuration.GlobalConfiguration;
import org.apache.flink.configuration.PipelineOptions;
import org.apache.flink.yarn.YarnClientYarnClusterInformationRetriever;
import org.apache.flink.yarn.YarnClusterDescriptor;
import org.apache.flink.yarn.YarnClusterInformationRetriever;
import org.apache.flink.yarn.configuration.YarnConfigOptions;
import org.apache.flink.yarn.configuration.YarnDeploymentTarget;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.yarn.api.records.ApplicationId;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.conf.YarnConfiguration;

import java.util.Collections;

public class SubmitJobApplicationMode{

//	private static final String DOMAIN = "v81:9000";
	private static final String DOMAIN = "master:8020";

	public static void main(String[] args){
		System.out.println("--------------------------begin--------------------------");

		//flink的本地配置目录，为了得到flink的配置
//		String configurationDirectory = "/ops/app/flink-1.11.1/conf/";
		String configurationDirectory = getFlinkConfPath();
		//存放flink集群相关的jar包目录
		String flinkLibs = "hdfs://"+DOMAIN+"/flink/libs/lib";
		//用户jar
		String userJarPath = "hdfs://"+DOMAIN+"/flink/jars/WordCount.jar";
		String flinkDistJar = "hdfs://"+DOMAIN+"/flink/libs/flink-yarn_2.11-1.11.1.jar";

		System.out.println("--------------------------yarnClient begin--------------------------");
		YarnClient yarnClient = YarnClient.createYarnClient();
		YarnConfiguration yarnConfiguration = new YarnConfiguration();
		yarnClient.init(yarnConfiguration);
		yarnClient.start();
		System.out.println("--------------------------yarnClient start--------------------------");

		YarnClusterInformationRetriever clusterInformationRetriever = YarnClientYarnClusterInformationRetriever.create(yarnClient);

		System.out.println("--------------------------flinkConfiguration begin--------------------------");
		//获取flink的配置
		Configuration flinkConfiguration = GlobalConfiguration.loadConfiguration(configurationDirectory);
		flinkConfiguration.set(CheckpointingOptions.INCREMENTAL_CHECKPOINTS, true);
		flinkConfiguration.set(PipelineOptions.JARS, Collections.singletonList(userJarPath));

		Path remoteLib = new Path(flinkLibs);
		flinkConfiguration.set(YarnConfigOptions.PROVIDED_LIB_DIRS,Collections.singletonList(remoteLib.toString()));

		flinkConfiguration.set(YarnConfigOptions.FLINK_DIST_JAR,flinkDistJar);
		//设置为application模式
		flinkConfiguration.set(DeploymentOptions.TARGET,YarnDeploymentTarget.APPLICATION.getName());
		//yarn application name
		flinkConfiguration.set(YarnConfigOptions.APPLICATION_NAME, "jobName");
		System.out.println("--------------------------flinkConfiguration end--------------------------");

		ClusterSpecification clusterSpecification = new ClusterSpecification.ClusterSpecificationBuilder().createClusterSpecification();

		// 设置用户jar的参数和主类
		ApplicationConfiguration appConfig = new ApplicationConfiguration(args, null);

		YarnClusterDescriptor yarnClusterDescriptor = new YarnClusterDescriptor(
				flinkConfiguration,
				yarnConfiguration,
				yarnClient,
				clusterInformationRetriever,
				true);
		ClusterClientProvider<ApplicationId> clusterClientProvider = null;
		try {
			System.out.println("--------------------------yarnClusterDescriptor.deployApplicationCluster begin--------------------------");
			clusterClientProvider = yarnClusterDescriptor.deployApplicationCluster(
					clusterSpecification,
					appConfig);
			System.out.println("--------------------------yarnClusterDescriptor.deployApplicationCluster end--------------------------");
		} catch (ClusterDeploymentException e){
			e.printStackTrace();
			System.out.println("error = "+e.getMessage());
		}

		System.out.println("--------------------------getClusterClient begin--------------------------");
		ClusterClient<ApplicationId> clusterClient = clusterClientProvider.getClusterClient();
		ApplicationId applicationId = clusterClient.getClusterId();
		System.out.println("--------------------------applicationId = "+applicationId+"--------------------------");
		System.out.println("--------------------------getClusterClient end--------------------------");

		System.out.println("--------------------------end--------------------------");
	}

	private static String getFlinkConfPath(){
		String os = System.getProperty("os.name").toLowerCase();
		String path = SubmitJobApplicationMode.class.getResource("/").getPath().replaceAll("%20", " ").substring(1).replace("/", "\\");
		if (!os.contains("windows")) {
			path = "/ops/app/flink-1.11.1/conf/";
		}
		return path;
	}

}