#!/bin/bash
#Author cz
#Date 2020.8.2

#hdfsDomain="master:8020"
hdfsDomain="v81:9000"

# 参数1：任务jar包在hdfs上的相对路径
# 参数2：运行jar包传递的参数
start(){
/ops/app/flink-1.11.1/bin/flink run-application -t yarn-application \
-Djobmanager.memory.process.size=1024m \
-Dtaskmanager.memory.process.size=2048m \
-Dyarn.application.name="MyFlinkWordCount" \
-Dtaskmanager.numberOfTaskSlots=1 \
-Dyarn.provided.lib.dirs="hdfs://${hdfsDomain}/flink/libs/lib;hdfs://${hdfsDomain}/flink/libs/plugins" hdfs://${hdfsDomain}${1} ${2}
  echo ------------------------ -Dyarn.provided.lib.dirs="hdfs://${hdfsDomain}/flink/libs/lib;hdfs://${hdfsDomain}/flink/libs/plugins" hdfs://${hdfsDomain}${1} ${2} ------------------------
}

start "${1}" "${2}"
# 执行示例：
# ./flink-application.sh "/flink/jars/flinkwordcount-jar-with-dependencies.jar" "--input file:///ops/app/flinkexecutordemo/input.log --output file:///ops/app/flinkexecutordemo/output.log"