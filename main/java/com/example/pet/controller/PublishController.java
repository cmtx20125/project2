package com.example.pet.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.pet.beans.HttpResponseEntity;
import com.example.pet.dao.entity.Pet;
import com.example.pet.dao.entity.Publish;
import com.example.pet.dao.entity.Report;
import com.example.pet.dao.entity.User;
import com.example.pet.mapper.PublishMapper;
import com.example.pet.mapper.ReportMapper;
import com.example.pet.mapper.UserMapper;
import com.example.pet.service.MinioService;
import com.example.pet.service.PublishService;
import com.example.pet.service.ReportService;
import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PublishController {
    @Autowired
    private MinioClient minioClient;
    @Autowired
    private PublishService publishService;
    @Autowired
    private PublishMapper publishMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MinioService minioService;
    @GetMapping("/viewAllPublish")
    public HttpResponseEntity viewAllSubject(@RequestParam("id") String id) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        List<Publish> subjects = publishMapper.selectList(null);
        if (subjects.size() == 0) {
            httpResponseEntity.setCode("0");
            httpResponseEntity.setData(null);
            httpResponseEntity.setMessage("无上报");
        } else {
            String minioPublicBaseUrl = "http://127.0.0.1:9005/pet/";
            subjects.removeIf(publish -> {
                User user = userMapper.selectOne(
                        Wrappers.<User>lambdaQuery().eq(User::getUserId, publish.getPublishUserId())
                );
                return user != null && user.getUserTagtr() == 1&&(!user.getUserId().equals(id));
            });
            // 直接拼接公开URL
            subjects.forEach(publish -> {
                User user = userMapper.selectOne(
                        Wrappers.<User>lambdaQuery().eq(User::getUserId, publish.getPublishUserId())
                );
                if(user.getUserTago()==1&&!user.getUserId().equals(id)){
                    publish.setPublishUserName("匿名");
                }
                String publicUrl = minioPublicBaseUrl + publish.getPublishCover();
                publish.setPublishCover(publicUrl);
                if(publish.getPublishImg()!=null){
                    publicUrl = minioPublicBaseUrl + publish.getPublishImg();
                    publish.setPublishImg(publicUrl);
                }
                if(publish.getPublishFile()!=null){
                    publicUrl = minioPublicBaseUrl + publish.getPublishFile();
                    publish.setPublishFile(publicUrl);
                }
                if(publish.getPublishTag().equals("1")){
                    publish.setPublishTag("图文");
                }else{
                    publish.setPublishTag("视频");
                }
            });
            httpResponseEntity.setCode("666");
            httpResponseEntity.setData(subjects);
            httpResponseEntity.setMessage("有上报");
            System.out.println(httpResponseEntity);
        }
        return httpResponseEntity;
    }
    @PostMapping("/publish/add")
    public HttpResponseEntity addPublish(@RequestPart("publishTag") String publishTag,
                                         @RequestPart("publishContent") String publishContent,
                                         @RequestPart("userId") String userId,
                                         @RequestPart("publishName") String publishName,
                                         @RequestPart("userName") String userName,
                                         @RequestPart("coverImage") MultipartFile coverImage,
                                         @RequestPart("contentImage") MultipartFile contentImage) throws Exception{
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        System.out.println("进入publish添加");
        try {
             int row=publishService.addPublish(
                    publishName,
                    publishContent,
                    userId,
                    userName,
                    publishTag,
                    coverImage,
                    contentImage
            );
             if(row>0){
                 httpResponseEntity.setCode("666");
                 httpResponseEntity.setMessage("成功添加");
             }
             return httpResponseEntity;
        } catch (Exception e) {
            httpResponseEntity.setCode("0");
            httpResponseEntity.setMessage("失败");
            return httpResponseEntity;
        }
    }
    @PostMapping("/publish/addd")
    public HttpResponseEntity addPublisht(@RequestPart("publishTag") String publishTag,
                                         @RequestPart("publishContent") String publishContent,
                                         @RequestPart("userId") String userId,
                                         @RequestPart("publishName") String publishName,
                                         @RequestPart("userName") String userName,
                                         @RequestPart("coverImage") MultipartFile coverImage,
                                         @RequestPart("contentImage") MultipartFile contentImage) throws Exception{
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        System.out.println("进入publish添加");
        try {
            int row=publishService.addPublish(
                    publishName,
                    publishContent,
                    userId,
                    userName,
                    publishTag,
                    coverImage,
                    contentImage
            );
            if(row>0){
                httpResponseEntity.setCode("666");
                httpResponseEntity.setMessage("成功添加");
            }
            return httpResponseEntity;
        } catch (Exception e) {
            httpResponseEntity.setCode("0");
            httpResponseEntity.setMessage("失败");
            return httpResponseEntity;
        }
    }
}
