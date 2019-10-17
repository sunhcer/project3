package com.stylefeng.guns.rest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

/**
 * @Description:
 * @Author: zhou
 * @Date: 2019/10/15
 * @Time 19:25
 */
@Configuration
public class RedisConfig {
    @Bean
    public Jedis jedis(){
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        return jedis;
    }
}
