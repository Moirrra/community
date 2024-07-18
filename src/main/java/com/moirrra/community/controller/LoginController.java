package com.moirrra.community.controller;

import com.google.code.kaptcha.Producer;
import com.moirrra.community.entity.User;
import com.moirrra.community.service.UserService;
import com.moirrra.community.util.CommunityConstant;
import com.moirrra.community.util.CommunityUtil;
import com.moirrra.community.util.RedisKeyUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Moirrra
 * @CreateTime: 2024-07-01
 * @Description: 登录注册
 * @Version: 1.0
 */
@Slf4j
@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private Producer kaptcharProducer;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${server.servlet.context-path}")
    private String contextPath;

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

    /**
     * 生成验证码
     * @param response
     */
    @GetMapping("/kaptcha")
    public void getKaptcha(HttpServletResponse response) {
        // 生成验证码
        String text = kaptcharProducer.createText();
        BufferedImage image = kaptcharProducer.createImage(text);

        // 生成验证码归属
        String kaptchaOwner = CommunityUtil.generateUUID();
        // 将验证码存入redis
        String kaptchaKey = RedisKeyUtil.getKaptchaKey(kaptchaOwner);
        redisTemplate.opsForValue().set(kaptchaKey, text, CommunityConstant.KAPTCHA_EXPIRED_SECONDS, TimeUnit.SECONDS);

        // 将验证码归属存入cookie
        Cookie cookie = new Cookie("kaptchaOwner", kaptchaOwner);
        cookie.setMaxAge(CommunityConstant.KAPTCHA_EXPIRED_SECONDS);
        cookie.setPath(contextPath);
        response.addCookie(cookie);

        // 将图片输出给浏览器
        response.setContentType("image/png");
        try {
            OutputStream os = response.getOutputStream();
            ImageIO.write(image, "png", os);
        } catch (IOException e) {
            log.error("响应验证码失败：" + e.getMessage());
        }
    }

    @PostMapping("/login")
    public String login(String username, String password, String code, boolean rememberMe,
                        Model model, HttpServletResponse response, @CookieValue("kaptchaOwner") String kaptchaOwner) {

        String kaptcha = null;
        if (StringUtils.isNotBlank(kaptchaOwner)) {
            String kaptchaKey = RedisKeyUtil.getKaptchaKey(kaptchaOwner);
            kaptcha = (String) redisTemplate.opsForValue().get(kaptchaKey);
        }

        if (StringUtils.isBlank(kaptcha) || StringUtils.isBlank(code) || !kaptcha.equalsIgnoreCase(code)) {
            model.addAttribute("codeMsg", "验证码不正确！");
            return "site/login";
        }

        int expiredTime = rememberMe ? CommunityConstant.REMEMBER_EXPIRED_SECONDS : CommunityConstant.DEFAULT_EXPIRED_SECONDS;
        Map<String, Object> map = userService.login(username, password, expiredTime);
        if (map.containsKey("ticket")) {
            // cookie
            Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
            cookie.setPath(contextPath);
            cookie.setMaxAge(expiredTime);
            response.addCookie(cookie);
            return "redirect:/index";
        } else {
            model.addAttribute("usernameMsg", map.get("usernameMsg"));
            model.addAttribute("passwordMsg", map.get("passwordMsg"));
            return "site/login";
        }
    }

    @GetMapping("/logout")
    public String logout(@CookieValue("ticket")String ticket) {
        userService.logout(ticket);
        return "redirect:/login";
    }
}
