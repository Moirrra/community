package com.moirrra.community.controller;

import com.moirrra.community.util.CommunityUtil;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.*;
import java.net.http.HttpResponse;

/**
 * @Author: Moirrra
 * @CreateTime: 2024-07-01
 * @Description:
 * @Version: 1.0
 */
@Controller
@RequestMapping("/test")
public class TestController {

    @GetMapping("/cookie/set")
    @ResponseBody
    public String setCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("code", CommunityUtil.generateUUID());
        // 生效范围
        cookie.setPath("community/test");
        cookie.setMaxAge(60 * 10);
        response.addCookie(cookie);

        return "set cookie";
    }

    @GetMapping("/cookie/get")
    @ResponseBody
    public String getCookie(@CookieValue("code")String code) {
        System.out.println(code);
        return "get cookie";
    }

    @GetMapping("/session/set")
    @ResponseBody
    public String setSession(HttpSession session) { // 只要声明就可以使用
        session.setAttribute("id", 1);
        session.setAttribute("name", "Test");
        return "set session";
    }

    @GetMapping("/session/get")
    @ResponseBody
    public String getSession(HttpSession session) {
        System.out.println("session.getAttribute(\"id\") = " + session.getAttribute("id"));
        System.out.println("session.getAttribute(\"name\") = " + session.getAttribute("name"));
        return "get session";
    }


}
