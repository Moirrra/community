package com.moirrra.community.mapper;

import com.moirrra.community.dao.UserMapper;
import com.moirrra.community.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
public class UserMapperTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    void testSelectUser() {
        User user = userMapper.getById(101);
        User user1 = userMapper.getByName("aaa");
        User user2 = userMapper.getByEmail("nowcoder117@sina.com");
        System.out.println(user);
        System.out.println(user1);
        System.out.println(user2);
    }

    @Test
    public void testInsertUser(){
        User user = new User();
        user.setUsername("test2");
        user.setPassword("1234567");
        user.setSalt("abcd");
        user.setEmail("test2@qq.com");
        user.setHeaderUrl("http://www.nowcoder.com/101.png");
        user.setCreateTime(new Date());
        userMapper.insertUser(user);
    }

    @Test
    public void testUpdateUser(){
        User user = new User();
        user.setId(151);
        user.setSalt("abcde");
        userMapper.updateUser(user);
    }

}
