package com.jackie.redis.pubsub;

import com.jackie.redis.util.RedisClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo")
public class Pubcontroller {
    public static final Logger logger =  LoggerFactory.getLogger(Pubcontroller.class);
    @Autowired
    RedisClient redisClient;

    @RequestMapping("/pub")
    public String pub(@RequestParam("devid") String devid){

          logger.info("devid=" + devid);
          redisClient.pub(devid, "message test");
          redisClient.setHash(devid,"11111", "message test" );

          return "ok";
    }

}
