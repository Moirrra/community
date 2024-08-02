package com.moirrra.community.service;

import com.moirrra.community.entity.DiscussPost;

import java.util.Map;

/**
 * @Author: Moirrra
 * @CreateTime: 2024-07-30
 * @Description:
 * @Version: 1.0
 */

public interface ElasticsearchService {

    // 保存帖子到elasticsearch服务器
    void saveDiscussPost(DiscussPost post);

    // 从elasticsearch服务器删除帖子
    void deleteDiscussPost(Integer id);

    // 搜索
    Map<String, Object> searchDiscussPost(String keyword, int current, int limit);

}
