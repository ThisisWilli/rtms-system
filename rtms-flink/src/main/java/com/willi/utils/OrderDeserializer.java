package com.willi.utils;

import com.alibaba.fastjson.JSON;
import com.willi.Bean.Order;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

/**
 * @program: bigdataplatform
 * @description:
 * @author: Hoodie_Willi
 * @create: 2020-02-19 20:19
 **/

public class OrderDeserializer implements Deserializer<Order> {

    @Override
    public void configure(Map map, boolean b) {

    }

    @Override
    public Order deserialize(String data, byte[] bytes) {
        return JSON.parseObject(data, Order.class);
    }

    @Override
    public void close() {

    }
}
