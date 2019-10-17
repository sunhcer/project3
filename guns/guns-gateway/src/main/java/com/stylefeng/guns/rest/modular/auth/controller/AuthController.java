package com.stylefeng.guns.rest.modular.auth.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.rest.auth.AuthRequest;
import com.stylefeng.guns.rest.auth.AuthResponse;
import com.stylefeng.guns.rest.auth.BaseAuthResponseVO;
import com.stylefeng.guns.rest.common.exception.BizExceptionEnum;
import com.stylefeng.guns.rest.modular.auth.util.JwtTokenUtil;
import com.stylefeng.guns.rest.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;


/**
 * 请求验证的
 *
 * @author fengshuonan
 * @Date 2017/8/24 14:22
 */
@RestController
public class AuthController {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Reference(interfaceClass = UserService.class,check = false)
    UserService userService;

    @Autowired
    Jedis jedis;
    @Value("${myexpire}")
    int expireSeconds;

    @RequestMapping(value = "${jwt.auth-path}")
    public BaseAuthResponseVO createAuthenticationToken(AuthRequest authRequest) {

        boolean validate = userService.login(authRequest);


        if (validate) {
            final String randomKey = jwtTokenUtil.getRandomKey();
            final String token = jwtTokenUtil.generateToken(authRequest.getUserName(), randomKey);
            int userId = userService.getUserId(authRequest.getCredenceName());

            //将其登录信息放入redis
            //用户id
            jedis.set(token, userId+"");
            jedis.expire(token, expireSeconds);
//            return ResponseEntity.ok(new AuthResponse(token, randomKey));
            return new BaseAuthResponseVO(0,new AuthResponse(token, randomKey));
        } else {
            throw new GunsException(BizExceptionEnum.AUTH_REQUEST_ERROR);
        }
    }
}
