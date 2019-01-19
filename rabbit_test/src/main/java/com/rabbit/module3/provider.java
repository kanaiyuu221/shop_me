package com.rabbit.module3;

import com.rabbit.util.RabbitmqConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;

public class provider {
    public static void main(String[] args) throws IOException {
        Connection conn= RabbitmqConnectionUtil.getConnection();
        Channel channel=conn.createChannel();

        channel.exchangeDeclare("rbqexchange1","fanout");

        channel.queueDeclare("rbqt33",false,false,false,null);
        channel.queueDeclare("rbqt4",false,false,false,null);

        channel.queueBind("rbqt33","rbqexchange1","");
        channel.queueBind("rbqt4","rbqexchange1","");

        for (int i = 0; i < 10; i++) {
            String str="rbrrbrbrbaaqqqqtessxten>>>>"+i;
            channel.basicPublish("rbqexchange1","",null,str.getBytes("utf-8"));
        }
        conn.close();


    }
}
