package com.stylefeng.guns.rest.order.service;


import com.stylefeng.guns.rest.order.model.BuyTicketsVO;
import com.stylefeng.guns.rest.user.model.BaseVo;
import com.stylefeng.guns.rest.order.model.MyOrderPage;
import com.stylefeng.guns.rest.order.model.OrderInfo;
import java.util.List;

public interface OrderService {
    public BaseVo buyTickets(String userId, BuyTicketsVO buyTicketsVO);

    List<OrderInfo> getMyOrderInfo(MyOrderPage myOrderPage, String userId);

    public void updateOrderInfo();
}





