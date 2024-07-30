package com.moirrra.community.service;

import com.moirrra.community.entity.Comment;

import java.util.List;

/**
 * @Author: Moirrra
 * @CreateTime: 2024-07-09
 * @Description:
 * @Version: 1.0
 */

public interface CommentService {

    List<Comment> findCommentsByEntity(Integer entityType, Integer entityId, int offset, int limit);

    int findCommentCountByEntity(Integer entityType, Integer entityId);

    int addComment(Comment comment);

    Comment findCommentById(Integer id);
}
