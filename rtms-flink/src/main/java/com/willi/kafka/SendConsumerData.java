package com.willi.kafka;

import com.alibaba.fastjson.JSON;
import com.csvreader.CsvReader;
import com.willi.Bean.Shopper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * @program: bigdataplatform
 * @description: kafkaProducer向topic发送数据，供flinkETL
 * @author: Hoodie_Willi
 * @create: 2020-02-19 19:30
 **/

public class SendConsumerData {
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
        props.put("value.serializer", "com.willi.utils.ShopperSerializer");
        KafkaProducer<String, Shopper> producer = new KafkaProducer<String, Shopper>(props);
        System.out.println("start send message....");
        // 开始发送消息
        try {
            // 用来保存数据
            // 定义一个CSV路径
            String csvFilePath = "/Users/williwei/Desktop/data_format/user_log.csv";
            // 创建CSV读对象 例如:CsvReader(文件路径，分隔符，编码格式);
            CsvReader reader = new CsvReader(csvFilePath, ',', StandardCharsets.UTF_8);
            // 跳过表头 如果需要表头的话，这句可以忽略
            reader.readHeaders();
            // 逐行读入除表头的数据
            while (reader.readRecord()) {
//                System.out.println(reader.getRawRecord());
                String rawData = reader.getRawRecord();
                System.out.println(rawData);
                String[] splitData = rawData.split(",");
                Shopper shopper = new Shopper();
//                Message message = new Message();
                shopper.setUserId(Integer.valueOf(splitData[0]));
                shopper.setItemId(Integer.valueOf(splitData[1]));
                shopper.setCatId(Integer.valueOf(splitData[2]));
                shopper.setMerchantId(Integer.valueOf(splitData[3]));
                shopper.setBrandId(Integer.valueOf(splitData[4]));
                shopper.setMonth(Integer.valueOf(splitData[5]));
                shopper.setDay(Integer.valueOf(splitData[6]));
                shopper.setAction(Integer.valueOf(splitData[7]));
                shopper.setAgeRange(Integer.valueOf(splitData[8]));
                shopper.setGender(Integer.valueOf(splitData[9]));
                shopper.setProvince(splitData[10]);
//                String data = JSON.toJSONString(message) ;
////                Object o = JSON.toJSON(message);
//                System.out.println(data);
                producer.send(new ProducerRecord<String, Shopper>("orgin", shopper));
                Thread.sleep(5000);
            }
            reader.close();
            // 遍历读取的CSV文件
//            for (int row = 0; row < csvFileList.size(); row++) {
//            // 取得第row行第0列的数据
//                String cell = csvFileList.get(row)[0];
//                System.out.println("------------>"+cell);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
