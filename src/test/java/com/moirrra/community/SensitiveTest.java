package com.moirrra.community;

import com.moirrra.community.util.SensitiveFilter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author: Moirrra
 * @CreateTime: 2024-07-06
 * @Description: 测试敏感词过滤
 * @Version: 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class SensitiveTest {

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Test
    public void testSensitiveFilter() {
        String text = "☆我们要禁止吸毒、赌博、嫖娼。平时还要礼貌用语，不能随便乱骂别人☆傻屌△";
        text = sensitiveFilter.filter(text);
        System.out.println(text);
    }
}
