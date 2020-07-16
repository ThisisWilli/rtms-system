package com.willi.flink;

import com.alibaba.fastjson.JSON;
import com.willi.Bean.Order;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.*;
import org.apache.flink.streaming.connectors.kafka.internals.KeyedSerializationSchemaWrapper;
import org.apache.flink.streaming.util.serialization.JSONKeyValueDeserializationSchema;
import org.apache.flink.streaming.util.serialization.KeyedDeserializationSchema;
import org.apache.flink.streaming.util.serialization.KeyedSerializationSchema;
import org.apache.flink.streaming.util.serialization.TypeInformationKeyValueSerializationSchema;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.KafkaProducer;

import java.util.Objects;
import java.util.Properties;

/**
 * @program: bigdataplatform
 * @description:
 * @author: Hoodie_Willi
 * @create: 2020-02-18 20:56
 **/

public class GetKafkaData {

    public static void main(String[] args) throws Exception {
        // 用户参数获取
        final ParameterTool parameterTool = ParameterTool.fromArgs(args);

        // 准备环境
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        // 开启检查点机制，并指定检查点之间的时间间隔
        env.enableCheckpointing(60000);
        // 检查点语义
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        // 设置执行checkpoint操作时的超时时间
        env.getCheckpointConfig().setCheckpointTimeout(100000);
        // 设置最大并发执行的检查点数量
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(100);
        // 将检查点持久化到外部存储
//        env.getCheckpointConfig().enableExternalizedCheckpoints();
        // 如果有更近的savepoint，是否将作业回退到该检查点
        env.getCheckpointConfig().setPreferCheckpointForRecovery(true);
        env.setStateBackend(new FsStateBackend("hdfs://"));
        System.out.println("start flink ETL...");

        // 配置consumer的配置信息
        Properties consumerProps = new Properties();
        consumerProps.setProperty("bootstrap.servers", "localhost:9092");
        // only required for Kafka 0.8
        consumerProps.setProperty("zookeeper.connect", "localhost:2182");
        consumerProps.setProperty("group.id", "test");
        // key的反序列化方式
        consumerProps.put("key.deserializer", "org.apache.kafka.common.serialization.IntegerDeserializer");
        // value的反序列化方式
        consumerProps.put("value.deserializer", "com.willi.utils.OrderDeserializer");
//        FlinkKafkaConsumer consumer = new FlinkKafkaConsumer<>("raw_{*}", new SimpleStringSchema(), consumerProps);



        FlinkKafkaConsumer<ConsumerRecord<String, String>> consumer = new FlinkKafkaConsumer<ConsumerRecord<String, String>>(
                java.util.regex.Pattern.compile("raw_[0-9]"),
                new MyKafkaDeserializationSchema(),
                consumerProps
        );
        // 获取raw_order中的订单原始数据，并对数据按照key值进行分流
        DataStream<ConsumerRecord<String, String>> source = env.addSource(consumer);
        // 过滤掉非空数据
        source.filter(Objects::nonNull)
                .keyBy(ConsumerRecord::key)
                .print();

        // execute program
        env.execute("Flink ETL");
    }
}
