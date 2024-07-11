package com.moirrra.community.service.impl;

import com.moirrra.community.dao.MessageMapper;
import com.moirrra.community.entity.Message;
import com.moirrra.community.service.MessageService;
import com.moirrra.community.util.CommunityConstant;
import com.moirrra.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @Author: Moirrra
 * @CreateTime: 2024-07-10
 * @Description:
 * @Version: 1.0
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Override
    public List<Message> findConversations(Integer userId, int offset, int limit) {
        return messageMapper.getConversations(userId, offset, limit);
    }

    @Override
    public int findConversationCountByUser(Integer userId) {
        return messageMapper.getConversationCountByUser(userId);
    }

    @Override
    public List<Message> findMessageByConversation(String conversationId, int offset, int limit) {
        return messageMapper.getMessagesByConversation(conversationId, offset, limit);
    }

    @Override
    public int findMessageCountByConversation(String conversationId) {
        return messageMapper.getMessageCountByConversation(conversationId);
    }

    @Override
    public int findUnreadMessageCount(Integer userId, String conversationId) {
        return messageMapper.getUnreadMessageCount(userId, conversationId);
    }

    @Override
    public int addMessage(Message message) {
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        message.setContent(sensitiveFilter.filter(message.getContent()));
        return messageMapper.insertMessage(message);
    }

    @Override
    public int readMessage(List<Integer> ids) {
        return messageMapper.updateStatus(ids, CommunityConstant.MESSAGE_READ);
    }


}
