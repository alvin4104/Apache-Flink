#!/bin/bash

set -o pipefail -e
export PRELAUNCH_OUT="/home/alvinnguyen41/hadoop-3.4.1/logs/userlogs/application_1735734892238_0001/container_1735734892238_0001_01_000001/prelaunch.out"
exec >"${PRELAUNCH_OUT}"
export PRELAUNCH_ERR="/home/alvinnguyen41/hadoop-3.4.1/logs/userlogs/application_1735734892238_0001/container_1735734892238_0001_01_000001/prelaunch.err"
exec 2>"${PRELAUNCH_ERR}"
echo "Setting up env variables"
export HADOOP_COMMON_HOME=${HADOOP_COMMON_HOME:-"/home/alvinnguyen41/hadoop-3.4.1"}
export HADOOP_HDFS_HOME=${HADOOP_HDFS_HOME:-"/home/alvinnguyen41/hadoop-3.4.1"}
export HADOOP_CONF_DIR=${HADOOP_CONF_DIR:-"/home/alvinnguyen41/hadoop-3.4.1/etc/hadoop"}
export HADOOP_YARN_HOME=${HADOOP_YARN_HOME:-"/home/alvinnguyen41/hadoop-3.4.1"}
export HADOOP_MAPRED_HOME=${HADOOP_MAPRED_HOME:-"/home/alvinnguyen41/hadoop-3.4.1"}
export HADOOP_TOKEN_FILE_LOCATION="/tmp/hadoop-alvinnguyen41/nm-local-dir/usercache/alvinnguyen41/appcache/application_1735734892238_0001/container_1735734892238_0001_01_000001/container_tokens"
export CONTAINER_ID="container_1735734892238_0001_01_000001"
export NM_PORT="35897"
export NM_HOST="localhost"
export NM_HTTP_PORT="8042"
export LOCAL_DIRS="/tmp/hadoop-alvinnguyen41/nm-local-dir/usercache/alvinnguyen41/appcache/application_1735734892238_0001"
export LOCAL_USER_DIRS="/tmp/hadoop-alvinnguyen41/nm-local-dir/usercache/alvinnguyen41/"
export LOG_DIRS="/home/alvinnguyen41/hadoop-3.4.1/logs/userlogs/application_1735734892238_0001/container_1735734892238_0001_01_000001"
export USER="alvinnguyen41"
export LOGNAME="alvinnguyen41"
export HOME="/home/"
export PWD="/tmp/hadoop-alvinnguyen41/nm-local-dir/usercache/alvinnguyen41/appcache/application_1735734892238_0001/container_1735734892238_0001_01_000001"
export LOCALIZATION_COUNTERS="429519118,0,24,0,12091"
export JVM_PID="$$"
export NM_AUX_SERVICE_mapreduce_shuffle="AAA0+gAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA="
export _CLIENT_HOME_DIR="hdfs://localhost:9000/user/alvinnguyen41"
export APP_SUBMIT_TIME_ENV="1735735136093"
export _APP_ID="application_1735734892238_0001"
export HADOOP_USER_NAME="alvinnguyen41"
export APPLICATION_WEB_PROXY_BASE="/proxy/application_1735734892238_0001"
export _CLIENT_SHIP_FILES="YarnLocalResourceDescriptor{key=log4j.properties, path=hdfs://localhost:9000/user/alvinnguyen41/.flink/application_1735734892238_0001/log4j.properties, size=2694, modificationTime=1735735121677, visibility=APPLICATION, type=FILE};YarnLocalResourceDescriptor{key=lib/log4j-slf4j-impl-2.17.1.jar, path=hdfs://localhost:9000/user/alvinnguyen41/.flink/application_1735734892238_0001/lib/log4j-slf4j-impl-2.17.1.jar, size=24279, modificationTime=1735735122267, visibility=APPLICATION, type=FILE};YarnLocalResourceDescriptor{key=lib/flink-table_2.12-1.14.5.jar, path=hdfs://localhost:9000/user/alvinnguyen41/.flink/application_1735734892238_0001/lib/flink-table_2.12-1.14.5.jar, size=39666418, modificationTime=1735735122978, visibility=APPLICATION, type=FILE};YarnLocalResourceDescriptor{key=lib/log4j-core-2.17.1.jar, path=hdfs://localhost:9000/user/alvinnguyen41/.flink/application_1735734892238_0001/lib/log4j-core-2.17.1.jar, size=1790452, modificationTime=1735735123128, visibility=APPLICATION, type=FILE};YarnLocalResourceDescriptor{key=lib/flink-shaded-zookeeper-3.4.14.jar, path=hdfs://localhost:9000/user/alvinnguyen41/.flink/application_1735734892238_0001/lib/flink-shaded-zookeeper-3.4.14.jar, size=7709731, modificationTime=1735735123386, visibility=APPLICATION, type=FILE};YarnLocalResourceDescriptor{key=lib/log4j-api-2.17.1.jar, path=hdfs://localhost:9000/user/alvinnguyen41/.flink/application_1735734892238_0001/lib/log4j-api-2.17.1.jar, size=301872, modificationTime=1735735123501, visibility=APPLICATION, type=FILE};YarnLocalResourceDescriptor{key=lib/flink-json-1.14.5.jar, path=hdfs://localhost:9000/user/alvinnguyen41/.flink/application_1735734892238_0001/lib/flink-json-1.14.5.jar, size=153142, modificationTime=1735735126933, visibility=APPLICATION, type=FILE};YarnLocalResourceDescriptor{key=lib/log4j-1.2-api-2.17.1.jar, path=hdfs://localhost:9000/user/alvinnguyen41/.flink/application_1735734892238_0001/lib/log4j-1.2-api-2.17.1.jar, size=208006, modificationTime=1735735127422, visibility=APPLICATION, type=FILE};YarnLocalResourceDescriptor{key=lib/flink-csv-1.14.5.jar, path=hdfs://localhost:9000/user/alvinnguyen41/.flink/application_1735734892238_0001/lib/flink-csv-1.14.5.jar, size=85586, modificationTime=1735735127892, visibility=APPLICATION, type=FILE};YarnLocalResourceDescriptor{key=plugins/metrics-jmx/flink-metrics-jmx-1.14.5.jar, path=hdfs://localhost:9000/user/alvinnguyen41/.flink/application_1735734892238_0001/plugins/metrics-jmx/flink-metrics-jmx-1.14.5.jar, size=17956, modificationTime=1735735128373, visibility=APPLICATION, type=FILE};YarnLocalResourceDescriptor{key=plugins/metrics-prometheus/flink-metrics-prometheus-1.14.5.jar, path=hdfs://localhost:9000/user/alvinnguyen41/.flink/application_1735734892238_0001/plugins/metrics-prometheus/flink-metrics-prometheus-1.14.5.jar, size=101136, modificationTime=1735735128433, visibility=APPLICATION, type=FILE};YarnLocalResourceDescriptor{key=plugins/README.txt, path=hdfs://localhost:9000/user/alvinnguyen41/.flink/application_1735734892238_0001/plugins/README.txt, size=654, modificationTime=1735735128475, visibility=APPLICATION, type=FILE};YarnLocalResourceDescriptor{key=plugins/metrics-influx/flink-metrics-influxdb-1.14.5.jar, path=hdfs://localhost:9000/user/alvinnguyen41/.flink/application_1735734892238_0001/plugins/metrics-influx/flink-metrics-influxdb-1.14.5.jar, size=983283, modificationTime=1735735128916, visibility=APPLICATION, type=FILE};YarnLocalResourceDescriptor{key=plugins/metrics-slf4j/flink-metrics-slf4j-1.14.5.jar, path=hdfs://localhost:9000/user/alvinnguyen41/.flink/application_1735734892238_0001/plugins/metrics-slf4j/flink-metrics-slf4j-1.14.5.jar, size=10034, modificationTime=1735735129403, visibility=APPLICATION, type=FILE};YarnLocalResourceDescriptor{key=plugins/metrics-datadog/flink-metrics-datadog-1.14.5.jar, path=hdfs://localhost:9000/user/alvinnguyen41/.flink/application_1735734892238_0001/plugins/metrics-datadog/flink-metrics-datadog-1.14.5.jar, size=551092, modificationTime=1735735129873, visibility=APPLICATION, type=FILE};YarnLocalResourceDescriptor{key=plugins/external-resource-gpu/gpu-discovery-common.sh, path=hdfs://localhost:9000/user/alvinnguyen41/.flink/application_1735734892238_0001/plugins/external-resource-gpu/gpu-discovery-common.sh, size=3189, modificationTime=1735735129939, visibility=APPLICATION, type=FILE};YarnLocalResourceDescriptor{key=plugins/external-resource-gpu/nvidia-gpu-discovery.sh, path=hdfs://localhost:9000/user/alvinnguyen41/.flink/application_1735734892238_0001/plugins/external-resource-gpu/nvidia-gpu-discovery.sh, size=1794, modificationTime=1735735130104, visibility=APPLICATION, type=FILE};YarnLocalResourceDescriptor{key=plugins/external-resource-gpu/flink-external-resource-gpu-1.14.5.jar, path=hdfs://localhost:9000/user/alvinnguyen41/.flink/application_1735734892238_0001/plugins/external-resource-gpu/flink-external-resource-gpu-1.14.5.jar, size=15732, modificationTime=1735735130200, visibility=APPLICATION, type=FILE};YarnLocalResourceDescriptor{key=plugins/metrics-graphite/flink-metrics-graphite-1.14.5.jar, path=hdfs://localhost:9000/user/alvinnguyen41/.flink/application_1735734892238_0001/plugins/metrics-graphite/flink-metrics-graphite-1.14.5.jar, size=175143, modificationTime=1735735130320, visibility=APPLICATION, type=FILE};YarnLocalResourceDescriptor{key=plugins/metrics-statsd/flink-metrics-statsd-1.14.5.jar, path=hdfs://localhost:9000/user/alvinnguyen41/.flink/application_1735734892238_0001/plugins/metrics-statsd/flink-metrics-statsd-1.14.5.jar, size=11905, modificationTime=1735735130805, visibility=APPLICATION, type=FILE};YarnLocalResourceDescriptor{key=Alvin24-1.0-SNAPSHOT.jar, path=hdfs://localhost:9000/user/alvinnguyen41/.flink/application_1735734892238_0001/Alvin24-1.0-SNAPSHOT.jar, size=238257746, modificationTime=1735735133555, visibility=APPLICATION, type=FILE};YarnLocalResourceDescriptor{key=flink-conf.yaml, path=hdfs://localhost:9000/user/alvinnguyen41/.flink/application_1735734892238_0001/application_1735734892238_0001-flink-conf.yaml2262773656697429789.tmp, size=1010, modificationTime=1735735135725, visibility=APPLICATION, type=FILE}"
export CLASSPATH=":Alvin24-1.0-SNAPSHOT.jar:lib/flink-csv-1.14.5.jar:lib/flink-json-1.14.5.jar:lib/flink-shaded-zookeeper-3.4.14.jar:lib/flink-table_2.12-1.14.5.jar:lib/log4j-1.2-api-2.17.1.jar:lib/log4j-api-2.17.1.jar:lib/log4j-core-2.17.1.jar:lib/log4j-slf4j-impl-2.17.1.jar:flink-dist_2.12-1.14.5.jar:job.graph:flink-conf.yaml::$HADOOP_CONF_DIR:$HADOOP_COMMON_HOME/share/hadoop/common/*:$HADOOP_COMMON_HOME/share/hadoop/common/lib/*:$HADOOP_HDFS_HOME/share/hadoop/hdfs/*:$HADOOP_HDFS_HOME/share/hadoop/hdfs/lib/*:$HADOOP_YARN_HOME/share/hadoop/yarn/*:$HADOOP_YARN_HOME/share/hadoop/yarn/lib/*"
export _FLINK_YARN_FILES="hdfs://localhost:9000/user/alvinnguyen41/.flink/application_1735734892238_0001"
export _FLINK_CLASSPATH=":Alvin24-1.0-SNAPSHOT.jar:lib/flink-csv-1.14.5.jar:lib/flink-json-1.14.5.jar:lib/flink-shaded-zookeeper-3.4.14.jar:lib/flink-table_2.12-1.14.5.jar:lib/log4j-1.2-api-2.17.1.jar:lib/log4j-api-2.17.1.jar:lib/log4j-core-2.17.1.jar:lib/log4j-slf4j-impl-2.17.1.jar:flink-dist_2.12-1.14.5.jar:job.graph:flink-conf.yaml:"
export _FLINK_DIST_JAR="YarnLocalResourceDescriptor{key=flink-dist_2.12-1.14.5.jar, path=hdfs://localhost:9000/user/alvinnguyen41/.flink/application_1735734892238_0001/flink-dist_2.12-1.14.5.jar, size=136098285, modificationTime=1735735134790, visibility=APPLICATION, type=FILE}"
export MALLOC_ARENA_MAX="4"
echo "Setting up job resources"
mkdir -p plugins/external-resource-gpu
ln -sf -- "/tmp/hadoop-alvinnguyen41/nm-local-dir/usercache/alvinnguyen41/appcache/application_1735734892238_0001/filecache/13/flink-external-resource-gpu-1.14.5.jar" "plugins/external-resource-gpu/flink-external-resource-gpu-1.14.5.jar"
mkdir -p lib
ln -sf -- "/tmp/hadoop-alvinnguyen41/nm-local-dir/usercache/alvinnguyen41/appcache/application_1735734892238_0001/filecache/21/log4j-core-2.17.1.jar" "lib/log4j-core-2.17.1.jar"
ln -sf -- "/tmp/hadoop-alvinnguyen41/nm-local-dir/usercache/alvinnguyen41/appcache/application_1735734892238_0001/filecache/24/flink-dist_2.12-1.14.5.jar" "flink-dist_2.12-1.14.5.jar"
mkdir -p lib
ln -sf -- "/tmp/hadoop-alvinnguyen41/nm-local-dir/usercache/alvinnguyen41/appcache/application_1735734892238_0001/filecache/25/log4j-1.2-api-2.17.1.jar" "lib/log4j-1.2-api-2.17.1.jar"
mkdir -p lib
ln -sf -- "/tmp/hadoop-alvinnguyen41/nm-local-dir/usercache/alvinnguyen41/appcache/application_1735734892238_0001/filecache/12/flink-json-1.14.5.jar" "lib/flink-json-1.14.5.jar"
mkdir -p lib
ln -sf -- "/tmp/hadoop-alvinnguyen41/nm-local-dir/usercache/alvinnguyen41/appcache/application_1735734892238_0001/filecache/28/flink-csv-1.14.5.jar" "lib/flink-csv-1.14.5.jar"
mkdir -p plugins
ln -sf -- "/tmp/hadoop-alvinnguyen41/nm-local-dir/usercache/alvinnguyen41/appcache/application_1735734892238_0001/filecache/23/README.txt" "plugins/README.txt"
ln -sf -- "/tmp/hadoop-alvinnguyen41/nm-local-dir/usercache/alvinnguyen41/appcache/application_1735734892238_0001/filecache/15/application_1735734892238_0001606754554455563302.tmp" "job.graph"
mkdir -p plugins/metrics-datadog
ln -sf -- "/tmp/hadoop-alvinnguyen41/nm-local-dir/usercache/alvinnguyen41/appcache/application_1735734892238_0001/filecache/26/flink-metrics-datadog-1.14.5.jar" "plugins/metrics-datadog/flink-metrics-datadog-1.14.5.jar"
mkdir -p plugins/metrics-jmx
ln -sf -- "/tmp/hadoop-alvinnguyen41/nm-local-dir/usercache/alvinnguyen41/appcache/application_1735734892238_0001/filecache/14/flink-metrics-jmx-1.14.5.jar" "plugins/metrics-jmx/flink-metrics-jmx-1.14.5.jar"
mkdir -p lib
ln -sf -- "/tmp/hadoop-alvinnguyen41/nm-local-dir/usercache/alvinnguyen41/appcache/application_1735734892238_0001/filecache/32/flink-table_2.12-1.14.5.jar" "lib/flink-table_2.12-1.14.5.jar"
mkdir -p plugins/external-resource-gpu
ln -sf -- "/tmp/hadoop-alvinnguyen41/nm-local-dir/usercache/alvinnguyen41/appcache/application_1735734892238_0001/filecache/17/nvidia-gpu-discovery.sh" "plugins/external-resource-gpu/nvidia-gpu-discovery.sh"
ln -sf -- "/tmp/hadoop-alvinnguyen41/nm-local-dir/usercache/alvinnguyen41/appcache/application_1735734892238_0001/filecache/16/Alvin24-1.0-SNAPSHOT.jar" "Alvin24-1.0-SNAPSHOT.jar"
ln -sf -- "/tmp/hadoop-alvinnguyen41/nm-local-dir/usercache/alvinnguyen41/appcache/application_1735734892238_0001/filecache/10/application_1735734892238_0001-flink-conf.yaml2262773656697429789.tmp" "flink-conf.yaml"
mkdir -p plugins/external-resource-gpu
ln -sf -- "/tmp/hadoop-alvinnguyen41/nm-local-dir/usercache/alvinnguyen41/appcache/application_1735734892238_0001/filecache/22/gpu-discovery-common.sh" "plugins/external-resource-gpu/gpu-discovery-common.sh"
mkdir -p lib
ln -sf -- "/tmp/hadoop-alvinnguyen41/nm-local-dir/usercache/alvinnguyen41/appcache/application_1735734892238_0001/filecache/19/log4j-slf4j-impl-2.17.1.jar" "lib/log4j-slf4j-impl-2.17.1.jar"
mkdir -p plugins/metrics-statsd
ln -sf -- "/tmp/hadoop-alvinnguyen41/nm-local-dir/usercache/alvinnguyen41/appcache/application_1735734892238_0001/filecache/18/flink-metrics-statsd-1.14.5.jar" "plugins/metrics-statsd/flink-metrics-statsd-1.14.5.jar"
mkdir -p plugins/metrics-slf4j
ln -sf -- "/tmp/hadoop-alvinnguyen41/nm-local-dir/usercache/alvinnguyen41/appcache/application_1735734892238_0001/filecache/27/flink-metrics-slf4j-1.14.5.jar" "plugins/metrics-slf4j/flink-metrics-slf4j-1.14.5.jar"
mkdir -p lib
ln -sf -- "/tmp/hadoop-alvinnguyen41/nm-local-dir/usercache/alvinnguyen41/appcache/application_1735734892238_0001/filecache/30/flink-shaded-zookeeper-3.4.14.jar" "lib/flink-shaded-zookeeper-3.4.14.jar"
ln -sf -- "/tmp/hadoop-alvinnguyen41/nm-local-dir/usercache/alvinnguyen41/appcache/application_1735734892238_0001/filecache/29/log4j.properties" "log4j.properties"
mkdir -p plugins/metrics-influx
ln -sf -- "/tmp/hadoop-alvinnguyen41/nm-local-dir/usercache/alvinnguyen41/appcache/application_1735734892238_0001/filecache/33/flink-metrics-influxdb-1.14.5.jar" "plugins/metrics-influx/flink-metrics-influxdb-1.14.5.jar"
mkdir -p plugins/metrics-graphite
ln -sf -- "/tmp/hadoop-alvinnguyen41/nm-local-dir/usercache/alvinnguyen41/appcache/application_1735734892238_0001/filecache/20/flink-metrics-graphite-1.14.5.jar" "plugins/metrics-graphite/flink-metrics-graphite-1.14.5.jar"
mkdir -p lib
ln -sf -- "/tmp/hadoop-alvinnguyen41/nm-local-dir/usercache/alvinnguyen41/appcache/application_1735734892238_0001/filecache/11/log4j-api-2.17.1.jar" "lib/log4j-api-2.17.1.jar"
mkdir -p plugins/metrics-prometheus
ln -sf -- "/tmp/hadoop-alvinnguyen41/nm-local-dir/usercache/alvinnguyen41/appcache/application_1735734892238_0001/filecache/31/flink-metrics-prometheus-1.14.5.jar" "plugins/metrics-prometheus/flink-metrics-prometheus-1.14.5.jar"
echo "Copying debugging information"
# Creating copy of launch script
cp "launch_container.sh" "/home/alvinnguyen41/hadoop-3.4.1/logs/userlogs/application_1735734892238_0001/container_1735734892238_0001_01_000001/launch_container.sh"
chmod 640 "/home/alvinnguyen41/hadoop-3.4.1/logs/userlogs/application_1735734892238_0001/container_1735734892238_0001_01_000001/launch_container.sh"
# Determining directory contents
echo "ls -l:" 1>"/home/alvinnguyen41/hadoop-3.4.1/logs/userlogs/application_1735734892238_0001/container_1735734892238_0001_01_000001/directory.info"
ls -l 1>>"/home/alvinnguyen41/hadoop-3.4.1/logs/userlogs/application_1735734892238_0001/container_1735734892238_0001_01_000001/directory.info"
echo "find -L . -maxdepth 5 -ls:" 1>>"/home/alvinnguyen41/hadoop-3.4.1/logs/userlogs/application_1735734892238_0001/container_1735734892238_0001_01_000001/directory.info"
find -L . -maxdepth 5 -ls 1>>"/home/alvinnguyen41/hadoop-3.4.1/logs/userlogs/application_1735734892238_0001/container_1735734892238_0001_01_000001/directory.info"
echo "broken symlinks(find -L . -maxdepth 5 -type l -ls):" 1>>"/home/alvinnguyen41/hadoop-3.4.1/logs/userlogs/application_1735734892238_0001/container_1735734892238_0001_01_000001/directory.info"
find -L . -maxdepth 5 -type l -ls 1>>"/home/alvinnguyen41/hadoop-3.4.1/logs/userlogs/application_1735734892238_0001/container_1735734892238_0001_01_000001/directory.info"
echo "Launching container"
exec /bin/bash -c "$JAVA_HOME/bin/java -Xmx473956352 -Xms473956352 -XX:MaxMetaspaceSize=268435456 -Dlog.file="/home/alvinnguyen41/hadoop-3.4.1/logs/userlogs/application_1735734892238_0001/container_1735734892238_0001_01_000001/jobmanager.log" -Dlog4j.configuration=file:log4j.properties -Dlog4j.configurationFile=file:log4j.properties org.apache.flink.yarn.entrypoint.YarnJobClusterEntrypoint -D jobmanager.memory.off-heap.size=134217728b -D jobmanager.memory.jvm-overhead.min=201326592b -D jobmanager.memory.jvm-metaspace.size=268435456b -D jobmanager.memory.heap.size=473956352b -D jobmanager.memory.jvm-overhead.max=201326592b 1> /home/alvinnguyen41/hadoop-3.4.1/logs/userlogs/application_1735734892238_0001/container_1735734892238_0001_01_000001/jobmanager.out 2> /home/alvinnguyen41/hadoop-3.4.1/logs/userlogs/application_1735734892238_0001/container_1735734892238_0001_01_000001/jobmanager.err"
