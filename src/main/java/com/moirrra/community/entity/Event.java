package com.moirrra.community.entity;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Moirrra
 * @CreateTime: 2024-07-28
 * @Description:
 * @Version: 1.0
 */
@Getter
public class Event {

    private String topic;

    private Integer userId; // 触发者

    private Integer entityId; // 触发实体

    private Integer entityType;

    private Integer entityUserId;

    private Map<String, Object> data = new HashMap<>();

    public Event setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public Event setUserId(Integer userId) {
        this.userId = userId;
        return this;
    }

    public Event setEntityId(Integer entityId) {
        this.entityId = entityId;
        return this;
    }

    public Event setEntityType(Integer entityType) {
        this.entityType = entityType;
        return this;
    }

    public Event setEntityUserId(Integer entityUserId) {
        this.entityUserId = entityUserId;
        return this;
    }

    public Event setData(String key, Object value) {
        this.data.put(key, value);
        return this;
    }
}
