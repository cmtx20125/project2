package com.example.pet.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.pet.beans.HttpResponseEntity;
import com.example.pet.dao.entity.Article;
import com.example.pet.mapper.ArticleMapper;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.errors.*;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.minio.http.Method; // 正确导入
// 不要误用 org.springframework.http.HttpMethod
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api")
public class ArticleController {
    @Autowired
    private MinioClient minioClient;
    @Resource
    ArticleMapper articleMapper;
    @RequestMapping("/viewAllArticle")
    public HttpResponseEntity viewAllSubject(){
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        List<Article> subjects = articleMapper.selectList(null);
        if (subjects.size() == 0){
            httpResponseEntity.setCode("0");
            httpResponseEntity.setData(null);
            httpResponseEntity.setMessage("无文章");
        }else {
            // MinIO 公开访问的基础URL（根据你的实际配置调整）
            String minioPublicBaseUrl = "http://127.0.0.1:9005/pet/";

            // 直接拼接公开URL
            subjects.forEach(article -> {
                String publicUrl = minioPublicBaseUrl + article.getArticlePic();
                article.setArticlePic(publicUrl);
                publicUrl = minioPublicBaseUrl + article.getArticleUserPic();
                article.setArticleUserPic(publicUrl);
            });
            }
            httpResponseEntity.setCode("666");
            httpResponseEntity.setData(subjects);
            httpResponseEntity.setMessage("有文章");
            System.out.println(httpResponseEntity);
        return httpResponseEntity;
    }
    @GetMapping("/QueryOneArticle/{id}")
    public HttpResponseEntity QueryOneSubject(@PathVariable("id") String id){
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        Article article = articleMapper.selectOne(Wrappers.<Article>lambdaQuery().eq(Article::getArticleId, id));
        if (article == null){
            httpResponseEntity.setCode("0");
            httpResponseEntity.setData(null);
            httpResponseEntity.setMessage("查询文章详细信息失败");
        }else {
            try {
                System.out.println(article.getArticlePic());
                String url= minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket("pet")
                        .object(article.getArticlePic())
                        .build()
                );
                System.out.println(url);
                article.setArticlePic(url);
            } catch (Exception e) {
                e.printStackTrace();
            }
            httpResponseEntity.setCode("666");
            httpResponseEntity.setData(article);
            httpResponseEntity.setMessage("查询文章的详细信息成功");
        }
        return httpResponseEntity;
    }
}
