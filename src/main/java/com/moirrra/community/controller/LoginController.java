package com.moirrra.community.controller;

import com.moirrra.community.entity.User;
import com.moirrra.community.service.UserService;
import com.moirrra.community.util.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

/**
 * @Author: Moirrra
 * @CreateTime: 2024-07-01
 * @Description: 登录注册
 * @Version: 1.0
 */
@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * 跳转注册界面
     * @return
     */
    @GetMapping("/register")
    public String getRegisterPage() {
        return "site/register";
    }

    /**
     * 跳转登录界面
     * @return
     */
    @GetMapping("/login")
    public String getLoginPage() {
        return "site/login";
    }



    /**
     * 注册
     * @param model
     * @param user
     * @return
     */
    @PostMapping("/register")
    public String register(Model model, User user) {
        Map<String, Object> map = userService.register(user);
        if (map == null || map.isEmpty()) {
            model.addAttribute("msg", "注册成功，我们已经向您的邮箱发送了一封激活邮件，请尽快激活！");
            model.addAttribute("target", "/index");
            return "site/operate-result";
        } else {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            model.addAttribute("emailMsg", map.get("emailMsg"));
            return "site/register";
        }
    }

    /**
     * 账号激活
     * @param model
     * @param userId
     * @param code
     * @return
     */
    @GetMapping("/activation/{userId}/{code}")
    public String activate(Model model, @PathVariable("userId") int userId, @PathVariable("code") String code) {
        int result = userService.activate(userId, code);
        if (result == CommunityConstant.ACTIVATION_SUCCESS) {
            model.addAttribute("msg", "激活成功，您的账号已经可以正常使用了！");
            model.addAttribute("target", "/login");
        } else if (result == CommunityConstant.ACTIVATION_REPEAT) {
            model.addAttribute("msg", "无效操作，您的账号已经激活过了！");
            model.addAttribute("target", "/index");
        } else {
            model.addAttribute("msg", "激活失败，激活码错误！");
            model.addAttribute("target", "/index");
        }
        return "site/operate-result";
    }
}
