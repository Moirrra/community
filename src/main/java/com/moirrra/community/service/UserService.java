package com.moirrra.community.service;

import com.moirrra.community.entity.LoginTicket;
import com.moirrra.community.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;


public interface UserService {
    User findUserById(Integer id);

    User findUserByName(String username);

    Map<String, Object> register(User user);

    int activate(int userId, String code);

    Map<String, Object> login(String username, String password, int expiredSeconds);

    void logout(String ticket);

    LoginTicket getLoginTicket(String ticket);

    // 更新头像
    void updateHeader(int userId, String headerUrl);

    // 修改密码
    Map<String, Object> updatePassword(int userId, String oldPassword, String newPassword);

    Collection<? extends GrantedAuthority> getAuthorities(int userId);
}


