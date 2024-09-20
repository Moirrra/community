package com.moirrra.community.dao;

import com.moirrra.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DiscussPostMapper {

    // 根据用户id查询帖子
    List<DiscussPost> getByUserId(@Param("userId") Integer userId, @Param("offset") Integer offset,
                                  @Param("limit") Integer limit, @Param("orderMode") Integer orderMode);

    // 获得用户发帖数
    int getPostCount(@Param("userId") Integer userId);

    // 新增文章
    int insertDiscussPost(DiscussPost discussPost);

    // 查询帖子详情
    DiscussPost getById(Integer id);

    // 更新帖子评论数
    int updateCommentCount(@Param("id") Integer id, @Param("commentCount")Integer commentCount);

    int updateType(@Param("id")Integer id, @Param("type")Integer type);

    int updateStatus(@Param("id")Integer id, @Param("status")Integer status);

    int updateScore(@Param("id")Integer id, @Param("score") Double score);
}
