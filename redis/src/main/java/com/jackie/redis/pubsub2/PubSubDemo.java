package com.jackie.redis.pubsub2;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class PubSubDemo {
    public static void main( String[] args )
    {
        // 连接redis服务端
        JedisPool jedisPool = new JedisPool(new JedisPoolConfig(), "192.168.2.101", 6381);

        System.out.println(String.format("redis pool is starting, redis ip %s, redis port %d", "192.168.2.101", 6381));

        Publisher publisher = new Publisher(jedisPool);    //发布者
        publisher.start();

        SubThread subThread = new SubThread(jedisPool);  //订阅者
        subThread.start();
    }
}
