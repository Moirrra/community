package com.moirrra.community.interceptor;

import com.moirrra.community.annotation.LoginRequired;
import com.moirrra.community.entity.User;
import com.moirrra.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @Author: Moirrra
 * @CreateTime: 2024-07-03
 * @Description: 登录状态访问拦截器
 * @Version: 1.0
 */
@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {

    @Autowired
    private HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            LoginRequired annotation = method.getAnnotation(LoginRequired.class);
            if (annotation != null && hostHolder.getUser() == null) { // 该方法需要登录才能访问 未登录
                response.sendRedirect(request.getContextPath() + "/login"); // 重定向至登录界面
                return false;
            }
        }

        return true;
    }
}
