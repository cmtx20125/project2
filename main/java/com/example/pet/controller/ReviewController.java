package com.example.pet.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.pet.beans.HttpResponseEntity;
import com.example.pet.beans.Response;
import com.example.pet.dao.entity.Report;
import com.example.pet.dao.entity.Review;
import com.example.pet.mapper.ReportMapper;
import com.example.pet.mapper.ReviewMapper;
import com.example.pet.service.ReportService;
import com.example.pet.service.ReviewService;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ReviewController {
    @Autowired
    private MinioClient minioClient;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private ReviewMapper reviewMapper;

    @RequestMapping("/viewAllReview")
    public HttpResponseEntity viewAllSubject(@RequestParam("publishId") String publishId) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        List<Review> subjects = reviewMapper.selectList(Wrappers.<Review>lambdaQuery().eq(Review::getPublishId,publishId));
        if (subjects.size() == 0) {
            httpResponseEntity.setCode("0");
            httpResponseEntity.setData(null);
            httpResponseEntity.setMessage("无上报");
        } else {
            String minioPublicBaseUrl = "http://127.0.0.1:9005/pet/";

            // 直接拼接公开URL
            subjects.forEach(review -> {
                String publicUrl = minioPublicBaseUrl + review.getReviewUserImg();
                review.setReviewUserImg(publicUrl);

            });
            httpResponseEntity.setCode("666");
            httpResponseEntity.setData(subjects);
            httpResponseEntity.setMessage("有上报");
            System.out.println(httpResponseEntity);
        }
        return httpResponseEntity;
    }


    @PostMapping("/reviewadd")
    public Response addReview(@RequestBody Review review){
        System.out.println("进入review的添加");
        Date date = new Date();
        review.setReviewTime(date);
        String str = review.getReviewUserImg();
        String results = str.substring("http://127.0.0.1:9005/pet/".length());
        review.setReviewUserImg(results);
        int result = reviewService.addReview(review);
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
