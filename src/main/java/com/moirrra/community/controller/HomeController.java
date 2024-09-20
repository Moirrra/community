package com.moirrra.community.controller;

import com.moirrra.community.entity.DiscussPost;
import com.moirrra.community.entity.Page;
import com.moirrra.community.entity.User;
import com.moirrra.community.service.DiscussPostService;
import com.moirrra.community.service.LikeService;
import com.moirrra.community.service.UserService;
import com.moirrra.community.util.CommunityConstant;
import com.moirrra.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;

    @GetMapping("/index")
    public String getIndexPage(Model model, Page page,
                               @RequestParam(name = "orderMode", defaultValue = "0") int orderMode) {
        // 方法调用前，会自动实例化Model和Page，并将Page注入Model
        // Thymeleaf中可以直接访问Page对象中的数据
        page.setRows(discussPostService.getPostCount(0));
        page.setPath("/index");

        List<DiscussPost> postList = discussPostService.getDiscussPost(0, page.getOffset(), page.getLimit(), orderMode);
        List<Map<String, Object>> discussPosts = new ArrayList<>();

        User currentUser = hostHolder.getUser();

        if (postList != null) {
            for (DiscussPost post : postList) {
                Map<String, Object> map = new HashMap<>();
                map.put("post", post);
                User user = userService.findUserById(post.getUserId());
                map.put("user", user);
                // 点赞
                Long likeCount = likeService.findEntityLikeCount(CommunityConstant.ENTITY_TYPE_POST, post.getId());
                map.put("likeCount", likeCount);
                // 状态
                int likeStatus = currentUser == null ? 0 : likeService.findEntityLikeStatus(currentUser.getId(), CommunityConstant.ENTITY_TYPE_POST, post.getId());
                map.put("likeStatus", likeStatus);
                discussPosts.add(map);
            }
        }
        model.addAttribute("discussPosts", discussPosts);
        model.addAttribute("orderMode", orderMode);

        return "index";
    }

    @GetMapping("/error")
    public String getErrorPage() {
        return "error/500";
    }

    @GetMapping("/denied")
    public String getDeniedPage() {
        return "error/404";
    }
}
