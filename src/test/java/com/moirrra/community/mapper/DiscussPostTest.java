package com.moirrra.community.mapper;

import com.moirrra.community.dao.DiscussPostMapper;
import com.moirrra.community.entity.DiscussPost;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class DiscussPostTest {
    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Test
    public void testGetByUserId() {
        List<DiscussPost> list = discussPostMapper.getByUserId(0, 0, 10, 0);
        for (DiscussPost post : list) {
            System.out.println(post);
        }
    }
}
