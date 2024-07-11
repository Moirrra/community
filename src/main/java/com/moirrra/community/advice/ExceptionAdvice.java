package com.moirrra.community.advice;

import com.moirrra.community.util.CommunityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Author: Moirrra
 * @CreateTime: 2024-07-11
 * @Description:
 * @Version: 1.0
 */
@ControllerAdvice(annotations = Controller.class)
@Slf4j
public class ExceptionAdvice {

    @ExceptionHandler(Exception.class)
    public void handleException(Exception e, HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 记录日志
        log.error("服务器发生异常：" + e.getMessage());
        for (StackTraceElement element: e.getStackTrace()) {
            log.error(element.toString());
        }

        // 判断普通请求或异步请求
        String xRequestedWith = request.getHeader("x-requested-with");
        if (xRequestedWith.equals("XMLHttpRequest")) {
            // 异步请求
            response.setContentType("application/plain;charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(CommunityUtil.getJSONString(1, "服务器异常"));
        } else {
            response.sendRedirect(request.getContextPath() + "/error");
        }
    }
}
