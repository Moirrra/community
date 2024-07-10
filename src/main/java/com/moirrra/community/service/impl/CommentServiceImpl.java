package com.moirrra.community.service.impl;

import com.moirrra.community.dao.CommentMapper;
import com.moirrra.community.entity.Comment;
import com.moirrra.community.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


    @Override
    public List<Comment> findCommentsByEntity(Integer entityType, Integer entityId, int offset, int limit) {
        return commentMapper.getByEntity(entityType, entityId, offset, limit);
    }

    @Override
    public int findCommentCountByEntity(Integer entityType, Integer entityId) {
        return commentMapper.getCountByEntity(entityType, entityId);
    }
}
