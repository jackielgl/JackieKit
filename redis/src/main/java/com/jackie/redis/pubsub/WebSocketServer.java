package com.jackie.redis.pubsub;

import com.jackie.redis.util.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 创建Websocket服务端类
 *
 * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端。
 * 注解的值将被用于监听用户连接的终端访问url地址，客户端可以通过这个url来连接到websocket服务器端。
 * 使用springboot的唯一区别是要@Componet声明下，而使用独立容器是由容器自己管理websocket的，但在
 * springboot中连容器都是spring管理的。
 *
 * 虽然@Component默认是单例模式的，但是springboot还是会为每个websocket连接初始化一个bean，所以
 * 可以使用一个静态的set保存起来。
 *
 *
 * 注意的是在客户端链接关闭的方法onClose中，一定要 删除之前的订阅监听对象，就是下面这行代码：
 * redisMessageListenerContainer.removeMessageListener(subscribeListener);否则在浏览器刷一下之后，后台会报错：
 * java.lang.IllegalStateException: The WebSocket session [0] has been closed and no method (apart from close()) may be called on a closed session
 *
 * 原因就是当链接关闭之后，session对象就没有了，而订阅者对象还是会接收消息，在用session对象发送消息时会报错。
 * 虽然代码中加了判断if (null != session && session.isOpen()) {  可以避免报错，但是为了防止内存泄漏，应该把没有用的监听者对象从容器中删除。
 *
 */
@Component
@ServerEndpoint("/websocket/server/{sid}")
public class WebSocketServer {
    public static final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);
    /**
     * 因为@ServerEndpoint不支持注入，所以使用SpringUtils获取IOC实例
     */

    public static RedisMessageListenerContainer redisMessageListenerContainer ;//= (RedisMessageListenerContainer)SpringUtil.getBean("redisMessageListenerContainer");  //从容器获得



    //静态变量，用来记录当前在线的连接数，应该把它设计成线程安全的
    private static AtomicInteger onlineCount = new AtomicInteger(0);

    // concurrent包的先去安全set，用来存放每个客户端对应的webSoket对象，若要实现服务端与单一客户端通信的话，可以使用map来存放
    // 其中key可以为用户标识
    private static CopyOnWriteArraySet<WebSocketServer> webSocketSet = new CopyOnWriteArraySet<WebSocketServer>();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    private SubscribleListener subscribleListener;



    @OnOpen
    public void onOpen(Session session){
        this.session = session;
        webSocketSet.add(this); // 加入set中
        addOnlineCount(); // 在线数加1
        logger.info("有新连接加入！当前在线人数为：" + getOnlineCount()  );
        subscribleListener = new SubscribleListener();
        subscribleListener.setSession(session);

        // 设置订阅topic
        String queryString = session.getQueryString();
        logger.info("queryString=" + queryString);

        if(queryString!=null && queryString.contains("=")){
            String[] devids = queryString.split("=");
            if(devids!= null && devids.length>1){
                String devId = devids[1];
                List<String> devList = devId==null?null: Arrays.asList(devId.split(","));
               for (String id: devList){
                   redisMessageListenerContainer.addMessageListener(subscribleListener, new ChannelTopic(id)); // 订阅topic为设备id
               }
            }
        }
        //redisMessageListenerContainer.addMessageListener(subscribleListener, new ChannelTopic("TOPIC"));

    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(){
        webSocketSet.remove(this); // 从set中删除
        subOnlineCount(); // 在线数减1
        redisMessageListenerContainer.removeMessageListener(subscribleListener);
        logger.info("有一个连接关闭，当前在线人数为" + getOnlineCount());

    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送来的消息
     * @param session 可选参考
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        logger.info("来自客户端的消息：" + message);
/*        // 群发消息
        for(WebSocketServer item : webSocketSet){
            try {
                if(false) {// 如果是取消消息，并且是这个人审核的将不发送。
                }
                item.sendMessage(message);
            }catch (IOException e){
                logger.error(e.getMessage(), e);
                continue;

            }
        }*/
    }

    /**
     * 发生错误时调用
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error){
        logger.info("发生错误", error);
    }

    /**
     * 这个方法是根据自己需要添加的方法
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException {
        logger.info("开始发送：" + message);
        this.session.getBasicRemote().sendText(message);
    }

    public int getOnlineCount(){
        return onlineCount.get();
    }
    public void addOnlineCount(){
        WebSocketServer.onlineCount.getAndIncrement();
    }
    public void subOnlineCount(){
        WebSocketServer.onlineCount.getAndDecrement();
    }
}
