package com.moirrra.community.dao;

import com.moirrra.community.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: Moirrra
 * @CreateTime: 2024-07-10
 * @Description: 私信
 * @Version: 1.0
 */
@Mapper
public interface MessageMapper {

    // 查询当前用户的会话列表 每个会话只返回最近1条消息
    List<Message> getConversations(@Param("userId") Integer userId,
                                   @Param("offset") int offset, @Param("limit") int limit);

    // 查询当前用户的回话数量
    int getConversationCountByUser(Integer userId);

    // 查询某个回话的私信列表
    List<Message> getMessagesByConversation(@Param("conversationId") String conversationId,
                                            @Param("offset") int offset, @Param("limit") int limit);

    // 查询某个回话所包含的私信数量
    int getMessageCountByConversation(String conversationId);

    // 查询未读私信数量
    int getUnreadMessageCount(@Param("userId") Integer userId, @Param("conversationId") String conversationId);

    // 新增私信
    int insertMessage(Message message);

    // 更新私信状态
    int updateStatus(@Param("ids") List<Integer> ids, @Param("status") Integer status);
}
