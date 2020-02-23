package com.willi.kafka;


import com.alibaba.fastjson.JSON;
import com.willi.bean.Neigh;
import com.willi.bean.Price;
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
        String[] splitData = msg.split(":");
        Neigh neigh = null;
//        String neigh = JSON.toJSONString(msg);
//        System.out.println("接收到了区域信息     " + neigh);
        if (splitData[0] != null && splitData[1] != null){
            neigh = new Neigh(Integer.parseInt(splitData[0]), splitData[1]);
        }

        synchronized (Consumer.class) {
            String neighData = JSON.toJSONString(neigh);
            WebSocketServer.sendInfo(neighData);
        }
    }

    @KafkaListener(topics = {"list_price"}, groupId = "test")
    public void listenPrice(String msg) throws InterruptedException {
        String[] splitData = msg.split(":");
        Price price = null;
        if (splitData[0] != null && splitData[1] != null){
            price = new Price(Integer.parseInt(splitData[0]), Double.valueOf(splitData[1]));
        }
        synchronized (Consumer.class){
            String priceData = JSON.toJSONString(price);
            WebSocketServer.sendInfo(priceData);
        }
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
