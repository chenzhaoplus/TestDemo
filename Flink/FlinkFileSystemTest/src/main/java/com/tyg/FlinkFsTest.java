package com.tyg;

import cn.hutool.core.io.FileUtil;
import com.tyg.model.FsConnConfig;
import com.tyg.utils.HdfsUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.flink.api.common.RuntimeExecutionMode;
import org.apache.flink.client.program.StreamContextEnvironment;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.DeploymentOptions;
import org.apache.flink.configuration.ExecutionOptions;
import org.apache.flink.configuration.JobManagerOptions;
import org.apache.flink.core.execution.DefaultExecutorServiceLoader;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * @Author: cz
 * @Date: 2021/9/26
 * @Description:
 */
public class FlinkFsTest {

    public static final String BATCH = "batch";
    public static final String FLINK_IP = "172.16.88.102";
    public static final Integer FLINK_PORT = 8081;
    private static final String FLINK_REMOTE_TARGET = "remote";

    public static void main(String[] args) {
        String srcExcel = "C:\\Users\\chenz\\Desktop\\123.xlsx";
        String dstDir = "/csv/in";
        String modelId = "45s6d4fs6f16ertwfw";
        String downDir = "/csv/out";
        String toDirPath = "C:\\Users\\chenz\\Desktop";
        FsConnConfig connConf = FsConnConfig.builder()
                .ip("172.16.88.102")
                .port("8020")
                .build();

        FlinkFsTest test = new FlinkFsTest();
        TableEnvironment tEnv = test.createTableEnvironment(BATCH);

        //String createSrcTableSql = readRes("sql/CreateSrcTable.sql");
        //TableResult tableResult = tEnv.executeSql(createSrcTableSql);

        createCsvTable(tEnv);
        HdfsUtils.uploadCsvByExcel(connConf, srcExcel, dstDir + "/" + modelId);
        HdfsUtils.delFiles(connConf, downDir + "/" + modelId, true);
        insertCsvTable(tEnv);
        HdfsUtils.downFileByCsv(connConf, downDir + "/" + modelId, toDirPath);

        //testExeSql(tEnv);
    }

    private static void createCsvTable(TableEnvironment tEnv) {
        String srcSql = "CREATE TABLE csvin (\n" +
                "\t`设备类型` string,\n" +
                "\t`字段` string,\n" +
                "\t`字段类型` string,\n" +
                "\t`字段说明` string\n" +
                ") WITH (\n" +
                "  'connector' = 'filesystem',           \n" +
                "  'path' = 'hdfs://master.dev.smcaiot.com:8020/csv/in/45s6d4fs6f16ertwfw',\n" +
                "  'format' = 'csv'\n" +
                ")";
        String dstSql = "CREATE TABLE csvout (\n" +
                "\t`设备类型` string,\n" +
                "\t`字段` string,\n" +
                "\t`字段类型` string,\n" +
                "\t`字段说明` string\n" +
                ") WITH (\n" +
                "  'connector' = 'filesystem',           \n" +
                "  'path' = 'hdfs://master.dev.smcaiot.com:8020/csv/out/45s6d4fs6f16ertwfw',\n" +
                "  'format' = 'csv'\n" +
                ")";

        tEnv.executeSql(srcSql);
        tEnv.executeSql(dstSql);
    }

    private static void insertCsvTable(TableEnvironment tEnv) {
        String insertSql = "insert into csvout select * from csvin";
        tEnv.executeSql(insertSql);
    }

    private static void testExeSql(TableEnvironment tEnv) {
        String srcSql = "CREATE TABLE if not exists `basic_car_test1` (\n" +
                "  `license` string,\n" +
                "  `license_type` string\n" +
                ") WITH (\n" +
                "   'connector' = 'jdbc',\n" +
                "   'url' = 'jdbc:postgresql://172.16.4.82:15432/statistical_analysis?useSSL=false&characterEncoding=utf-8&serverTimezone=GMT%2B8',\n" +
                "   'table-name' = 'ods_tmp.basic_car_test1',\n" +
                "   'username' = 'postgres',\n" +
                "   'password' = 'smcaiot'\n" +
                ")";
        String dstSql = "CREATE TABLE if not exists `basic_car_test2` (\n" +
                "  `license` string,\n" +
                "  `license_type` string\n" +
                ") WITH (\n" +
                "   'connector' = 'jdbc',\n" +
                "   'url' = 'jdbc:postgresql://172.16.4.82:15432/statistical_analysis?useSSL=false&characterEncoding=utf-8&serverTimezone=GMT%2B8',\n" +
                "   'table-name' = 'ods_tmp.basic_car_test2',\n" +
                "   'username' = 'postgres',\n" +
                "   'password' = 'smcaiot'\n" +
                ")";
        String insertSql = "INSERT INTO basic_car_test2 SELECT * from basic_car_test1 limit 1";

        tEnv.executeSql(srcSql);
        tEnv.executeSql(dstSql);
        tEnv.executeSql(insertSql);
    }

    private TableEnvironment createTableEnvironment(String type) {
        //EnvironmentSettings settings = EnvironmentSettings.newInstance().build();
        //return TableEnvironment.create(settings);

        if (BATCH.equalsIgnoreCase(type)) {
            Configuration configuration = new Configuration();
            configuration.set(ExecutionOptions.RUNTIME_MODE, RuntimeExecutionMode.BATCH);
            configuration.setString(JobManagerOptions.ADDRESS, FLINK_IP);
            configuration.setInteger(JobManagerOptions.PORT, FLINK_PORT);
            configuration.setString(DeploymentOptions.TARGET, FLINK_REMOTE_TARGET);
            configuration.setBoolean(DeploymentOptions.ATTACHED, true);
            StreamContextEnvironment.setAsContext(new DefaultExecutorServiceLoader(), configuration,
                    this.getClass().getClassLoader(), false, false);
            return TableEnvironment.create(configuration);
        }
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createRemoteEnvironment(FLINK_IP, FLINK_PORT);
        return StreamTableEnvironment.create(env);
    }

    public static String readRes(String filePath) {
        String os = System.getProperty("os.name").toLowerCase();
        String path = "";
        String fileName = FilenameUtils.getName(filePath);
        String dirPath = FilenameUtils.getPath(filePath);
        if (os.contains("windows")) {
            path = FlinkFsTest.class.getResource("/").getPath() + dirPath;
        } else {
            path = "/opt/" + dirPath;
        }
        return FileUtil.readString(path + fileName, "utf-8");
    }

}
