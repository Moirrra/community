package com.moirrra.community.service.impl;

import com.moirrra.community.service.LikeService;
import com.moirrra.community.util.RedisKeyUtil;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
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

    public void like(Integer userId, Integer entityType, Integer entityId, Integer entityUserId) {
        // 编程式事务 一次点赞两个增加
        redisTemplate.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                // get key
                String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
                String userLikeKey = RedisKeyUtil.getUserLikeKey(entityUserId);

                // 查看userId是否在集合中
                // 查询要放在事务之前，否则事务提交之后才执行
                Boolean isMember = redisTemplate.opsForSet().isMember(entityLikeKey, userId);

                operations.multi();

                if (BooleanUtils.isTrue(isMember)) {
                    // 取消点赞
                    redisTemplate.opsForSet().remove(entityLikeKey, userId);
                    // 减少用户的被点赞数
                    redisTemplate.opsForValue().decrement(userLikeKey);
                } else {
                    // 点赞
                    redisTemplate.opsForSet().add(entityLikeKey, userId);
                    // 增加用户的被点赞数
                    redisTemplate.opsForValue().increment(userLikeKey);
                }

                return operations.exec();
            }
        });

    }

    @Override
    public long findEntityLikeCount(Integer entityType, Integer entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        Long count = redisTemplate.opsForSet().size(entityLikeKey);
        return count == null ? 0 : count;
    }

    @Override
    public int findEntityLikeStatus(Integer userId, Integer entityType, Integer entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return BooleanUtils.isTrue(redisTemplate.opsForSet().isMember(entityLikeKey, userId)) ? 1 : 0;
    }

    @Override
    public int findUserLikeCount(Integer userId) {
        String userLikeKey = RedisKeyUtil.getUserLikeKey(userId);
        Integer count = (Integer) redisTemplate.opsForValue().get(userLikeKey);
        return count == null ? 0 : count;
    }
}
