package com.rabbit.module2;

import com.rabbit.util.RabbitmqConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;

public class consumer {
    public static void main(String[] args) throws IOException {
        Connection conn= RabbitmqConnectionUtil.getConnection();
        Channel channel=conn.createChannel();

        channel.queueDeclare("rbqt2",false,false,false,null);

        channel.basicConsume("rbqt2",true,new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("消费者2>>>> "+new String(body,"utf-8"));
            }
        });
    }
}
