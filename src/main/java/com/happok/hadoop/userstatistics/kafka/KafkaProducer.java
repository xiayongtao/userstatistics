package com.happok.hadoop.userstatistics.kafka;

import com.alibaba.fastjson.JSONObject;
import com.happok.hadoop.userstatistics.entity.TrafficEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SuccessCallback;

/**
 * @author: xiayt
 * @date: 2019/4/18/018 11:43
 */
@Slf4j
@Component
@EnableScheduling
public class KafkaProducer {
    @Autowired
    private KafkaTemplate kafkaTemplate;

    // private static Logger logger = LoggerFactory.getLogger(KafkaProducer.class);

    /**
     * 发送消息到Kafka
     *
     * @param channel    Topic
     * @param jsonString 数据包
     */
    public void sendChannelMess(String channel, String jsonString) {
        ListenableFuture future = kafkaTemplate.send(channel, jsonString);
        future.addCallback(new SuccessCallback() {
            @Override
            public void onSuccess(Object o) {
                log.info("发送成功:" + o.toString());
            }
        }, new FailureCallback() {
            @Override
            public void onFailure(Throwable throwable) {
                log.info("发送异常:" + throwable.toString());
            }
        });
    }

    @Scheduled(cron = "0/5 * * * * ?")
    public void task() {
        // String message = new Date().toString();
        TrafficEntity trafficEntity = new TrafficEntity("2019-4-18", 12, 100, 200);
        String jsonString = JSONObject.toJSONString(trafficEntity);
        sendChannelMess("match", jsonString);
    }

}
