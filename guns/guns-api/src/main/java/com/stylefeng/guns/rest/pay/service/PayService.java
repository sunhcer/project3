package com.stylefeng.guns.rest.pay.service;

import com.stylefeng.guns.rest.film.vo.SFilmIndexPage;

public interface PayService {

    SFilmIndexPage orderPayQRcode(int orderId);

    int queryOrderStatusBySandBox(int orderId);
}
