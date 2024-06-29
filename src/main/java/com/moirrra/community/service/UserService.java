package com.moirrra.community.service;

import com.moirrra.community.entity.User;
import org.springframework.stereotype.Service;


public interface UserService {
    User findUserById(Integer id);
}
