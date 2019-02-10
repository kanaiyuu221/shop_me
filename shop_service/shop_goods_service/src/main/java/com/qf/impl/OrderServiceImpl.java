package com.qf.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.dao.IAddressDao;
import com.qf.dao.ICartDao;
import com.qf.dao.IOrderDao;
import com.qf.dao.IOrderDetilsDao;
import com.qf.entity.*;
import com.qf.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private IAddressDao addressDao;

    @Autowired
    private ICartDao cartDao;

    @Autowired
    private IOrderDao orderDao;

    @Autowired
    private IOrderDetilsDao orderDetilsDao;

    @Override
    @Transactional
    public int addOrder(Integer aid, Integer[] cids, User user) {
        //根据地址id查询地址
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("id",aid);
        Address address=addressDao.selectOne(queryWrapper);

        //根据购物车id查询购物车
        List<Cart> clist = cartDao.queryCartsByIds(cids);

        double allPrice=0;
        for (Cart cart : clist) {
            allPrice += cart.getGnumber()*cart.getGoods().getPrice();
        }

        //生成订单
        Orders orders=new Orders();
        orders.setOrderid(UUID.randomUUID().toString());
        orders.setPerson(address.getPerson());
        orders.setAddress(address.getAddress());
        orders.setCode(address.getCode());
        orders.setOrdertime(new Date());
        orders.setPhone(address.getPhone());
        orders.setOprice(allPrice);
        orders.setStatus(0);
        orders.setUid(user.getId());

        orderDao.insert(orders);

        //根据购物车生成订单详情
        for (Cart cart : clist) {
            OrderDetils orderDetils=new OrderDetils();
            orderDetils.setOid(orders.getId());
            orderDetils.setGid(cart.getGid());
            orderDetils.setGname(cart.getGoods().getTitle());
            orderDetils.setGinfo(cart.getGoods().getGinfo());
            orderDetils.setPrice(cart.getGoods().getPrice());
            orderDetils.setGimage(cart.getGoods().getGimage());
            //
            orderDetilsDao.insert(orderDetils);
        }
        //删除购物车
        cartDao.deleteBatchIds(Arrays.asList(cids));


        return 1;
    }

    /**
     * 查询用户订单
     * @param uid
     * @return
     */
    @Override
    public List<Orders> queryByUid(Integer uid) {
        //查询用户 所有订单列表
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.eq("uid",uid);
        List<Orders> orders = orderDao.selectList(queryWrapper);
        //查询详细订单新信息
        for (Orders order : orders) {
            QueryWrapper queryWrapper1=new QueryWrapper();
            queryWrapper1.eq("oid",order.getId());
            List<OrderDetils> list = orderDetilsDao.selectList(queryWrapper1);
            order.setOrderDetils(list);
        }

        return orders;
    }

    /**
     * 后台查询所有订单
     * @param uid
     * @return
     */
    @Override
    public List<Orders> queryAllOrder() {
        List<Orders> orders = orderDao.selectList(null);
        return orders;
    }
}
