package com.stylefeng.guns.rest.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stylefeng.guns.rest.order.model.SeatsInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Description:
 * @Author: zhou
 * @Date: 2019/10/15
 * @Time 19:25
 */
@Configuration
public class RedisConfig {
    @Bean
    public Jedis jedis() throws IOException {
        //初始化jedis的时候  将座位表放在这里面
        InputStream input = RedisConfig.class.getClassLoader().getResourceAsStream("seats/12314.json");

        StringBuilder sb = new StringBuilder();
        byte[] bytes = new byte[1024];
        int len = 0;

        while ((len = (input.read(bytes))) != -1){
            sb.append(new String(bytes, 0, len));
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String content = sb.toString().replace("\n", "");
        SeatsInfo seatsInfo = objectMapper.readValue(content, SeatsInfo.class);
        String string = objectMapper.writeValueAsString(seatsInfo);

        Jedis jedis = new Jedis("127.0.0.1", 6379);

        jedis.set("12314.json", string);

        return jedis;
    }
}
