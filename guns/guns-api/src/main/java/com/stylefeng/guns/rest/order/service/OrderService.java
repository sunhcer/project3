package com.stylefeng.guns.rest.order.service;


import com.stylefeng.guns.rest.order.model.BuyTicketsVO;
import com.stylefeng.guns.rest.user.model.BaseVo;

public interface OrderService {
    public BaseVo buyTickets(String userId, BuyTicketsVO buyTicketsVO);
}
