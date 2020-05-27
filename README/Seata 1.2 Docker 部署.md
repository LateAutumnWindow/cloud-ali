版本

```xml
版本参考地址：https://github.com/alibaba/spring-cloud-alibaba/wiki/版本说明
<dependencyManagement>
     <dependencies>
         <dependency>
             <groupId>org.springframework.boot</groupId>
             <artifactId>spring-boot-dependencies</artifactId>
             <version>2.2.5.RELEASE</version>
             <type>pom</type>
             <scope>import</scope>
         </dependency>
         <dependency>
             <groupId>org.springframework.cloud</groupId>
             <artifactId>spring-cloud-dependencies</artifactId>
             <version>Hoxton.SR3</version>
             <type>pom</type>
             <scope>import</scope>
         </dependency>
         <dependency>
             <groupId>com.alibaba.cloud</groupId>
             <artifactId>spring-cloud-alibaba-dependencies</artifactId>
             <version>2.2.1.RELEASE</version>
             <type>pom</type>
             <scope>import</scope>
         </dependency>
     </dependencies>
</dependencyManagement>


<dependency>
	<groupId>io.seata</groupId>
	<artifactId>seata-spring-boot-starter</artifactId>
	<version>1.2.0</version>
</dependency>
```



## 1. Seata 安装

```txt
1. Docker 安装：
docker pull seataio/seata-server:1.2.0

2. 创建 Seata TC 端 MySQL 数据库执行以下数据库建表语句
	https://github.com/seata/seata/blob/1.1.0/script/server/db/mysql.sql
	
3. 创建 Clinent 段 UNDOLOG 表执行以下数据库建表语句
	// 每个服务的数据库都要创建，一个库的话创建一次就可以
	https://github.com/seata/seata/blob/1.1.0/script/client/at/db/mysql.sql

4. 启动镜像
docker run --name seata-server -e SEATA_PORT=8091 -e SEATA_IP=192.168.1.180 -p 8091:8091 -d  镜像ID

	解析：
	docker run 
		// 容器名
		--name seata-server
		-e SEATA_PORT=8091
		-e SEATA_IP=192.168.1.180
		// 宿主机与容器端口映射
		-p 8091:8091 
		-d 镜像ID

或者可以自定义配置启动需要自己创建配置文件做为容器卷
docker run --name seata-server \
        -p 8091:8091 \
        -e SEATA_CONFIG_NAME=file:/root/seata-config/registry \
        -v /PATH/TO/CONFIG_FILE:/root/seata-config  \
        seataio/seata-server

		
		
		
5. 进入容器修改配置 /resources/file.conf, registry.conf 文件
	docker exec -it 容器ID sh
	
	cd /resources/
	
	vi file.conf
	store {
  		mode = "db"
  		db {
            datasource = "druid"
            dbType = "mysql"
            driverClassName = "com.mysql.jdbc.Driver"
            url = "jdbc:mysql://192.168.1.180:3306/seata"
            user = "root"
            password = "123456"
            minConn = 5
            maxConn = 30
            globalTable = "global_table"
            branchTable = "branch_table"
            lockTable = "lock_table"
            queryLimit = 100
            maxWait = 5000
         }
	}
	-------------------------------------------------------
	vi registry.conf
	
    registry {
      type = "nacos"
      nacos {
        application = "seata-server"
        serverAddr = "192.168.1.180:8848"
        namespace = ""
        cluster = "default"
        username = "nacos"
        password = "nacos"
      }
    }

    config {
      type = "nacos"
      nacos {
        serverAddr = "192.168.1.180:8848"
        namespace = ""
        group = "SEATA_GROUP"
        username = "nacos"
        password = "nacos"
      }
    }
    
   创建 config.txt
   
   创建 nacos-config.sh
   
   执行 nacos-config.sh
   
   启动脚本 sh ./nacos-config.sh -h 192.168.1.180 -p 8848 -g SEATA_GROUP -u nacos -w nacos
    
```

## 1.1nacos-config.sh

```txt
#!/usr/bin/env bash
# Copyright 1999-2019 Seata.io Group.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at、
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

while getopts ":h:p:g:t:u:w:" opt
do
  case $opt in
  h)
    host=$OPTARG
    ;;
  p)
    port=$OPTARG
    ;;
  g)
    group=$OPTARG
    ;;
  t)
    tenant=$OPTARG
    ;;
  u)
    username=$OPTARG
    ;;
  w)
    password=$OPTARG
    ;;
  ?)
    echo " USAGE OPTION: $0 [-h host] [-p port] [-g group] [-t tenant] [-u username] [-w password] "
    exit 1
    ;;
  esac
done

if [[ -z ${host} ]]; then
    host=localhost
fi
if [[ -z ${port} ]]; then
    port=8848
fi
if [[ -z ${group} ]]; then
    group="SEATA_GROUP"
fi
if [[ -z ${tenant} ]]; then
    tenant=""
fi
if [[ -z ${username} ]]; then
    username=""
fi
if [[ -z ${password} ]]; then
    password=""
fi

nacosAddr=$host:$port
contentType="content-type:application/json;charset=UTF-8"

echo "set nacosAddr=$nacosAddr"
echo "set group=$group"

failCount=0
tempLog=$(mktemp -u)
function addConfig() {
  curl -X POST -H "${contentType}" "http://$nacosAddr/nacos/v1/cs/configs?dataId=$1&group=$group&content=$2&tenant=$tenant&username=$username&password=$password" >"${tempLog}" 2>/dev/null
  if [[ -z $(cat "${tempLog}") ]]; then
    echo " Please check the cluster status. "
    exit 1
  fi
  if [[ $(cat "${tempLog}") =~ "true" ]]; then
    echo "Set $1=$2 successfully "
  else
    echo "Set $1=$2 failure "
    (( failCount++ ))
  fi
}

count=0
for line in $(cat $(dirname "$PWD")/config.txt | sed s/[[:space:]]//g); do
  (( count++ ))
	key=${line%%=*}
    value=${line#*=}
	addConfig "${key}" "${value}"
done

echo "========================================================================="
echo " Complete initialization parameters,  total-count:$count ,  failure-count:$failCount "
echo "========================================================================="

if [[ ${failCount} -eq 0 ]]; then
	echo " Init nacos config finished, please start seata-server. "
else
	echo " init nacos config fail. "
fi
```



