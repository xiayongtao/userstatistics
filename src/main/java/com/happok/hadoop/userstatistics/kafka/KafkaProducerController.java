package com.happok.hadoop.userstatistics.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: xiayt
 * @date: 2019/4/18/018 11:35
 */
@RestController
@RequestMapping("kafka")
public class KafkaProducerController {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @RequestMapping("send")
    public String send(String msg) {
        kafkaTemplate.send("test_topic", msg);
        return "success";
    }
}
