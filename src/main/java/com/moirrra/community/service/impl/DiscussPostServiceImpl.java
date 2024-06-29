package com.moirrra.community.service.impl;

import com.moirrra.community.dao.DiscussPostMapper;
import com.moirrra.community.entity.DiscussPost;
import com.moirrra.community.service.DiscussPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DiscussPostServiceImpl implements DiscussPostService {
    @Autowired
    private DiscussPostMapper discussPostMapper;

    public List<DiscussPost> getDiscussPost(Integer userId, Integer offset, Integer limit) {
        return discussPostMapper.getByUserId(userId, offset, limit);
    }

    @Override
    public int getPostCount(Integer userId) {
        return discussPostMapper.getPostCount(userId);
    }
}
