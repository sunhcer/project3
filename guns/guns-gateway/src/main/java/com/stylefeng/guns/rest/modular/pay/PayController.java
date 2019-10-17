package com.stylefeng.guns.rest.modular.pay;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.rest.film.vo.SFilmIndexPage;
import com.stylefeng.guns.rest.pay.service.PayService;
import com.stylefeng.guns.rest.pay.service.SOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

@RestController
public class PayController {

    @Reference(interfaceClass = SOrderService.class,check = false)
    SOrderService sOrderService;
    @Autowired
    Jedis jedis;
    @Reference(interfaceClass = PayService.class,check = false)
    PayService payService;
    @RequestMapping("order/getPayInfo")
    //测试参数接收post
    public SFilmIndexPage orderPayInfo(int orderId){
        SFilmIndexPage qrcode=payService.orderPayQRcode(orderId);
        return qrcode;
    }

    @RequestMapping("order/getPayResult")
    public SFilmIndexPage orderGetPayResult(int orderId,int tryNums){
        if (tryNums<4){
            int orderStatus=payService.queryOrderStatusBySandBox(orderId);
            if (orderStatus==1) {
                //已支付,去自己的数据库改变订单状态
                SFilmIndexPage order = sOrderService.orderGetPayResult(orderId);
                return order;
            }
        }
        return null;
    }
}
