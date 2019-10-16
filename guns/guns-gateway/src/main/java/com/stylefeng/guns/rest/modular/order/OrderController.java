package com.stylefeng.guns.rest.modular.order;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.rest.config.properties.JwtProperties;
import com.stylefeng.guns.rest.modular.auth.util.JedisUtil;
import com.stylefeng.guns.rest.order.model.BaseOrderResponseVO;
import com.stylefeng.guns.rest.order.model.MyOrderPage;
import com.stylefeng.guns.rest.order.model.OrderInfo;
import com.stylefeng.guns.rest.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class OrderController {

    @Autowired
    JwtProperties jwtProperties;

    @Autowired
    Jedis jedis;

    @Autowired
    JedisUtil jedisUtil;

    @Reference(interfaceClass = OrderService.class,check = false)
    OrderService orderService;

    //获取该用户的订单信息
    @RequestMapping("/order/getOrderInfo")
    public BaseOrderResponseVO getMyOrderInfo(MyOrderPage myOrderPage, HttpServletRequest request){
        String userId = jedisUtil.getUserId(request);   //拿到该userId
//        String userId=1+"";
        List<OrderInfo> myOrderInfo = orderService.getMyOrderInfo(myOrderPage, userId);
        BaseOrderResponseVO VO = new BaseOrderResponseVO();
        if(myOrderInfo==null){
            VO.setStatus(1);
            VO.setMsg("订单列表为空哦！~");
        }else {
            VO.setData(myOrderInfo);
            VO.setStatus(0);
        }
        return VO;
    }

}
