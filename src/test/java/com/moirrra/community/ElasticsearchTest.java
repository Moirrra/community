package com.moirrra.community;

import com.alibaba.fastjson.JSONObject;
import com.moirrra.community.dao.DiscussPostMapper;
import com.moirrra.community.dao.elasticsearch.DiscussPostRepository;
import com.moirrra.community.entity.DiscussPost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author: Moirrra
 * @CreateTime: 2024-07-30
 * @Description:
 * @Version: 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class ElasticsearchTest {

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private DiscussPostRepository discussPostRepository;

    @Autowired
    private ElasticsearchRestTemplate restTemplate;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Test
    public void testInsert() {
        discussPostRepository.save(discussPostMapper.getById(280));
        discussPostRepository.save(discussPostMapper.getById(281));
        discussPostRepository.save(discussPostMapper.getById(282));
    }

    @Test
    public void testInsertList() {
        discussPostRepository.saveAll(discussPostMapper.getByUserId(101, 0, 100));
        discussPostRepository.saveAll(discussPostMapper.getByUserId(102, 0, 100));
        discussPostRepository.saveAll(discussPostMapper.getByUserId(103, 0, 100));
        discussPostRepository.saveAll(discussPostMapper.getByUserId(111, 0, 100));
        discussPostRepository.saveAll(discussPostMapper.getByUserId(112, 0, 100));
        discussPostRepository.saveAll(discussPostMapper.getByUserId(133, 0, 100));
        discussPostRepository.saveAll(discussPostMapper.getByUserId(134, 0, 100));
        discussPostRepository.saveAll(discussPostMapper.getByUserId(138, 0, 100));
        discussPostRepository.saveAll(discussPostMapper.getByUserId(149, 0, 100));
        discussPostRepository.saveAll(discussPostMapper.getByUserId(152, 0, 100));
    }

    @Test
    public void testUpdate() {
        DiscussPost discussPost = discussPostMapper.getById(111);
        discussPost.setContent("我是互联网新人，请多多指教");
        discussPostRepository.save(discussPost);
    }

    @Test
    public void testDelete() {
        discussPostRepository.deleteById(111);
        // 全部删除
        discussPostRepository.deleteAll();
    }

    @Test
    public void testSearchNoHighlight() {
        SearchRequest searchRequest = new SearchRequest("discusspost");

        // 构建搜索条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                .query(QueryBuilders.multiMatchQuery("互联网寒冬", "title", "content"))
                .sort(SortBuilders.fieldSort("type").order(SortOrder.DESC))
                .sort(SortBuilders.fieldSort("score").order(SortOrder.DESC))
                .sort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC))
                .from(0)
                .size(10);

        searchRequest.source(searchSourceBuilder);

        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            List<DiscussPost> list = new LinkedList<>();
            for (SearchHit hit : searchResponse.getHits().getHits()) {
                DiscussPost discussPost = JSONObject.parseObject(hit.getSourceAsString(), DiscussPost.class);
                //System.out.println(discussPost);
                list.add(discussPost);
            }
            System.out.println(list.size());
            for (DiscussPost post : list) {
                System.out.println(post);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSearchHighlight() {
        SearchRequest searchRequest = new SearchRequest("discusspost");

        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("title");
        highlightBuilder.field("content");
        highlightBuilder.preTags("<em>");
        highlightBuilder.postTags("</em>");

        // 构建搜索条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                .query(QueryBuilders.multiMatchQuery("互联网寒冬", "title", "content"))
                .sort(SortBuilders.fieldSort("type").order(SortOrder.DESC))
                .sort(SortBuilders.fieldSort("score").order(SortOrder.DESC))
                .sort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC))
                .from(0)
                .size(10)
                .highlighter(highlightBuilder);

        searchRequest.source(searchSourceBuilder);

        try {
            SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
            List<DiscussPost> list = new LinkedList<>();
            for (SearchHit hit : searchResponse.getHits().getHits()) {
                DiscussPost discussPost = JSONObject.parseObject(hit.getSourceAsString(), DiscussPost.class);

                // 处理高亮显示的内容
                HighlightField titleField = hit.getHighlightFields().get("title");
                if (titleField != null) {
                    discussPost.setTitle(titleField.getFragments()[0].toString());
                }
                HighlightField contentField = hit.getHighlightFields().get("content");
                if (contentField != null) {
                    discussPost.setContent(contentField.getFragments()[0].toString());
                }

                list.add(discussPost);
            }
            System.out.println(list.size());
            for (DiscussPost post : list) {
                System.out.println(post);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
