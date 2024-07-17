package com.moirrra.community.service;

import java.util.List;
import java.util.Map;

/**
 * @Author: Moirrra
 * @CreateTime: 2024-07-16
 * @Description:
 * @Version: 1.0
 */

public interface FollowService {

    // 关注
    void follow(Integer userId, Integer entityType, Integer entityId);

    // 取关
    void unfollow(Integer userId, Integer entityType, Integer entityId);

    // 查询关注的实体的数量
    long findFolloweeCount(Integer userId, Integer entityType);

    // 查询实体的粉丝数量
    long findFollowerCount(Integer entityType, Integer entityId);

    // 查询当前用户是否已关注该实体
    boolean hasFollowed(Integer userId, Integer entityType, Integer entityId);

    // 查询用户关注列表
    List<Map<String, Object>> findFollowees(Integer userId, int offset, int limit);

    // 查询用户粉丝列表
    List<Map<String, Object>> findFollowers(Integer userId, int offset, int limit);
}
