package com.moirrra.community.service.impl;

import com.moirrra.community.dao.CommentMapper;
import com.moirrra.community.dao.DiscussPostMapper;
import com.moirrra.community.entity.Comment;
import com.moirrra.community.service.CommentService;
import com.moirrra.community.service.DiscussPostService;
import com.moirrra.community.util.CommunityConstant;
import com.moirrra.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @Author: Moirrra
 * @CreateTime: 2024-07-09
 * @Description:
 * @Version: 1.0
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Autowired
    private DiscussPostService discussPostService;


    @Override
    public List<Comment> findCommentsByEntity(Integer entityType, Integer entityId, int offset, int limit) {
        return commentMapper.getByEntity(entityType, entityId, offset, limit);
    }

    @Override
    public int findCommentCountByEntity(Integer entityType, Integer entityId) {
        return commentMapper.getCountByEntity(entityType, entityId);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public int addComment(Comment comment) {
        if (comment == null) {
            throw new IllegalArgumentException("参数不能为空！");
        }
        // 过滤
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveFilter.filter(comment.getContent()));
        int rows = commentMapper.insertComment(comment);

        // 更新帖子评论数
        if (comment.getEntityType() == CommunityConstant.ENTITY_TYPE_POST) {
            int count = commentMapper.getCountByEntity(comment.getEntityType(), comment.getEntityId());
            discussPostService.updateCommentCount(comment.getEntityId(), count);
        }
        return 0;
    }
}
