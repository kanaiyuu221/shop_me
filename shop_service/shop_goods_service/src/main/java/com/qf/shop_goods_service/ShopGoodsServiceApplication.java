package com.qf.shop_goods_service;

import com.alibaba.dubbo.config.spring.context.annotation.DubboComponentScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = "com.qf")
@DubboComponentScan("com.qf.impl")
@MapperScan("com.qf.dao")
@EnableTransactionManagement
public class ShopGoodsServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShopGoodsServiceApplication.class, args);
    }


    /**
     * 创建队列
     * @return
     */
    @Bean
    public Queue getQueue(){
        return new Queue("good_queue");
    }

    /**
     * 创建交换机
     * @return
     */
    @Bean
    public FanoutExchange getExchange(){
        return new FanoutExchange("good_exchange");
    }

    /**
     * 绑定交换机和队列
     */
    @Bean
    public Binding bingExchangeAndQueue(Queue getQueue,FanoutExchange getExchange){
        return BindingBuilder.bind(getQueue).to(getExchange);
    }
}

