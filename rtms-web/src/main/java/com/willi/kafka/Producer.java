package com.willi.kafka;

//import com.willi.service.WebSocketServer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

/**
 * @program: bigdataplatform
 * @description:
 * @author: Hoodie_Willi
 * @create: 2020-02-15 15:22
 **/

public class Producer {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 0);
        props.put("buffer.memory", 33554432);
        // key的序列化方式
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // value的序列化方式
        props.put("value.serializer", "org.apache.kafka.common.seriization.StringSerializer");
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(props);
        System.out.println("start send message....");
        // 开始发送消息
        for (int i = 0; i < 10; i++){
            String key = "key = " + i;
            String value = "value = " + i;
            ProducerRecord<String, String> record = new ProducerRecord<String, String>("topic20200213", key, value);
            try{
                producer.send(record);
//                WebSocketServer.sendInfo("kafka正在发送消息");
                Thread.sleep(2000);
                System.out.println("发送完毕");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
