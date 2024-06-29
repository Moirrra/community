package com.moirrra.community.entity;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {
    private Integer id;

    private String username;

    private String password;

    private String salt;

    private String email;

    /**
     * 用户身份
     * 0-普通用户; 1-超级管理员; 2-版主;
     */
    private Integer type;

    /**
     * 用户状态
     * 0-未激活; 1-已激活;
     */
    private Integer status;

    private String activationCode;

    private String headerUrl;

    private Date createTime;
}
