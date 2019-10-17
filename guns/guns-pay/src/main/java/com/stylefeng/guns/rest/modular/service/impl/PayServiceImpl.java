package com.stylefeng.guns.rest.modular.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alipay.demo.trade.Main;
import com.stylefeng.guns.rest.common.persistence.model.PayDataRef;
import com.stylefeng.guns.rest.film.vo.SFilmIndexPage;
import com.stylefeng.guns.rest.pay.service.PayService;
import org.springframework.stereotype.Component;

@Component
@Service(interfaceClass = PayService.class)
public class PayServiceImpl implements PayService {
    @Override
    public SFilmIndexPage orderPayQRcode(int orderId) {
        Main main = new Main();
        String qRCodeAddress= main.test_trade_precreate(orderId);
        String orderId1=orderId+"";
        PayDataRef payDataRef = new PayDataRef(orderId1, qRCodeAddress);
        SFilmIndexPage<PayDataRef> page = new SFilmIndexPage<>();
        page.setStatus(0);
        page.setData(payDataRef);
        page.setImgPre("http://www.duolaima.com");
        return page;
    }

    @Override
    public int queryOrderStatusBySandBox(int orderId) {
        Main main = new Main();
        int status = main.test_trade_query(orderId);
        return status;
    }
}
