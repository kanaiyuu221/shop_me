package com.qf.service;

import com.qf.entity.User;

public interface IUserService {
    User queryByUsernameAndPassword(String username,String password);
}
