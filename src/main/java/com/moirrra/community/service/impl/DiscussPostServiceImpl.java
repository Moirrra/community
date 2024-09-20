package com.moirrra.community.service.impl;

import com.moirrra.community.dao.DiscussPostMapper;
import com.moirrra.community.entity.DiscussPost;
import com.moirrra.community.service.DiscussPostService;
import com.moirrra.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@Service
public class DiscussPostServiceImpl implements DiscussPostService {
    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    public List<DiscussPost> getDiscussPost(Integer userId, Integer offset, Integer limit, Integer orderMode) {
        return discussPostMapper.getByUserId(userId, offset, limit, orderMode);
    }

    @Override
    public int getPostCount(Integer userId) {
        return discussPostMapper.getPostCount(userId);
    }

    @Override
    public int addDiscussPost(DiscussPost post) {
        if (post == null) {
            throw new IllegalArgumentException("参数不能为空!");
        }

        // 转义HTML标记
        post.setTitle(HtmlUtils.htmlEscape(post.getTitle()));
        post.setContent(HtmlUtils.htmlEscape(post.getContent()));
        // 过滤敏感词
        post.setTitle(sensitiveFilter.filter(post.getTitle()));
        post.setContent(sensitiveFilter.filter(post.getContent()));

        return discussPostMapper.insertDiscussPost(post);
    }

    @Override
    public DiscussPost findDiscussPostById(Integer id) {
        return discussPostMapper.getById(id);
    }

    @Override
    public int updateCommentCount(Integer id, Integer commentCount) {
        return discussPostMapper.updateCommentCount(id, commentCount);
    }

    @Override
    public int updateType(Integer id, Integer type) {
        return discussPostMapper.updateType(id, type);
    }

    @Override
    public int updateStatus(Integer id, Integer status) {
        return discussPostMapper.updateStatus(id, status);
    }

    @Override
    public int updateScore(Integer id, Double score) {
        return discussPostMapper.updateScore(id, score);
    }
}
