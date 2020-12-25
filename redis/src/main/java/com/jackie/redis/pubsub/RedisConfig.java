package com.jackie.redis.pubsub;

import com.jackie.redis.util.RedisClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * redis消息监听者容器
 *
 * 这个配置类的作用是要注入ServerEndpointExporter,这个bean会自动注册使用了@ServerEndpoint注解声明的Websoket endpoint.
 * 如果是使用独立的servlet容器，而不是直接使用springboot的内置容器，就不要注入ServerEndpointExporter,因为他将由容器自己提供和管理。
 *
 * 这里使用springboot的内置tomcat容器，所以还是要创建这个配置类，作用就是注入ServerEndpointExporter
 */
@Configuration /*相当于xml中的beans*/
public class RedisConfig {
    public static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);

    /**
     * 需要手动注册RedisMesssageListenerContainer加入IOC容器
     * @param connectionFactory
     * @return
     */
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory){
        logger.info("开始RedisMessageListenerContainer初始化");
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        logger.info("结束RedisMessageListenerContainer初始化，返回");
        WebSocketServer.redisMessageListenerContainer=container;
        return container;
    }

    @Bean
    public RedisClient redisCache(StringRedisTemplate stringRedisTemplate){
        RedisClient redisClient = new RedisClient();
        redisClient.setRedisTemplate(stringRedisTemplate);
        return redisClient;
    }
}
