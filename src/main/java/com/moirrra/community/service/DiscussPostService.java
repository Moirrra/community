package com.moirrra.community.service;

import com.moirrra.community.dao.DiscussPostMapper;
import com.moirrra.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


public interface DiscussPostService {
    List<DiscussPost> getDiscussPost(Integer userId, Integer offset, Integer limit);

    int getPostCount(Integer userId);

    int addDiscussPost(DiscussPost post);

    DiscussPost findDiscussPostById(Integer id);

    int updateCommentCount(Integer id, Integer commentCount);

    int updateType(Integer id, Integer type);

    int updateStatus(Integer id, Integer status);
}
