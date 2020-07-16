package com.willi.flink;

import org.apache.flink.api.common.typeinfo.TypeHint;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema;
import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * \* project: bigdataplatform
 * \* package: com.willi.flink
 * \* author: Willi Wei
 * \* date: 2020-07-16 14:49:57
 * \* description:
 * \
 */
public class MyKafkaDeserializationSchema implements KafkaDeserializationSchema<ConsumerRecord<String, String>> {
    @Override
    public boolean isEndOfStream(ConsumerRecord<String, String> nextElement) {
        return false;
    }

    @Override
    public ConsumerRecord<String, String> deserialize(ConsumerRecord<byte[], byte[]> record) throws Exception {
        return new ConsumerRecord<String, String>(
                record.topic(),
                record.partition(),
                record.offset(),
                new String(record.key()),
                new String(record.value())
        );
    }

    @Override
    public TypeInformation<ConsumerRecord<String, String>> getProducedType() {
        return TypeInformation.of(new TypeHint<ConsumerRecord<String, String>>() {
        });
    }
}