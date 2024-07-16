package com.moirrra.community.controller;

import com.moirrra.community.entity.Comment;
import com.moirrra.community.entity.DiscussPost;
import com.moirrra.community.entity.Page;
import com.moirrra.community.entity.User;
import com.moirrra.community.service.CommentService;
import com.moirrra.community.service.DiscussPostService;
import com.moirrra.community.service.LikeService;
import com.moirrra.community.service.UserService;
import com.moirrra.community.util.CommunityConstant;
import com.moirrra.community.util.CommunityUtil;
import com.moirrra.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
    private UserService userService;
    
    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;

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

    /**
     * 查询帖子详情
     * @param discussPostId
     * @param model
     * @return
     */
    @GetMapping("/detail/{discussPostId}")
    public String getDiscussPost(@PathVariable("discussPostId") Integer discussPostId, Model model, Page page) {
        // 帖子
        DiscussPost post = discussPostService.findDiscussPostById(discussPostId);
        model.addAttribute("post", post);
        // 作者
        User user = userService.findUserById(post.getUserId());
        model.addAttribute("user", user);

        // 点赞数量
        Long likeCount = likeService.findEntityLikeCount(CommunityConstant.ENTITY_TYPE_POST, discussPostId);
        model.addAttribute("likeCount", likeCount);
        // 点赞状态
        User currentUser = hostHolder.getUser();
        int likeStatus = currentUser == null ? 0 :
                likeService.findEntityLikeStatus(currentUser.getId(), CommunityConstant.ENTITY_TYPE_POST, discussPostId);
        model.addAttribute("likeStatus", likeStatus);

        // 查询评论分页信息
        page.setLimit(5);
        page.setPath("/discuss/detail/" + discussPostId);
        page.setRows(post.getCommentCount());
        // 帖子评论列表
        List<Comment> commentList = commentService.findCommentsByEntity(
                CommunityConstant.ENTITY_TYPE_POST, post.getId(), page.getOffset(), page.getLimit());
        // 帖子评论VO列表
        List<Map<String, Object>> commentVOList = new ArrayList<>();
        if (commentList != null) {
            for (Comment comment : commentList) {
                Map<String, Object> commentVO = new HashMap<>();
                // 评论
                commentVO.put("comment", comment);
                // 评论用户
                commentVO.put("user", userService.findUserById(comment.getUserId()));

                // 点赞数量
                likeCount = likeService.findEntityLikeCount(CommunityConstant.ENTITY_TYPE_COMMENT, comment.getId());
                commentVO.put("likeCount", likeCount);
                // 点赞状态
                likeStatus = currentUser == null ? 0 :
                        likeService.findEntityLikeStatus(currentUser.getId(), CommunityConstant.ENTITY_TYPE_COMMENT, comment.getId());
                commentVO.put("likeStatus", likeStatus);

                // 楼中楼 评论的回复
                List<Comment> replyList = commentService.findCommentsByEntity(
                        CommunityConstant.ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);
                List<Map<String, Object>> replyVOList = new ArrayList<>();
                if (replyList != null) {
                    for (Comment reply : replyList) {
                        Map<String, Object> replyVO = new HashMap<>();
                        replyVO.put("reply", reply);
                        replyVO.put("user", userService.findUserById(reply.getUserId()));
                        // 回复目标
                        User target = reply.getTargetId() == 0 ? null : userService.findUserById(reply.getTargetId());
                        replyVO.put("target", target);
                        // 点赞数量
                        likeCount = likeService.findEntityLikeCount(CommunityConstant.ENTITY_TYPE_COMMENT, reply.getId());
                        replyVO.put("likeCount", likeCount);
                        // 点赞状态
                        likeStatus = currentUser == null ? 0 :
                                likeService.findEntityLikeStatus(currentUser.getId(), CommunityConstant.ENTITY_TYPE_COMMENT, reply.getId());
                        replyVO.put("likeStatus", likeStatus);
                        replyVOList.add(replyVO);
                    }
                }
                commentVO.put("replies", replyVOList);

                // 回复数量
                int replyCount = commentService.findCommentCountByEntity(CommunityConstant.ENTITY_TYPE_COMMENT, comment.getId());
                commentVO.put("replyCount", replyCount);

                commentVOList.add(commentVO);
            }
        }

        model.addAttribute("comments", commentVOList);

        return "site/discuss-detail";
    }
    
    
}
