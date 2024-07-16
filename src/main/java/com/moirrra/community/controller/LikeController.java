package com.moirrra.community.controller;

import com.moirrra.community.entity.User;
import com.moirrra.community.service.LikeService;
import com.moirrra.community.util.CommunityUtil;
import com.moirrra.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Moirrra
 * @CreateTime: 2024-07-16
 * @Description:
 * @Version: 1.0
 */
@Controller
public class LikeController {

    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;

    @PostMapping("/like")
    @ResponseBody
    public String like(Integer entityType, Integer entityId) {
        User currentUser = hostHolder.getUser();

        // 点赞
        likeService.like(currentUser.getId(), entityType, entityId);

        // 点赞数量
        Long likeCount = likeService.findEntityLikeCount(entityType, entityId);
        // 点赞状态
        int likeStatus = currentUser == null ? 0 : likeService.findEntityLikeStatus(currentUser.getId(), entityType, entityId);

        Map<String, Object> map = new HashMap<>();
        map.put("likeCount", likeCount);
        map.put("likeStatus", likeStatus);

        return CommunityUtil.getJSONString(0, null, map);
    }
}
