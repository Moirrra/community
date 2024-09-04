package com.moirrra.community.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @Author: Moirrra
 * @CreateTime: 2024-08-16
 * @Description:
 * @Version: 1.0
 */

public interface DataService {
    // 将指定IP计入UV
    void recordUV(String ip);

    // 统计指定日期范围内的UV
    long calculateUV(Date startDate, Date endDate);

    // 将指定用户计入DAU
    void recordDAU(int userId);

    // 统计指定日期范围内的DAU
    long calculateDAU(Date startDate, Date endDate);
}
