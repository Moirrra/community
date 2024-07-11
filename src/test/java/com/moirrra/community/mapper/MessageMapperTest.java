package com.moirrra.community.mapper;

import com.moirrra.community.CommunityApplication;
import com.moirrra.community.dao.MessageMapper;
import com.moirrra.community.entity.Message;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @Author: Moirrra
 * @CreateTime: 2024-07-10
 * @Description:
 * @Version: 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MessageMapperTest {

    @Autowired
    private MessageMapper messageMapper;

    @Test
    public void testGetMessages() {
        List<Message> list = messageMapper.getConversations(111, 0, 20);
        for (Message message : list) {
            System.out.println(message);
        }

        int cnt = messageMapper.getConversationCountByUser(111);
        System.out.println(cnt);

        List<Message> list1 = messageMapper.getMessagesByConversation("111_112", 0, 10);
        for (Message message : list1) {
            System.out.println(message);
        }

        int cnt1 = messageMapper.getMessageCountByConversation("111_112");
        System.out.println(cnt1);

        int cnt2 = messageMapper.getUnreadMessageCount(131, "111_131");
        System.out.println(cnt2);

    }
}
