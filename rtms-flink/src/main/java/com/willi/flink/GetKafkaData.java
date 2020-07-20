package com.willi.flink;

import com.alibaba.fastjson.JSON;
import com.willi.Bean.Order;
import com.willi.Bean.WarnMessage;
import com.willi.sink.WarnMySQLSink;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.connectors.kafka.*;
import org.apache.flink.util.Collector;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.KafkaProducer;

import java.time.Duration;
import java.util.Iterator;
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
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);


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



        FlinkKafkaConsumer<ConsumerRecord<String, String>> warnConsumer = new FlinkKafkaConsumer<ConsumerRecord<String, String>>(
                java.util.regex.Pattern.compile("raw_warn"),
                new MyKafkaDeserializationSchema(),
                consumerProps
        );

        FlinkKafkaConsumer<ConsumerRecord<String, String>> qualityConsumer = new FlinkKafkaConsumer<ConsumerRecord<String, String>>(
                java.util.regex.Pattern.compile("raw_quality_.*"),
                new MyKafkaDeserializationSchema(),
                consumerProps
        );

        // 获取raw_order中的订单原始数据，并对数据按照key值进行分流
        DataStream<ConsumerRecord<String, String>> warnSource = env.addSource(warnConsumer);
//        DataStreamSource<ConsumerRecord<String, String>> qualitySource = env.addSource(qualityConsumer);


        // 别的数据存储到mysql
//        warnSource.map(data->JSON.parseObject(data.value(), WarnMessage.class))
//                .addSink(new WarnMySQLSink());

        // 利用窗口统计30s内所有工位发生的警报信息，允许10s的迟到信息
        warnSource.map(data->JSON.parseObject(data.value(), WarnMessage.class))
                .returns(WarnMessage.class)
                .assignTimestampsAndWatermarks(WatermarkStrategy.<WarnMessage>forBoundedOutOfOrderness(Duration.ofSeconds(1))
                .withTimestampAssigner(new SerializableTimestampAssigner<WarnMessage>() {
                    @Override
                    public long extractTimestamp(WarnMessage warnMessage, long l) {
                        return warnMessage.getTimestamp();
                    }
                })
                ).map(data->new Tuple2<String, Integer>(data.getProductionLineName(), 1))
                .returns(Types.TUPLE(Types.STRING, Types.INT))
                .keyBy(data->data.f0)
                .timeWindow(Time.seconds(1))
                .reduce(new ReduceFunction<Tuple2<String, Integer>>() {
                    @Override
                    public Tuple2<String, Integer> reduce(Tuple2<String, Integer> t1, Tuple2<String, Integer> t2) throws Exception {
                        return new Tuple2<String, Integer>(t1.f0, t1.f1 + t2.f1);
                    }
                }, new ProcessWindowFunction<Tuple2<String, Integer>, String, String, TimeWindow>() {
                    @Override
                    public void process(String s, Context context, Iterable<Tuple2<String, Integer>> elements, Collector<String> collector) throws Exception {
                        System.out.println("=============================");
                        System.out.println("window: [" + context.window().getStart() + "->" + context.window().getEnd() + "]");
                        System.out.println("窗口中的数据为:");
                        Iterator<Tuple2<String, Integer>> iterator = elements.iterator();
                        while (iterator.hasNext()){
                            System.out.println(iterator.next());
                        }
                        System.out.println("============================");
                    }
                }).print();

        // execute program
        env.execute("Flink ETL");
    }
}


