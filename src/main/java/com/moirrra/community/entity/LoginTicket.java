package com.moirrra.community.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * @Author: Moirrra
 * @CreateTime: 2024-07-02
 * @Description: 登录凭证
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginTicket {

    private Integer id;

    private Integer userId;

    private String ticket;

    private Integer status;

    private Date expired;
}
