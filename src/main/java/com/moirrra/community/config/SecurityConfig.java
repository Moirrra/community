package com.moirrra.community.config;

import com.moirrra.community.util.CommunityConstant;
import com.moirrra.community.util.CommunityUtil;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer;
import org.springframework.security.config.ldap.LdapBindAuthenticationManagerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Author: Moirrra
 * @CreateTime: 2024-08-12
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * 静态资源不做认证
     *
     * @return
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/resources/**");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // 授权
        http.authorizeHttpRequests((authorizeHttpRequests) ->
                        authorizeHttpRequests
                                .antMatchers(
                                        "/user/setting",
                                        "/user/upload",
                                        "/discuss/add",
                                        "/comment/add/**",
                                        "/letter/**",
                                        "/notice/**",
                                        "/like",
                                        "/follow",
                                        "/unfollow"
                                )
                                .hasAnyAuthority(
                                        CommunityConstant.AUTHORITY_USER,
                                        CommunityConstant.AUTHORITY_ADMIN,
                                        CommunityConstant.AUTHORITY_MODERATOR
                                )
                                .antMatchers(
                                        "/discuss/top",
                                        "/discuss/wonderful"
                                )
                                .hasAnyAuthority(
                                        CommunityConstant.AUTHORITY_MODERATOR
                                )
                                .antMatchers(
                                        "/discuss/delete",
                                        "/data/**"
                                )
                                .hasAnyAuthority(
                                        CommunityConstant.AUTHORITY_ADMIN
                                )
                                .anyRequest()
                                .permitAll()

        ).csrf((csrf) -> csrf.disable());

        // 权限不够的时候处理
        http.exceptionHandling((exceptionHandling) ->
                exceptionHandling
                        .authenticationEntryPoint(new AuthenticationEntryPoint() {
                            // 没有登录
                            @Override
                            public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
                                String xRequestedWith = request.getHeader("x-requested-with");
                                if ("XMLHttpRequest".equals(xRequestedWith)) { // 异步请求
                                    response.setContentType("application/plain;charset=utf-8");
                                    PrintWriter writer = response.getWriter();
                                    writer.write(CommunityUtil.getJSONString(401, "你还没有登录！"));
                                    log.info(authException.getMessage());
                                } else {
                                    // 重定向至登录页面
                                    response.sendRedirect(request.getContextPath() + "/login");
                                }
                            }
                        })
                        .accessDeniedHandler(new AccessDeniedHandler() {
                            // 权限不足
                            @Override
                            public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
                                String xRequestedWith = request.getHeader("x-requested-with");
                                if ("XMLHttpRequest".equals(xRequestedWith)) { // 异步请求
                                    response.setContentType("application/plain;charset=utf-8");
                                    PrintWriter writer = response.getWriter();
                                    writer.write(CommunityUtil.getJSONString(403, "你没有访问此功能的权限！"));
                                } else {
                                    // 重定向至登录页面
                                    response.sendRedirect(request.getContextPath() + "/denied");
                                }
                            }
                        })
        );

        // Security 底层默认会拦截 /logout 请求，进行退出的处理。
        // 我们覆盖它默认的逻辑，才能执行我们自己退出的代码
        http.logout((logout) ->
                logout.logoutUrl("/securitylogout")
        );

        return http.build();
    }

}
