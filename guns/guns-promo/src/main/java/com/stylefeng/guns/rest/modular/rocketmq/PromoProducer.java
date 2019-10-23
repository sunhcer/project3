package com.stylefeng.guns.rest.modular.rocketmq;

import com.alibaba.fastjson.JSON;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeStockLogMapper;
import com.stylefeng.guns.rest.common.persistence.model.MtimeStockLog;
import com.stylefeng.guns.rest.promo.model.PromoToken;
import com.stylefeng.guns.rest.promo.service.PromoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.nio.charset.Charset;
import java.util.HashMap;

/**
 * @Description:
 * @Author: zhou
 * @Date: 2019/10/21
 * @Time 17:15
 */
@Component
@Slf4j
public class PromoProducer {
    private DefaultMQProducer mqProducer;
    private TransactionMQProducer transactionMQProducer;
    @Value("${rocketmq.name-server}")
    String nameServer;
    @Value("${rocketmq.topic}")
    String topic;
    @Value("${rocketmq.producer-group}")
    String producerGroup;
    @Autowired
    PromoService promoService;
    @Autowired
    MtimeStockLogMapper stockLogMapper;

    @PostConstruct
    public void init() {
        mqProducer = new DefaultMQProducer(producerGroup);

        transactionMQProducer = new TransactionMQProducer();

        transactionMQProducer.setNamesrvAddr(nameServer);
        transactionMQProducer.setProducerGroup(producerGroup);
        try {
            transactionMQProducer.start();
            log.info("transactionProducer初始化成功");
        } catch (MQClientException e) {
            e.printStackTrace();
            log.info("transactionProducer初始化失败");
        }

        //初始化
        transactionMQProducer.setTransactionListener(new TransactionListener() {
            /**
             * 执行本地事务
             * @param message
             * @param args
             * @return
             */
            @Override
            public LocalTransactionState executeLocalTransaction(Message message, Object args) {
                if (args == null){
                    //消息发送失败        args为null 也无法操作数据库流水表
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                }

                HashMap hashMap = (HashMap) args;

                PromoToken promoToken = (PromoToken) hashMap.get("promoToken");
                String promoLogId = (String) hashMap.get("promoLogId");
                Integer userId = (Integer) hashMap.get("userId");

                if (hashMap == null) {
                    //参数错误  直接回滚
                    stockLogMapper.updateLogStatus(promoLogId, 3);
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                }

                boolean flag = false;
                try {
                    //本地事务中创建订单扣减库存
                    flag = promoService.createOrderByTransaction(promoToken, userId);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("订单生成失败, promoId:{}   amount:{}", promoToken.getPromoId(), promoToken.getAmount());
                    stockLogMapper.updateLogStatus(promoLogId, 3);
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                }

                if (flag) {
                    //事务成功
                    int affectRows = stockLogMapper.updateLogStatus(promoLogId, 2);
                    return LocalTransactionState.COMMIT_MESSAGE;
                } else {
                    //事务失败
                    stockLogMapper.updateLogStatus(promoLogId, 3);
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                }
            }

            /**
             * 检查事务状态
             * @param messageExt
             * @return
             */
            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
                byte[] body = messageExt.getBody();
                String jsonString = new String(body);
                HashMap hashMap = JSON.parseObject(jsonString, HashMap.class);

                String promoLogId = (String) hashMap.get("promoLogId");

                MtimeStockLog stockLog = stockLogMapper.selectById(promoLogId);
                Integer status = stockLog.getStatus();
                if (status == 2){
                    return LocalTransactionState.COMMIT_MESSAGE;
                }else if(status == 3){
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                }else{
                    return LocalTransactionState.UNKNOW;
                }
            }
        });

    }

//    /**
//     * 扣减库存的消息
//     * @return  消息是否发送成功
//     * @Param
//     */
//    public boolean sendDecreaseStockMessage(Integer promoId, Integer amount){
//        log.info("进入了生产者,promoId:{} amount:{}", promoId, amount);
//
//        HashMap<String, Object> hashMap = new HashMap<>();
//        hashMap.put("promoId", promoId);
//        hashMap.put("amount", amount);
//        String jsonString = JSON.toJSONString(hashMap);
//
//        Message message = new Message(topic, jsonString.getBytes(Charset.forName("utf-8")));
//        SendResult sendResult = null;
//        try {
//            sendResult = mqProducer.send(message);
//        } catch (MQClientException e) {
//            //只要进入异常        就代表消息发送失败
//            e.printStackTrace();
//            log.info("消息发送失败,promoId:{} amount:{}", promoId, amount);
//            return false;
//        } catch (RemotingException e) {
//            //只要进入异常        就代表消息发送失败
//            e.printStackTrace();
//            log.info("消息发送失败,promoId:{} amount:{}", promoId, amount);
//            return false;
//        } catch (MQBrokerException e) {
//            //只要进入异常        就代表消息发送失败
//            e.printStackTrace();
//            log.info("消息发送失败,promoId:{} amount:{}", promoId, amount);
//            return false;
//        } catch (InterruptedException e) {
//            //只要进入异常        就代表消息发送失败
//            e.printStackTrace();
//            log.info("消息发送失败,promoId:{} amount:{}", promoId, amount);
//            return false;
//        }
//
//        if (sendResult != null && (sendResult.getSendStatus()) == SendStatus.SEND_OK){
//            //如果不为空 并且状态是发送ok 则代表消息发送成功
//            log.info("消息发送成功,promoId:{} amount:{}", promoId, amount);
//            return true;
//        }
//        log.info("消息发送失败,promoId:{} amount:{}", promoId, amount);
//        return false;
//    }

    /**
     * rocketMQ消息事务
     *
     * @param promoId 商品id
     * @param amount  商品数量
     * @return 是否成功
     */
    public boolean sendTransactionStockMessage(PromoToken promoToken, Integer userId) {

        String promoLogId = promoService.initPromoLog(promoToken);
        if (promoLogId == null) {
            //日志生成失败
            return false;
        }

        HashMap<Object, Object> hashMap = new HashMap<>();
        hashMap.put("promoToken", promoToken);
        hashMap.put("promoLogId", promoLogId);
        hashMap.put("userId", userId);

        HashMap<Object, Object> argsMap = new HashMap<>();
        argsMap.put("promoToken", promoToken);
        argsMap.put("promoLogId", promoLogId);
        argsMap.put("userId", userId);

        String string = JSON.toJSONString(promoToken);

        Message message = new Message(topic, string.getBytes(Charset.forName("utf-8")));
        TransactionSendResult result = null;
        try {
            result = transactionMQProducer.sendMessageInTransaction(message, argsMap);
        } catch (MQClientException e) {
            e.printStackTrace();
            log.info("消息发送失败,promoId:{} amount:{}", promoToken.getPromoId(), promoToken.getAmount());
            return false;
        }

        if (LocalTransactionState.COMMIT_MESSAGE == result.getLocalTransactionState()) {
            //如果本地事务的状态是提交 才返回true  其他一律返回false
            return true;
        }

        return false;
    }
}
