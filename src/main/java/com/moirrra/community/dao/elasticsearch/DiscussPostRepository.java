package com.moirrra.community.dao.elasticsearch;

import com.moirrra.community.entity.DiscussPost;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author: Moirrra
 * @CreateTime: 2024-07-30
 * @Description:
 * @Version: 1.0
 */
@Repository
public interface DiscussPostRepository extends ElasticsearchRepository<DiscussPost, Integer> {

}
