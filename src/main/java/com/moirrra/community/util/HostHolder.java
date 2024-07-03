package com.moirrra.community.util;

import com.moirrra.community.entity.User;
import org.springframework.stereotype.Component;

/**
 * @Author: Moirrra
 * @CreateTime: 2024-07-02
 * @Description: 持有用户信息，用于代替session对象
 * @Version: 1.0
 */
@Component
public class HostHolder {

    private ThreadLocal<User> users = new ThreadLocal<User>();

    public void setUser(User user) {
        users.set(user);
    }

    public User getUser() {
        return users.get();
    }

    public void clear() {
        users.remove();
    }
}
