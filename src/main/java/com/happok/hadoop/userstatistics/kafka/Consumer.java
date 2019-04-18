package com.happok.hadoop.userstatistics.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author: xiayt
 * @date: 2019/4/18/018 11:36
 */
@Component
public class Consumer {
    @KafkaListener(topics = "nginxtopic")
    public void listen(ConsumerRecord<?, ?> record) throws Exception {
        System.out.printf("topic = %s, offset = %d, value = %s \n", record.topic(), record.offset(), record.value());
    }
}
