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
    public static final int ACCOUNT_NOT_ACTIVATED = 0;

    // 已激活
    public static final int ACCOUNT_ACTIVATED = 1;

    /**
     * 登录凭证是否有效
     */
    public static final int LOGIN_TICKET_VALID = 0;

    public static final int LOGIN_TICKET_INVALID = 1;


    /**
     * 默认登录凭证超时时间
     */
    public static final int DEFAULT_EXPIRED_SECONDS = 3600 * 12;

    /**
     * 记住我登录凭证超时时间
     */
    public static final int REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 100;

    /**
     * 验证码超时时间
     */
    public static final int KAPTCHA_EXPIRED_SECONDS = 60;

    /**
     * 用户信息缓存超时时间
     */
    public static final int USER_EXPIRED_SECONDS = 3600;

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


    /**
     * 主题类型
     */
    public static final String TOPIC_COMMENT = "comment";

    public static final String TOPIC_LIKE = "like";

    public static final String TOPIC_FOLLOW = "follow";
    // 发帖
    public static final String TOPIC_PUBLISH = "publish";
    // 删帖
    public static final String TOPIC_DELETE = "delete";
    // 分享
    public static final String TOPIC_SHARE = "share";

    // 系统id
    public static final int SYSTEM_USER_ID = 1;

    // 点赞状态
    public static final int STATUS_LIKE = 1;


    /**
     * 用户权限
     */
    // 普通用户
    public static final String AUTHORITY_USER = "user";

    // 管理员
    public static final String AUTHORITY_ADMIN = "admin";

    // 版主
    public static final String AUTHORITY_MODERATOR = "moderator";

}
