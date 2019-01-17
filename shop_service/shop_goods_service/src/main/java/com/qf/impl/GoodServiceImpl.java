package com.qf.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.qf.dao.IGoodsDao;
import com.qf.entity.Goods;
import com.qf.service.IGoodsService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Service
public class GoodServiceImpl implements IGoodsService {

    @Autowired
    private IGoodsDao goodsDao;

    @Override
    public List<Goods> queryAll() {
        List<Goods> goods = goodsDao.selectList(null);
        return goods;
    }

    @Override
    public Goods insert(Goods goods) {
        goodsDao.insert(goods);
        return goods;
    }
}
