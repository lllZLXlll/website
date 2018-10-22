package com.wchm.website.service;

import com.wchm.website.entity.Operation;
import com.wchm.website.util.Result;
import org.apache.poi.ss.formula.functions.T;

/**
 * redis 操作类
 */
public interface RedisService {
    /**
     * set存数据，默认时间两分钟
     *
     * @param key
     * @param value
     * @return
     */
    boolean set(String key, String value);

    /**
     * set存数据，自定义保存时间，单位分钟
     *
     * @param key
     * @param value
     * @param minutes 保存多少分钟
     * @return
     */
    boolean setMinutes(String key, String value, Integer minutes);

    /**
     * set存数据，自定义保存时间，单位小时
     *
     * @param key
     * @param value
     * @param hours 保存多少小时
     * @return
     */
    boolean setHours(String key, String value, Integer hours);

    /**
     * get获取数据
     *
     * @param key
     * @return
     */
    String get(String key);

    /**
     * 设置有效天数
     *
     * @param key
     * @param expire
     * @return
     */
    boolean expire(String key, long expire);

    /**
     * 移除数据
     *
     * @param key
     * @return
     */
    boolean remove(String key);

    /**
     *
     * @param beanClass
     * @param value
     * @return
     */
    Object strToBean(Class<?> beanClass, String value);

}