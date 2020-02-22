package com.willi.kafka;


import com.alibaba.fastjson.JSON;
import com.willi.service.WebSocketServer;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @program: bigdataplatform
 * @description:
 * @author: Hoodie_Willi
 * @create: 2020-02-15 15:25
 **/
@Component
public class Consumer {

    @KafkaListener(topics = {"list_neigh"}, groupId = "test")
    public void listenNeigh(String msg) throws InterruptedException {
        String neigh = JSON.toJSONString(msg);
        System.out.println("接收到了区域信息     " + neigh);
        WebSocketServer.sendInfo(neigh);
    }

    @KafkaListener(topics = {"list_price"}, groupId = "test")
    public void listenPrice(String msg) throws InterruptedException {
        String price = JSON.toJSONString(msg);
        System.out.println("接收到了价格信息     " + msg);
//        WebSocketServer.sendInfo(price);
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
