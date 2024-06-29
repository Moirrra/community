package com.moirrra.community.dao;

import com.moirrra.community.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserMapper {
    // 根据id查询用户
    @Select("select * from user where id = #{id}")
    User getById(Integer id);

    @Select("select * from user where username = #{username}")
    User getByName(String username);

    @Select("select * from user where email = #{email}")
    User getByEmail(String email);

    // 增加用户
    void insertUser(User user);

    // 修改用户
    void updateUser(User user);

    // 删除用户
    @Delete("delete from user where id = #{id}")
    void deleteUserById(Integer id);
}
