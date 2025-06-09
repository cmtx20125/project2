package com.example.pet.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.pet.beans.HttpResponseEntity;
import com.example.pet.beans.Response;
import com.example.pet.dao.dto.DialogueDto;
import com.example.pet.dao.dto.UnreadUserDto;
import com.example.pet.dao.entity.Article;
import com.example.pet.dao.entity.Dialogue;
import com.example.pet.dao.entity.Pet;
import com.example.pet.dao.entity.Review;
import com.example.pet.mapper.DialogueMapper;
import com.example.pet.service.DialogueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.xml.transform.Result;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DialogueController {

    @Autowired
    private DialogueService dialogueService;
    @Autowired
    private DialogueMapper dialogueMapper;

    @GetMapping("/viewAllUnread")
    public HttpResponseEntity getUnreadUsers(@RequestParam("userId") String userId) {
        System.out.println("我进入dialogue");
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        List<UnreadUserDto> unreadUsers = dialogueService.findUser(userId);
        if (unreadUsers.size() == 0){
            httpResponseEntity.setCode("0");
            httpResponseEntity.setData(null);
            httpResponseEntity.setMessage("无文章");
        }else {
            httpResponseEntity.setCode("666");
            httpResponseEntity.setData(unreadUsers);
            httpResponseEntity.setMessage("有文章");
            System.out.println(httpResponseEntity);
        }

        return httpResponseEntity;
    }
    @GetMapping("/viewAllDialogue")
    public HttpResponseEntity getDialogue(@RequestParam("userId") String id,@RequestParam("PId") String PId){
        System.out.println("进入dialogue读取");
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
       List<Dialogue> subjects = dialogueService.getDialogue(id, PId);
        if (subjects.size() == 0) {
            httpResponseEntity.setCode("0");
            httpResponseEntity.setData(null);
            httpResponseEntity.setMessage("无上报");
        } else {
            httpResponseEntity.setCode("666");
            httpResponseEntity.setData(subjects);
            httpResponseEntity.setMessage("有上报");
            System.out.println(httpResponseEntity);
        }
        return httpResponseEntity;

    }
    @GetMapping("/viewAllUsers")
    public HttpResponseEntity getUser(@RequestParam("userId") String id){
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        List<DialogueDto> subjects = dialogueService.getChatPartnerIds(id);
        if (subjects.size() == 0) {
            httpResponseEntity.setCode("0");
            httpResponseEntity.setData(null);
            httpResponseEntity.setMessage("无上报");
        } else {
            httpResponseEntity.setCode("666");
            httpResponseEntity.setData(subjects);
            httpResponseEntity.setMessage("有上报");
            System.out.println(httpResponseEntity.getData());
        }
        return httpResponseEntity;

    }
    @GetMapping("/updateDialogue")
    public HttpResponseEntity updateDialogue(@RequestParam("userId") String receiverId,
                                             @RequestParam("PId") String senderId) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();

        int update= dialogueService.markMessagesAsRead(receiverId, senderId);
        if (update == 0) {
            httpResponseEntity.setCode("0");
            httpResponseEntity.setData(null);
            httpResponseEntity.setMessage("无上报");
        } else {
            httpResponseEntity.setCode("666");
            httpResponseEntity.setData(update);
            httpResponseEntity.setMessage("有上报");
            System.out.println(httpResponseEntity);
        }
        return httpResponseEntity;

    }
    @PostMapping("/dialogueAdd")
    public Response addDialogue(@RequestBody Dialogue dialogue) {
        System.out.println("进入review的添加");
        Date date = new Date();
        dialogue.setDialogueTime(date);

        int result = dialogueService.addDialogue(dialogue);
        Response response = new Response();
        if (result == 1) {
            response.setSuccess(true);
            response.setMessage("添加成功");
            return response;
        } else {
            response.setSuccess(false);
            response.setMessage("添加失败");
            return response;
        }

    }

}
