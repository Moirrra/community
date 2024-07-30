package com.moirrra.community.dao;

import com.moirrra.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: Moirrra
 * @CreateTime: 2024-07-09
 * @Description:
 * @Version: 1.0
 */
@Mapper
public interface CommentMapper {

    List<Comment> getByEntity(@Param("entityType") Integer entityType, @Param("entityId") Integer entityId,
                              @Param("offset") int offset, @Param("limit") int limit);

    int getCountByEntity(@Param("entityType") Integer entityType, @Param("entityId")Integer entityId);

    int insertComment(Comment comment);

    Comment getById(Integer id);

}
