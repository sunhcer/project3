//package com.stylefeng.guns.rest.modular.rocketmq;
//
//import org.apache.rocketmq.spring.core.RocketMQTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
///**
// * @Description:
// * @Author: zhou
// * @Date: 2019/10/20
// * @Time 11:18
// */
//@Component
//public class ReduceStockProducer {
//
//    @Autowired
//    private RocketMQTemplate rocketMQTemplate;
//
//    public void sendMsg(String topic, String msg){
//        rocketMQTemplate.convertAndSend(topic, msg);
//    }
//}
