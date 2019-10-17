package com.stylefeng.guns.rest.modular.pay;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.rest.film.vo.SFilmIndexPage;
import com.stylefeng.guns.rest.pay.model.PayResultVo;
import com.stylefeng.guns.rest.pay.model.QROrderRef;
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
    public SFilmIndexPage orderPayInfo(String orderId){
        //查找相应的订单信息
        QROrderRef orderRef=sOrderService.queryQROrderRef(orderId);
        SFilmIndexPage qrcode=payService.orderPayQRcode(orderRef);
        return qrcode;
    }

    @RequestMapping("order/getPayResult")
    public SFilmIndexPage orderGetPayResult(PayResultVo resultVo){
        int tryNums1=Integer.valueOf(resultVo.getTryNums());
//        int orderId1=Integer.valueOf(resultVo.getOrderId());
        String orderId = resultVo.getOrderId();
        SFilmIndexPage filmIndexPage = new SFilmIndexPage();
        if (tryNums1<8) {
            int orderStatus = payService.queryOrderStatusBySandBox(orderId);
            if (orderStatus == 1) {
                //已支付,去自己的数据库改变订单状态
                filmIndexPage = sOrderService.orderGetPayResult(orderId);
                return filmIndexPage;
            }
        }
                //未支付,返回一个支付失败
                filmIndexPage.setData("");
                filmIndexPage.setImgPre("");
                filmIndexPage.setNowPage("");
                filmIndexPage.setTotalPage("");
                filmIndexPage.setStatus(1);
                filmIndexPage.setMsg("支付失败!");

        return filmIndexPage;
    }
}
