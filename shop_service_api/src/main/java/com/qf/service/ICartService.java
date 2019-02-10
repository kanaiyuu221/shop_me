package com.qf.service;

import com.qf.entity.Cart;
import com.qf.entity.User;

import java.util.List;

public interface ICartService {

    String addCart(String cartToken, Cart cart, User user);

    int mergeCart(String cartToken,User user);

    List<Cart> queryCartList(String cartToken,User user);
}
