package com.willi.kafka;

import com.alibaba.fastjson.JSON;
import com.csvreader.CsvReader;
import com.willi.Bean.Order;
import com.willi.Bean.QualityMessage;
import com.willi.Bean.WarnMessage;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.ProducerFencedException;

import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @program: bigdataplatform
 * @description: kafkaProducer向topic发送数据，供flinkETL
 * @author: Hoodie_Willi
 * @create: 2020-02-19 19:30
 **/

public class SendData {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "node02:9092,node03:9092,node04:9092");
        props.put("acks", "all");
        // producer发生错误，重新发送消息的概率
        props.put("retries", 3);
        // 当有多个消息需要被发送到同一个分区时，producer会把它们放在同一个批次中，batch满了之后，消息会被发送
        props.put("batch.size", 16384);
        // producer在批次填满或者linger.ms达到上限之后把批次发送出去
        props.put("linger.ms", 0);
        // producer内存缓冲的大小，生产者缓存要发送到服务器的信息，避免应用程序发送消息的速度超过生产者的发送速度导致抛出异常
        props.put("buffer.memory", 204800);
        // 每台机器唯一的事务id
        props.put("transactional.id", "producer-123");
        // 设置幂等性
        props.put("enable.idempotence", true);
        // key的序列化方式
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // value的序列化方式
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        System.out.println("start send message....");
        producer.initTransactions();
        // 开始发送消息
        try {
            // 用来保存数据
            // 定义一个CSV路径
            String csvFilePath = "D:\\IdeaProject\\GenerateData\\data\\data.csv";
//             创建CSV读对象 例如:CsvReader(文件路径，分隔符，编码格式);
            CsvReader reader = new CsvReader(csvFilePath, ',', StandardCharsets.UTF_8);
            // 跳过表头 如果需要表头的话，这句可以忽略
//            reader.readHeaders();
            // 逐行读入除表头的数据
            while (reader.readRecord()) {
                // 生成[0,10]区间的整数，每隔sleepTimes内发送一条消息
                int sleepTime = new Random().nextInt(5);
                String rowData = reader.getRawRecord();
                System.out.println(rowData);
                String[] splitData = rowData.split("\t");
                // 过滤描述中也有逗号的情况，直接过掉这条数据，后期进行优化

                WarnMessage warnMessage = null;
                QualityMessage qualityMessage = null;
                if (splitData.length > 2 && "warn".equals(splitData[1])){
                    warnMessage = new WarnMessage(
                            // 事件id
                            splitData[0].trim(),
                            // 事件类型
                            splitData[1].trim(),
                            // timestamp
                            Long.parseLong(splitData[2].trim()),
                            // 生产线名称
                            splitData[3].trim(),
                            // 生产的设备
                            splitData[4].trim(),
                            // 报警信息
                            splitData[5].trim(),
                            // 是否已经报警
                            splitData[6].trim(),
                            // 事件发生的时长
                            splitData[7].trim()
                    );
                    String rawData = JSON.toJSONString(warnMessage);


                    try {

                        producer.beginTransaction();
                        // 发送到相应工位的topic，每个topic中包含设备报警信息，产品质量信息，设备工作事件三种信息，key为每种消息的类型
                        producer.send(new ProducerRecord<String, String>("raw_warn", warnMessage.getType(), rawData));
//                        producer.send(new ProducerRecord<String, String>("raw_warn", "warn", "77777"));
                        producer.commitTransaction();
                    } catch (ProducerFencedException e) {
                        e.printStackTrace();
                        producer.abortTransaction();
                    }
                }else if (splitData.length > 2 && "quality".equals(splitData[1])){
                    qualityMessage = new QualityMessage(
                            // 事件id
                            splitData[0].trim(),
                            // 事件类型
                            splitData[1].trim(),
                            // timestamp
                            Long.parseLong(splitData[2].trim()),
                            // 生产线名称
                            splitData[3].trim(),
                            // 生产的设备
                            splitData[4].trim(),
                            // 哪个门板
                            splitData[5].trim(),
                            // 角度信息
                            Double.parseDouble(splitData[6].trim())
                    );

                    String rawData = JSON.toJSONString(qualityMessage);
                    try {

                        producer.beginTransaction();
                        // 发送到相应工位的topic，每个topic中包含设备报警信息，产品质量信息，设备工作事件三种信息，key为每种消息的类型
                        producer.send(new ProducerRecord<String, String>("raw_quality", qualityMessage.getType(), rawData));
//                        producer.send(new ProducerRecord<String, String>("raw_warn", "warn", "87"));
                        producer.commitTransaction();
                    } catch (ProducerFencedException e) {
                        e.printStackTrace();
                        producer.abortTransaction();
                    }
                }

                TimeUnit.SECONDS.sleep(3);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
