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

import javax.servlet.http.HttpServletRequest;

@RestController
public class OrderController {

    @Autowired
    JedisUtil jedisUtil;
    @Reference(interfaceClass = UserService.class, check = false)
    UserService userService;

    @Reference(interfaceClass = OrderService.class, check = false)
    OrderService orderService;


    ///order/buyTickets
    @PostMapping("/order/buyTickets")
    public BaseVo buyTickets(BuyTicketsVO buyTicketsVO, HttpServletRequest request){
        String userId = jedisUtil.getUserId(request);

        //如果用户id不为空
        if (userId == null){
            //token验证失效
            throw new GunsException(BizExceptionEnum.TOKEN_ERROR);
        }

        BaseVo baseVo = orderService.buyTickets(userId, buyTicketsVO);

        return baseVo;
    }
}
