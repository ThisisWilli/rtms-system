# rtms-system
一款基于springboot，集成flink，kafka的实时数据监控系统，最终通过e-charts绘制图表在web端显示。

## 你需要拥有(安装)

* Kafka0.1或者更高版本
* flink1.9或更改版本，集群或者单机都可以
* springboot2.1.1或更改版本
* zookeeper3.4.14或更高版本

## 各模块简介

以后在博客中更新

## 数据

数据选用的是开源的airbub的订单数据

## 项目运行

* 首先开启zookeeper

  ```
   ~ zkServer.sh start
  ```

* 启动kafka

  ```
   ./kafka-server-start.sh -daemon ../config/server.properties 
  ```

* 启动flink

  ```
  bin ./start-cluster.sh 
  ```

* 切换到rtms-flink模块，并用maven编译rtms-flink模块

  ```
   cd rtms-flink 
   mvn clean package
  ```

* 运行打包出来的jar包

  ```
  flink run -p 1 -c com.willi.GetKafkaData ./target/rtms-flink-0.0.1.jar
  ```

* 运行rtms-flink/src/main/java/com/willi/kafka/SendOrderData.java，发送数据

## 运行结果
![](https://github.com/ThisisWilli/rtms-system/blob/master/%E6%95%88%E6%9E%9C%E5%9B%BE.gif)
