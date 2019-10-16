package com.stylefeng.guns.rest.modular.order;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.stylefeng.guns.rest.cinema.model.Film;
import com.stylefeng.guns.rest.cinema.service.CinemaService;
import com.stylefeng.guns.rest.common.persistence.dao.MoocOrderTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeFieldTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MoocOrderT;
import com.stylefeng.guns.rest.common.persistence.model.MtimeFieldT;
import com.stylefeng.guns.rest.order.model.MyOrderPage;
import com.stylefeng.guns.rest.order.model.OrderInfo;
import com.stylefeng.guns.rest.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Service(interfaceClass = OrderService.class)
@Component
public class OrderServiceImpl implements OrderService {

    @Autowired
    MoocOrderTMapper moocOrderTMapper;

    @Autowired
    MtimeFieldTMapper mtimeFieldTMapper;

    @Reference(interfaceClass = CinemaService.class,check = false)
    CinemaService cinemaService;

    @Override
    public List<OrderInfo> getMyOrderInfo(MyOrderPage myOrderPage, String userId) {
        Page<MoocOrderT> page = new Page<>();
        page.setSize(myOrderPage.getPageSize());
        page.setCurrent(myOrderPage.getNowPage());

        EntityWrapper<MoocOrderT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("order_user", userId);
        List<MoocOrderT> list = moocOrderTMapper.selectPage(page, entityWrapper);
        List<OrderInfo> list1=new ArrayList<>();
        for (MoocOrderT order : list) {  //拿到每个小定单
            OrderInfo orderInfo = new OrderInfo();
            String cinemaName = cinemaService.getCinemaInfoByCinemaId(order.getCinemaId()).getCinemaName();
            orderInfo.setCinemaName(cinemaName);//  1
            MtimeFieldT field = mtimeFieldTMapper.selectById(order.getFieldId());
            orderInfo.setFieldTime("19年10月16日"+field.getBeginTime());//  2
            Film film = cinemaService.getFilmInfoByFieldId(order.getFieldId());
            orderInfo.setFilmName(film.getFilmName());//    3
            orderInfo.setOrderId(order.getUuid());//     4
            orderInfo.setOrderPrice(order.getOrderPrice().toString());//     5
            //0-待支付,1-已支付,2-已关闭
            Integer status = order.getOrderStatus();
            if(status==0){
                orderInfo.setOrderStatus("待支付");
            }else if(status==1){
                orderInfo.setOrderStatus("已支付");
            }else {
                orderInfo.setOrderStatus("已关闭");//     6
            }
            orderInfo.setOrderTimestamp("1571206839");//     7
            orderInfo.setSeatsName(order.getSeatsIds());//  8
            list1.add(orderInfo);
        }
        return list1;
    }
}
