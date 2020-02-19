package com.willi.utils;

import com.alibaba.fastjson.JSON;
import com.willi.Bean.Shopper;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

/**
 * @program: bigdataplatform
 * @description: 消费者序列化器
 * @author: Hoodie_Willi
 * @create: 2020-02-19 20:14
 **/

public class ShopperSerializer implements Serializer<Shopper> {

    @Override
    public void configure(Map map, boolean b) {

    }

    @Override
    public byte[] serialize(String topic, Shopper shopper) {
        return JSON.toJSONBytes(shopper);
    }

    @Override
    public void close() {

    }
}
