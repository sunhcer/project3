package com.stylefeng.guns.rest.modular.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alipay.demo.trade.Main;
import com.stylefeng.guns.rest.common.persistence.model.PayDataRef;
import com.stylefeng.guns.rest.film.vo.SFilmIndexPage;
import com.stylefeng.guns.rest.pay.model.QROrderRef;
import com.stylefeng.guns.rest.pay.service.PayService;
import org.springframework.stereotype.Component;

@Component
@Service(interfaceClass = PayService.class)
public class PayServiceImpl implements PayService {
    @Override
    public SFilmIndexPage orderPayQRcode(QROrderRef orderRe) {
        Main main = new Main();
        //找到订单
        String orderId=orderRe.getUUID();
        //根据订单信息生成二维码
        String qRCodeAddress= main.test_trade_precreate(orderRe);
        PayDataRef payDataRef = new PayDataRef(orderId, qRCodeAddress);
        SFilmIndexPage<PayDataRef> page = new SFilmIndexPage<>();
        page.setStatus(0);
        page.setData(payDataRef);
        page.setImgPre("http://192.168.3.39/");
        return page;
    }

    @Override
    public int queryOrderStatusBySandBox(String orderId) {
        Main main = new Main();
        int status = main.test_trade_query(orderId);
        return status;
    }
}
