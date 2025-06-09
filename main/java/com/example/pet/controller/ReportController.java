package com.example.pet.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.pet.beans.HttpResponseEntity;
import com.example.pet.beans.Response;
import com.example.pet.dao.entity.Article;
import com.example.pet.dao.entity.Report;
import com.example.pet.mapper.ArticleMapper;
import com.example.pet.mapper.ReportMapper;
import com.example.pet.service.ReportService;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.http.Method;
import jakarta.annotation.Resource;
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
public class ReportController {
    @Autowired
    private MinioClient minioClient;
    @Autowired
    private ReportService reportService;
    @Autowired
    private ReportMapper reportMapper;

    @RequestMapping("/viewAllReport")
    public HttpResponseEntity viewAllSubject(@RequestParam("la") String latitude,
                                             @RequestParam("lo") String longitude,@RequestParam("userId") String userId) {
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        double la = Double.parseDouble(latitude);
        double lo = Double.parseDouble(longitude);
        List<Report> subjects = reportService.getSortedReports(userId,la,lo);
        if (subjects.size() == 0) {
            httpResponseEntity.setCode("0");
            httpResponseEntity.setData(null);
            httpResponseEntity.setMessage("无上报");
        } else {
            String minioPublicBaseUrl = "http://127.0.0.1:9005/pet/";

            // 直接拼接公开URL
            subjects.forEach(report -> {
                String publicUrl = minioPublicBaseUrl + report.getReportCover();
                report.setReportCover(publicUrl);
                if(report.getReportImg()!=null){
                    publicUrl = minioPublicBaseUrl + report.getReportImg();
                    report.setReportImg(publicUrl);
                }
               if(report.getReportFile()!=null){
                   publicUrl = minioPublicBaseUrl + report.getReportFile();
                   report.setReportFile(publicUrl);
               }
               if(report.getReportTagOne().equals("1")){
                   report.setReportTagOne("图文");
               }else{
                   report.setReportTagOne("视频");
               }
            });
                httpResponseEntity.setCode("666");
                httpResponseEntity.setData(subjects);
                httpResponseEntity.setMessage("有上报");
                System.out.println(httpResponseEntity);
            }
            return httpResponseEntity;
        }
        @PostMapping("addJ")
        public Response addJ(@RequestParam("userId") String userId,
                             @RequestParam("reportId") String reportId,@RequestParam("reportType") String reportType){
        Response response = new Response();
        int result= reportService.recordUserBehavior(userId,reportId,reportType);
        if(result>0){
            response.setSuccess(true);
            response.setMessage("成功");
        }else{
            response.setSuccess(false);
            response.setMessage("失败");
        }
        return response;
        }

        @GetMapping("/QueryOneReport/{id}")
        public HttpResponseEntity QueryOneSubject (@PathVariable("id") String id){
            HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
            Report report = reportMapper.selectOne(Wrappers.<Report>lambdaQuery().eq(Report::getReportId, id));
            if (report == null) {
                httpResponseEntity.setCode("0");
                httpResponseEntity.setData(null);
                httpResponseEntity.setMessage("查询文章详细信息失败");
            } else {
                try {
                    System.out.println(report.getReportImg());
                    String url = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket("pet")
                            .object(report.getReportImg())
                            .build()
                    );
                    report.setReportImg(url);
                    url = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket("pet")
                            .object(report.getReportCover())
                            .build()
                    );
                    System.out.println(url);
                    report.setReportCover(url);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                httpResponseEntity.setCode("666");
                httpResponseEntity.setData(report);
                httpResponseEntity.setMessage("查询文章的详细信息成功");
            }
            return httpResponseEntity;
        }

        @PostMapping("/report/add")
        public HttpResponseEntity addReport(@RequestPart("publishTag") String publishTag,
                                            @RequestPart("publishContent") String publishContent,
                                            @RequestPart("userId") String userId,
                                            @RequestPart("publishName") String publishName,
                                            @RequestPart("address") String address,
                                            @RequestPart("userName") String userName,
                                            @RequestPart("reportId") String reportId,
                                            @RequestPart("la") String reportLa,
                                            @RequestPart("lo") String reportLo,
                                            @RequestPart("coverImage") MultipartFile coverImage,
                                            @RequestPart("contentImage") MultipartFile contentImage) throws Exception{
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
            Report report = new Report();
            report.setReportTagOne(publishTag);
            report.setReportId(reportId);
            report.setReportContent(publishContent);
            report.setReportUserId(userId);
            report.setReportUserName(userName);
            report.setReportAddress(address);
            report.setReportLa(reportLa);
            report.setReportLo(reportLo);
            report.setReportName(publishName);
            Date time =new Date();
            report.setReportTime(time);
            String url = reportService.uploadToMinio(coverImage);
            report.setReportCover(url);
            String urll = reportService.uploadToMinio(contentImage);
            report.setReportImg(urll);
            report.setReportCunZai("存在");
            int result = reportService.addReport(report);

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
    @PostMapping("/report/addd")
    public HttpResponseEntity addReportt(@RequestPart("publishTag") String publishTag,
                                        @RequestPart("publishContent") String publishContent,
                                        @RequestPart("userId") String userId,
                                        @RequestPart("publishName") String publishName,
                                        @RequestPart("address") String address,
                                        @RequestPart("userName") String userName,
                                        @RequestPart("reportId") String reportId,
                                         @RequestPart("la") String reportLa,
                                         @RequestPart("lo") String reportLo,
                                        @RequestPart("coverImage") MultipartFile coverImage,
                                        @RequestPart("contentImage") MultipartFile contentImage) throws Exception{
        HttpResponseEntity httpResponseEntity = new HttpResponseEntity();
        Report report = new Report();
        report.setReportTagOne(publishTag);
        report.setReportId(reportId);
        report.setReportContent(publishContent);
        report.setReportUserId(userId);
        report.setReportUserName(userName);
        report.setReportAddress(address);
        report.setReportLa(reportLa);
        report.setReportLo(reportLo);
        report.setReportName(publishName);
        Date time =new Date();
        report.setReportTime(time);
        String url = reportService.uploadToMinio(coverImage);
        report.setReportCover(url);
        String urll = reportService.uploadToMinio(contentImage);
        report.setReportFile(urll);
        report.setReportCunZai("存在");
        int result = reportService.addReport(report);

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
        @GetMapping("/updateReport")
        public Response updateReport(@RequestBody Report report) {
            int result = reportService.updateReport(report);
            Response response = new Response();
            if (result == 1) {
                response.setSuccess(true);
                response.setMessage("成功");
                return response;
            } else {
                response.setSuccess(false);
                response.setMessage("失败");
                return response;
            }
        }



}

