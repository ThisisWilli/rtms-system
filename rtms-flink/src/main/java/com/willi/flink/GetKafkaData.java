package com.willi.flink;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;

import java.util.Properties;

/**
 * @program: bigdataplatform
 * @description:
 * @author: Hoodie_Willi
 * @create: 2020-02-18 20:56
 **/

public class GetKafkaData {
    public static void main(String[] args) throws Exception {
        // set up the batch execution environment
//		StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
//		final StreamExecutionEnvironment env = StreamExecutionEnvironment
//				.createRemoteEnvironment("localhost", 8081, "/Users/williwei/flinkStudy/out/artifacts/flinkStudy_jar/flinkStudy.jar");
//		DataSource<String> source = env.readTextFile("data/data1");
//		source.map(line->line+"=======").print();
        System.out.println("start job...");
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "localhost:9092");
// only required for Kafka 0.8
        properties.setProperty("zookeeper.connect", "localhost:2182");
        properties.setProperty("group.id", "test");
        properties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        // value的序列化方式
        properties.put("value.deserializer", "streaming.kafka.bean.PersonDeserializer");
        FlinkKafkaConsumer<String> consumer = new FlinkKafkaConsumer<>("orgin", new SimpleStringSchema(), properties);
        DataStreamSource<String> source = env.addSource(consumer);
        SingleOutputStreamOperator<String> map = source.map(line -> {
            return line;
        });
        map.writeAsText("/Users/williwei/IdeaProjects/BigData-Platform/rtms-flink/data/result");
        // execute program
        env.execute("Flink Batch Java API Skeleton");
    }
}
