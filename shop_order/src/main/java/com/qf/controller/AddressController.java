package com.qf.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.qf.entity.Address;
import com.qf.entity.User;
import com.qf.service.IAddressService;
import com.qf.util.Islogin;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/addr")
public class AddressController {

    @Reference
    private IAddressService addressService;

    @RequestMapping("/insert")
    @ResponseBody
    @Islogin
    public String insertAddress(Address address, User user){
        address.setUid(user.getId());
        System.out.println("收到的地址新"+address);
        addressService.insertAddress(address);

        return null;
    }
}
