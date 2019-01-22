package com.qf.listener;

import com.qf.entity.Goods;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

@Component
@RabbitListener(queues = "good_queue")
public class MyRabbitmqListener {

    @Autowired
    private Configuration configuration;

    @RabbitHandler
    public void handlerMsg(Goods goods){
        System.out.println("消息队列中接收到的消息>>>> "+goods);

        Map<String,Object> map=new HashMap<>();
        map.put("goods",goods);

        String path=this.getClass().getResource("/static/Page/").getPath()+goods.getId()+".html";
        System.out.println("path>>>> "+path);

        try (Writer writer=new FileWriter(path);){
            Template template=configuration.getTemplate("goods.ftl");
            template.process(map,writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
