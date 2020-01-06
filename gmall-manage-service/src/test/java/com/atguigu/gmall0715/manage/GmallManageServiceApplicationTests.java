package com.atguigu.gmall0715.manage;

import com.atguigu.gmall0715.config.RedisUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import redis.clients.jedis.Jedis;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmallManageServiceApplicationTests {
    @Autowired
    private RedisUtil redisUtil;
    @Test
    public void contextLoads() {
    }

    /**
     * 测试redis引入成功
     */
    @Test
    public void redisTest(){
        Jedis jedis = redisUtil.getJedis();
        jedis.set("spring", "testRedis");
    }

}
