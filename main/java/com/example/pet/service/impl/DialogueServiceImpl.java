package com.example.pet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.pet.dao.dto.DialogueDto;
import com.example.pet.dao.dto.UnreadUserDto;
import com.example.pet.dao.entity.Dialogue;
import com.example.pet.dao.entity.User;
import com.example.pet.mapper.DialogueMapper;
import com.example.pet.mapper.PetMapper;
import com.example.pet.service.DialogueService;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@Accessors(chain = true)
public class DialogueServiceImpl extends ServiceImpl<DialogueMapper, Dialogue> implements DialogueService {

    @Autowired
    private DialogueMapper dialogueMapper;

    @Override
    public List<UnreadUserDto> findUser(String receiverId) {
        // 1. 查询所有发给当前用户且未读的消息
        return dialogueMapper.selectUsersWithUnreadMessages(receiverId);
    }
    @Override
    public int addDialogue(Dialogue dialogue){
        int result1 = dialogueMapper.insert(dialogue);
        if (result1 == 1 ){
            return 1;
        } else {
            return -1;
        }
    }
    @Override
    public List<DialogueDto> getChatPartnerIds(String currentUserId) {
        // 参数校验
        if (StringUtils.isBlank(currentUserId)) {
            throw new IllegalArgumentException("用户ID不能为空");
        }

        // 执行查询
        return dialogueMapper.selectChatPartners(currentUserId);
    }
    @Override
    public int markMessagesAsRead(String receiverId, String senderId) {
        // 更新数据库状态
        int updated = dialogueMapper.markMessagesAsRead(receiverId, senderId);
            return updated;
    }
    @Override
    public List<Dialogue> getDialogue(String userId,String Pid){
        return dialogueMapper.getDialoguesBetween(userId, Pid);
    }
}
