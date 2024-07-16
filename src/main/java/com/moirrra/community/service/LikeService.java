package com.moirrra.community.service;

/**
 * @Author: Moirrra
 * @CreateTime: 2024-07-15
 * @Description:
 * @Version: 1.0
 */

public interface LikeService {

    // 点赞: 将userId加入集合
    void like(Integer userId, Integer entityType, Integer entityId, Integer entityUserId);

    // 查询某实体点赞数量：看集合里有多少个userId
    long findEntityLikeCount(Integer entityType, Integer entityId);

    // 查询用户点赞状态: userId是否在集合中
    int findEntityLikeStatus(Integer userId, Integer entityType, Integer entityId);

    // 查询用户被点赞数
    int findUserLikeCount(Integer userId);

}
