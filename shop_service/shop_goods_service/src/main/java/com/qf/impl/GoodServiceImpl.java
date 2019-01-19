package com.qf.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.qf.dao.IGoodsDao;
import com.qf.entity.Goods;
import com.qf.service.IGoodsService;
import com.qf.service.ISearchService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


@Service
public class GoodServiceImpl implements IGoodsService {

    @Autowired
    private IGoodsDao goodsDao;

    @Reference
    private ISearchService searchService;

    @Override
    public List<Goods> queryAll() {
        List<Goods> goods = goodsDao.selectList(null);
        return goods;
    }

    @Override
    public Goods insert(Goods goods) {
        System.out.println("页面传过来的未添加商品："+goods);
        //数据库
        goodsDao.insert(goods);
        System.out.println("添加以后的商品："+goods);
        //索引库
        searchService.insertIndexed(goods);
        //

        return goods;
    }


}
