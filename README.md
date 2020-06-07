# cloud-ali

## 1. 项目目标

```txt
简化搭建框架，希望任何配置或修改任何组件稍微改下配置文件就可以了。
```



## 2. 项目组件简介

```txt
1. 分布式框架（Spring_cloud_alibaba）
  1.1. 本项目只搭建一个分布式框架不涉及任何业务
  1.2. 如果又什么不好的地方请指教
  1.3. 提交意见的时候最好带有文档信息，注明为什么以及修改建议。方便他人查看
  1.4. 提交意见建议请放在本目录 README 文件夹，方便他人查看图文并茂最好，文件名最好见名知意。
  
2. 组件介绍
  2.1. 配置，注册中心采用 Nacos
  2.2. 网关采用 Spring_Cloud_Gateway
  2.3. 服务熔断限流采用 Sentinel
  2.4. 链路追踪采用 Sleuth
  2.5. 消息驱动采用 Spring_cloud_Stream
  2.6. 分布式事务采用 Seata
  2.7. 分库分表采用 ShardingSphere
  2.8. 分布式锁采用 Redisson
```



## 3. 阶段目标

```txt
1. 本阶段先构建网关 Spring_Cloud_Gateway 并出好文档 ------ 完成
2. 整和了Seata分布式事务  ------  完成
3. 整和了MybatisPlus ------  完成
4. 整合了分布式锁Redisson ------  完成
5. 整合了ShardingSphere ------  完成
```



## 4. 项目启动

```txt
详细步骤查看 README 文件夹下( 初始化项目.docx )

1. 先克隆下载项目
git clone https://github.com/LateAutumnWindow/cloud-ali.git

2. Nacos 配置，注册中心搭建
详情见根目录 /README/初始化项目.md

3. Seata 的 Server 端安装
详情见根目录 /README/初始化项目.md

4. 创建数据库
详情见根目录 /README/初始化项目.md

5. 测试： 1001--用户ID / E101--货物CODE / 0--购买数量
localhost:1001/create/order/1001/E101/0
//在service方法模拟了报错
int i = 10 / orderCount;

```

