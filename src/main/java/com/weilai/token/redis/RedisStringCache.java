package com.weilai.token.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisStringCache {
    @Autowired
    private StringRedisTemplate template;

    final private int EXPIRE_TIME = 5000;

    //增加缓存
    public void cache(String key, String value, String cacheType) {
        template.opsForValue().set(cacheType + key, value, EXPIRE_TIME, TimeUnit.SECONDS);
    }

    //查询缓存
    public String get(String key, String cacheType) {
        return template.opsForValue().get(cacheType + key);
    }

    //删除缓存
    public void remove(String key, String cacheType) {
        template.delete(cacheType + key);
    }
}