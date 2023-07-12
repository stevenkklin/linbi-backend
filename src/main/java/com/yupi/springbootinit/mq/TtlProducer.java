package com.yupi.springbootinit.mq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;

public class TtlProducer {

    private final static String QUEUE_NAME = "ttl_queue";

    public static void main(String[] argv) throws Exception {
        // 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("175.178.38.11");
        factory.setUsername("admin");
        factory.setPassword("123456");
        // 建立连接、创建频道
        try (Connection connection = factory.newConnection();
             // 创建一个频道（相对于客户端）
             Channel channel = connection.createChannel()) {

            // 发送消息
            String message = "Hello World!";

            byte[] messageBodyBytes = "Hello, world!".getBytes();
            AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                    .expiration("60000")
                    .build();
            channel.basicPublish(QUEUE_NAME, null, properties, message.getBytes(StandardCharsets.UTF_8));

            System.out.println(" [x] Sent '" + message + "'");
        }
    }
}