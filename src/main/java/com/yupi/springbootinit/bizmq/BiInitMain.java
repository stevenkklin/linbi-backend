package com.yupi.springbootinit.bizmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 用于创建测试程序用到的交换机和队列（只用在程序启动前启动一次）
 * @author Steven
 * @create 2023-07-13-9:43
 */
public class BiInitMain {

    public static void main(String[] args) {
        System.out.println("testBiinitMain");
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost("175.178.38.11");
            factory.setUsername("admin");
            factory.setPassword("123456");
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();

            String EXCHANGE_NAME = BiMqConstant.BI_EXCHANGE_NAME;
            channel.exchangeDeclare(EXCHANGE_NAME, "direct");

            // 创建队列，随机分配一个队列名称
            String queueName = BiMqConstant.BI_QUEUE_NAME;
            channel.queueDeclare(queueName, true, false, false, null);
            channel.queueBind(queueName, EXCHANGE_NAME, BiMqConstant.BI_ROUTING_KEY);

        } catch (Exception e) {

        }

    }
}
