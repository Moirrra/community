package com.moirrra.community.event;

import com.alibaba.fastjson.JSONObject;
import com.moirrra.community.entity.Event;
import com.moirrra.community.entity.Message;
import com.moirrra.community.util.CommunityConstant;
import com.moirrra.community.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * @Author: Moirrra
 * @CreateTime: 2024-07-28
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Component
public class EventConsumer {

    @Autowired
    private MessageService messageService;

    @KafkaListener(topics = {
            CommunityConstant.TOPIC_COMMENT,
            CommunityConstant.TOPIC_LIKE,
            CommunityConstant.TOPIC_FOLLOW},
            groupId = "${spring.kafka.consumer.group-id}"
    )
    public void handleMessage(ConsumerRecord record) {
        if (record == null || record.value() == null) {
            log.error("消息的内容为空！");
            return;
        }

        Event event = JSONObject.parseObject(record.value().toString(), Event.class);
        if (event == null) {
            log.error("消息格式错误！");
            return;
        }

        // 发送三类通知
        Message message = new Message();
        message.setFromId(CommunityConstant.SYSTEM_USER_ID);
        message.setToId(event.getEntityUserId());
        message.setConversationId(event.getTopic());
        message.setStatus(CommunityConstant.MESSAGE_UNREAD);
        message.setCreateTime(new Date());

        Map<String, Object> content = new HashMap<>();
        // 以下属性会在系统通知模版中使用
        content.put("userId", event.getUserId());
        content.put("entityType", event.getEntityType());
        content.put("entityId", event.getEntityId());
        if (!event.getData().isEmpty()) {
            for (Map.Entry<String, Object> entry : event.getData().entrySet()) {
                content.put(entry.getKey(), entry.getValue());
            }
        }
        message.setContent(JSONObject.toJSONString(content));
        messageService.addMessage(message);
    }
}
