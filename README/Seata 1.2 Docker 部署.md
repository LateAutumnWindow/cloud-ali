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
```

### 1.1 SeataClient端

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

