//package com.stylefeng.guns.rest.modular.rocketmq;
//
//import com.alibaba.fastjson.JSON;
//import com.stylefeng.guns.rest.common.persistence.dao.MtimePromoStockMapper;
//import com.stylefeng.guns.rest.promo.model.PromoToken;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
//import org.apache.rocketmq.spring.core.RocketMQListener;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
///**
// * @Description:
// * @Author: zhou
// * @Date: 2019/10/20
// * @Time 11:33
// */
//@Component
//@Slf4j
//@RocketMQMessageListener(
//        topic = "promoStock",
//        consumerGroup = "promo-consumer",
//        selectorExpression = "*"
//)
//public class RocketMqConsumer implements RocketMQListener<String> {
//    @Autowired
//    MtimePromoStockMapper mtimePromoStockMapper;
//    @Override
//    public void onMessage(String msg) {
//        System.out.println("消费端收到了消息");
//        System.out.println("消息是:" + msg);
//
//        PromoToken token = JSON.parseObject(msg, PromoToken.class);
//
//        int decreaseStock = mtimePromoStockMapper.decreaseStock(token.getAmount(), token.getPromoId());
//        if (decreaseStock == 0){
//            //扣减失败
//            log.error("扣减库存失败:" + token);
//        }else{
//            log.info("扣减库存成功:" + token);
//        }
//
//    }
//}
