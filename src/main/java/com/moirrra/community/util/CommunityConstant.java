package com.moirrra.community.util;

/**
 * @Author: Moirrra
 * @CreateTime: 2024-07-01
 * @Description: 常量
 * @Version: 1.0
 */

public class CommunityConstant {
    /**
     * 激活状态
     */
    // 激活成功
    public static final int ACTIVATION_SUCCESS = 0;

    // 重复激活
    public static final int ACTIVATION_REPEAT = 1;

    // 激活失败
    public static final int ACTIVATION_FAILURE = 2;

    /**
     * 账号状态
     */
    // 未激活
    public static final int NOT_ACTIVATED = 0;

    // 已激活
    public static final int ACTIVATED = 1;

    /**
     * 登录凭证是否有效
     */
    public static final int VALID = 0;

    // 已激活
    public static final int INVALID = 1;


    /**
     * 默认登录凭证超时时间
     */
    public static final int DEFAULT_EXPIRED_SECONDS = 3600 * 12;

    /**
     * 记住我登录凭证超时时间
     */
    public static final int REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 100;

    /**
     * 文件大小
     */
    public static final long ONE_MB = 1024 * 1024;

    /**
     * 评论实体类型
     */
    public static final int ENTITY_TYPE_POST = 1;

    public static final int ENTITY_TYPE_COMMENT = 2;

    public static final int ENTITY_TYPE_USER = 3;

    /**
     * 消息状态
     * 0-未读;1-已读;2-删除;
     */
    public static final int MESSAGE_UNREAD = 0;

    public static final int MESSAGE_READ = 1;

    public static final int MESSAGE_DELETED = 2;
}
