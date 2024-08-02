package com.moirrra.community.controller;

import com.moirrra.community.entity.DiscussPost;
import com.moirrra.community.service.ElasticsearchService;
import com.moirrra.community.service.LikeService;
import com.moirrra.community.service.UserService;
import com.moirrra.community.entity.Page;
import com.moirrra.community.util.CommunityConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Moirrra
 * @CreateTime: 2024-07-31
 * @Description:
 * @Version: 1.0
 */
@Controller
@Slf4j
public class SearchController {

    @Autowired
    private ElasticsearchService elasticsearchService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    /**
     * search?keyword=xxx
     * @param keyword
     * @param page
     * @param model
     * @return
     */
    @GetMapping("/search")
    public String search(String keyword, Page page, Model model) {
        // 搜索帖子
        // 自定义的page从1开始
        Map<String, Object> searchResult = elasticsearchService.searchDiscussPost(keyword, page.getCurrent() - 1, page.getLimit());
        List<Map<String, Object>> discussPosts = new ArrayList<>();
        if (searchResult != null && !searchResult.isEmpty()) {
            List<DiscussPost> list = (List<DiscussPost>) searchResult.get("list");
            log.info(String.valueOf(list.size()));
            for (DiscussPost post : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("post", post);
                map.put("user", userService.findUserById(post.getUserId()));
                map.put("likeCount", likeService.findEntityLikeCount(CommunityConstant.ENTITY_TYPE_POST, post.getId()));
                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts", discussPosts);
        model.addAttribute("keyword", keyword);
        // 分页信息
        page.setPath("/search?keyword=" + keyword);
        if (searchResult != null) {
            long total = (long) searchResult.get("total");
            page.setRows((int) total);
            log.info(String.valueOf(total));
        } else {
            page.setRows(0);
        }

        return "site/search";
    }
}
