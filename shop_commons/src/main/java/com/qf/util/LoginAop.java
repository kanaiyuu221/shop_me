package com.qf.util;

import com.qf.entity.User;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;

@Aspect
public class LoginAop {

    @Autowired
    private RedisTemplate redisTemplate;

    @Around("@annotation(Islogin)")
    public Object isLogin(ProceedingJoinPoint proceedingJoinPoint){

        //获取request
        ServletRequestAttributes requestAttributes= (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request=requestAttributes.getRequest();
        String token=null;
        Cookie[] cookies=request.getCookies();
        if (cookies != null){
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("login_token")){
                    token=cookie.getValue();
                    break;
                }
            }
        }

        System.out.println("通过cookie获取到的token");
        User user=null;

        if (token!=null){
            user= (User) redisTemplate.opsForValue().get(token);
            System.out.println("token不为空时redis取得的user："+user);
        }
        if (user == null){
            //未登录的情况 强制返回登陆页面
            MethodSignature methodSignature= (MethodSignature) proceedingJoinPoint.getSignature();
            Method method=methodSignature.getMethod();
            Islogin is=method.getAnnotation(Islogin.class);
            boolean flag=is.tologin();
            if (flag){
                String returnUrl=request.getRequestURL()+"?"+request.getQueryString();

                try {
                    returnUrl= URLEncoder.encode(returnUrl,"utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                return "redirect:http://localhost:8086/sso/tologin?returnUrl=" + returnUrl;
            }
        }

        //运行中间方法
        Object[] args=proceedingJoinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            if (args[i] != null && args[i].getClass()==User.class){
                args[i]=user;
            }
        }

        Object result=null;

        try {
            result=proceedingJoinPoint.proceed(args);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return result;
    }
}
