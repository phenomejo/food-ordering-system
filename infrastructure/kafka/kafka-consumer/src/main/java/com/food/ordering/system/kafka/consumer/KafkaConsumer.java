package com.food.ordering.system.kafka.consumer;

import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerRecords;

public interface KafkaConsumer<T extends SpecificRecordBase> {

    void receive(ConsumerRecords<String, T> consumerRecords);

}
