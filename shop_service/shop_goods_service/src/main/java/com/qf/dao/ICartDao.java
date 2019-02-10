package com.qf.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.qf.entity.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ICartDao extends BaseMapper<Cart> {
    /**
     * 根据用户id查询用户购物车
     * @param uid
     * @return
     */
    List<Cart> queryCartByUserId(Integer uid);

    List<Cart> queryCartsByIds(@Param("cids") Integer[] cids);
}
