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

采用子生成的监控数据

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


## 实现的需求
### 统计单位时间内，生产线上每种设备的报警次数，并按降序排序
 具体实现见com/willi/flink/RealTimeWarnAnalyse.java
实现思路：
 1. 首先将源数据流按照设备的名称用keyBy算子分成不同的stream，并提取出数据中的时间戳
 2. 定义一个5s的时间窗口，自定义一个aggregate函数，使用累加器统计每个设备在5s中只能的报警次数，在aggregate算子中定义windowFunction的
 匿名内部类，提取出每个窗口的具体信息
 3. 将数据按照窗口的结束时间进行keyBy分类，并在process方法中使用状态编程，记录先前到达的数据
 4. 存储每个相同结束时间窗口的数据，并在所有数据都到达之后，触发计算

