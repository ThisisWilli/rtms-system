package com.willi.serializer;

import com.alibaba.fastjson.JSON;
import com.willi.Bean.QualityMessage;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

/**
 * \* project: bigdataplatform
 * \* package: com.willi.utils
 * \* author: Willi Wei
 * \* date: 2020-07-17 14:17:08
 * \* description:
 * \
 */
public class QualityMessageSerializer implements Serializer<QualityMessage> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String topic, QualityMessage data) {
        return JSON.toJSONBytes(data);
    }



    @Override
    public void close() {

    }
}