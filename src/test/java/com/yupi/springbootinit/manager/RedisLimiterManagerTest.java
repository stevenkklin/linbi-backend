package com.yupi.springbootinit.manager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Steven
 * @create 2023-07-06-19:39
 */
@SpringBootTest
class RedisLimiterManagerTest {

    @Resource
    RedisLimiterManager redisLimiterManager;

    @Test
    void doRateLimit() throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            redisLimiterManager.doRateLimit("1");
            System.out.println("成功");
        }
        Thread.sleep(1000);
        for (int i = 0; i < 7; i++) {
            redisLimiterManager.doRateLimit("1");
            System.out.println("成功");
        }
    }
}