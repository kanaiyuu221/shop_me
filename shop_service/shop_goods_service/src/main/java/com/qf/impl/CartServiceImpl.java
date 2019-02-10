package com.qf.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qf.dao.ICartDao;
import com.qf.dao.IGoodsDao;
import com.qf.entity.Cart;
import com.qf.entity.Goods;
import com.qf.entity.User;
import com.qf.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.UUID;

@Service
public class CartServiceImpl implements ICartService {

    @Autowired
    private ICartDao cartDao;

    @Autowired
    private IGoodsDao goodsDao;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public String addCart(String cartToken, Cart cart, User user) {

        //是否登陆
        if (user != null){
            cart.setUid(user.getId());
            cartDao.insert(cart);
        }else {
            if (cartToken == null){
                cartToken= UUID.randomUUID().toString();
            }
            //将购物车物品信息添加进临时购物车
            redisTemplate.opsForList().leftPush(cartToken,cart);
        }
        return cartToken;
    }

    /**
     * 登陆后合并购物车与临时购物车
     * @param cartToken
     * @param user
     * @return
     */
    @Override
    public int mergeCart(String cartToken, User user) {
        if (cartToken != null && redisTemplate.opsForList().size(cartToken) > 0){
            List<Cart> list = redisTemplate.opsForList().range(cartToken, 0, redisTemplate.opsForList().size(cartToken));
            for (Cart cart : list) {
                cart.setUid(user.getId());
                //插入数据库
                cartDao.insert(cart);
            }
            redisTemplate.delete(cartToken);
            return 1;
        }

        return 0;
    }

    @Override
    public List<Cart> queryCartList(String cartToken, User user) {

        List<Cart> list=null;
        if (user != null){
            //已登陆 mysql
            list = cartDao.queryCartByUserId(user.getId());
        }else {
            //未登录 redis
            if (cartToken!=null && redisTemplate.opsForList().size(cartToken)>0){
                list = redisTemplate.opsForList().range(cartToken,0,redisTemplate.opsForList().size(cartToken));
                //根据购物车信息查询购物车商品详情
                for (Cart cart : list) {
                    QueryWrapper wrapper=new QueryWrapper();
                    wrapper.eq("id",cart.getGid());
                    Goods goods = goodsDao.selectOne(wrapper);
                    cart.setGoods(goods);
                }
            }
        }

        return list;
    }
}
