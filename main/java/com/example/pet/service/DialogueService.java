package com.example.pet.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.pet.dao.dto.DialogueDto;
import com.example.pet.dao.dto.UnreadUserDto;
import com.example.pet.dao.entity.Article;
import com.example.pet.dao.entity.Dialogue;
import com.example.pet.dao.entity.User;

import java.io.Serializable;
import java.util.List;

public interface DialogueService extends IService<Dialogue> {

    /**
     * 查询满足条件的文章列表
     */
    public List<Dialogue> getDialogue(String userId,String Pid);

    public List<UnreadUserDto> findUser(String receiverId);

    public int addDialogue(Dialogue dialogue);

    public List<DialogueDto> getChatPartnerIds(String currentUserId) ;

    public int markMessagesAsRead(String receiverId, String senderId);


}
