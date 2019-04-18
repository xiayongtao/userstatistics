package com.happok.hadoop.userstatistics.kafka;

import com.alibaba.fastjson.JSON;
import com.happok.hadoop.userstatistics.entity.TrafficEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author: xiayt
 * @date: 2019/4/18/018 11:44
 */
@Component
@Slf4j
public class KafkaConsumer {

    /**
     * 监听比赛主题,有消息就读取
     *
     * @param jsonString 数据包
     */
    @KafkaListener(topics = {"match"})
    public void receiveMessage(String jsonString) {

        TrafficEntity trafficEntity = JSON.parseObject(jsonString, TrafficEntity.class);
        log.info("接收数据:" + trafficEntity.toString());
        // 后续操作待完善
    }
}
