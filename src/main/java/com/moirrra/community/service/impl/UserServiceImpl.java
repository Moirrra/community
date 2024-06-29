package com.moirrra.community.service.impl;

import com.moirrra.community.dao.UserMapper;
import com.moirrra.community.entity.User;
import com.moirrra.community.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public User findUserById(Integer id) {
        return userMapper.getById(id);
    }
}
