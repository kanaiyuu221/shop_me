package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.entity.Address;
import com.qf.entity.Cart;
import com.qf.entity.Orders;
import com.qf.entity.User;
import com.qf.service.IAddressService;
import com.qf.service.ICartService;
import com.qf.service.IOrderService;
import com.qf.util.Islogin;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Reference
    private IAddressService addressService;

    @Reference
    private ICartService cartService;

    @Reference
    private IOrderService orderService;

    @RequestMapping("/edit")
    @Islogin(tologin = true)
    public String edit(Integer[] gid, User user, Model model){
        System.out.println("收到的购物车数组"+gid);
        //查询用户地址
        List<Address> addresses = addressService.queryAddressByUid(user.getId());
        model.addAttribute("addresses",addresses);
        //查询购物车
        List<Cart> carts = cartService.queryCartList(null, user);//所有购物车
        List<Cart> carts2=new ArrayList<>();//选中付款的 新购物车
        for (Cart cart : carts) {
            for (Integer integer : gid) {
                if (cart.getGid()==integer){
                    carts2.add(cart);
                }
            }
        }
        model.addAttribute("carts",carts2);
        return "orderedit";
    }

    @RequestMapping("/insert")
    @ResponseBody
    @Islogin
    public int insertOrder(Integer aid,@RequestParam("cids[]") Integer[] cids,User user){
        int i = orderService.addOrder(aid, cids,user);
        return i;
    }

    @RequestMapping("/showlist")
    @Islogin(tologin = true)
    public String showlist(User user,Model model){
        List<Orders> orders = orderService.queryByUid(user.getId());
        System.out.println("查询到的订单"+orders);
        model.addAttribute("orders",orders);
        return "orderlist";
    }
}
