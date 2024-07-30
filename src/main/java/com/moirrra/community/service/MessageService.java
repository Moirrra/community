package com.moirrra.community.service;

import com.moirrra.community.entity.Message;

import java.util.List;

/**
 * @Author: Moirrra
 * @CreateTime: 2024-07-10
 * @Description:
 * @Version: 1.0
 */

public interface MessageService {

    List<Message> findConversations(Integer userId, int offset, int limit);

    int findConversationCountByUser(Integer userId);

    List<Message> findMessageByConversation(String conversationId, int offset, int limit);

    int findMessageCountByConversation(String conversationId);

    int findUnreadMessageCount(Integer userId, String conversationId);

    int addMessage(Message message);

    int readMessage(List<Integer> ids);

    Message findLatestNotice(Integer userId, String topic);

    int findNoticeCount(Integer userId, String topic);

    int findNoticeUnreadCount(Integer userId, String topic);

    List<Message> findNotices(Integer userId, String topic, int offset, int limit);
}
