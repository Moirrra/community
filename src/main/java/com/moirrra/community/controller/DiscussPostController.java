package com.moirrra.community.controller;

import com.moirrra.community.entity.DiscussPost;
import com.moirrra.community.entity.User;
import com.moirrra.community.service.DiscussPostService;
import com.moirrra.community.util.CommunityUtil;
import com.moirrra.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * @Author: Moirrra
 * @CreateTime: 2024-07-08
 * @Description: 帖子相关
 * @Version: 1.0
 */
@Controller
@RequestMapping("/discuss")
public class DiscussPostController {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;

    @PostMapping("/add")
    @ResponseBody
    public String addDiscussPost(String title, String content) {
        User user = hostHolder.getUser();
        if (user == null) {
            return CommunityUtil.getJSONString(403, "你还没有登录");
        }

        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle(title);
        post.setContent(content);
        post.setCreateTime(new Date());
        discussPostService.addDiscussPost(post);

        // 报错情况将来统一处理
        return CommunityUtil.getJSONString(0, "发布成功！");
    }
}
