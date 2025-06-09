package com.example.pet.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.pet.beans.HttpResponseEntity;
import com.example.pet.beans.Response;
import com.example.pet.dao.entity.Animal;
import com.example.pet.dao.entity.Pet;
import com.example.pet.dao.entity.Report;
import com.example.pet.mapper.PetMapper;
import com.example.pet.mapper.ReportMapper;
import com.example.pet.service.PetService;

import io.minio.MinioClient;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PetController {
    @Autowired
    private MinioClient minioClient;
    @Autowired
    private PetService petService;
    @Autowired
    private PetMapper petMapper;

    @GetMapping("/viewAllPet")
    public HttpResponseEntity viewAllSubject(@RequestParam("id") String id) {
        System.out.println("进来pet");
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        List<Pet> subjects = petMapper.selectList(Wrappers.<Pet>lambdaQuery().eq(Pet::getUserId, id));
        if (subjects.size() == 0) {
            httpResponseEntity.setCode("0");
            httpResponseEntity.setData(null);
            httpResponseEntity.setMessage("无上报");
        } else {
            String minioPublicBaseUrl = "http://127.0.0.1:9005/pet/";

            // 直接拼接公开URL
            subjects.forEach(report -> {
                String publicUrl = minioPublicBaseUrl + report.getPetImg();
                report.setPetImg(publicUrl);

            });
            httpResponseEntity.setCode("666");
            httpResponseEntity.setData(subjects);
            httpResponseEntity.setMessage("有上报");
            System.out.println(httpResponseEntity);
        }
        return httpResponseEntity;
    }


    @PostMapping("/petAdd")
    public HttpResponseEntity addPet( @RequestPart("name") String name,
                                      @RequestPart("gender") String gender,
                                      @RequestPart("v") String v,
                                      @RequestPart("jue") String jue,
                                      @RequestPart("ReportId") String reportId,
                                      @RequestPart("age") String age,
                                      @RequestPart("s") String s,
                                      @RequestPart("content") String content,
                                      @RequestPart("animalImage") MultipartFile animalImage)throws Exception{
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        String url = petService.uploadToMinio(animalImage);
        Pet pet = new Pet();
        pet.setPetName(name);
        pet.setPetGender(gender);
        pet.setPetYM(v);
        pet.setPetJue(jue);
        pet.setUserId(reportId);
        pet.setPetYear(age);
        pet.setPetYuan(s);
        pet.setPetContent(content);
        pet.setPetImg(url);
        int result = petService.addPet(pet);

        if (result == 1) {
            httpResponseEntity.setCode("666");
            httpResponseEntity.setMessage("添加宠物成功");
            return httpResponseEntity;
        } else {
            httpResponseEntity.setCode("0");
            httpResponseEntity.setMessage("添加宠物失败");

            //返回 400 Bad Request 表示请求不合法.(待推敲哪个状态码更合适)
            return httpResponseEntity;
        }
    }
    @PostMapping("/updatePet")
    public HttpResponseEntity updateReport(@RequestPart("name") String name,
                                           @RequestPart("gender") String gender,
                                           @RequestPart("v") String v,
                                           @RequestPart("jue") String jue,
                                           @RequestPart("ReportId") String reportId,
                                           @RequestPart("age") String age,
                                           @RequestPart("s") String s,
                                           @RequestPart("content") String content,
                                           @RequestPart("id") String id,
                                           @RequestPart("animalImage") MultipartFile animalImage)throws Exception {
        String url = petService.uploadToMinio(animalImage);
        Pet pet = new Pet();
        pet.setPetName(name);
        pet.setPetGender(gender);
        pet.setPetYM(v);
        pet.setPetJue(jue);
        pet.setUserId(reportId);
        pet.setPetYear(age);
        pet.setPetYuan(s);
        pet.setPetContent(content);
        pet.setPetImg(url);
        pet.setPetId(id);
        int result = petService.updatePet(pet);
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();

        if (result == 1) {
            httpResponseEntity.setCode("666");
            httpResponseEntity.setMessage("更新成功");
            httpResponseEntity.setData(result);

        } else {
            httpResponseEntity.setCode("0");
            httpResponseEntity.setMessage("更新失败");
            httpResponseEntity.setData(result);
        }
        return httpResponseEntity;
    }
    @PostMapping("/addPetn")
    public Response addP(@RequestBody Pet pet){
        Response response = new Response();
        pet.setCreateTime(new Date());
        int result = petService.addPet(pet);

        if(result>0){
            response.setSuccess(true);
            response.setMessage("成功");
        }else{
            response.setSuccess(false);
            response.setMessage("失败");
        }
        return response;
    }


}
