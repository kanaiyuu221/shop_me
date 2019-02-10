package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.entity.Orders;
import com.qf.service.IOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/backOrder")
public class BackOrderController {

    @Reference
    private IOrderService orderService;

    @RequestMapping("/backOrderList")
    public String showBackOrderList(Model model){
        List<Orders> orders = orderService.queryAllOrder();
        System.out.println("后台查询到的订单表:>>>"+orders);
        model.addAttribute("borderlist",orders);
        return "backorderlist";
    }
}
