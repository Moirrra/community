package com.moirrra.community.controller;

import com.moirrra.community.entity.Message;
import com.moirrra.community.entity.Page;
import com.moirrra.community.entity.User;
import com.moirrra.community.service.MessageService;
import com.moirrra.community.service.UserService;
import com.moirrra.community.util.CommunityConstant;
import com.moirrra.community.util.CommunityUtil;
import com.moirrra.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * @Author: Moirrra
 * @CreateTime: 2024-07-10
 * @Description: 私信
 * @Version: 1.0
 */
@Controller
public class MessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    /**
     * 显示私信列表
     * @param model
     * @param page
     * @return
     */
    @GetMapping("/letter/list")
    public String getLetterList(Model model, Page page) {
        User user = hostHolder.getUser();

        // 分页信息
        page.setLimit(5);
        page.setPath("/letter/list");
        page.setRows(messageService.findConversationCountByUser(user.getId()));

        // 查询
        List<Map<String, Object>> conversations = new ArrayList<>();
        // 查询会话列表
        List<Message> conversationList = messageService.findConversations(
                user.getId(), page.getOffset(), page.getLimit());
        if (conversationList != null) {
            for (Message message : conversationList) {
                Map<String, Object> map = new HashMap<>();
                // 会话
                map.put("conversation", message);
                // 未读消息数
                map.put("unreadCount", messageService.findUnreadMessageCount(
                        user.getId(), message.getConversationId()));
                // 会话消息数
                map.put("letterCount", messageService.findMessageCountByConversation(
                        message.getConversationId()));
                // 对方用户
                Integer targetId = user.getId().equals(message.getFromId()) ? message.getToId() : message.getFromId();
                map.put("target", userService.findUserById(targetId));

                conversations.add(map);
            }
        }

        model.addAttribute("conversations", conversations);

        // 查询未读消息总数
        int unreadMessageCount = messageService.findUnreadMessageCount(user.getId(), null);
        model.addAttribute("letterUnreadCount", unreadMessageCount);

        return "/site/letter";
    }

    /**
     * 私信详情
     * @param conversationId
     * @param model
     * @param page
     * @return
     */
    @GetMapping("/letter/detail/{conversationId}")
    public String getLetterDetail(@PathVariable("conversationId") String conversationId, Model model, Page page) {
        // 分页信息
        page.setLimit(5);
        page.setPath("/letter/detail/" + conversationId);
        page.setRows(messageService.findMessageCountByConversation(conversationId));

        List<Message> letterList = messageService.findMessageByConversation(conversationId, page.getOffset(), page.getLimit());
        List<Map<String,Object>> letters = new ArrayList<>();
        if (letterList != null) {
            for (Message message : letterList) {
                Map<String,Object> map = new HashMap<>();
                map.put("letter", message);
                map.put("fromUser", userService.findUserById(message.getFromId()));
                letters.add(map);
            }
        }
        model.addAttribute("letters", letters);
        model.addAttribute("target", getMessageTarget(conversationId));

        // 设置已读
        List<Integer> ids = getLetterIds(letterList);
        if (!ids.isEmpty()) {
            messageService.readMessage(ids);
        }

        return "site/letter-detail";
    }

    /**
     * 返回未读消息id列表
     * @param letterList
     * @return
     */
    private List<Integer> getLetterIds(List<Message> letterList) {
        List<Integer> ids = new ArrayList<>();
        if (letterList != null) {
            for (Message message : letterList) {
                if (hostHolder.getUser().getId().equals(message.getToId())
                        && message.getStatus() == CommunityConstant.MESSAGE_UNREAD) {
                    // 当前用户接受的未读消息
                    ids.add(message.getId());
                }
            }
        }
        return ids;
    }

    /**
     * 获取私信目标用户
     * @param conversationId
     * @return
     */
    private User getMessageTarget(String conversationId) {
        String[] ids = conversationId.split("_");
        Integer id0 = Integer.parseInt(ids[0]);
        Integer id1 = Integer.parseInt(ids[1]);
        if (hostHolder.getUser().getId().equals(id0)) {
            return userService.findUserById(id1);
        } else {
            return userService.findUserById(id0);
        }
    }

    /**
     * 发送私信
     * @param toName
     * @param content
     * @return
     */
    @PostMapping("/letter/send")
    @ResponseBody
    public String sendLetter(String toName, String content) {
        User target = userService.findUserByName(toName);
        if (target == null) {
            return CommunityUtil.getJSONString(1, "目标用户不存在！");
        }

        Message message = new Message();
        message.setFromId(hostHolder.getUser().getId());
        message.setToId(target.getId());
        if (message.getFromId() < message.getToId()) {
            message.setConversationId(message.getFromId() + "_" + message.getToId());
        } else {
            message.setConversationId(message.getToId() + "_" + message.getFromId());
        }
        message.setContent(content);
        message.setStatus(CommunityConstant.MESSAGE_UNREAD);
        message.setCreateTime(new Date());
        messageService.addMessage(message);

        return CommunityUtil.getJSONString(0);
    }
}
