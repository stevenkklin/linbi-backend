package com.yupi.springbootinit.mq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Steven
 * @create 2023-07-12-18:03
 */
public class DlxDirectConsumer {

    private static final String DEAD_EXCHANGE_NAME = "dlx-direct-exchange";

    private static final String WORK_EXCHANGE_NAME = "direct2-exchange";

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("175.178.38.11");
        factory.setUsername("admin");
        factory.setPassword("123456");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(WORK_EXCHANGE_NAME, "direct");

        // 指定死信队列参数
        Map<String, Object> args = new HashMap<>();
        // 要绑到哪个交换机
        args.put("x-dead-letter-exchange", DEAD_EXCHANGE_NAME);
        // 指定死信要转发到哪个死信队列
        args.put("x-dead-letter-routing-key", "waibao");

        // 创建队列
        String queueName = "xiaodog_queue";
        channel.queueDeclare(queueName, true, false, false, args);
        channel.queueBind(queueName, WORK_EXCHANGE_NAME, "xiaodog");

        // 指定死信队列参数
        Map<String, Object> args2 = new HashMap<>();
        // 要绑到哪个交换机
        args2.put("x-dead-letter-exchange", DEAD_EXCHANGE_NAME);
        // 指定死信要转发到哪个死信队列
        args2.put("x-dead-letter-routing-key", "laoban");

        // 创建队列
        String queueName2 = "xiaocyu_queue";
        channel.queueDeclare(queueName2, true, false, false, args2);
        channel.queueBind(queueName2, WORK_EXCHANGE_NAME, "xiaoyu");

        System.out.println("Wating for message. To exit press CTRL+C");

        DeliverCallback xiaodogDeliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            // 拒绝消息
            channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, false);
            System.out.println(" [xiaodog] Received " +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };

        DeliverCallback xiaocatDeliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            // 拒绝消息
            channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, false);
            System.out.println(" [xiaoyu] Received " +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };

        channel.basicConsume(queueName, false, xiaodogDeliverCallback, consumerTag -> {
        });
        channel.basicConsume(queueName2, false, xiaocatDeliverCallback, consumerTag -> {
        });


    }


}
