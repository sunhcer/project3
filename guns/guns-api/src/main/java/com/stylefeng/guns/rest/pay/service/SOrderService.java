package com.stylefeng.guns.rest.pay.service;

import com.stylefeng.guns.rest.film.vo.SFilmIndexPage;
import com.stylefeng.guns.rest.pay.model.QROrderRef;

public interface SOrderService {
    SFilmIndexPage orderGetPayResult(int orderId);

    QROrderRef queryQROrderRef(String s);
}
