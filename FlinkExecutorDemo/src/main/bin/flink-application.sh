#!/bin/bash
#Author cz
#Date 2020.8.2

start(){
	/ops/app/flink-1.11.1/bin/flink run-application -t yarn-application \
	-Djobmanager.memory.process.size=1024m \
	-Dtaskmanager.memory.process.size=2048m \
	-Dyarn.application.name="MyFlinkWordCount" \
	-Dtaskmanager.numberOfTaskSlots=1 \
	-Dyarn.provided.lib.dirs="hdfs://v81:9000/flink/libs/lib;hdfs://v81:9000/flink/libs/plugins" hdfs://v81:9000${1}
}

start ${1}