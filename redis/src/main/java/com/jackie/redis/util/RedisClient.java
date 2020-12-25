package com.jackie.redis.util;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RedisClient {

    private StringRedisTemplate redisTemplate;

    public void setRedisTemplate(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public RedisTemplate<String, String> getRedisTemplate() {
        return redisTemplate;
    }

    public void set(String key, String val) throws Exception {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set(key, val);
    }

    public Boolean set(String key, String val, long expireSecond) throws Exception {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set(key, val);
        return redisTemplate.expire(key, expireSecond, TimeUnit.SECONDS);
    }

    public String get(String key) throws Exception {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        return ops.get(key);
    }

    public Boolean exists(String key) throws Exception {

        return redisTemplate.hasKey(key);
    }

    /**
     * 设置hash
     * {bigK:[{k:v},{k:v}]}
     *
     * @param key 最外层k
     * @param field    内层k
     * @param v    内层v
     */
    public void setHash(String key, String field, String v) {
        redisTemplate.opsForHash().put(key, field, v);
    }

    /**
     * 获取hash
     *
     * @param key
     * @return
     */
    public Map<String, String> getHash(String key) {
        return redisTemplate.<String, String>opsForHash().entries(key);
    }

    public long delHash(String key, String field){
        return redisTemplate.<String, String>opsForHash().delete(key, field);
    }

    public void pub(String topic, String message){
        redisTemplate.convertAndSend(topic, message);
    }

}
