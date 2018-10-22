package com.wchm.website.service.impl;

import com.wchm.website.entity.Admin;
import com.wchm.website.entity.Operation;
import com.wchm.website.service.RedisService;
import com.wchm.website.util.Result;
import net.sf.json.JSONObject;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean set(final String key, final String value) {
        redisTemplate.opsForValue().set(key, value, 2, TimeUnit.MINUTES);
        return true;
    }

    @Override
    public boolean setMinutes(String key, String value, Integer minutes) {
        redisTemplate.opsForValue().set(key, value, minutes, TimeUnit.MINUTES);
        return true;
    }

    @Override
    public boolean setHours(String key, String value, Integer hours) {
        redisTemplate.opsForValue().set(key, value, hours, TimeUnit.HOURS);
        return true;
    }

    @Override
    public String get(final String key) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value != null) {
            return value.toString();
        }
        return null;
    }

    @Override
    public boolean expire(final String key, long expire) {
        return redisTemplate.expire(key, expire, TimeUnit.SECONDS);
    }

    @Override
    public boolean remove(final String key) {
        return redisTemplate.delete(key);
    }

    @Override
    public Object strToBean(Class<?> beanClass, String value) {
        JSONObject jsonObject = JSONObject.fromObject(value);
        return JSONObject.toBean(jsonObject, beanClass);
    }


}

