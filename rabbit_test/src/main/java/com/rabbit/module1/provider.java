package com.rabbit.module1;

import com.rabbit.util.RabbitmqConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class provider {

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection conn= RabbitmqConnectionUtil.getConnection();
        Channel channel=conn.createChannel();

        channel.queueDeclare("rabbittest",false,false,false,null);
        String str="rrrrrrfisrtjkgjgkjgjggjkgjgjgjggjgj1111";
        channel.basicPublish("","rabbittest",null,str.getBytes("utf-8"));
        conn.close();
    }
}
