package com.willi.utils;

import com.alibaba.fastjson.JSON;
import com.willi.Bean.WarnMessage;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

/**
 * \* project: bigdataplatform
 * \* package: com.willi.utils
 * \* author: Willi Wei
 * \* date: 2020-07-16 11:02:17
 * \* description:
 * \
 */
public class WarnMessageSerializer implements Serializer<WarnMessage> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String topic, WarnMessage warnMessage) {
        return JSON.toJSONBytes(warnMessage);
    }

    @Override
    public void close() {

    }
}