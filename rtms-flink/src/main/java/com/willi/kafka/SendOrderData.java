package com.willi.kafka;

import com.alibaba.fastjson.JSON;
import com.csvreader.CsvReader;
import com.willi.Bean.Order;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.Random;

/**
 * @program: bigdataplatform
 * @description: kafkaProducer向topic发送数据，供flinkETL
 * @author: Hoodie_Willi
 * @create: 2020-02-19 19:30
 **/

public class SendOrderData {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 0);
        props.put("buffer.memory", 33554432);
        // key的序列化方式
        props.put("key.serializer", "org.apache.kafka.common.serialization.IntegerSerializer");
        // value的序列化方式
        props.put("value.serializer", "com.willi.utils.OrderSerializer");
        KafkaProducer<Integer, Order> producer = new KafkaProducer<>(props);
        System.out.println("start send message....");
        // 开始发送消息
        try {
            // 用来保存数据
            // 定义一个CSV路径
            String csvFilePath = "/Users/williwei/Desktop/data_format/listings.csv";
//            Source.fromFile(csvFilePath)
//             创建CSV读对象 例如:CsvReader(文件路径，分隔符，编码格式);
            CsvReader reader = new CsvReader(csvFilePath, ',', StandardCharsets.UTF_8);
            // 跳过表头 如果需要表头的话，这句可以忽略
            reader.readHeaders();
            // 逐行读入除表头的数据
            while (reader.readRecord()) {
//                System.out.println(reader.getRawRecord());
                // 生成[0,10]区间的整数，每隔sleepTimes内发送一条消息
                int sleepTime = new Random().nextInt(5);
                String rowData = reader.getRawRecord();
//                System.out.println(rowData);
                String[] splitData = rowData.split(",");
                // 过滤描述中也有逗号的情况，直接过掉这条数据，后期进行优化
                if (splitData.length != 16){
                    continue;
                }
                Order order = new Order(
                    Integer.valueOf(splitData[0]),
                    splitData[1],
                    Integer.valueOf(splitData[2]),
                    splitData[3],
                    splitData[4],
                    splitData[5],
                    Double.valueOf(splitData[6]),
                    Double.valueOf(splitData[7]),
                    splitData[8],
                    Double.valueOf(splitData[9]),
                    Integer.valueOf(splitData[10]),
                    Integer.valueOf(splitData[11]),
                    splitData[12],
                    Double.valueOf(splitData[13]),
                    Integer.valueOf(splitData[14]),
                    Integer.valueOf(splitData[15])
                );
                String rawData = JSON.toJSONString(order);
                System.out.println(rawData);
////                System.out.println(data);
                producer.send(new ProducerRecord<Integer, Order>("raw_order", order.getId(), order));
                Thread.sleep(3 * 1000);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
