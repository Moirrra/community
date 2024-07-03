package com.moirrra.community.interceptor;

import com.moirrra.community.entity.LoginTicket;
import com.moirrra.community.entity.User;
import com.moirrra.community.service.UserService;
import com.moirrra.community.util.CommunityConstant;
import com.moirrra.community.util.CookieUtil;
import com.moirrra.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @Author: Moirrra
 * @CreateTime: 2024-07-02
 * @Description: 登录凭证拦截器
 * @Version: 1.0
 */
@Component
public class LoginTicketInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 获取凭证
        String ticket = CookieUtil.getValue(request, "ticket");

        if (ticket != null) {
            LoginTicket loginTicket = userService.getLoginTicket(ticket);
            if (loginTicket != null
                && loginTicket.getStatus() == CommunityConstant.VALID
                && loginTicket.getExpired().after(new Date())) {
                User user = userService.findUserById(loginTicket.getUserId());
                // 在本次请求中持有用户
                hostHolder.setUser(user);
            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUser();
        if (user != null && modelAndView != null) {
            modelAndView.addObject("loginUser", user);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
    }
}
