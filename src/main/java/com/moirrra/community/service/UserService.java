package com.moirrra.community.service;

import com.moirrra.community.entity.LoginTicket;
import com.moirrra.community.entity.User;
import org.springframework.stereotype.Service;

import java.util.Map;


public interface UserService {
    User findUserById(Integer id);

    Map<String, Object> register(User user);

    int activate(int userId, String code);

    Map<String, Object> login(String username, String password, int expiredSeconds);

    void logout(String ticket);

    LoginTicket getLoginTicket(String ticket);
}


