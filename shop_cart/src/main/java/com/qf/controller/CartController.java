package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.gson.Gson;
import com.qf.entity.Cart;
import com.qf.entity.User;
import com.qf.service.ICartService;
import com.qf.util.Islogin;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Reference
    private ICartService cartService;

    @Islogin
    @RequestMapping("/add")
    public String addGoodToCart(@CookieValue(value = "cart_token",required = false) String cartToken, Cart cart, User user, HttpServletResponse response){
        System.out.println("收到购物车添加商品信息："+cart.getGid()+cart.getGnumber());
        System.out.println("接到的用户/是否登陆" + user);
        System.out.println("获取到的购物车信息"+cart);
        String cToken = cartService.addCart(cartToken, cart, user);
        if (cartToken ==null){
            Cookie cookie=new Cookie("cart_token",cToken);
            cookie.setMaxAge(60*60*24);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
        return "succ";
    }


    @RequestMapping("/showlist")
    @Islogin
    @ResponseBody
    public String showlist(@CookieValue(value = "cart_token",required = false)String cartToken,User user){
        List<Cart> list = cartService.queryCartList(cartToken, user);

        return "cartlist("+ new Gson().toJson(list)+")";
    }

    @RequestMapping("/cartlist")
    @Islogin
    public String cartlist(@CookieValue(value = "cart_token",required = false)String cartToken, User user, Model model){
        List<Cart> list = cartService.queryCartList(cartToken, user);
        model.addAttribute("carts",list);
        return "cartlist";
    }
}
