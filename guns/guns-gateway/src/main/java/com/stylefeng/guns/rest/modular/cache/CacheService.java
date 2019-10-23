package com.stylefeng.guns.rest.modular.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: zhou
 * @Date: 2019/10/23
 * @Time 10:59
 */
@Component
public class CacheService {
    private Cache<String,Object> cache;

    @PostConstruct
    public void init(){
        cache = CacheBuilder.newBuilder()
                .expireAfterWrite(60, TimeUnit.SECONDS)
                .initialCapacity(10)
                .maximumSize(100)
                .build();
    }

    public void set(String key, Object value){
        cache.put(key, value);
    }

    public Object get(String key){
        return cache.getIfPresent(key);
    }
}
