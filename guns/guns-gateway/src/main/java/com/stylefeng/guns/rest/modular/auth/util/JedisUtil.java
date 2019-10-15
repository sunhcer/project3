package com.stylefeng.guns.rest.modular.auth.util;

import com.stylefeng.guns.rest.config.properties.JwtProperties;
import com.stylefeng.guns.rest.user.model.BaseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description:
 * @Author: zhou
 * @Date: 2019/10/15
 * @Time 20:55
 */
@Component
public class JedisUtil {
    @Autowired
    JwtProperties jwtProperties;

    @Autowired
    Jedis jedis;

    public String getUserId(HttpServletRequest request) {
        String requestHeader = request.getHeader(jwtProperties.getHeader());
        String authToken = null;
        authToken = requestHeader.substring(7);
        String id = jedis.get(authToken);
        return id;
    }
}
