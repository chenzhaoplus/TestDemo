package com.example.demo.test;

import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Upjar {

    private static final String HDFS_DOMAIN = "master:8020";

    public static void main(String[] args) throws IOException {
        List<String> arg = new ArrayList<>();
        arg.add("sh");
        arg.add("-c");
        arg.add("./flink run -p 2 -C file://绝对路径 -C file://绝对路径 -C file://绝对路径 -c 主类路径 主类所在jar包的绝对路径\n");
        ProcessBuilder pb = new ProcessBuilder(arg);
        //设置工作目录
        pb.directory(new File("/ops/app/flink-1.11.1/bin"));
        //redirectErrorStream 属性默认值为false，意思是子进程的标准输出和错误输出被发送给两个独立的流，可通过 Process.getInputStream() 和 Process.getErrorStream() 方法来访问
        //值设置为 true，标准错误将与标准输出合并。合并的数据可从 Process.getInputStream() 返回的流读取，而从 Process.getErrorStream() 返回的流读取将直接到达文件尾
        pb.redirectErrorStream(true);
        File log = new File("/ops/app/flink-1.11.1/logs");
        pb.redirectOutput(ProcessBuilder.Redirect.appendTo(log));
        Process p = pb.start();
        assert pb.redirectInput() == ProcessBuilder.Redirect.PIPE;
        //重定向标准输出到日志
        assert pb.redirectOutput().file() == log;
        assert p.getInputStream().read() == -1;
    }

    private void getJarFromHdfs(){
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        DataSource<String> jar = env.readTextFile("hdfs://"+HDFS_DOMAIN+"/flink/jars/WordCount.jar");

    }

}

