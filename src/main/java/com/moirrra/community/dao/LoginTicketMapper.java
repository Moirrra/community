package com.moirrra.community.dao;

import com.moirrra.community.entity.LoginTicket;
import org.apache.ibatis.annotations.*;

/**
 * @Author: Moirrra
 * @CreateTime: 2024-07-02
 * @Description:
 * @Version: 1.0
 */

@Mapper
public interface LoginTicketMapper {

    @Insert("insert into login_ticket(user_id,ticket,status,expired) " +
            "values(#{userId},#{ticket},#{status},#{expired})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id") // 自动生成key 并注入LoginTicket的id
    int insertLoginTicket(LoginTicket loginTicket);

    @Select("select * from login_ticket where ticket = #{ticket}")
    LoginTicket selectByTicket(String ticket);

    @Update("update login_ticket set status = #{status} where ticket = #{ticket}")
    int updateStatus(@Param("ticket") String ticket, @Param("status") int status);
}
