package com.stylefeng.guns.rest.order.service;

import com.stylefeng.guns.rest.order.model.MyOrderPage;
import com.stylefeng.guns.rest.order.model.OrderInfo;

import java.util.List;

public interface OrderService {
    List<OrderInfo> getMyOrderInfo(MyOrderPage myOrderPage, String userId);

}
