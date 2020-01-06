package com.atguigu.gmall0715.config;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author sujie
 * @date 2020-01-05-19:32
 */

public class RedisUtil {
    /**
     *  创建JedisPool
     */
    private JedisPool jedisPool;

    /**
     * 初始化连接池
     * @param host
     * @param port
     * @param timeOut
     */
    public void initJedisPool(String host,int port,int timeOut){
        // 创建配置连接池的参数类
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 设置连接池最大核心数
        jedisPoolConfig.setMaxTotal(200);
        // 设置等待时间
        jedisPoolConfig.setMaxWaitMillis(10*1000);
        // 最少剩余数
        jedisPoolConfig.setMinIdle(10);
        // 排队等待
        jedisPoolConfig.setBlockWhenExhausted(true);
        // 设置当用户获取到jedis 时，做自检看当前获取到的jedis 是否可以使用！
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPool = new JedisPool(jedisPoolConfig,host,port,timeOut);

    }

    /**
     * 获取jedis
     * @return
     */
    public Jedis getJedis(){
       return jedisPool.getResource();
    }

}
