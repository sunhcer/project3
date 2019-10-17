package com.stylefeng.guns.rest.modular.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

/**
 * @Description:
 * @Author: zhou
 * @Date: 2019/10/15
 * @Time 20:55
 */
@Component
public class JedisUtil {
    @Autowired
    Jedis jedis;


}
