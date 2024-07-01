package com.moirrra.community;

import com.moirrra.community.util.MailClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * @Author: Moirrra
 * @CreateTime: 2024-06-30
 * @Description: 测试邮件功能
 * @Version: 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MailTests {
    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    public void testTextMail() {
        mailClient.sendMail("moira736043695@163.com", "Test 邮件发送", "牛客论坛项目测试！");
    }

    @Test
    public void testHtmlMail() {
        Context context = new Context();
        context.setVariable("username", "sunday");
        String content = templateEngine.process("/mail/demo", context);
        System.out.println(content);
        mailClient.sendMail("moira736043695@163.com", "Test html邮件发送", content);
    }
}
