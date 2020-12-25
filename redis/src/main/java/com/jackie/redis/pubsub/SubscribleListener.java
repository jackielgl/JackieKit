package com.jackie.redis.pubsub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import javax.websocket.Session;
import java.io.IOException;

/**
 * 创建消息订阅监听者类
 *
 * 这个消息订阅监听者类持有websocket的客户端会话对象（session)。当接收到订阅的消息时，通过这个会话对象
 * （sesson)将发送到前端，从而实现消息的主动推送。
 */
public class SubscribleListener implements MessageListener {
    public static final Logger logger = LoggerFactory.getLogger(SubscribleListener.class);

    private Session session; // websocket的客户端连接绘画对象
    public Session getSession(){
        return session;
    }
    public void setSession(Session session){
        this.session = session;
    }

    /**
     * 接收发布者的消息
     * @param message
     * @param bytes
     */
    @Override
    public void onMessage(Message message, byte[] bytes) {
        String msg = new String(message.getBody());
        logger.info(new String(bytes) + "主题发布：" + msg);
        if(null != session && session.isOpen()){
            try {
                session.getBasicRemote().sendText(msg);
            }catch (IOException e){
                logger.error(e.getMessage(), e);
            }
        }
    }
}
