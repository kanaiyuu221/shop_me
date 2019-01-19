package com.rabbit.util;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 获取唯一rabbitmq connection 工具类
 */
public class RabbitmqConnectionUtil {

    private static ConnectionFactory connectionFactory;

    /**
     * 建立连接
     */
    static {
        connectionFactory=new ConnectionFactory();
        connectionFactory.setHost("10.211.55.5");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        connectionFactory.setVirtualHost("/admin");

    }

    /**
     * 获取connection
     * @return
     */
    public static Connection getConnection(){
        Connection connection=null;
        try {
            connection=connectionFactory.newConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        return connection;
    }

}
