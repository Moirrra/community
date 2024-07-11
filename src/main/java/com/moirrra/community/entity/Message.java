package com.moirrra.community.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * @Author: Moirrra
 * @CreateTime: 2024-07-10
 * @Description: 私信
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Message {
    private Integer id;

    private Integer fromId;

    private Integer toId;

    private String conversationId;

    private String content;

    /**
     * 0-未读
     * 1-已读
     * 2-删除
     */
    private Integer status;

    private Date createTime;
}
