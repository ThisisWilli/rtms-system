package com.willi.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

/**
 * @program: bigdataplatform
 * @description:
 * @author: Hoodie_Willi
 * @create: 2020-02-15 15:25
 **/
@Component
public class Consumer {

    @KafkaListener(topics = {"topic20200213"}, groupId = "test")
    public void listen(String msg) throws InterruptedException {
        System.out.println("接收到了消息" + msg.toString() + new Date());
        Thread.sleep(5000);
    }
//    public static void main(String[] args) {
//        Properties props = new Properties();
//        props.put("bootstrap.servers", "localhost:9092");
//        props.put("group.id", "group01");
//        props.put("enable.auto.commit", "false");
//        props.put("auto.offset.reset", "earliest");
//        props.put("auto.commit.interval.ms", "1000");
//        // key的序列化方式
//        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//        // value的序列化方式
//        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
//        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
//        consumer.subscribe(Arrays.asList("topic20200212"));
//        try {
//            while(true){
//                ConsumerRecords<String, String> records = consumer.poll(5000);
//                System.out.println("接收到的信息总数为：" + records.count());
//                for (ConsumerRecord<String, String> record : records) {
//                    System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value().toString());
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
