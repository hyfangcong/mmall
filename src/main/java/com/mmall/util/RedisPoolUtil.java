package com.mmall.util;

import com.mmall.common.RedisPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

/**
 * @author: fangcong
 * @date: 2020/3/21
 */
@Slf4j
public class RedisPoolUtil {
    public static String get(String key){
        String value = null;
        Jedis jedis = null;
        try {
            jedis = RedisPool.getJedis();
            value = jedis.get(key);
        } catch (Exception e) {
            log.error("get occurred error:key{}, error", key, e);
        } finally {
            if(jedis != null)
                jedis.close();
        }
        return value;
    }

    public static String set(String key, String value){
        Jedis jedis = null;
        String result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.set(key, value);
        } catch (Exception e) {
            log.error("set occurred error:key{}, value{}, error", key, value, e);
        } finally {
            if(jedis != null)
                jedis.close();
        }
        return result;
    }

    /**
     * 设置带有过期时间的key，单位是秒
     * @param key
     * @param value
     * @param exTime
     */
    public static String setex(String key, String value, int exTime){
        Jedis jedis = null;
        String result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.setex(key, exTime, value);
        } catch (Exception e) {
            log.error("setex occurred error:key{}, value{}, error", key, value, e);
        } finally {
            if(jedis != null)
                jedis.close();
        }
        return result;
    }

    public static Long expire(String key, int exTime){
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.expire(key, exTime);
        } catch (Exception e) {
            log.error("set occurred error:key{}, error", key, e);
        } finally {
            if(jedis != null)
                jedis.close();
        }
        return result;
    }


    public static Long del(String key){
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.del(key);
        } catch (Exception e) {
            log.error("set occurred error:key{}, error", key, e);
        } finally {
            if(jedis != null)
                jedis.close();
        }
        return result;
    }
}
