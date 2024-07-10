package com.moirrra.community.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

/**
 * @Author: Moirrra
 * @CreateTime: 2024-07-09
 * @Description: 评论
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Comment {

    private Integer id;

    private Integer userId;

    private Integer entityType;

    private Integer entityId;

    private Integer targetId;

    private String content;

    private Integer status;

    private Date createTime;
}