## 1.2config.txt

```txt
transport.type=TCP
transport.server=NIO
transport.heartbeat=true
transport.enableClientBatchSendRequest=false
transport.threadFactory.bossThreadPrefix=NettyBoss
transport.threadFactory.workerThreadPrefix=NettyServerNIOWorker
transport.threadFactory.serverExecutorThreadPrefix=NettyServerBizHandler
transport.threadFactory.shareBossWorker=false
transport.threadFactory.clientSelectorThreadPrefix=NettyClientSelector
transport.threadFactory.clientSelectorThreadSize=1
transport.threadFactory.clientWorkerThreadPrefix=NettyClientWorkerThread
transport.threadFactory.bossThreadSize=1
transport.threadFactory.workerThreadSize=default
transport.shutdown.wait=3
service.vgroupMapping.my_test_tx_group=default
service.default.grouplist=127.0.0.1:8091
service.enableDegrade=false
service.disableGlobalTransaction=false
client.rm.asyncCommitBufferLimit=10000
client.rm.lockRetryInternal=10
client.rm.lockRetryTimes=30
client.rm.reportRetryCount=5
client.rm.lockRetryPolicyBranchRollbackOnConflict=true
client.rm.tableMetaCheckEnable=false
client.rm.sqlParserType=druid
client.rm.reportSuccessEnable=false
client.tm.commitRetryCount=5
client.tm.rollbackRetryCount=5
store.mode=db
store.file.dir=file_store/data
store.file.maxBranchSessionSize=16384
store.file.maxGlobalSessionSize=512
store.file.fileWriteBufferCacheSize=16384
store.file.flushDiskMode=async
store.file.sessionReloadReadSize=100
store.db.datasource=dbcp
store.db.dbType=mysql
store.db.driverClassName=com.mysql.jdbc.Driver
store.db.url=jdbc:mysql://192.168.1.180:3306/seata?useUnicode=true   # 改这里
store.db.user=root													 # 改这里
store.db.password=123456											 # 改这里
store.db.minConn=1
store.db.maxConn=3
store.db.globalTable=global_table
store.db.branchTable=branch_table
store.db.queryLimit=100
store.db.lockTable=lock_table
server.recovery.committingRetryPeriod=1000
server.recovery.asynCommittingRetryPeriod=1000
server.recovery.rollbackingRetryPeriod=1000
server.recovery.timeoutRetryPeriod=1000
server.maxCommitRetryTimeout=-1
server.maxRollbackRetryTimeout=-1
server.rollbackRetryTimeoutUnlockEnable=false
client.undo.dataValidation=true
client.undo.logSerialization=jackson
server.undo.logSaveDays=7
server.undo.logDeletePeriod=86400000
client.undo.logTable=undo_log
client.log.exceptionRate=100
transport.serialization=seata
transport.compressor=none
metrics.enabled=false
metrics.registryType=compact
metrics.exporterList=prometheus
metrics.exporterPrometheusPort=9898
```



## 1.3 SeataClient端

```txt
// 客户端依赖
<dependency>
   <groupId>io.seata</groupId>
   <artifactId>seata-spring-boot-starter</artifactId>
   <version>1.2.0</version>
</dependency>


// 客户端配置文件
seata:
  enabled: true
  application-id: applicationName
  tx-service-group: my_test_tx_group
  enable-auto-data-source-proxy: true
  use-jdk-proxy: false
  excludes-for-auto-proxying: firstClassNameForExclude,secondClassNameForExclude
  client:
    rm:
      async-commit-buffer-limit: 1000
      report-retry-count: 5
      table-meta-check-enable: false
      report-success-enable: false
      saga-branch-register-enable: false
      lock:
        retry-interval: 10
        retry-times: 30
        retry-policy-branch-rollback-on-conflict: true
    tm:
      commit-retry-count: 5
      rollback-retry-count: 5
    undo:
      data-validation: true
      log-serialization: jackson
      log-table: undo_log
    log:
      exceptionRate: 100
  service:
    vgroup-mapping:
      my_test_tx_group: default
    grouplist:
      default: 127.0.0.1:8091
    enable-degrade: false
    disable-global-transaction: false
  transport:
    shutdown:
      wait: 3
    thread-factory:
      boss-thread-prefix: NettyBoss
      worker-thread-prefix: NettyServerNIOWorker
      server-executor-thread-prefix: NettyServerBizHandler
      share-boss-worker: false
      client-selector-thread-prefix: NettyClientSelector
      client-selector-thread-size: 1
      client-worker-thread-prefix: NettyClientWorkerThread
      worker-thread-size: default
      boss-thread-size: 1
    type: TCP
    server: NIO
    heartbeat: true
    serialization: seata
    compressor: none
    enable-client-batch-send-request: true
  config:
    type: file
    nacos:
      namespace:
      serverAddr: 192.168.1.180:8848
      group: SEATA_GROUP
      userName: "nacos"
      password: "nacos"
  registry:
    type: nacos
    nacos:
      application: seata-server
      server-addr: 192.168.1.180:8848
      namespace:
      userName: "nacos"
      password: "nacos"
```

