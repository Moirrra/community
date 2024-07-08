package com.moirrra.community.dao;

import com.moirrra.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DiscussPostMapper {

    // 根据用户id查询帖子
    List<DiscussPost> getByUserId(@Param("userId") Integer userId, @Param("offset") Integer offset, @Param("limit") Integer limit);

    // 获得用户发帖数
    int getPostCount(@Param("userId") Integer userId);

    // 新增文章
    int insertDiscussPost(DiscussPost discussPost);

    // 查询帖子详情

    // 更新帖子
}
