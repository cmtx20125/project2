package com.example.pet.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.pet.beans.HttpResponseEntity;
import com.example.pet.beans.Response;
import com.example.pet.dao.entity.*;
import com.example.pet.mapper.AnimalMapper;
import com.example.pet.mapper.ArticleMapper;
import com.example.pet.mapper.PetMapper;
import com.example.pet.mapper.ReportMapper;
import com.example.pet.service.AnimalService;
import com.example.pet.service.PetService;
import com.example.pet.service.ReportService;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.http.Method;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AnimalController {
    @Autowired
    private MinioClient minioClient;
    @Autowired
    AnimalMapper animalMapper;
    @Autowired
    PetService petService;
    @Autowired
    ReportMapper reportMapper;
    @Autowired
    private AnimalService animalService;
    @Autowired
    private ReportService reportService;
    @RequestMapping("/getAnimalById")
    public HttpResponseEntity getAnimalById(@RequestParam("reportId") String reportId){
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        Animal animal = animalMapper.selectOne(Wrappers.<Animal>lambdaQuery().eq(Animal::getReportId,reportId)
                );
        if (animal == null) {
            httpResponseEntity.setCode("0");
            httpResponseEntity.setData(null);
            httpResponseEntity.setMessage("动物不存在");
        } else {
            String minioPublicBaseUrl = "http://127.0.0.1:9005/pet/";

            // 直接拼接公开URL
                String publicUrl = minioPublicBaseUrl + animal.getAnimalImg();
                animal.setAnimalImg(publicUrl);

            httpResponseEntity.setCode("666");
            httpResponseEntity.setData(animal);
            httpResponseEntity.setMessage("查找成功");


        }
        return httpResponseEntity;
    }
    @PostMapping("/animalAdd")
    public HttpResponseEntity addAnimal(@RequestPart("name") String name,
                                                         @RequestPart("gender") String gender,
                                                         @RequestPart("v") String v,
                                                         @RequestPart("jue") String jue,
                                                         @RequestPart("ReportId") String reportId,
                                                         @RequestPart("age") String age,
                                                         @RequestPart("s") String s,
                                                         @RequestPart("content") String content,
                                                         @RequestPart("animalImage") MultipartFile animalImage)throws Exception{
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        Animal animal = new Animal();
        String url = animalService.uploadToMinio(animalImage);
        animal.setAnimalJue(jue);
        animal.setAnimalYM(v);
        animal.setAnimalImg(url);
        animal.setAnimalContent(content);
        animal.setAnimalGender(gender);
        animal.setReportId(reportId);
        animal.setAnimalName(name);
        animal.setAnimalYear(age);
        animal.setAnimalYuan(s);
        Date date = new Date();
        animal.setAnimalTime(date);
        animal.setAnimalYang("0");
        int result = animalService.addAnimal(animal);
        if (result == 1) {
            httpResponseEntity.setCode("666");
            httpResponseEntity.setMessage("成功添加");

        } else {
            httpResponseEntity.setCode("0");
            httpResponseEntity.setMessage("失败添加");

            //返回 400 Bad Request 表示请求不合法.(待推敲哪个状态码更合适)
        }
        return httpResponseEntity;
    }
    @PostMapping("/updateAnimal")
    public HttpResponseEntity updatePet(@RequestPart("name") String name,
                                        @RequestPart("gender") String gender,
                                        @RequestPart("v") String v,
                                        @RequestPart("jue") String jue,
                                        @RequestPart("ReportId") String reportId,
                                        @RequestPart("age") String age,
                                        @RequestPart("s") String s,
                                        @RequestPart("content") String content,@RequestPart("id") String id,
                                        @RequestPart("animalImage") MultipartFile animalImage) throws Exception{
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        System.out.println(reportId);
       int row = animalService.updateAnimal(name,
               gender,
              v,
               jue,
               reportId,
               age,
               s,
               content,id,
               animalImage);
       if(row>0){
           httpResponseEntity.setCode("666");
           httpResponseEntity.setMessage("成功修改");
           return httpResponseEntity;
       }else{
           httpResponseEntity.setCode("0");
           httpResponseEntity.setMessage("失败修改");
           return httpResponseEntity;
       }
    }
    @PostMapping("/deleteAnimal")
    public Response lingAnimal(@RequestBody Animal animal) {
        int result = animalService.lingAnimal(animal);
        Report report = reportMapper.selectOne(Wrappers.<Report>lambdaQuery().eq(Report::getReportId, animal.getReportId()));
        Response response = new Response();
        if(report!=null){
            int row = reportService.updateReport(report);
            if(row==1){
                System.out.println("成功");
            }else{
                System.out.println("失败");
            }
        }else{
            System.out.println("report是空");
        }
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
