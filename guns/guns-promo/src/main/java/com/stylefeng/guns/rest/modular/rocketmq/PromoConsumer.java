package com.stylefeng.guns.rest.modular.rocketmq;

import com.alibaba.fastjson.JSON;
import com.stylefeng.guns.rest.common.persistence.dao.MtimePromoStockMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;

/**
 * @Description:
 * @Author: zhou
 * @Date: 2019/10/21
 * @Time 19:42
 */
@Component
@Slf4j
public class PromoConsumer {
    private DefaultMQPushConsumer mqPushConsumer;
    @Value("${rocketmq.name-server}")
    String nameServer;
    @Value("${rocketmq.topic}")
    String topic;
    @Value("${rocketmq.consumer-group}")
    String consumerGroup;
    @Autowired
    MtimePromoStockMapper promoStockMapper;


    @PostConstruct
    public void init(){
        mqPushConsumer = new DefaultMQPushConsumer(consumerGroup);
        mqPushConsumer.setNamesrvAddr(nameServer);
        try {
            mqPushConsumer.subscribe(topic, "*");
        } catch (MQClientException e) {
            log.error("消费者初始化失败");
            e.printStackTrace();
        }
        log.error("消费者初始化成功");

        mqPushConsumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                MessageExt messageExt = list.get(0);
                String jsonString = new String(messageExt.getBody());

                HashMap hashMap = JSON.parseObject(jsonString, HashMap.class);
                Integer promoId = (Integer) hashMap.get("promoId");
                Integer amount = (Integer) hashMap.get("amount");

                int affectRows = promoStockMapper.decreaseStock(amount, promoId);
                if (affectRows < 1){
                    log.error("消费失败,promoId:{}  amount:{}", promoId, amount);
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
                log.error("消息消费成功,promoId:{}  amount:{}", promoId, amount);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        try {
            mqPushConsumer.start();
        } catch (MQClientException e) {
            e.printStackTrace();
            log.error("消费者启动失败");
        }

    }


}
