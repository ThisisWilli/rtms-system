package com.willi.flink;

import com.alibaba.fastjson.JSON;
import com.willi.Bean.Order;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer011;
import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema;
import org.apache.flink.streaming.connectors.kafka.internals.KeyedSerializationSchemaWrapper;
import org.apache.flink.streaming.util.serialization.KeyedSerializationSchema;
import org.apache.kafka.clients.producer.KafkaProducer;

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

        // set up the batch execution environment
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
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
        FlinkKafkaConsumer consumer = new FlinkKafkaConsumer<>("raw_order", new SimpleStringSchema(), consumerProps);

        // 获取raw_order中的订单原始数据
        DataStream<String> source = env.addSource(consumer);

        // 配置kafka生产者
//        Properties producerPros = new Properties();
//        producerPros.setProperty("bootstrap.servers", "localhost:9092");
//        producerPros.setProperty("zookeeper.connect", "localhost:2182");

        // 将订单中的区域数据发送到kafka中的list_neigh中
        source.map((line) -> {
            // 将每一行数据解析成map格式的数据，再将不同的数据写入不同的kafkatopic
            Order order = JSON.parseObject(line, Order.class);
            Integer id = order.getId();
            String area = order.getNeighbourhood();
            area = area.split("/")[0];
            return id + ":" + area;
        }).addSink(new FlinkKafkaProducer<String>(
                "localhost:9092",
                "list_neigh",
                new SimpleStringSchema())
        );

        source.map((line)->{
            Order order = JSON.parseObject(line, Order.class);
            Integer id = order.getId();
            Double price = order.getPrice();
            return id + ":" + price;
        }).addSink(new FlinkKafkaProducer<String>(
                "localhost:9092",
                "list_price",
                new SimpleStringSchema()
        ));
        // execute program
        env.execute("Flink ETL");
    }
}
