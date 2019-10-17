package com.stylefeng.guns.rest.pay.service;

import com.stylefeng.guns.rest.film.vo.SFilmIndexPage;

public interface SOrderService {
    SFilmIndexPage orderGetPayResult(int orderId);
}
