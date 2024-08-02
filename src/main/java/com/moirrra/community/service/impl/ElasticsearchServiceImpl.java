package com.moirrra.community.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.moirrra.community.dao.elasticsearch.DiscussPostRepository;
import com.moirrra.community.entity.DiscussPost;
import com.moirrra.community.service.ElasticsearchService;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: Moirrra
 * @CreateTime: 2024-07-30
 * @Description:
 * @Version: 1.0
 */
@Service
@Slf4j
public class ElasticsearchServiceImpl implements ElasticsearchService {

    @Autowired
    private DiscussPostRepository postRepository;

    @Autowired
    private ElasticsearchRestTemplate esTemplate;


    @Override
    public void saveDiscussPost(DiscussPost post) {
        postRepository.save(post);
    }

    @Override
    public void deleteDiscussPost(Integer id) {
        postRepository.deleteById(id);
    }

    @Override
    public Map<String, Object> searchDiscussPost(String keyword, int current, int limit) {

        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery(keyword, "title", "content"))
                .withSorts(SortBuilders.fieldSort("type").order(SortOrder.DESC))
                .withSorts(SortBuilders.fieldSort("score").order(SortOrder.DESC))
                .withSorts(SortBuilders.fieldSort("createTime").order(SortOrder.DESC))
                .withSorts(SortBuilders.fieldSort("id").order(SortOrder.DESC))
                .withPageable(PageRequest.of(current, limit))
                .withHighlightFields( //高亮显示
                        new HighlightBuilder.Field("title").preTags("<em>").postTags("</em>"),
                        new HighlightBuilder.Field("content").preTags("<em>").postTags("</em>")
                ).build();

        SearchHits<DiscussPost> searchHits = esTemplate.search(nativeSearchQuery, DiscussPost.class);

        Map<String, Object> map = new HashMap<>();
        long total = searchHits.getTotalHits();
        map.put("total", total);
        log.info("current=" + current);
        log.info("total=" + total);
        List<DiscussPost> list = new ArrayList<>();
        if (searchHits.getTotalHits() != 0) {
            for (SearchHit<DiscussPost> searchHit : searchHits) {
                DiscussPost post = new DiscussPost();
                post.setId(searchHit.getContent().getId());
                post.setUserId(searchHit.getContent().getUserId());
                post.setTitle(searchHit.getContent().getTitle());
                post.setContent(searchHit.getContent().getContent());
                post.setStatus(searchHit.getContent().getStatus());
                post.setType(searchHit.getContent().getType());
                String createTime = searchHit.getContent().getCreateTime().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US);
                Date date = null;
                try {
                    date = (Date) sdf.parse(createTime);
                } catch (ParseException e) {
                    log.error(e.getMessage());
                }
                post.setCreateTime(date);
                post.setCommentCount(searchHit.getContent().getCommentCount());

                // 获得刚刚构建的高光区域，填到帖子的内容和标题上
                List<String> contentField = searchHit.getHighlightFields().get("content");
                if (contentField != null) {
                    post.setContent(contentField.get(0));
                }
                List<String> titleField = searchHit.getHighlightFields().get("title");
                if (titleField != null) {
                    post.setTitle(titleField.get(0));
                }
                list.add(post);
            }
            log.info("list=" + list.size());
        }

        map.put("list", list);
        return map;

    }
}
