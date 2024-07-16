package com.moirrra.community.service.impl;

import com.moirrra.community.service.LikeService;
import com.moirrra.community.util.RedisKeyUtil;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


/**
 * @Author: Moirrra
 * @CreateTime: 2024-07-15
 * @Description:
 * @Version: 1.0
 */
@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void like(Integer userId, Integer entityType, Integer entityId) {
        // get key
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        // 查看userId是否在集合中
        Boolean isMember = redisTemplate.opsForSet().isMember(entityLikeKey, userId);
        if (BooleanUtils.isTrue(isMember)) {
            // 取消点赞
            redisTemplate.opsForSet().remove(entityLikeKey, userId);
        } else {
            // 点赞
            redisTemplate.opsForSet().add(entityLikeKey, userId);
        }
    }

    @Override
    public Long findEntityLikeCount(Integer entityType, Integer entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().size(entityLikeKey);
    }

    @Override
    public int findEntityLikeStatus(Integer userId, Integer entityType, Integer entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return BooleanUtils.isTrue(redisTemplate.opsForSet().isMember(entityLikeKey, userId)) ? 1 : 0;
    }
}
