package com.atguigu.gmall0715.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author sujie
 * @date 2020-01-05-19:55
 */
@Configuration
public class RedisConfig {
    private static final String HOST_STATUS = "disabled";
    /**
     *  :disabled 表示如果配置文件中没有获取到host ，则表示默认值disabled
     */
    @Value("${spring.redis.host:disabled}")
    private String host;
    @Value("${spring.redis.port:0}")
    private int port;
    @Value("${spring.redis.timeOut:10000}")
    private int timeOut;

    @Bean
    public RedisUtil getRedisUtil(){
        //判断host是否可用
        if(HOST_STATUS.equals(host)){
            return null;
        }
        RedisUtil redisUtil = new RedisUtil();
        redisUtil.initJedisPool(host, port, timeOut);
        return redisUtil;
    }

    }
