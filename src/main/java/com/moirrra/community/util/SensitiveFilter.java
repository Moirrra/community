package com.moirrra.community.util;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Moirrra
 * @CreateTime: 2024-07-06
 * @Description: 敏感词过滤器
 * @Version: 1.0
 */
@Component
@Slf4j
public class SensitiveFilter {

    // 替换符
    private static final String REPLACEMENT = "***";

    // 根节点
    private TrieNode root = new TrieNode();

    // bean初始化后调用
    @PostConstruct
    public void init() {
        // target/classes
        try (
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ) {
            String keyword;
            while ((keyword = reader.readLine()) != null) {
                // 添加到前缀树
                this.addKeyword(keyword);
            }
        } catch (IOException e) {
            log.error("加载敏感词文件失败：" + e.getMessage());
        }

    }

    // 将敏感词添加到前缀树
    private void addKeyword(String keyword) {
        TrieNode tempNode = root;
        for (int i = 0; i < keyword.length(); i++) {
            char c = keyword.charAt(i);
            TrieNode child = tempNode.getChild(c);
            // 不存在就添加
            if (child == null) {
                // 初始化子节点
                child = new TrieNode();
                tempNode.addChild(c, child);
            }
            // 更新
            tempNode = child;

            // 设置结束标识
            if (i == keyword.length() - 1) {
                tempNode.setKeywordEnd(true);
            }
        }
    }

    /**
     * 过滤敏感词
     * @param text 待过滤文本
     * @return 过滤后的文本
     */
    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }

        // 指针1
        TrieNode tempNode = root;
        // 指针2
        int begin = 0;
        // 指针3
        int pos = 0;
        // 结果
        StringBuilder sb = new StringBuilder();

        while (pos < text.length()) {
            char c = text.charAt(pos);

            // 跳过特殊符号  符号 敏 符号 感 符号 词
            if (isSymbol(c)) {
                // 指针1 处于 root  保留该符号，指针2向前一步
                if (tempNode == root) {
                    sb.append(c);
                    begin++;
                }
                pos++;
                continue;
            }

            // 检查下级节点
            tempNode = tempNode.getChild(c);
            if (tempNode == null) {
                // 以begin开头的字符串不是敏感词
                sb.append(text.charAt(begin));
                begin++;
                pos = begin;
                // 重新指向root
                tempNode = root;
            } else if (tempNode.isKeywordEnd()) {
                // 发现敏感词 替换[begin,pos]
                sb.append(REPLACEMENT);
                pos++;
                begin = pos;
            } else {
                // 检查下一个字符
                pos++;
            }
        }

        // 最后一批 指针3提前到终点 2没到
        sb.append(text.substring(begin));

        return sb.toString();
    }

    // 判断是否为特殊符号
    private boolean isSymbol(Character c) {
        // 0x2E80 - 0x9FFF 东亚文字范围
        return CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }

    /**
     * 前缀树
     */
    @Data
    private class TrieNode {

        // 关键词结束表示
        private boolean isKeywordEnd = false;

        // 子节点 key-val: 下级字符-下级节点
        private Map<Character, TrieNode> children = new HashMap<>();

        // 添加子节点
        public void addChild(Character c, TrieNode node) {
            children.put(c, node);
        }

        // 获取子节点
        public TrieNode getChild(Character c) {
            return children.get(c);
        }
    }

}
