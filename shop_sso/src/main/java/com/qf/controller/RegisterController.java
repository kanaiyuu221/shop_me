package com.qf.controller;

import com.qf.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.concurrent.TimeUnit;


@Controller
@RequestMapping("/reg")
public class RegisterController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private JavaMailSender mailSender;

    @RequestMapping("/toRegister")
    public String reg(){
        return "register";
    }

    @RequestMapping("/register")
    public String register(User user) throws MessagingException {
        System.out.println("接收到的新用户信息"+user);
        redisTemplate.opsForValue().set(user.getUsername(),user,10,TimeUnit.MINUTES);
        //发送激活邮件
        MimeMessage mimeMessage=mailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(mimeMessage);
        helper.setFrom("byesolitia@vip.qq.com");
        //设置发送到...
        helper.setTo(user.getEmail());
        //设置标题
        helper.setSubject("ShopCZ 注册激活邮件！");
        //设置内容
        String text="欢迎注册ShopCZ，请点击<a href='http://localhost:8086/reg/activate?name="+user.getUsername()+"'>这里</a>激活账号，10分钟内有效";
        System.out.println("要发送的内容"+text);
        helper.setText(text,true);
        helper.setSentDate(new Date());
        //发送邮件
        mailSender.send(mimeMessage);

        return "temp";
    }


    @RequestMapping("/activate")
    @ResponseBody
    public String activate(String name){
        System.out.println("接收到的用户名："+name);
        User user= (User) redisTemplate.opsForValue().get(name);
        System.out.println("redis中取出来的user"+user);
        return "激活成功请点击<a href='http://localhost:8082'>这里</a>返回首页";
    }
}
