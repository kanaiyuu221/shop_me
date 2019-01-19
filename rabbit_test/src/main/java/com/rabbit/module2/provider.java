package com.rabbit.module2;

import com.rabbit.util.RabbitmqConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;

public class provider {
    public static void main(String[] args) throws IOException {
        Connection conn= RabbitmqConnectionUtil.getConnection();
        Channel channel=conn.createChannel();

        channel.queueDeclare("rbqt2",false,false,false,null);

        for (int i = 0; i < 14; i++) {
            String str="rbqt2ttesxt>>> "+i;
            channel.basicPublish("","rbqt2",null,str.getBytes("utf-8"));
        }
        conn.close();
    }

}
