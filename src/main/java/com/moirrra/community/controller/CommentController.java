package com.moirrra.community.controller;

import com.moirrra.community.entity.Comment;
import com.moirrra.community.entity.DiscussPost;
import com.moirrra.community.entity.Event;
import com.moirrra.community.event.EventProducer;
import com.moirrra.community.service.CommentService;
import com.moirrra.community.service.DiscussPostService;
import com.moirrra.community.util.CommunityConstant;
import com.moirrra.community.util.HostHolder;
import com.moirrra.community.util.RedisKeyUtil;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

/**
 * @Author: Moirrra
 * @CreateTime: 2024-07-10
 * @Description: 评论
 * @Version: 1.0
 */
@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private RedisTemplate redisTemplate;

    @PostMapping("/add/{discussPostId}")
    public String addComment(@PathVariable("discussPostId") Integer discussPostId, Comment comment) {
        comment.setUserId(hostHolder.getUser().getId());
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        commentService.addComment(comment);

        // 触发事件: 评论系统通知
        Event event = new Event()
                .setTopic(CommunityConstant.TOPIC_COMMENT)
                .setUserId(hostHolder.getUser().getId())
                .setEntityId(comment.getEntityId())
                .setEntityType(comment.getEntityType())
                .setData("postId", discussPostId);

        // 根据类型查询不同的表
        if (comment.getEntityType() == CommunityConstant.ENTITY_TYPE_POST) {
            DiscussPost target = discussPostService.findDiscussPostById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
        } else if (comment.getEntityType() == CommunityConstant.ENTITY_TYPE_COMMENT) {
            Comment target = commentService.findCommentById(comment.getEntityId());
            event.setEntityUserId(target.getUserId());
        }

        eventProducer.triggerEvent(event);

        if (comment.getEntityType() == CommunityConstant.ENTITY_TYPE_POST) {
            // 触发事件：发帖保存到es服务器
            event = new Event()
                    .setTopic(CommunityConstant.TOPIC_PUBLISH)
                    .setUserId(comment.getUserId())
                    .setEntityType(CommunityConstant.ENTITY_TYPE_POST)
                    .setEntityId(discussPostId);
            eventProducer.triggerEvent(event);

            // 计算帖子分数
            String redisKey = RedisKeyUtil.getPostScoreKey();
            redisTemplate.opsForSet().add(redisKey, discussPostId);
        }

        return "redirect:/discuss/detail/" + discussPostId;
    }
}
