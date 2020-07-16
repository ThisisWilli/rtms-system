package com.willi.kafka;

import com.alibaba.fastjson.JSON;
import com.csvreader.CsvReader;
import com.willi.Bean.Order;
import com.willi.Bean.WarnMessage;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.errors.ProducerFencedException;

import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.Random;

/**
 * @program: bigdataplatform
 * @description: kafkaProducer向topic发送数据，供flinkETL
 * @author: Hoodie_Willi
 * @create: 2020-02-19 19:30
 **/

public class SendData {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
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
        props.put("transactional.id", "producer-1");
        // 设置幂等性
        props.put("enable.idempotence", true);
        // key的序列化方式
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        // value的序列化方式
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        KafkaProducer<String, String> producer = new KafkaProducer<>(props);
        System.out.println("start send message....");
        // 开始发送消息
        try {
            // 用来保存数据
            // 定义一个CSV路径
//            String csvFilePath = "/Users/williwei/Desktop/data_format/listings.csv";
            String csvFilePath = "/data/warning.csv";
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
//                Order order = new Order(
//                    Integer.valueOf(splitData[0]),
//                    splitData[1],
//                    Integer.valueOf(splitData[2]),
//                    splitData[3],
//                    splitData[4],
//                    splitData[5],
//                    Double.valueOf(splitData[6]),
//                    Double.valueOf(splitData[7]),
//                    splitData[8],
//                    Double.valueOf(splitData[9]),
//                    Integer.valueOf(splitData[10]),
//                    Integer.valueOf(splitData[11]),
//                    splitData[12],
//                    Double.valueOf(splitData[13]),
//                    Integer.valueOf(splitData[14]),
//                    Integer.valueOf(splitData[15])
//                );
                WarnMessage warnMessage = null;
                if (splitData.length > 2 && "warn".equals(splitData[1])){
                    /**
                     * @param id 事件id
                     *      * @param timestamp 事件的eventtime
                     *      * @param productionLineName 生产线的名称
                     *      * @param deviceName 生产的设备
                     *      * @param warnInformation 报警信息
                     *      * @param warnStatus 是否已经报警，分为报警，已经报警，解除报警
                     *      * @param warnDuration 事件的发生时长
                     */
                    warnMessage = new WarnMessage(
                            splitData[0].trim(),
                            splitData[1].trim(),
                            Long.parseLong(splitData[1].trim()),
                            splitData[2].trim(),
                            splitData[3].trim(),
                            splitData[4].trim(),
                            splitData[5].trim(),
                            splitData[6].trim()
                    );
                }


                String rawData = JSON.toJSONString(warnMessage);
                System.out.println(rawData);
////                System.out.println(data);
//                producer.send(new ProducerRecord<Integer, Order>("raw_order", order.getId(), order));
                if (warnMessage != null){
                    try {
                        producer.initTransactions();
                        producer.beginTransaction();
                        // 发送到相应工位的topic，每个topic中包含设备报警信息，产品质量信息，设备工作事件三种信息，key为每种消息的类型
                        producer.send(new ProducerRecord<String, String>
                                ("raw_" + splitData[2], warnMessage.getProductionLineName(), rawData));
                        producer.commitTransaction();
                    } catch (ProducerFencedException e) {
                        e.printStackTrace();
                        producer.abortTransaction();
                    }
                }
                Thread.sleep(3 * 1000);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
