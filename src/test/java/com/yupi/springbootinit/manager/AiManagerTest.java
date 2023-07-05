package com.yupi.springbootinit.manager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Steven
 * @create 2023-07-04-15:39
 */
@SpringBootTest
class AiManagerTest {

    @Resource
    private AiManager aiManager;


    @Test
    void doChat() {
        String ans = aiManager.doChat(132452345324L,"分析需求：\n" +
                "分析网站用户增长情况\n" +
                "原始数据：\n" +
                "日期，用户数\n" +
                "1号，10\n" +
                "2号，20\n" +
                "3号，5");
        System.out.println(ans);
    }
}