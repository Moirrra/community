package com.moirrra.community.service.impl;

import com.moirrra.community.service.DataService;
import com.moirrra.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Author: Moirrra
 * @CreateTime: 2024-08-16
 * @Description:
 * @Version: 1.0
 */
@Service
public class DataServiceImpl implements DataService {

    @Autowired
    private RedisTemplate redisTemplate;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");


    @Override
    public void recordUV(String ip) {
        String key = RedisKeyUtil.getUVKey(sdf.format(new Date()));
        redisTemplate.opsForHyperLogLog().add(key, ip);
    }

    @Override
    public long calculateUV(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("参数不能为空！");
        }

        // 整理key
        List<String> keyList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        while (!calendar.getTime().after(endDate)) {
            String key = RedisKeyUtil.getUVKey(sdf.format(calendar.getTime()));
            keyList.add(key);
            calendar.add(Calendar.DATE, 1);
        }

        // 合并数据
        String uvKey = RedisKeyUtil.getUVKey(sdf.format(startDate), sdf.format(endDate));
        redisTemplate.opsForHyperLogLog().union(uvKey, keyList.toArray());

        return redisTemplate.opsForHyperLogLog().size(uvKey);
    }

    @Override
    public void recordDAU(int userId) {
        String key = RedisKeyUtil.getDAUKey(sdf.format(new Date()));
        redisTemplate.opsForValue().setBit(key, userId, true);
    }

    @Override
    public long calculateDAU(Date startDate, Date endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("参数不能为空！");
        }

        // 整理key
        List<byte[]> keyList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        while (!calendar.getTime().after(endDate)) {
            String key = RedisKeyUtil.getDAUKey(sdf.format(calendar.getTime()));
            keyList.add(key.getBytes());
            calendar.add(Calendar.DATE, 1);
        }

        // OR运算
        Object res = redisTemplate.execute((RedisCallback) connection -> {
            String key = RedisKeyUtil.getDAUKey(sdf.format(startDate), sdf.format(endDate));
            connection.bitOp(RedisStringCommands.BitOperation.OR,
                    key.getBytes(),
                    keyList.toArray(new byte[0][0]));
            Long count = connection.bitCount(key.getBytes());
            return count == null ? 0 : count;
        });

        return res == null ? 0L : (long) res;
    }
}
