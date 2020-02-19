package com.willi.utils;

import com.alibaba.fastjson.JSON;
import com.willi.Bean.Shopper;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

/**
 * @program: bigdataplatform
 * @description:
 * @author: Hoodie_Willi
 * @create: 2020-02-19 20:19
 **/

public class ShopperDeserializer implements Deserializer<Shopper> {

    @Override
    public void configure(Map map, boolean b) {

    }

    @Override
    public Shopper deserialize(String data, byte[] bytes) {
        return JSON.parseObject(data, Shopper.class);
    }

    @Override
    public void close() {

    }
}
