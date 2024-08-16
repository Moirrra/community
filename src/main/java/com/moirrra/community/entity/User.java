package com.moirrra.community.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties({"enabled","accountNonExpired", "accountNonLocked", "credentialsNonExpired", "authorities"})
public class User implements UserDetails {
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

    // true: 账号没有过期
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // true: 账号未锁定
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // true: 凭证未过期
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // true: 账号可用
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<>();
        list.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                switch (type) {
                    case 1:
                        return "ADMIN";
                    default:
                        return "USER";
                }
            }
        });
        return null;
    }
}
