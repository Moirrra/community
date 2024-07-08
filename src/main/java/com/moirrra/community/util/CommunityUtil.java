package com.moirrra.community.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: Moirrra
 * @CreateTime: 2024-07-01
 * @Description:
 * @Version: 1.0
 */

public class CommunityUtil {
    /**
     * 随机生成字符串
     * @return
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    /**
     * MD5加密
     * @param salt
     * @return
     */
    public static String md5(String salt) {
        if (StringUtils.isBlank(salt)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(salt.getBytes());
    }

    /**
     * 转换为JSON字符串
     * @param code
     * @param msg
     * @param map
     * @return
     */
    public static  String getJSONString(int code, String msg, Map<String, Object> map) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        if (map != null) {
            for (String key : map.keySet()) {
                json.put(key, map.get(key));
            }
        }
        return json.toJSONString();
    }

    public static String getJSONString(int code, String msg) {
        return getJSONString(code, msg, null);
    }

    public static void main(String[] args) {
        // test getJSONString()
        Map<String, Object> map = new HashMap<>();
        map.put("age", 1);
        map.put("name", "hello");
        System.out.println(getJSONString(200,"ok",map));
    }
}
