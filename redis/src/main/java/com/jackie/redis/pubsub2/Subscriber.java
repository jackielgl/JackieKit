package com.jackie.redis.pubsub2;

import redis.clients.jedis.JedisPubSub;

/**
 * 订阅者
 *
 */
public class Subscriber extends JedisPubSub {
    @Override
    public void onMessage(String channel, String message) { // 收到消息调用
        //super.onMessage(channel, message);
        System.out.println(String.format("receive redis published message, channel %s, message %s", channel, message));
    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) { // 订阅了频道调用
        //super.onSubscribe(channel, subscribedChannels);
        System.out.println(String.format("subscribe redis channel success, channel %s, subscribedChannels %d",
                channel, subscribedChannels));
    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) { // 取消订阅调用
        //super.onUnsubscribe(channel, subscribedChannels);
        System.out.println(String.format("unsubscribe redis channel, channel %s, subscribedChannels %d",
                channel, subscribedChannels));

    }
}
