package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.gson.Gson;
import com.qf.entity.User;
import com.qf.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Controller
@RequestMapping("/sso")
public class LoginController {

    @Autowired
    private RedisTemplate redisTemplate;

    @Reference
    private IUserService userService;

    @RequestMapping("/tologin")
    public String toLogin(String returnUrl,Model model){
        model.addAttribute("returnUrl",returnUrl);
        return "login";
    }

    @RequestMapping("/login")
    public String login(String username, String password, Model model,HttpServletResponse response,String returnUrl){
        System.out.println("接收到的："+username+" "+password);
        model.addAttribute("username",username);
        model.addAttribute("password",password);
        User user=userService.queryByUsernameAndPassword(username,password);
        System.out.println("查询到的user"+user);
        if (user!=null){

            if (returnUrl==null||"".equals(returnUrl)){
                returnUrl="http://localhost:8082";
            }

            String uuid= UUID.randomUUID().toString();
            redisTemplate.opsForValue().set(uuid,user);
            Cookie cookie=new Cookie("login_token",uuid);
            cookie.setMaxAge(60*60);
            cookie.setPath("/");
            response.addCookie(cookie);
            return "redirect:"+returnUrl;

        }
        model.addAttribute("error","用户名或密码错误～～～！！！  ");
        return "temp";
    }

    @RequestMapping("islogin")
    @ResponseBody
    public String isLogin(@CookieValue(value = "login_token",required = false)String token){
        System.out.println("获取的用户cookie>>> "+token);
        User user=null;

        if (token!=null){
            user= (User) redisTemplate.opsForValue().get(token);
        }
        return user!=null?"islogin("+new Gson().toJson(user)+")":"islogin(null)";
    }

    @RequestMapping("logout")
    public String loginout(@CookieValue(value = "login_token",required = false)String token,HttpServletResponse response){
        redisTemplate.delete(token);
        Cookie cookie=new Cookie("login_token",null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "login";
    }
}
