package com.willi.flink;

import com.alibaba.fastjson.JSON;
import com.willi.Bean.Order;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;
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
import scala.Tuple8;

import java.util.Objects;
import java.util.Properties;

/**
 * @program: bigdataplatform
 * @description:
 * @author: Hoodie_Willi
 * @create: 2020-02-18 20:56
 * TODO:1.窗口
 *      2.sideoutput分流
 *      3.mysql sink
 **/

public class GetKafkaData {

    public static void main(String[] args) throws Exception {
        // 用户参数获取
        final ParameterTool parameterTool = ParameterTool.fromArgs(args);

        // 准备环境
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();


        /*=====================开启checkpoint========================*/
//        // 开启检查点机制，并指定检查点之间的时间间隔
//        env.enableCheckpointing(60000);
//        // 检查点语义
//        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
//        // 设置执行checkpoint操作时的超时时间
//        env.getCheckpointConfig().setCheckpointTimeout(100000);
//        // 设置最大并发执行的检查点数量
//        env.getCheckpointConfig().setMaxConcurrentCheckpoints(100);
//        // 将检查点持久化到外部存储
////        env.getCheckpointConfig().enableExternalizedCheckpoints();
//        // 如果有更近的savepoint，是否将作业回退到该检查点
//        env.getCheckpointConfig().setPreferCheckpointForRecovery(true);
//        env.setStateBackend(new FsStateBackend("hdfs://"));
        /*=====================checkpoint========================*/


        System.out.println("start flink ETL...");

        /*================配置kafka消费者信息=======================*/
        // 配置consumer的配置信息
        Properties consumerProps = new Properties();
        consumerProps.setProperty("bootstrap.servers", "node01:9092, node02:9092,node03:9092");
        // only required for Kafka 0.8
        consumerProps.setProperty("group.id", "flink_etl");
        consumerProps.put("auto.offset.reset", "earliest");
        // key的反序列化方式
        consumerProps.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        // value的反序列化方式
        consumerProps.put("value.deserializer", "com.willi.utils.JSONDeserializer");



        FlinkKafkaConsumer<ConsumerRecord<String, String>> consumer = new FlinkKafkaConsumer<ConsumerRecord<String, String>>(
                java.util.regex.Pattern.compile("raw_.*"),
                new MyKafkaDeserializationSchema(),
                consumerProps
        );

        FlinkKafkaConsumer<ConsumerRecord<String, String>> consumer2 = new FlinkKafkaConsumer<ConsumerRecord<String, String>>(
                java.util.regex.Pattern.compile("raw_.*"),
                new MyKafkaDeserializationSchema(),
                consumerProps
        );

        // 获取raw_order中的订单原始数据，并对数据按照key值进行分流
        DataStream<ConsumerRecord<String, String>> source = env.addSource(consumer);
        DataStreamSource<ConsumerRecord<String, String>> source2 = env.addSource(consumer2);
        source.filter(Objects::nonNull)
                .keyBy(ConsumerRecord::key)
                .map(ConsumerRecord::value);


        // execute program
        env.execute("Flink ETL");
    }
}
