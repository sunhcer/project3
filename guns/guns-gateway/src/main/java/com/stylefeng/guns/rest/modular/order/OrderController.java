package com.stylefeng.guns.rest.modular.order;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.rest.common.exception.BizExceptionEnum;
import com.stylefeng.guns.rest.modular.auth.util.JedisUtil;
import com.stylefeng.guns.rest.order.model.BuyTicketsVO;
import com.stylefeng.guns.rest.order.service.OrderService;
import com.stylefeng.guns.rest.user.model.BaseVo;
import com.stylefeng.guns.rest.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import com.stylefeng.guns.rest.config.properties.JwtProperties;
import com.stylefeng.guns.rest.order.model.BaseOrderResponseVO;
import com.stylefeng.guns.rest.order.model.MyOrderPage;
import com.stylefeng.guns.rest.order.model.OrderInfo;
import org.springframework.web.bind.annotation.RequestMapping;
import redis.clients.jedis.Jedis;
import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
public class OrderController {

    @Autowired
    JedisUtil jedisUtil;
    @Reference(interfaceClass = UserService.class, check = false)
    UserService userService;

    @Reference(interfaceClass = OrderService.class, check = false)
    OrderService orderService;

    @Autowired
    JwtProperties jwtProperties;

    @Autowired
    Jedis jedis;


    ///order/buyTickets
    @PostMapping("/order/buyTickets")
    public BaseVo buyTickets(BuyTicketsVO buyTicketsVO, HttpServletRequest request){
        String userId = jedisUtil.getUserId(request);

        BaseVo baseVo = new BaseVo();

        //如果用户id不为空
        if (userId == null){
            //token验证失效
            baseVo.setStatus(700);
//            throw new GunsException(BizExceptionEnum.TOKEN_ERROR);
            return baseVo;
        }

        baseVo = orderService.buyTickets(userId, buyTicketsVO);

        return baseVo;
    }

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

