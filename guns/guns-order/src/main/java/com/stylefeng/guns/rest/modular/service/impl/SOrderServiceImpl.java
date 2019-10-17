package com.stylefeng.guns.rest.modular.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.stylefeng.guns.rest.common.persistence.dao.MoocOrderTMapper;
import com.stylefeng.guns.rest.common.persistence.model.SOrderBoxRef;
import com.stylefeng.guns.rest.film.vo.SFilmIndexPage;
import com.stylefeng.guns.rest.pay.model.QROrderRef;
import com.stylefeng.guns.rest.pay.service.SOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Service(interfaceClass = SOrderService.class)
public class SOrderServiceImpl implements SOrderService {
    @Autowired
    MoocOrderTMapper sOrderMapper;
    @Override
    public SFilmIndexPage orderGetPayResult(String orderId) {
        sOrderMapper.updateOrderStatusBySandBox(orderId+"");
        SOrderBoxRef orderBoxRef = new SOrderBoxRef(orderId + "", 1, "支付成功");
        SFilmIndexPage<SOrderBoxRef> sOrderPage = new SFilmIndexPage<>();
        sOrderPage.setStatus(0);
        sOrderPage.setData(orderBoxRef);
        return sOrderPage;
    }

    @Override
    public QROrderRef queryQROrderRef(String orderId) {
        QROrderRef orderRef=sOrderMapper.queryOrderPrice(orderId);
        return orderRef;
    }
}
